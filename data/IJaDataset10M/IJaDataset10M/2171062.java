package ru.cos.sim.visualizer.traffic.gui;

import java.util.List;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import ru.cos.sim.communication.dto.VehicleDTO;
import ru.cos.sim.road.init.data.LinkLocationData;
import ru.cos.sim.road.init.data.NodeLocationData;
import ru.cos.sim.visualizer.traffic.information.TreeItem;
import ru.cos.sim.visualizer.traffic.information.UniverseTabelModel;
import ru.cos.sim.visualizer.traffic.parser.trace.location.LinkLocation;
import ru.cos.sim.visualizer.traffic.parser.trace.location.NodeLocation;

public class CarInformationTempHandler {

    protected JXTreeTable table;

    protected UniverseTabelModel model;

    protected int currentID = 0;

    protected boolean hasCar = false;

    public CarInformationTempHandler(UniverseTabelModel model) {
        this.table = table;
        this.model = model;
    }

    public void updateCarInformation(int linkId, int segId, float speed, int laneId, int uid) {
        TreePath universe = table.getPathForRow(0);
        TreePath root = universe.getParentPath();
        boolean universeExpanded = table.isExpanded(universe);
        int carRow = getIsCarLoad(root, universeExpanded);
        boolean carExpanded = table.isExpanded(carRow);
        this.model.updateCarInformation(root, linkId, segId, speed, laneId, uid);
        if (carExpanded) table.expandRow(1);
        if (universeExpanded) table.expandPath(universe);
    }

    public void updateCarInformation(int nodeId, float speed, int uid) {
        TreePath universe = table.getPathForRow(0);
        TreePath root = universe.getParentPath();
        boolean universeExpanded = table.isExpanded(universe);
        int carRow = getIsCarLoad(root, universeExpanded);
        boolean carExpanded = table.isExpanded(carRow);
        this.model.updateCarInformation(root, nodeId, speed, uid);
        if (carExpanded) table.expandRow(1);
        if (universeExpanded) table.expandPath(universe);
    }

    public void refreshNodeCarInfo(VehicleDTO vdto) {
        TreePath universe = table.getPathForRow(0);
        boolean universeExpanded = table.isExpanded(universe);
        TreePath root = universe.getParentPath();
        int carRow = getIsCarLoad(root, universeExpanded);
        boolean carExpanded = table.isExpanded(carRow);
        NodeLocationData nodeLocationData = (NodeLocationData) vdto.getLocation();
        this.model.updateCarInformation(root, nodeLocationData.getNodeId(), vdto.getSpeed(), vdto.getAgentId());
        if (universeExpanded) table.expandPath(universe);
        if (carExpanded) {
            table.expandRow(carRow);
        }
    }

    public void refreshTraceCarInfo(VehicleDTO vdto) {
        TreePath universe = table.getPathForRow(0);
        boolean universeExpanded = table.isExpanded(universe);
        TreePath root = universe.getParentPath();
        int carRow = getIsCarLoad(root, universeExpanded);
        boolean carExpanded = table.isExpanded(carRow);
        LinkLocationData linkLocationData = (LinkLocationData) vdto.getLocation();
        this.model.updateCarInformation(root, linkLocationData.getLinkId(), linkLocationData.getSegmentId(), vdto.getSpeed(), linkLocationData.getLaneIndex(), vdto.getAgentId());
        if (universeExpanded) table.expandPath(universe);
        if (carExpanded) {
            table.expandRow(carRow);
        }
    }

    private int getIsCarLoad(TreePath root, boolean univExpanded) {
        if (!univExpanded) return 1;
        List<TreeItem> list = ((TreeItem) root.getLastPathComponent()).getChildren().get(0).getChildren();
        return list.size() + 1;
    }
}
