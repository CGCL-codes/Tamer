package herschel.phs.prophandler.tools.missionevolver.data;

import herschel.phs.prophandler.tools.missionevolver.MissionEvolverMain;
import herschel.phs.prophandler.tools.missionevolver.gui.datatree.DataTree;
import javax.swing.ImageIcon;

public class ConstraintNode extends OidNode {

    public static ImageIcon CONSTRAINT = new ImageIcon(ConstraintNode.class.getResource(MissionEvolverMain.RESOURCE_BASE_DIR + "constraint.gif"));

    public ConstraintNode(int id, String program, String name, InfoList dataList) {
        super(id, OidNode.CONSTRAINT, program, name, dataList);
    }

    public void expand(DataTree container) {
        container.showData(getDataList());
    }

    @Override
    public ImageIcon getIcon() {
        return CONSTRAINT;
    }
}
