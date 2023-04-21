package org.openthinclient.console.wizards.initrealm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openthinclient.console.Messages;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class OtherDSNoticePanel implements WizardDescriptor.Panel, EnableableWizardPanel {

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

    private JPanel component;

    private WizardDescriptor wizardDescriptor;

    private String baseDN;

    private JLabel explanationLabel;

    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    public JComponent getComponent() {
        if (null == component) {
            final DefaultFormBuilder dfb = new DefaultFormBuilder(new FormLayout("fill:max(250dlu;pref):grow"), Messages.getBundle());
            explanationLabel = new JLabel(Messages.getString("OtherDSNoticePanel.explanation", baseDN));
            dfb.append(explanationLabel);
            component = dfb.getPanel();
            component.setName(Messages.getString("OtherDSNoticePanel.name"));
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public boolean isValid() {
        wizardDescriptor.putProperty("WizardPanel_errorMessage", null);
        return true;
    }

    public void readSettings(Object settings) {
        wizardDescriptor = (WizardDescriptor) settings;
        baseDN = (String) wizardDescriptor.getProperty("selectedBaseDN");
        Object tco = wizardDescriptor.getProperty("ACISetupTakenCareOf");
        explanationLabel.setEnabled(tco == null || !((Boolean) tco).booleanValue());
        explanationLabel.setText(Messages.getString("OtherDSNoticePanel.explanation", baseDN));
    }

    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    public void storeSettings(Object settings) {
    }

    public boolean isEnabled(WizardDescriptor wd) {
        if (null == wd) return true;
        Object tco = wd.getProperty("ACISetupTakenCareOf");
        return tco == null || !((Boolean) tco).booleanValue();
    }
}
