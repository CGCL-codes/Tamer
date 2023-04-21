package org.blogtrader.platform.core.netbeans.options.general;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import org.blogtrader.platform.core.option.OptionsManager;
import org.blogtrader.platform.core.persistence.PersistenceManager;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Implementation of one panel in Options Dialog.
 *
 * @author Caoyuan Deng
 */
public final class GeneralOptionsPanelController extends OptionsPanelController {

    private PropertyChangeSupport pcs;

    private GeneralOptionsPanel optionsPanel;

    private boolean changed = false;

    private String currentPropertyValue = "property value";

    public GeneralOptionsPanelController() {
        optionsPanel = new GeneralOptionsPanel();
    }

    public void update() {
        optionsPanel.proxyTypeRadioGroup.add(optionsPanel.noProxyRadio);
        optionsPanel.proxyTypeRadioGroup.add(optionsPanel.systemProxyRadio);
        optionsPanel.proxyTypeRadioGroup.add(optionsPanel.httpProxyRadio);
        Proxy proxy = OptionsManager.getProxy();
        if (proxy == null) {
            optionsPanel.proxyTypeRadioGroup.setSelected(optionsPanel.systemProxyRadio.getModel(), true);
        } else {
            switch(proxy.type()) {
                case DIRECT:
                    optionsPanel.proxyTypeRadioGroup.setSelected(optionsPanel.noProxyRadio.getModel(), true);
                    break;
                case HTTP:
                    optionsPanel.proxyTypeRadioGroup.setSelected(optionsPanel.httpProxyRadio.getModel(), true);
                    break;
                default:
                    optionsPanel.proxyTypeRadioGroup.setSelected(optionsPanel.systemProxyRadio.getModel(), true);
            }
            InetSocketAddress addr = (InetSocketAddress) proxy.address();
            String hostName = (addr == null) ? "" : addr.getHostName();
            int port = (addr == null) ? 80 : addr.getPort();
            optionsPanel.proxyHost.setText(hostName);
            optionsPanel.proxyPort.setText(String.valueOf(port));
        }
        if (optionsPanel.proxyTypeRadioGroup.getSelection().equals(optionsPanel.httpProxyRadio.getModel())) {
            optionsPanel.proxyHost.setEnabled(true);
            optionsPanel.proxyPort.setEnabled(true);
        } else {
            optionsPanel.proxyHost.setEnabled(false);
            optionsPanel.proxyPort.setEnabled(false);
        }
    }

    /**
     * this method is called when Ok button has been pressed
     * save values here */
    public void applyChanges() {
        String proxyHostStr = optionsPanel.proxyHost.getText();
        String proxyPortStr = optionsPanel.proxyPort.getText();
        Proxy proxy = null;
        ButtonModel selectedRadio = optionsPanel.proxyTypeRadioGroup.getSelection();
        if (selectedRadio.equals(optionsPanel.noProxyRadio.getModel())) {
            proxy = Proxy.NO_PROXY;
        } else if (selectedRadio.equals(optionsPanel.httpProxyRadio.getModel())) {
            int port = 80;
            try {
                port = Integer.parseInt(proxyPortStr.trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            InetSocketAddress addr = new InetSocketAddress(proxyHostStr, port);
            proxy = new Proxy(Proxy.Type.HTTP, addr);
        } else {
            proxy = null;
        }
        OptionsManager.setProxy(proxy);
        PersistenceManager.getDefalut().saveProperties();
    }

    /**
     * This method is called when Cancel button has been pressed
     * revert any possible changes here
     */
    public void cancel() {
        OptionsManager.setOptionsLoaded(false);
    }

    public boolean isValid() {
        return true;
    }

    public boolean isChanged() {
        return changed;
    }

    public HelpCtx getHelpCtx() {
        return new HelpCtx("netbeans.optionsDialog.editor.example");
    }

    public JComponent getComponent(Lookup masterLookup) {
        return optionsPanel;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
    }
}
