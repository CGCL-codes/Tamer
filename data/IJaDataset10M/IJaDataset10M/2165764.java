package ui;

import static java.lang.Math.random;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import ui.model.TableListModel;
import databaseConnect.HostConfigurationRequestProcessor;
import databaseConnect.HostConfigurationUpdateOrInsertProcessor;

@SuppressWarnings("serial")
public class HostOriginatorIFrame extends JInternalFrame {

    private static String windowTitle = "neuen Host erstellen";

    private Vector<String> header = null;

    private Vector<Vector<String>> rows = null;

    private JTable keyValueList = null;

    private mySouth myS;

    private JScrollPane myC;

    private myToolbar toolbar;

    private BorderLayout layout = new BorderLayout();

    private int id = 0;

    class myToolbar extends JToolBar {

        private JButton saveScenario = null;

        private String saveScenarioButtonTitle = "Speichern";

        private JButton loadScenario = null;

        private String loadScenarioButtonTitle = "Laden";

        private JButton addService = null;

        private String addServiceButtonTitle = "add Service";

        public myToolbar() {
            super();
            this.add(this.getSaveButton());
            this.add(this.getLoadButton());
            this.add(this.getAddServiceButton());
        }

        private JButton getSaveButton() {
            if (saveScenario == null) {
                saveScenario = new JButton(saveScenarioButtonTitle);
                saveScenario.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        HostConfigurationUpdateOrInsertProcessor.setHostParameterRows(getKeyValueListContent());
                        HostConfigurationUpdateOrInsertProcessor.setHostServices(myS.getServices(), getID());
                    }
                });
            }
            return saveScenario;
        }

        private JButton getLoadButton() {
            if (loadScenario == null) {
                loadScenario = new JButton(loadScenarioButtonTitle);
                loadScenario.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        SimulationDesktop.getInstance().add(new ScenarioOriginatorIFrame());
                    }
                });
            }
            return loadScenario;
        }

        private JButton getAddServiceButton() {
            if (addService == null) {
                addService = new JButton(addServiceButtonTitle);
                addService.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        myS.addService();
                    }
                });
            }
            return addService;
        }
    }

    class mySouth extends JSplitPane {

        private JTable NeighbourList = null;

        private JTable ServiceList = null;

        public mySouth(int ID, int width, int height) {
            super();
            JScrollPane myR = new JScrollPane(this.getServiceList(ID));
            this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            this.setRightComponent(myR);
            this.setDividerLocation(200);
        }

        private JTable getServiceList(int hostID) {
            if (ServiceList == null) {
                Vector<String> tmp = new Vector<String>();
                tmp.add("Services");
                ServiceList = new JTable(new TableListModel(HostConfigurationRequestProcessor.getHostServiceRows(hostID), tmp));
            }
            return ServiceList;
        }

        public void addService() {
            ((TableListModel) ServiceList.getModel()).addRow();
            ServiceList.updateUI();
        }

        public JTable getNeighbourList() {
            return NeighbourList;
        }

        public Vector<String> getServices() {
            if (((TableListModel) ServiceList.getModel()).getAllValues().size() > 0) {
                return ((TableListModel) ServiceList.getModel()).getAllValues().get(0);
            }
            return new Vector<String>();
        }
    }

    public HostOriginatorIFrame() {
        super(windowTitle, true, true, true, true);
        int x = (int) (random() * 100);
        this.setBounds(x, x, x + 400, x + 400);
        this.setLayout(layout);
        Dimension dim = this.getPreferredSize();
        myC = new JScrollPane(getKeyValueList());
        myS = new mySouth(getID(), dim.width, dim.height / 2);
        JSplitPane mySP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, myC, myS);
        mySP.setDividerLocation(200);
        toolbar = new myToolbar();
        this.add(toolbar, BorderLayout.NORTH);
        this.add(mySP, BorderLayout.CENTER);
        this.toFront();
        this.setVisible(true);
    }

    private Vector<String> getHeader() {
        if (header == null) {
            header = new Vector<String>();
            header.add("K E Y");
            header.add("V A L U E");
        }
        return header;
    }

    private Vector<Vector<String>> getRows() {
        if (rows == null) {
            rows = HostConfigurationRequestProcessor.getHostParameterRows();
        }
        return rows;
    }

    private JTable getKeyValueList() {
        if (keyValueList == null) {
            keyValueList = new JTable(new TableListModel(getRows(), getHeader()));
        }
        return keyValueList;
    }

    public int getID() {
        try {
            id = Integer.parseInt((String) this.getKeyValueList().getValueAt(0, 1));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return id;
    }

    private Vector<Vector<String>> getKeyValueListContent() {
        if (keyValueList == null) {
            return new Vector<Vector<String>>();
        }
        return ((TableListModel) keyValueList.getModel()).getAllValues();
    }
}
