package spaceopera.gui.components;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import spaceopera.gui.SpaceOpera;
import spaceopera.gui.window.ColonyDetail;
import spaceopera.universe.SOConstants;
import spaceopera.universe.colony.BuildProject;

public class ExistingBuildingsDetail extends BuildProjectDetail implements ActionListener, SOConstants {

    public ExistingBuildingsDetail(SpaceOpera so, ColonyDetail cd, BuildProject p) {
        super(so, cd, p);
    }

    public void actionPerformed(ActionEvent event) {
        String c = event.getActionCommand();
        if (c.equals("quit")) {
            dispose();
        }
    }

    public void init() {
        setTitle("Support cost overview");
        resourceListDetail.removeAll();
        Vector listData = new Vector();
        if (buildProject != null) {
            listTitle.setText("Support cost per " + buildProject.getName() + ":");
            for (int i = R_MIN; i < R_MAX; i++) {
                if (buildProject.getSupportCost(i) > 0) {
                    String str = spaceOpera.getResourceName(i) + "                             ";
                    str = str.substring(0, 15);
                    double units = buildProject.getSupportCost(i);
                    str = str + units + "     ";
                    str = str.substring(0, 20) + " " + spaceOpera.getResourceUnit(i);
                    listData.addElement(str);
                }
            }
            showImage();
        }
        resourceListDetail.setListData(listData);
    }

    public void refresh() {
        Vector bQ = colonyDetail.getColony().getBuildings();
        if (!bQ.contains(buildProject)) {
            if (bQ.size() > 0) {
                buildProject = (BuildProject) bQ.elementAt(0);
            } else {
                buildProject = null;
            }
        }
        if (isVisible()) {
            init();
        }
    }

    public void position() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension dim = kit.getScreenSize();
        setLocation((dim.width - WINDOWSIZEX), (dim.height - getHeight()));
    }
}
