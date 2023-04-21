package eu.more.measurementservicegui.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import org.apache.log4j.Logger;
import eu.more.JXTAErrorTransmissionService.generated.JXTAErrorTransmissionServiceInvoker;
import eu.more.measurementservicegui.logic.LogMessageControllerImpl;
import eu.more.measurementservicegui.model.ServiceConstants;
import eu.more.measurementservicegui.model.log.LogDataTable;
import eu.more.measurementservicegui.model.log.LogMessageConstants;
import eu.more.measurementservicegui.model.log.LogMessageModel;
import eu.more.measurementservicegui.resources.GeneralMessages;
import eu.more.measurementservicegui.resources.LogMessages;

public class LogMessagePanel extends JPanel implements Observer {

    private static final long serialVersionUID = 3216180489087845003L;

    private final LogMessageControllerImpl controller;

    private final LogMessageModel model;

    private final JFrame parent;

    private JDialog dialog;

    private JPanel servicePanel = null;

    private JPanel messagePanel = null;

    private JPanel requestPanel = null;

    private JButton discoverButton = null;

    private JButton requestButton = null;

    private JComboBox serviceComboBox = null;

    private Insets defaultInsets = new Insets(0, 10, 5, 0);

    private JFormattedTextField numberOfLogMessagesTextField = null;

    private Logger logger = Logger.getLogger(LogMessagePanel.class);

    private LogDataTable logTable;

    private LogMessageTableModel dataModel;

    private Preferences pref;

    public LogMessagePanel(LogMessageControllerImpl logMessageControllerImpl, LogMessageModel model, JFrame parent) {
        this.controller = logMessageControllerImpl;
        this.model = model;
        this.parent = parent;
        model.addObserver(this);
        initialize();
        setEnabled();
        setVisible(true);
        controller.invokeServiceDiscovery();
    }

    public void update(Observable o, Object arg) {
        System.out.println("Retrieving an model update");
        if (!(o instanceof LogMessageModel)) {
            return;
        }
        logger.info("Retrieving an model update");
        if (arg instanceof ServiceConstants) {
            ServiceConstants controllingArg = (ServiceConstants) arg;
            if (controllingArg == ServiceConstants.REQUEST_SUCCESS) {
                JOptionPane.showMessageDialog(this, LogMessages.getString("LogMessages.Text.RequestSuccessful"), GeneralMessages.getString("GeneralMessages.Title.RequestSuccessful"), JOptionPane.INFORMATION_MESSAGE);
                dataModel.setData(model.getMessages());
            } else if (controllingArg == ServiceConstants.REQUEST_FAILED) {
                JOptionPane.showMessageDialog(this, GeneralMessages.getString("GeneralMessages.Message.RequestFailed"), GeneralMessages.getString("GeneralMessages.Title.RequestFailed"), JOptionPane.ERROR_MESSAGE);
            } else if (controllingArg == ServiceConstants.REQUEST_FAILED_NO_INVOKER) {
                JOptionPane.showMessageDialog(this, LogMessages.getString("LogMessages.Title.NoServiceSelected"), LogMessages.getString("LogMessages.Text.NoServiceSelected"), JOptionPane.ERROR_MESSAGE);
            } else if (controllingArg == ServiceConstants.SERVICE_DISCOVERY_FAILED) {
                if (dialog != null) dialog.dispose();
                JOptionPane.showMessageDialog(this, LogMessages.getString("LogMessages.Text.DiscoveryFailed"), GeneralMessages.getString("GeneralMessages.Title.DiscoveryFailed"), JOptionPane.ERROR_MESSAGE);
                fillServiceComboBox();
                setEnabled();
            } else if (controllingArg == ServiceConstants.SERVICE_DISCOVERY_SUCCESS) {
                if (dialog != null) dialog.dispose();
                fillServiceComboBox();
                setEnabled();
            } else if (controllingArg == ServiceConstants.SERVICE_DISCOVERY_STARTED) {
                discoverButton.setEnabled(false);
                dialog = new JDialog(parent, GeneralMessages.getString("GeneralMessages.Title.DiscoveryInProgress"), true);
                final JOptionPane pane = new JOptionPane(GeneralMessages.getString("GeneralMessages.Message.DiscoveryInProgress"), JOptionPane.INFORMATION_MESSAGE);
                pane.addPropertyChangeListener(new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent e) {
                        if (dialog.isVisible() && (e.getSource() == pane) && (((Integer) pane.getValue()).intValue() == JOptionPane.OK_OPTION)) {
                            dialog.setVisible(false);
                        }
                    }
                });
                dialog.setContentPane(pane);
                dialog.setLocationRelativeTo(null);
                dialog.pack();
                dialog.setVisible(true);
                dialog.repaint();
            }
        } else if (arg instanceof LogMessageConstants) {
            @SuppressWarnings("unused") LogMessageConstants argCon = (LogMessageConstants) arg;
        }
    }

    private void initialize() {
        GridBagLayout layoutMng = new GridBagLayout();
        setLayout(layoutMng);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 0;
        constraints.gridy = 0;
        add(createServicePanel(), constraints);
        constraints.weighty = 1.0;
        constraints.gridy = 1;
        add(createMessagePanel(), constraints);
        constraints.weighty = 0;
        constraints.gridy = 2;
        add(createRequestPanel(), constraints);
        try {
            pref = Preferences.userNodeForPackage(LogMessagePanel.class);
        } catch (Exception e) {
            logger.info("While loading preferences the error" + e.getMessage() + " occures. Continuing ...!");
        }
    }

    private JPanel createRequestPanel() {
        if (requestPanel == null) {
            requestPanel = new JPanel();
            requestPanel.setBorder(new TitledBorder(LogMessages.getString("LogMessages.Title.RequestPanel")));
            GridBagLayout layoutMng = new GridBagLayout();
            requestPanel.setLayout(layoutMng);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = defaultInsets;
            constraints.gridx = 0;
            constraints.gridy = 0;
            requestPanel.add(new JLabel(LogMessages.getString("LogMessages.Text.ResultSize")), constraints);
            constraints.gridx = 1;
            try {
                NumberFormatter format = new NumberFormatter();
                numberOfLogMessagesTextField = new JFormattedTextField(format);
                numberOfLogMessagesTextField.setColumns(5);
                if (pref == null) numberOfLogMessagesTextField.setText("5"); else numberOfLogMessagesTextField.setText(pref.get("numberOfLogs", "5"));
                requestPanel.add(numberOfLogMessagesTextField, constraints);
            } catch (Exception e) {
                logger.warn(e);
            }
            constraints.gridx = 2;
            constraints.weightx = 1;
            requestButton = new JButton(LogMessages.getString("LogMessages.Text.RequestButton"));
            requestButton.addActionListener(new InnerActionListener());
            requestPanel.add(requestButton, constraints);
        }
        return requestPanel;
    }

    private JPanel createMessagePanel() {
        if (messagePanel == null) {
            messagePanel = new JPanel();
            messagePanel.setBorder(new TitledBorder(LogMessages.getString("LogMessages.Title.LogPanel")));
            GridBagLayout layoutMng = new GridBagLayout();
            messagePanel.setLayout(layoutMng);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = defaultInsets;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            dataModel = new LogMessageTableModel();
            logTable = new LogDataTable(dataModel);
            JScrollPane scrollPane = new JScrollPane(logTable);
            logTable.setFillsViewportHeight(true);
            messagePanel.add(scrollPane, constraints);
        }
        return messagePanel;
    }

    private JPanel createServicePanel() {
        if (servicePanel == null) {
            servicePanel = new JPanel();
            servicePanel.setBorder(new TitledBorder(GeneralMessages.getString("GeneralMessages.Title.DiscoveryPanel")));
            GridBagLayout layoutMng = new GridBagLayout();
            servicePanel.setLayout(layoutMng);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = defaultInsets;
            discoverButton = new JButton(GeneralMessages.getString("GeneralMessages.Text.DiscoveryButton"));
            discoverButton.addActionListener(new InnerActionListener());
            constraints.gridx = 0;
            constraints.gridy = 0;
            servicePanel.add(discoverButton, constraints);
            serviceComboBox = new JComboBox();
            serviceComboBox.addItem(GeneralMessages.getString("GeneralMessages.Text.ServiceBox"));
            constraints.gridx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1;
            servicePanel.add(serviceComboBox, constraints);
        }
        return servicePanel;
    }

    private void fillServiceComboBox() {
        HashMap<String, JXTAErrorTransmissionServiceInvoker> services = model.getServices();
        serviceComboBox.removeAllItems();
        if (services.size() == 0) {
            serviceComboBox.addItem(GeneralMessages.getString("GeneralMessages.Text.ServiceBox"));
        } else {
            serviceComboBox.addItem(GeneralMessages.getString("GeneralMessages.Text.ServiceBox"));
            Set<String> keySet = services.keySet();
            for (String currentKey : keySet) {
                serviceComboBox.addItem(currentKey);
            }
            if (services.size() == 1) serviceComboBox.setSelectedIndex(1);
        }
        System.out.println("....");
    }

    private void setEnabled() {
        serviceComboBox.setEnabled(!model.getCurrentState());
        requestButton.setEnabled(!model.getCurrentState());
        discoverButton.setEnabled(true);
    }

    class InnerActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == discoverButton) {
                controller.invokeServiceDiscovery();
            } else if (source == requestButton) {
                String text = numberOfLogMessagesTextField.getText();
                if (pref != null) pref.put("numberOfLogs", text);
                if (text.trim().length() == 0) text = "0";
                controller.invokeGetLogMessages(Integer.parseInt(text), (String) serviceComboBox.getSelectedItem());
            }
        }
    }
}
