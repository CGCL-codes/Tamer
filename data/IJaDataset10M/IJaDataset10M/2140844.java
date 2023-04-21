package basys.client.ui;

import java.awt.Dimension;
import java.util.ResourceBundle;
import basys.LocaleResourceBundle;
import basys.client.Project;
import basys.client.ui.event.ShowViewEvent;
import basys.client.ui.event.ShowViewEventListener;
import basys.client.ui.tablemodels.*;
import basys.client.ui.tree.ArchitecturalTree;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;
import javax.swing.*;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;

/**
 * ProjectviewPane.java
 * 
 * 
 * @author	oalt
 * @version $Id: ProjectviewPane.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class ProjectviewPane extends JSplitPane implements ShowViewEventListener {

    private static Logger logger = Logger.getLogger(ProjectviewPane.class);

    private static ResourceBundle locale = LocaleResourceBundle.getLocale();

    private Project p;

    private StructuralViewPane svp;

    public ProjectviewPane(Project p) {
        this.p = p;
        initView();
        this.setPreferredSize(new Dimension(750, 550));
    }

    public void initView() {
        JPanel panel = new JPanel();
        ArchitecturalTree archtree = new ArchitecturalTree(p);
        archtree.setPreferredSize(new Dimension(200, 550));
        archtree.setMinimumSize(new Dimension(200, 550));
        this.add(archtree, JSplitPane.LEFT);
        JTabbedPane tabpane = new JTabbedPane();
        this.svp = new StructuralViewPane();
        tabpane.addTab(locale.getString("tabname.structural"), svp);
        this.add(tabpane, JSplitPane.RIGHT);
        this.p.getArchitecturalDataModel().addObserver(archtree);
        archtree.addShowViewEventListener(this);
    }

    /**
	 * @see basys.client.ui.event.ShowViewEventListener#updateView(basys.client.ui.event.ShowViewEvent)
	 */
    public void updateView(ShowViewEvent e) {
        String id = e.getSelectedObjectId();
        logger.debug("updateView called: " + id);
        ArchitecturalDataModel amodel = p.getArchitecturalDataModel();
        InstallationModel imodel = p.getInstallationModel();
        if (id.startsWith("project-")) {
            ProjectTableModel model = new ProjectTableModel(this.p, id);
            amodel.addObserver(model);
            StructuralDnDTable table = new StructuralDnDTable(model, amodel, id);
            table.addMouseListener(new TablePopupListener(table, this.p));
            String name = amodel.getName(id) + ": ";
            svp.setStructuralViewPanel(new MyTablePanel(table, name + locale.getString("l.buildings")));
        } else if (id.startsWith("building-")) {
            BuildingTableModel model = new BuildingTableModel(this.p, id);
            amodel.addObserver(model);
            StructuralDnDTable table = new StructuralDnDTable(model, amodel, id);
            table.addMouseListener(new TablePopupListener(table, this.p));
            String name = amodel.getName(id) + ": ";
            svp.setStructuralViewPanel(new MyTablePanel(table, name + locale.getString("l.floors")));
        } else if (id.startsWith("floor-")) {
            FloorTableModel model = new FloorTableModel(this.p, id);
            amodel.addObserver(model);
            StructuralDnDTable table = new StructuralDnDTable(model, amodel, id);
            table.addMouseListener(new TablePopupListener(table, this.p));
            String name = amodel.getName(id) + ": ";
            svp.setStructuralViewPanel(new MyTablePanel(table, name + locale.getString("l.rooms")));
        } else if (id.startsWith("room-")) {
            RoomPanel rp = new RoomPanel(this.p, id);
            p.getArchitecturalDataModel().addObserver(rp);
            svp.setStructuralViewPanel(rp);
        } else if (id.startsWith("junctionbox-")) {
            BusDeviceTablemodel model = new BusDeviceTablemodel(this.p, id);
            amodel.addObserver(model);
            DnDTable table = new DnDTable(model);
            String name = amodel.getName(id) + ": ";
            svp.setStructuralViewPanel(new MyTablePanel(table, name + locale.getString("l.busdevices")));
        }
        this.validate();
    }
}
