package ru.cos.sim.visualizer.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import ru.cos.sim.communication.FrameProperties;
import ru.cos.sim.visualizer.traffic.simulation.VisualController;

/**
 * Handles frame data in the system. Contains information about parts of the trace,
 * like nodes and segments, that are currently visible on the screen. According to this 
 * data simulation engine returns data about cars on the trace.
 * 
 * @author Dudinov Ivan
 */
public class FrameDataHandler {

    /**
	 * Frame Data about links
	 */
    private HashMap<Integer, Set<Integer>> linkData;

    /**
	 * Array of links, that are registered in the system;
	 */
    private ArrayList<LinkFrameData> links;

    /**
	 * Frame Data about nodes. Contains list of currently visible nodes; 
	 */
    private CommonFrameData nodeData;

    /**
	 * Flag of the actual data in the object. If it is false
	 * Data must be updated before be get from somewhere. According to
	 * this flag data updates on time.
	 */
    private boolean isActual = false;

    /**
	 * Instance of the class
	 */
    private static FrameDataHandler instance;

    private FrameProperties data;

    /**
	 * initialise data containers 
	 * @private
	 */
    private FrameDataHandler() {
        linkData = new HashMap<Integer, Set<Integer>>();
        links = new ArrayList<LinkFrameData>();
        nodeData = new CommonFrameData();
        isActual = false;
    }

    /**
	 * Adds link to the list of links. If link is not registered here, no cars
	 * will be visible on this link.
	 * @param uuid - id of the link
	 * @param data - link frame data
	 */
    public void addLink(Integer uuid, LinkFrameData data) {
        links.add(data);
    }

    public void changeObjecttoVisible(ViewableObjectInformation e) {
        e.linkData.changeToVisible(e);
    }

    public void changeObjectToInvisible(ViewableObjectInformation e) {
        e.linkData.changeToInVisible(e);
    }

    /**
	 * Updates data about segments and nodes, that are currently visible.
	 */
    protected void updateData() {
        for (LinkFrameData data : links) {
            if (data.isVisible()) {
                data.update();
                linkData.put(data.getUuid(), data.segarray);
            }
        }
        this.nodeData.update();
    }

    /**
	 * Returns instance of the FrameDataHandler. This is single way to obtain
	 * object of the FrameDataHandler.
	 * @return - instance of the FrameDatahandler
	 */
    public static FrameDataHandler getInstance() {
        if (instance == null) instance = new FrameDataHandler();
        return instance;
    }

    /**
	 * Returns current frame data about links.
	 * @return - links frame data
	 */
    public HashMap<Integer, Set<Integer>> getLinkFrameData() {
        if (!isActual) {
            this.updateData();
            isActual = true;
        }
        return linkData;
    }

    /**
	 * Returns current frame data about nodes
	 * @return nodes frame data
	 */
    public Set<Integer> getNodeFrameData() {
        if (!isActual) {
            this.updateData();
            isActual = true;
        }
        return nodeData.segarray;
    }

    /**
	 * Returns link to the list of the node data. Do not handle it manually;
	 * @return
	 * @deprecated
	 */
    public CommonFrameData getNodeListLink() {
        return nodeData;
    }

    public void setFrameProperties() {
        if (isActual) return;
        linkData = new HashMap<Integer, Set<Integer>>();
        this.updateData();
        isActual = true;
        data = new FrameProperties(linkData, nodeData.segarray);
        VisualController.getInstance().setFrameProperties(data);
    }

    public void setDataInvalid() {
        this.isActual = false;
    }

    public void dispose() {
        instance = null;
    }

    @Override
    public String toString() {
        String result = "";
        for (Integer l : linkData.keySet()) {
            result += "Link " + l.toString() + "\n";
            for (Integer b : linkData.get(l)) {
                result += "  Segment " + b.toString() + "\n";
            }
            result += "/Link \n";
        }
        return result;
    }
}
