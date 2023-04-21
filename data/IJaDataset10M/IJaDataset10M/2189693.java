package missionevolver.data;

import missionevolver.MissionEvolverMain;
import missionevolver.gui.datatree.DataTree;
import javax.swing.ImageIcon;

public class ExceptionNode extends OidNode {

    public static ImageIcon EXCEPTION = new ImageIcon(ExceptionNode.class.getResource(MissionEvolverMain.RESOURCE_BASE_DIR + "exclamation.png"));

    public ExceptionNode(int id, String program, String name, DataList dl) {
        super(id, OidNode.EXCEPTION, program, name, dl);
    }

    @Override
    public void expand(DataTree container, boolean force) {
        container.showData(getDataList());
    }

    @Override
    public ImageIcon getIcon() {
        return EXCEPTION;
    }
}
