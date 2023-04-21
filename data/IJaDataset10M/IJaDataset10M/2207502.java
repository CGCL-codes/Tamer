package gpsxml.action;

import gpsxml.AccessControl;
import gpsxml.ArchiveData;
import gpsxml.gui.DataSetsPanel;
import gpsxml.gui.MainPanel;
import gpsxml.gui.MenusPanel;
import gpsxml.gui.ServicesPanel;
import gpsxml.xml.DataSet;
import gpsxml.xml.Service;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class ServiceAccessControl implements ActionListener {

    MainPanel mainPanel;

    ServicesPanel servicesPanel;

    DataSetsPanel dataSetsPanel;

    private MenusPanel menusPanel;

    public ServiceAccessControl(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.servicesPanel = mainPanel.getServicesPanel();
        this.dataSetsPanel = mainPanel.getDataSetsPanel();
        this.menusPanel = mainPanel.getMenusPanel();
    }

    public void actionPerformed(ActionEvent e) {
        ArchiveData archiveData = MainPanel.getArchiveData();
        AccessControl accessControl = archiveData.getAccessControl();
        Service service = servicesPanel.getSelectedService();
        DataSet dataSet = dataSetsPanel.getCurrentDataSet();
        HashMap<String, String> urlHashMap = accessControl.getUrlHashMap();
        String url = service.getGenomeSpecies() + " ";
        url += service.getGenomeVersion() + " " + dataSet.getDescription();
        String urlID = accessControl.getURLID(url);
        if (urlID == null) {
            urlID = accessControl.getNewValidURLID();
            urlHashMap.put(urlID, url);
        } else {
            urlHashMap.remove(urlID);
        }
        servicesPanel.getServiceList().clearSelection();
    }
}
