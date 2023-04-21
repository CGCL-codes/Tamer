package org.gdi3d.xnavi.panels.catalog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.media.ding3d.vecmath.Point2d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xmlbeans.XmlException;
import x0.oasisNamesTcEbxmlRegrepXsdRim3.ClassificationType;
import x0.oasisNamesTcEbxmlRegrepXsdRim3.InternationalStringType;
import x0.oasisNamesTcEbxmlRegrepXsdRim3.LocalizedStringType;
import x0.oasisNamesTcEbxmlRegrepXsdRim3.ServiceBindingType;
import x0.oasisNamesTcEbxmlRegrepXsdRim3.ServiceType;
import org.gdi3d.xnavi.listeners.TextFieldChangeListener;
import org.gdi3d.xnavi.navigator.Navigator;
import org.gdi3d.xnavi.panels.prefs.PreferencesFrame;
import org.gdi3d.xnavi.services.catalog.WebCatalogService_XMLBeans;
import org.gdi3d.xnavi.viewer.Java3DViewer;

public class CatalogSearchPanel extends JFrame implements ActionListener, MouseListener, ListSelectionListener {

    private static int FRAME_WIDTH = 500;

    public static String FILTER_ALL = "No Filter";

    public static String FILTER_WMS_1_1 = "urn:ogc:serviceType:WebMapService:1.1";

    public static String FILTER_WMS_1_3_0 = "urn:ogc:serviceType:WebMapService:1.3.0";

    public static String FILTER_WMST_0_3 = "urn:ogc:serviceType:WebMapService-Tiling:0.3";

    public static String FILTER_W3DS = "urn:ogc:serviceType:Web3DMapService";

    public static String FILTER_SOS_1_0 = "urn:ogc:serviceType:SensorObservationService:1.0";

    public static String FILTER_CSW_2_0_2 = "urn:ogc:serviceType:CatalogueService:2.0.2";

    public static String FILTER_CSW_2_0_2_EBRIM = "urn:ogc:serviceType:CatalogueService:2.0.2:HTTP:ebRIM";

    public static String FILTER_WCS_JPIP_0_9 = "urn:ogc:serviceType:WebCoverageService-JPIP:0.9";

    public static String FILTER_RS3D = "urn:ogc:serviceType:OpenLSRouteService";

    public static String FILTER_WFS_1_1 = "urn:ogc:serviceType:WebFeatureService:1.1";

    public static String FILTER_SPS_1_0 = "urn:ogc:serviceType:SensorPlanningService:1.0";

    public static final String[] FILTERS = { FILTER_ALL, FILTER_WMS_1_1, FILTER_WMS_1_3_0, FILTER_WMST_0_3, FILTER_W3DS, FILTER_SOS_1_0, FILTER_CSW_2_0_2, FILTER_CSW_2_0_2_EBRIM, FILTER_WCS_JPIP_0_9, FILTER_RS3D, FILTER_WFS_1_1, FILTER_SPS_1_0 };

    public static final String[] FILTER_NAMES = { "ALL", "WMS 1.1", "WMS 1.3", "WMTS", "W3DS", "SOS 1.0", "CSW 2.0.2", "CSW 2.0.2 ebRIM", "ECS JPIP 0.9", "RS3D", "WFS 1.1", "SPS 1.0" };

    private TextFieldChangeListener textFieldChangeListener;

    public void setTextFieldChangeListener(TextFieldChangeListener textFieldChangeListener) {
        this.textFieldChangeListener = textFieldChangeListener;
    }

    int margin = 8;

    int labelAndTextBoxHeight = 20;

    int lineDistance = 30;

    int label2LinesHeight = 30;

    private JList servicesList;

    private Vector<ServiceType> services;

    private JPanel serviceDescriptionPanel, selectionPanel;

    private JLabel serviceDescriptionLabel;

    private String activeFileter;

    private JComboBox filterComboBox;

    private JComboBox selectedServiceTypeComboBox;

    private JTextField selectionTextField;

    private JButton ApplyButton;

    private JButton OKButton;

    private JButton CancelButton;

    private JTextField targetTextField;

    private String catalogServiceURL;

    private PreferencesFrame preferencesFrame;

    public static void main(String[] args) {
        Navigator.initI18N("de");
        Navigator.webCatalogService = new WebCatalogService_XMLBeans("http://registry.wrs.galdosinc.com/ows6/query");
        CatalogSearchPanel catalogSearchPanel = new CatalogSearchPanel(null, Navigator.webCatalogService.getServiceEndPoint(), null, "urn:ogc:serviceType:WebMapService:1.1");
        catalogSearchPanel.repaint();
    }

    public CatalogSearchPanel(PreferencesFrame preferencesFrame, String catalogServiceURL, JTextField targetTextField, String filter) {
        super("Browse for Services in Catalog");
        this.preferencesFrame = preferencesFrame;
        this.catalogServiceURL = catalogServiceURL;
        this.targetTextField = targetTextField;
        this.activeFileter = filter;
        this.setSize(FRAME_WIDTH, 553);
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        int y = 0;
        JPanel filterPanel = createFilterPanel(FRAME_WIDTH - 2 * margin, 50);
        filterPanel.setBounds(0, y, FRAME_WIDTH - 2 * margin, 50);
        y += 50;
        this.add(filterPanel);
        JPanel servicesPanel = createServicesPanel(FRAME_WIDTH - 2 * margin, 150);
        servicesPanel.setBounds(0, y, FRAME_WIDTH - 2 * margin, 150);
        y += 150;
        this.add(servicesPanel);
        serviceDescriptionPanel = createServiceDescriptionPanel(FRAME_WIDTH - 2 * margin, 200);
        serviceDescriptionPanel.setBounds(0, y, FRAME_WIDTH - 2 * margin, 200);
        y += 210;
        this.add(serviceDescriptionPanel);
        selectionPanel = this.createSelectionPanel(FRAME_WIDTH - 8, 53);
        selectionPanel.setBounds(0, y, FRAME_WIDTH - 8, 53);
        y += 63;
        this.add(selectionPanel);
        JPanel okCancelPanel = createOkCancelPanel(FRAME_WIDTH - 8, 40);
        okCancelPanel.setBounds(0, y, FRAME_WIDTH - 2 * margin, 40);
        this.add(okCancelPanel);
        filterSelected(activeFileter);
    }

    private void filterSelected(String filter) {
        int index = -1;
        for (int i = 0; i < FILTERS.length; i++) {
            if (FILTERS[i] == filter) {
                index = i;
                break;
            }
        }
        filterComboBox.setSelectedIndex(index);
        String search = activeFileter;
        if (search == FILTER_ALL) search = null;
        WebCatalogService_XMLBeans webCatalogService = new WebCatalogService_XMLBeans(this.catalogServiceURL);
        services = webCatalogService.search(search);
        if (services != null) {
            Object[] data = new String[services.size()];
            for (int i = 0; i < services.size(); i++) {
                ServiceType service = services.get(i);
                String accessURI = getAccessURIFromService(service);
                String string = "";
                if (accessURI != null) {
                    string += accessURI.toString() + " ";
                } else {
                    String name = InternationalStringToString(service.getName());
                    if (name != null) {
                        string = name.toString();
                    }
                }
                data[i] = string;
            }
            servicesList.setListData(data);
            serviceDescriptionLabel.setText(null);
        }
    }

    public JPanel createFilterPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setBorder(new TitledBorder("Filter"));
        filterComboBox = new JComboBox(FILTERS);
        filterComboBox.addActionListener(this);
        filterComboBox.setBounds(10, 20, width - 20, height - 30);
        panel.add(filterComboBox);
        return panel;
    }

    public JPanel createServicesPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.white);
        panel.setBorder(new TitledBorder("Found Services"));
        servicesList = new JList();
        servicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        servicesList.setLayoutOrientation(JList.VERTICAL);
        servicesList.setVisibleRowCount(-1);
        servicesList.setBorder(null);
        servicesList.setBackground(new Color(240, 240, 240));
        servicesList.addListSelectionListener(this);
        JScrollPane scrollPane = new JScrollPane(servicesList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(5);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 20, width - 20, height - 30);
        scrollPane.setBorder(null);
        panel.add(scrollPane);
        return panel;
    }

    JScrollPane servicesScrollPane;

    public JPanel createServiceDescriptionPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Service Description"));
        serviceDescriptionLabel = new JLabel("");
        serviceDescriptionLabel.setBackground(Color.WHITE);
        servicesScrollPane = new JScrollPane(serviceDescriptionLabel);
        servicesScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        servicesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        servicesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        servicesScrollPane.setBounds(10, 20, width - 20, height - 30);
        servicesScrollPane.setBackground(Color.white);
        servicesScrollPane.setMinimumSize(new Dimension(width - 20, height - 30));
        servicesScrollPane.setPreferredSize(new Dimension(width - 20, height - 30));
        servicesScrollPane.setBorder(null);
        panel.add(servicesScrollPane);
        return panel;
    }

    public JPanel createSelectionPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("URL of Service Endpoint"));
        panel.setLayout(null);
        int cb_width = 100;
        selectionTextField = new JTextField();
        selectionTextField.setBounds(10, 20, width - 20 - cb_width - 5, height - 30);
        panel.add(selectionTextField);
        selectedServiceTypeComboBox = new JComboBox(FILTER_NAMES);
        selectedServiceTypeComboBox.setBounds(10 + width - 20 - cb_width, 20, cb_width, height - 30);
        panel.add(selectedServiceTypeComboBox);
        return panel;
    }

    public JPanel createOkCancelPanel(int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        int buttonWidth = 95;
        int buttonHeight = 30;
        int topMargin = 3;
        ApplyButton = new JButton(Navigator.i18n.getString("APPLY"));
        ApplyButton.addActionListener(this);
        ApplyButton.setBounds(width / 4 - buttonWidth / 2, topMargin, buttonWidth, buttonHeight);
        panel.add(ApplyButton);
        OKButton = new JButton(Navigator.i18n.getString("OK"));
        OKButton.addActionListener(this);
        OKButton.setBounds(width / 2 - buttonWidth / 2, topMargin, buttonWidth, buttonHeight);
        panel.add(OKButton);
        CancelButton = new JButton(Navigator.i18n.getString("CANCEL"));
        CancelButton.addActionListener(this);
        CancelButton.setBounds(width * 3 / 4 - buttonWidth / 2, topMargin, buttonWidth, buttonHeight);
        panel.add(CancelButton);
        return panel;
    }

    public void showServiceDescription(ServiceType service) {
        String html = "<html><head><div style=\"font-size:11pt\">" + "<table width=450>";
        if (service.getId() != null) {
            String id = service.getId().toString();
            html += "<tr><td><b>Catalog ID:</b></td></tr>";
            html += "<tr><td>" + id + "</td></tr>";
        }
        if (service.getName() != null) {
            String name = InternationalStringToString(service.getName());
            html += "<tr><td><b>Name:</b></td></tr>";
            html += "<tr><td>" + name + "</td></tr>";
        }
        String serviceType = FILTERS[0];
        ClassificationType[] getClassificationArray = service.getClassificationArray();
        if (getClassificationArray != null) {
            html += "<tr><td><b>Service Type:</b></td></tr>";
            for (int i = 0; i < getClassificationArray.length; i++) {
                serviceType = getClassificationArray[i].getClassificationNode().trim();
                html += "<tr><td>" + serviceType + "</td></tr>";
            }
        }
        String accessURI = getAccessURIFromService(service);
        if (accessURI != null) {
            html += "<tr><td><b>Access URI:</b></td></tr>";
            html += "<tr><td>" + accessURI.toString() + "</td></tr>";
            selectionTextField.setText(accessURI.toString());
        } else {
            selectionTextField.setText("no URI available");
        }
        int filter_index = -1;
        for (int i = 0; i < FILTERS.length; i++) {
            if (serviceType.equals(FILTERS[i])) {
                filter_index = i;
            }
        }
        selectedServiceTypeComboBox.setSelectedIndex(filter_index);
        if (service.getDescription() != null) {
            String description = InternationalStringToString(service.getDescription());
            html += "<tr><td><b>Description:</b></td></tr>";
            html += "<tr><td>" + description + "</td></tr>";
        }
        html += "</table>" + "</div></body></html>";
        serviceDescriptionLabel.setText(html);
    }

    private String InternationalStringToString(InternationalStringType internationalString) {
        String name = null;
        String name_en = null;
        String name_first = null;
        LocalizedStringType[] er = internationalString.getLocalizedStringArray();
        for (int i = 0; i < er.length; i++) {
            String lang = er[i].getLang();
            String value = er[i].getValue();
            if (name_first == null) {
                name_first = value;
            }
            if (lang.equals(Navigator.getLanguageCode())) {
                name = value;
                break;
            }
            if (lang.equals("en")) {
                name_en = value;
            }
        }
        if (name == null) {
            if (name_en != null) {
                name = name_en;
            } else name = name_first;
        }
        return name;
    }

    private String getAccessURIFromService(ServiceType service) {
        ServiceBindingType[] serviceBindings = service.getServiceBindingArray();
        String accessURI = null;
        for (int j = 0; j < serviceBindings.length; j++) {
            ServiceBindingType serviceBinding = serviceBindings[j];
            if (serviceBinding != null) {
                if (serviceBinding.getAccessURI() != null) {
                    accessURI = serviceBinding.getAccessURI();
                }
            }
        }
        return accessURI;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            int index = servicesList.getSelectedIndex();
            if (index == -1) {
            } else {
                this.showServiceDescription(services.get(index));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filterComboBox) {
            String filter = filterComboBox.getSelectedItem().toString();
            if (filter != this.activeFileter) {
                activeFileter = filter;
                this.filterSelected(activeFileter);
            }
        }
        if (e.getSource() == CancelButton) {
            this.dispose();
        }
        if (e.getSource() == this.OKButton) {
            if (targetTextField != null) {
                targetTextField.setText(selectionTextField.getText());
            }
            this.dispose();
            preferencesFrame.setCatalog_selected_serviceType(FILTERS[selectedServiceTypeComboBox.getSelectedIndex()]);
            if (this.textFieldChangeListener != null) {
                textFieldChangeListener.textFieldChanged(targetTextField);
            }
        }
        if (e.getSource() == this.ApplyButton) {
            if (targetTextField != null) {
                targetTextField.setText(selectionTextField.getText());
            }
            preferencesFrame.setCatalog_selected_serviceType(FILTERS[selectedServiceTypeComboBox.getSelectedIndex()]);
            if (this.textFieldChangeListener != null) {
                textFieldChangeListener.textFieldChanged(targetTextField);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
