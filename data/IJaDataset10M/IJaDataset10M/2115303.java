package com.inetmon.jn.healthmonitor.extraViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.inetmon.jn.healthmonitor.Activator;
import com.inetmon.jn.healthmonitor.core.DataManager;
import com.inetmon.jn.healthmonitor.data.ServerInformations;
import com.inetmon.jn.healthmonitor.data.Service;
import com.inetmon.jn.healthmonitor.data.ServiceInformations;
import com.inetmon.jn.healthmonitor.listener.ButtonSelectionListener;
import com.inetmon.jn.healthmonitor.listener.ComboSelectionListener;
import com.inetmon.jn.healthmonitor.listener.TextListener;
import com.inetmon.jn.healthmonitor.listener.TextModifyListener;
import com.inetmon.jn.healthmonitor.views.ServerView;

/**
 * UI design for search page
 * 
 * @author inetmon
 */
public class EditServicePage extends WizardPage {

    private String[] ipv4 = new String[Service.getIpv4Nb()];

    private String[] ipv6 = new String[Service.getIpv6Nb()];

    private Label[] ipv4Label = new Label[Service.getIpv4Nb()];

    private Label[] ipv6Label = new Label[Service.getIpv6Nb()];

    private Text[] ipv4Text = new Text[Service.getIpv4Nb()];

    private Text[] ipv6Text = new Text[Service.getIpv6Nb()];

    private Text serviceNameText;

    private String serviceName;

    /**
	 * @uml.property name="mac"
	 */
    private String mac;

    private String macTemplate = "00:00:00:00:00:00";

    private String ipTemplate;

    private String netbios = "";

    private String workgroup = "";

    private String os = "";

    private String location = "";

    private String description = "Updated";

    private String port = "";

    private int serviceInformationsId;

    private Group informationsGroup;

    private Group macGroup;

    private Group nodeGroup;

    private Label macLabel;

    private Label serviceLabel;

    private Label toServiceLabel;

    private Label netbiosLabel;

    private Label osLabel;

    private Label descriptionLabel;

    private Label workgroupLabel;

    private Label locationLabel;

    /**
	 * @uml.property name="macText"
	 */
    private Text macText;

    private Text netbiosText;

    private Text osText;

    private Text descriptionText;

    private Text locationText;

    /**
	 * @uml.property name="portText"
	 */
    private Text portText;

    /**
	 * @uml.property name="serviceText"
	 */
    private Text serviceText;

    private Text workgroupText;

    private Text subnetText;

    private Label portLabel;

    private Label serviceNameLabel;

    /**
	 * @uml.property name="checkBox"
	 */
    private Button checkBox;

    /**
	 * @uml.property name="toServiceCombo"
	 */
    private Combo toServiceCombo;

    private Shell shell;

    private Service service;

    private ServerInformations serverInfo;

    private static EditServicePage editServerPage;

    /**
	 * @uml.property name="serverFind"
	 */
    private boolean serverFind = false;

    public final int SEARCH = 1;

    public final int CHECKBOX = 0;

    private DataManager dataManager;

    private int serviceID;

    /**
	 * EditServerPage constructor set the windows parameters and initialize
	 * variables
	 */
    public EditServicePage(int serviceId) {
        super("View/Edit window", "View/Edit", null);
        setDescription("View/Edit server informations");
        dataManager = Activator.getDefault().getDataManager();
        setImageDescriptor(Activator.getDefault().getImageRegistry().getDescriptor(Integer.toString(Activator.EDIT_SERVER)));
        setPageComplete(true);
        ipTemplate = "000.000.000.000";
        service = dataManager.getServiceList().findServiceById(serviceId);
        serverInfo = service.getServerInformations();
        if (service == null) service = new Service();
        editServerPage = this;
        serviceID = serviceId;
    }

    /**
	 * Creates the page contents
	 * 
	 * @param parent
	 *            the parent composite
	 */
    public void createControl(Composite parent) {
        Composite page = new Composite(parent, SWT.NONE);
        FormLayout formLayout = new FormLayout();
        formLayout.marginBottom = 2;
        formLayout.marginTop = 5;
        formLayout.marginLeft = 5;
        formLayout.marginRight = 5;
        formLayout.spacing = 5;
        page.setLayout(formLayout);
        macGroup = new Group(page, SWT.NONE);
        macGroup.setText("Server");
        macGroup.setLayout(new GridLayout(6, false));
        FormData macGroupData = new FormData();
        macGroupData.width = 610;
        macGroup.setLayoutData(macGroupData);
        GridData data = new GridData();
        data.widthHint = 120;
        macLabel = new Label(macGroup, SWT.LEFT);
        macLabel.setText("MAC Address :");
        macText = new Text(macGroup, SWT.BORDER);
        if (!service.getServerInformations().getMac().trim().equalsIgnoreCase("")) macText.setText(service.getServerInformations().getMac()); else macText.setText(macTemplate);
        macText.setTextLimit(20);
        macText.setLayoutData(data);
        macText.addListener(SWT.Verify, new TextListener(macText));
        macText.addModifyListener(new TextModifyListener(this));
        serviceLabel = new Label(macGroup, SWT.LEFT);
        serviceLabel.setText("Server type :");
        serviceText = new Text(macGroup, SWT.BORDER);
        GridData typeComboData = new GridData();
        serviceText.setText(service.getServiceInformations().getServiceName());
        serviceText.setEditable(false);
        toServiceLabel = new Label(macGroup, SWT.CENTER);
        toServiceLabel.setText(" Change to :");
        toServiceCombo = new Combo(macGroup, SWT.READ_ONLY);
        GridData toServiceComboData = new GridData();
        setComboService();
        createNodeGroup(page);
        createInformationsGroup(page);
        checkBox = new Button(page, SWT.CHECK);
        checkBox.setText("To edit service common informations");
        checkBox.setSelection(true);
        FormData checkBoxData = new FormData();
        checkBoxData.top = new FormAttachment(nodeGroup);
        FormData nodeGroupData = new FormData();
        nodeGroupData.top = new FormAttachment(macGroup);
        nodeGroupData.height = 173;
        FormData informationsGroupData = new FormData();
        informationsGroupData.top = new FormAttachment(macGroup);
        informationsGroupData.left = new FormAttachment(nodeGroup);
        nodeGroup.setLayoutData(nodeGroupData);
        informationsGroup.setLayoutData(informationsGroupData);
        checkBox.setLayoutData(checkBoxData);
        serviceText.setLayoutData(typeComboData);
        toServiceCombo.setLayoutData(toServiceComboData);
        displayCombosInformations();
        toServiceCombo.addSelectionListener(new ComboSelectionListener());
        checkBox.addSelectionListener(new ButtonSelectionListener());
        editServerPage.setControl(page);
    }

    private void createNodeGroup(Composite parent) {
        nodeGroup = new Group(parent, SWT.NONE);
        nodeGroup.setText("Node informations");
        GridLayout nodeLayout = new GridLayout(2, false);
        nodeGroup.setLayout(nodeLayout);
        GridData data = new GridData();
        data.widthHint = 128;
        ipv4 = service.getIpv4();
        for (int i = 0; i < Service.getIpv4Nb(); i++) {
            ipv4Label[i] = new Label(nodeGroup, SWT.LEFT);
            ipv4Label[i].setText("IPV4 Address " + (i + 1) + " :");
            ipv4Text[i] = new Text(nodeGroup, SWT.BORDER);
            ipv4Text[i].setTextLimit(20);
            if (i >= ipv4.length || ipv4[i] == null || ipv4[i].equals(";;;;") || ipv4[i].equals("")) {
                ipv4Text[i].setText(ipTemplate);
            } else ipv4Text[i].setText(ipv4[i]);
            ipv4Text[i].setLayoutData(data);
            ipv4Text[i].addListener(SWT.Verify, new TextListener(ipv4Text[i]));
            ipv4Text[i].addModifyListener(new TextModifyListener(this));
        }
        ipv6 = service.getIpv6();
        for (int i = 0; i < Service.getIpv6Nb(); i++) {
            ipv6Label[i] = new Label(nodeGroup, SWT.LEFT);
            ipv6Label[i].setText("IPV6 Address " + (i + 1) + " :");
            ipv6Text[i] = new Text(nodeGroup, SWT.BORDER);
            ipv6Text[i].setTextLimit(20);
            if (i >= ipv6.length || ipv6[i] == null || ipv6[i].equals(";;;;") || ipv6[i].equals("")) {
                ipv6Text[i].setText(ipTemplate);
            } else ipv6Text[i].setText(ipv6[i]);
            ipv6Text[i].setLayoutData(data);
            ipv6Text[i].addModifyListener(new TextModifyListener(this));
        }
        portLabel = new Label(nodeGroup, SWT.LEFT);
        portLabel.setText("Port :");
        portText = new Text(nodeGroup, SWT.BORDER);
        portText.setTextLimit(20);
        portText.setLayoutData(data);
        portText.setText(service.getTablePort());
        portText.addModifyListener(new TextModifyListener(this));
    }

    private void createInformationsGroup(Composite parent) {
        informationsGroup = new Group(parent, SWT.NONE);
        informationsGroup.setText("Server informations");
        informationsGroup.setLayout(new GridLayout(2, false));
        GridData data = new GridData();
        data.widthHint = 263;
        serviceNameLabel = new Label(informationsGroup, SWT.LEFT);
        serviceNameLabel.setText("Service Name :");
        serviceNameText = new Text(informationsGroup, SWT.BORDER);
        serviceNameText.setTextLimit(64);
        serviceNameText.setLayoutData(data);
        serviceNameText.setText(service.getServiceName());
        netbiosLabel = new Label(informationsGroup, SWT.LEFT);
        netbiosLabel.setText("NetBios Name :");
        netbiosText = new Text(informationsGroup, SWT.BORDER);
        netbiosText.setTextLimit(64);
        netbiosText.setLayoutData(data);
        netbiosText.setText(service.getServerInformations().getNetbios());
        osLabel = new Label(informationsGroup, SWT.LEFT);
        osLabel.setText("OS:");
        osText = new Text(informationsGroup, SWT.BORDER);
        osText.setTextLimit(64);
        osText.setLayoutData(data);
        osText.setText(service.getServerInformations().getOs());
        descriptionLabel = new Label(informationsGroup, SWT.LEFT);
        descriptionLabel.setText("Description:");
        descriptionText = new Text(informationsGroup, SWT.BORDER);
        descriptionText.setTextLimit(64);
        descriptionText.setLayoutData(data);
        descriptionText.setText(service.getServerInformations().getDescription());
        locationLabel = new Label(informationsGroup, SWT.LEFT);
        locationLabel.setText("Location:");
        locationText = new Text(informationsGroup, SWT.BORDER);
        locationText.setTextLimit(64);
        locationText.setLayoutData(data);
        locationText.setText(service.getServerInformations().getLocation());
        workgroupLabel = new Label(informationsGroup, SWT.LEFT);
        workgroupLabel.setText("Workgroup:");
        workgroupText = new Text(informationsGroup, SWT.BORDER);
        workgroupText.setTextLimit(64);
        workgroupText.setLayoutData(data);
        workgroupText.setText(service.getServerInformations().getWorkgroup());
        netbiosText.addModifyListener(new TextModifyListener(this));
        osText.addModifyListener(new TextModifyListener(this));
        workgroupText.addModifyListener(new TextModifyListener(this));
        locationText.addModifyListener(new TextModifyListener(this));
        descriptionText.addModifyListener(new TextModifyListener(this));
    }

    /**
	 * Display server informations inside informations group text box.
	 */
    public void displayservice() {
        serviceNameText.setText(service.getServiceName());
        serviceText.setText(service.getServiceInformations().getServiceName());
        netbiosText.setText(service.getServerInformations().getNetbios());
        netbios = service.getServerInformations().getNetbios();
        descriptionText.setText(service.getServerInformations().getDescription());
        description = service.getServerInformations().getDescription();
        locationText.setText(service.getServerInformations().getLocation());
        location = service.getServerInformations().getLocation();
        osText.setText(service.getServerInformations().getOs());
        os = service.getServerInformations().getOs();
        workgroupText.setText(service.getServerInformations().getWorkgroup());
        workgroup = service.getServerInformations().getWorkgroup();
    }

    /**
	 * Display server informations inside node group text box.
	 */
    public void displayNodeInformations() {
        ipv6 = service.getIpv6();
        portText.setText(service.getTablePort());
        port = service.getTablePort();
        ipv4 = service.getIpv4();
        for (int i = 0; i < service.getIpv4().length; i++) {
            if (ipv4[i] == null || ipv4[i].equals(";;;;") || ipv4[i].equals("")) {
                ipv4Text[i].setText(ipTemplate);
            } else ipv4Text[i].setText(ipv4[i]);
        }
        for (int i = 0; i < service.getIpv6().length; i++) {
            if (ipv6[i] == null || ipv6[i].equals(";;;;") || ipv6[i].equals("")) {
                ipv6Text[i].setText(ipTemplate);
            } else ipv6Text[i].setText(ipv6[i]);
        }
        return;
    }

    /**
	 * Display the server type list and select the server type.
	 */
    public void displayCombosInformations() {
        toServiceCombo.setItems(dataManager.getServiceInformationsList().getServiceNameList());
        String currentSelection = serviceText.getText();
        String item;
        for (int i = 0; i < toServiceCombo.getItemCount(); i++) {
            item = toServiceCombo.getItem(i);
            if (item.equals(currentSelection)) {
                toServiceCombo.select(i);
                serviceInformationsId = dataManager.getServiceInformationsList().findServiceInformations(toServiceCombo.getItem(toServiceCombo.getSelectionIndex())).getId();
                return;
            }
        }
    }

    /**
	 * Keep the ipv6, ipv4 and port informations in text box but disable them.
	 */
    public void disableNodeGroup() {
        displayNodeInformations();
        portText.setEnabled(false);
        for (int i = 0; i < Service.getIpv4Nb(); i++) {
            ipv4Text[i].setEnabled(false);
        }
        for (int i = 0; i < Service.getIpv6Nb(); i++) {
            ipv6Text[i].setEnabled(false);
        }
        subnetText.setEnabled(false);
    }

    /**
	 * Remove all server type from the comboBox and disable it.
	 */
    public void disableCombo() {
        toServiceCombo.setEnabled(false);
        toServiceCombo.removeAll();
    }

    /**
	 * Enable all node group compoments and display their informations .
	 */
    public void enableNodeGroup() {
        portText.setEnabled(true);
        for (int i = 0; i < Service.getIpv4Nb(); i++) {
            ipv4Text[i].setEnabled(true);
        }
        for (int i = 0; i < Service.getIpv6Nb(); i++) {
            ipv6Text[i].setEnabled(true);
        }
        toServiceCombo.setEnabled(true);
        displayNodeInformations();
    }

    /**
	 * Enable combo type and load server type inside
	 */
    public void enableCombos() {
        toServiceCombo.setEnabled(true);
        displayCombosInformations();
    }

    /**
	 * Store text components informations inside variables
	 */
    private void storeInformations() {
        mac = macText.getText();
        serviceName = serviceNameText.getText();
        netbios = netbiosText.getText();
        location = locationText.getText();
        os = osText.getText();
        workgroup = workgroupText.getText();
        port = portText.getText();
        description = descriptionText.getText();
        ipv4 = new String[Service.getIpv4Nb()];
        for (int i = 0; i < Service.getIpv4Nb(); i++) {
            try {
                ipv4[i] = ipv4Text[i].getText();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("i is :" + i);
            }
        }
        ipv6 = new String[Service.getIpv6Nb()];
        for (int i = 0; i < Service.getIpv6Nb(); i++) {
            try {
                ipv6[i] = ipv6Text[i].getText();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("i is :" + i);
            }
        }
        serviceInformationsId = dataManager.getServiceInformationsList().findServiceInformations(toServiceCombo.getItem(toServiceCombo.getSelectionIndex())).getId();
    }

    /**
	 * Edit server informations method. Store the new server informations
	 */
    public boolean editServer() {
        storeInformations();
        if (!checkIPBox()) {
            System.out.println("gone !!");
            return false;
        }
        if (FonctionIPV4.isValidIPV4(ipv4)) {
            for (int i = 0; i < 4; i++) {
                if (!ipv4[i].equals(ipTemplate)) setIpv4(FonctionIPV4.removeZero(ipv4[i]), i); else ipv4[i] = "";
            }
            java.util.Date date1 = new java.util.Date();
            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            String dateNow = dateFormat.format(date1);
            if (dateNow.startsWith("0")) {
                dateNow = dateNow.substring(1, dateNow.length());
            }
            String[] ipv4Temp = ipv4;
            String[] subNet = new String[4];
            int[] lastpoint = new int[4];
            for (int i = 0; i < 4; i++) {
                lastpoint[i] = ipv4Temp[i].lastIndexOf(".");
                if (lastpoint[i] != -1) {
                    String debutchaine = ipv4Temp[i].substring(0, lastpoint[i]);
                    subNet[i] = debutchaine + ".0";
                } else {
                    subNet[i] = "";
                }
            }
            Service service = dataManager.getServiceList().findServiceByName(serviceName, netbios);
            if (service != null && !serviceText.getText().trim().equals(toServiceCombo.getItem(toServiceCombo.getSelectionIndex()).trim())) {
                MessageDialog.openInformation(shell, "Warning informations", "Unable to update since this service already existed");
                return false;
            }
            ServiceInformations serviceInfo = dataManager.getServiceInformationsList().findServiceInformations(serviceInformationsId);
            Service serviceTemp = dataManager.getServiceList().findServiceById(serviceID);
            serverInfo = dataManager.getServerInformationsList().findServerInformationsByID(serviceTemp.getServerInformations().getId());
            if (serverInfo != null) {
                serverInfo.setServerInformations(new ServerInformations(serverInfo.getId(), mac, netbios, os, workgroup, location, description));
                dataManager.updateServerInformations(serverInfo);
            }
            service = new Service(service.getId(), serviceName, port, ipv4, ipv6, serverInfo, serviceInfo);
            if (checkBox.getSelection()) {
                dataManager.updateService(service);
                dataManager.updateIPv4(service);
                dataManager.updateIPv6(service);
            }
            ServerView.UpdateServiceElements(service);
            return true;
        } else {
            MessageDialog.openWarning(shell, "Invalid IPV4", "The field IPV4 is invalid");
            return false;
        }
    }

    /**
	 * Check if all ip address Text box have correct ip address return true if
	 * all are correct, else show a error message and return false.
	 * 
	 * @return boolean
	 */
    private boolean checkIPBox() {
        String template = "000.000.000.000";
        if (ipv4Text[1].getText().equals(template) && ipv4Text[1].isEnabled()) ipv4Text[1].setEnabled(false);
        if (ipv4Text[2].getText().equals(template) && ipv4Text[2].isEnabled()) ipv4Text[2].setEnabled(false);
        if (ipv4Text[3].getText().equals(template) && ipv4Text[3].isEnabled()) ipv4Text[3].setEnabled(false);
        if (!ipv4Text[2].isEnabled() && !ipv4Text[3].isEnabled() && ipv4Text[1].isEnabled()) {
            if (ipv4Text[0].getText().equals(ipv4Text[1].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1 and 2 have the same IP");
                return false;
            }
        }
        if (!ipv4Text[3].isEnabled() && ipv4Text[2].isEnabled() && ipv4Text[1].isEnabled()) {
            if (ipv4Text[0].getText().equals(ipv4Text[1].getText()) && ipv4Text[0].getText().equals(ipv4Text[2].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1,2 and 3 have the same IP");
                return false;
            }
            if (ipv4Text[0].getText().equals(ipv4Text[2].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1 and 3 have the same IP");
                return false;
            }
            if (ipv4Text[0].getText().equals(ipv4Text[1].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1 and 2 have the same IP");
                return false;
            }
            if (ipv4Text[1].getText().equals(ipv4Text[2].getText())) {
                errorMessage("Ipv4 error", "IP Adress 2 and 3 have the same IP");
                return false;
            }
        }
        if (ipv4Text[3].isEnabled() && ipv4Text[2].isEnabled() && ipv4Text[1].isEnabled()) {
            if (ipv4Text[0].getText().equals(ipv4Text[1].getText()) && ipv4Text[0].getText().equals(ipv4Text[2].getText()) && ipv4Text[0].getText().equals(ipv4Text[3].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1,2,3 and 4 have the same IP");
                return false;
            }
            if (ipv4Text[0].getText().equals(ipv4Text[1].getText()) && ipv4Text[0].getText().equals(ipv4Text[2].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1,2 and 3 have the same IP");
                return false;
            }
            if (ipv4Text[1].getText().equals(ipv4Text[2].getText()) && ipv4Text[1].getText().equals(ipv4Text[3].getText())) {
                errorMessage("Ipv4 error", "IP Adress 2,3 and 4 have the same IP");
                return false;
            }
            if (ipv4Text[0].getText().equals(ipv4Text[3].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1 and 4 have the same IP");
                return false;
            }
            if (ipv4Text[0].getText().equals(ipv4Text[2].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1 and 3 have the same IP");
                return false;
            }
            if (ipv4Text[0].getText().equals(ipv4Text[1].getText())) {
                errorMessage("Ipv4 error", "IP Adress 1 and 2 have the same IP");
                return false;
            }
            if (ipv4Text[1].getText().equals(ipv4Text[2].getText())) {
                errorMessage("Ipv4 error", "IP Adress 2 and 3 have the same IP");
                return false;
            }
            if (ipv4Text[1].getText().equals(ipv4Text[3].getText())) {
                errorMessage("Ipv4 error", "IP Adress 2 and 4 have the same IP");
                return false;
            }
            if (ipv4Text[1].getText().equals(ipv4Text[3].getText())) {
                errorMessage("Ipv4 error", "IP Adress 3 and 4 have the same IP");
                return false;
            }
        }
        return true;
    }

    public void setIpv4(String ipv4Address, int ipv4Num) {
        ipv4[ipv4Num] = ipv4Address;
    }

    public void setIpv6(String ipv6Address, int ipv6Num) {
        ipv4[ipv6Num] = ipv6Address;
    }

    private void setComboService() {
        toServiceCombo.setItems(dataManager.getServiceInformationsList().getServiceNameList());
    }

    private void errorMessage(String message1, String message2) {
        MessageDialog.openWarning(shell, message1, message2);
    }

    /**
	 * @param macAdress
	 * @uml.property name="mac"
	 */
    public void setMac(String macAdress) {
        mac = macAdress;
    }

    /**
	 * @return
	 * @uml.property name="macText"
	 */
    public Text getMacText() {
        return macText;
    }

    /**
	 * @return
	 * @uml.property name="serviceText"
	 */
    public Text getServiceText() {
        return serviceText;
    }

    /**
	 * @return
	 * @uml.property name="toServiceCombo"
	 */
    public Combo getToServiceCombo() {
        return toServiceCombo;
    }

    /**
	 * @return
	 * @uml.property name="portText"
	 */
    public Text getPortText() {
        return portText;
    }

    /**
	 * @return
	 * @uml.property name="checkBox"
	 */
    public Button getCheckBox() {
        return checkBox;
    }

    /**
	 * @return
	 * @uml.property name="serverFind"
	 */
    public boolean getServerFind() {
        return serverFind;
    }

    public static EditServicePage getDefault() {
        return editServerPage;
    }
}
