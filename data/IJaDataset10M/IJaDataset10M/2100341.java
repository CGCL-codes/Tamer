package org.digitall.apps.resourcescontrol.interfaces.resourcesadmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.digitall.common.resourcescontrol.classes.Resource;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.environment.Environment;

public class ResourcesAdminToAvailableList extends BasicPrimitivePanel {

    private ResourcesAdminMain parentMain;

    private BasicPanel content = new BasicPanel();

    private BasicPanel findPanel = new BasicPanel();

    private int[] sizeColumnList = { 390, 140, 75, 113 };

    private Vector headerList = new Vector();

    private Vector dataRow = new Vector();

    private GridPanel listPanel = new GridPanel(30, sizeColumnList, "Stock de Recursos Disponibles", dataRow);

    private Resource resource;

    public ResourcesAdminToAvailableList(ResourcesAdminMain _parentMain) {
        try {
            parentMain = _parentMain;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(790, 390));
        this.setPreferredSize(new Dimension(790, 390));
        setTitle("Recursos Existentes y Disponibles");
        content.setLayout(null);
        findPanel.setBounds(new Rectangle(5, 5, 625, 45));
        findPanel.setLayout(null);
        listPanel.setBounds(new Rectangle(5, 5, 780, 380));
        content.add(listPanel, null);
        content.add(findPanel, null);
        this.add(content, BorderLayout.CENTER);
        setHeaderList();
    }

    private void setHeaderList() {
        headerList.removeAllElements();
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("Name"));
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("Brand"));
        headerList.addElement("*");
        headerList.addElement(Environment.lang.getProperty("Unit"));
        headerList.addElement(Environment.lang.getProperty("AvailableQty"));
        headerList.addElement("*");
        listPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                }
            }
        });
        listPanel.setParams("resourcescontrol.getAllResourcesAvailable", "-1", headerList);
    }

    public void refresh() {
        String params = "" + resource.getIdResource();
        listPanel.refresh(params);
    }

    public void setResource(Resource resource) {
        this.resource = resource;
        refresh();
    }
}
