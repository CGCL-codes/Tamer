package com.leclercb.taskunifier.gui.components.configuration.fields.proxy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUWorker;
import com.leclercb.taskunifier.gui.swing.TUWorkerDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.HttpUtils;

public class ProxyTestConnectionFieldType extends ConfigurationFieldType.Button {

    public ProxyTestConnectionFieldType(ConfigurationPanel panel) {
        super(Translations.getString("configuration.proxy.test_connection"), new TestConnection(panel));
    }

    private static class TestConnection implements ActionListener {

        private ConfigurationPanel panel;

        public TestConnection(ConfigurationPanel panel) {
            CheckUtils.isNotNull(panel);
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            this.panel.saveAndApplyConfig();
            TUWorkerDialog<Void> dialog = new TUWorkerDialog<Void>(MainFrame.getInstance().getFrame(), Translations.getString("configuration.proxy.test_connection"));
            ProgressMonitor monitor = new ProgressMonitor();
            monitor.addListChangeListener(dialog);
            dialog.setWorker(new TUWorker<Void>(monitor) {

                @Override
                protected Void longTask() throws Exception {
                    this.publish(new DefaultProgressMessage(Translations.getString("configuration.proxy.test_connection")));
                    try {
                        HttpUtils.getHttpGetResponse(new URI(Constants.TEST_CONNECTION));
                        TestConnection.this.showResult(true);
                    } catch (Throwable t) {
                        TestConnection.this.showResult(false);
                    }
                    return null;
                }
            });
            dialog.setVisible(true);
        }

        private void showResult(final boolean result) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (result) {
                        JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), Translations.getString("configuration.proxy.test_connection.success"), Translations.getString("general.information"), JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        ErrorInfo info = new ErrorInfo(Translations.getString("general.error"), Translations.getString("configuration.proxy.test_connection.failed"), null, null, null, null, null);
                        JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
                    }
                }
            });
        }
    }
}
