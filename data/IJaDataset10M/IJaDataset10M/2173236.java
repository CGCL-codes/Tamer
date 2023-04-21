package gov.ca.bdo.modeling.dsm2.map.client.map;

import gov.ca.bdo.modeling.dsm2.map.client.display.MapControlPanel;
import gov.ca.dsm2.input.model.Channel;
import gov.ca.dsm2.input.model.Gate;
import gov.ca.dsm2.input.model.Node;
import gov.ca.dsm2.input.model.Reservoir;
import gov.ca.dsm2.input.model.XSection;
import gov.ca.modeling.dsm2.widgets.client.events.MessageEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapMouseMoveHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polyline;

/**
 * @author nsandhu
 * 
 */
public class AddMapElementClickHandler implements MapClickHandler {

    private final class ChannelLineMouseMoveHandler implements MapMouseMoveHandler {

        private Polyline channelLine;

        public ChannelLineMouseMoveHandler() {
            channelLine = null;
        }

        public void startLine() {
            mapPanel.getMap().addMapMouseMoveHandler(this);
        }

        public void onMouseMove(MapMouseMoveEvent event) {
            if (channelLine == null) {
                LatLng p1 = LatLng.newInstance(previousNode.getLatitude(), previousNode.getLongitude());
                LatLng p2 = event.getLatLng();
                channelLine = new Polyline(new LatLng[] { p1, p2 }, "blue");
                mapPanel.getMap().addOverlay(channelLine);
            }
            channelLine.deleteVertex(1);
            channelLine.insertVertex(1, event.getLatLng());
        }

        public void clearLine() {
            if (channelLine == null) {
                return;
            }
            mapPanel.getMap().removeMapMouseMoveHandler(this);
            mapPanel.getMap().removeOverlay(channelLine);
            channelLine = null;
        }
    }

    private MapPanel mapPanel;

    private Node previousNode;

    private EventBus eventBus;

    private ChannelLineMouseMoveHandler channelLineHandler;

    private MapControlPanel controlPanel;

    public AddMapElementClickHandler(MapPanel mapPanel, MapControlPanel controlPanel, EventBus eventBus) {
        this.mapPanel = mapPanel;
        this.controlPanel = controlPanel;
        this.eventBus = eventBus;
    }

    public void onClick(MapClickEvent event) {
        int type = controlPanel.getAddTypeSelected();
        if ((type != ElementType.CHANNEL) && (type != ElementType.XSECTION)) {
            LatLng latLng = event.getLatLng();
            if (latLng == null) {
                String msg = "You clicked on a marker. If you'd like to add a node, click on the map instead. You can move it overlap later.";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            if (type == ElementType.NODE) {
                NodeMarkerDataManager nodeManager = mapPanel.getNodeManager();
                Node n = new Node();
                n.setId(nodeManager.getNewNodeId());
                n.setLatitude(latLng.getLatitude());
                n.setLongitude(latLng.getLongitude());
                mapPanel.getNodeManager().addNode(n);
            } else if (type == ElementType.GATE) {
                GateOverlayManager gateManager = mapPanel.getGateManager();
                Gate g = new Gate();
                g.setName("GATE_" + (gateManager.getNumberOfGates() + 1));
                g.setLatitude(latLng.getLatitude());
                g.setLongitude(latLng.getLongitude());
                gateManager.addGate(g);
            } else if (type == ElementType.RESERVOIR) {
                ReservoirOverlayManager reservoirManager = mapPanel.getReservoirManager();
                Reservoir r = new Reservoir();
                r.setLatitude(latLng.getLatitude());
                r.setLongitude(latLng.getLongitude());
                r.setName("RESERVOIR_" + (reservoirManager.getNumberOfReservoirs() + 1));
                reservoirManager.addReservoir(r);
            } else if (type == ElementType.OUTPUT) {
            }
        } else if (type == ElementType.CHANNEL) {
            Overlay overlay = event.getOverlay();
            if (overlay == null) {
                String msg = "You have selected adding a channel and did not click on a node marker";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            NodeMarkerDataManager nodeManager = mapPanel.getNodeManager();
            Node node = nodeManager.getNodeForMarker(overlay);
            if (node == null) {
                String msg = "You have selected adding a channel and clicked on a marker that is not a node!";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            if (previousNode == null) {
                String msg = "Adding a channel with upnode (" + node.getId() + "). Now click on downnode.";
                eventBus.fireEvent(new MessageEvent(msg));
                if (channelLineHandler == null) {
                    channelLineHandler = new ChannelLineMouseMoveHandler();
                }
                channelLineHandler.startLine();
                previousNode = node;
            } else {
                String msg = "Adding channel with upnode (" + previousNode.getId() + ") and downnode (" + node.getId() + ")";
                eventBus.fireEvent(new MessageEvent(msg));
                ChannelLineDataManager channelManager = mapPanel.getChannelManager();
                Channel channel = new Channel();
                channel.setUpNodeId(previousNode.getId());
                channel.setDownNodeId(node.getId());
                channel.setId(channelManager.getNewChannelId());
                channelManager.addChannel(channel);
                channelLineHandler.clearLine();
                previousNode = null;
            }
        } else if (type == ElementType.XSECTION) {
            Overlay overlay = event.getOverlay();
            if (overlay == null) {
                String msg = "You have selected adding a xsection and did not click on a channel connection line";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            if (!(overlay instanceof Polyline)) {
                String msg = "When adding a xsection click on a channel connection line. You clicked on a marker?";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            String channelId = mapPanel.getChannelManager().getChannelId(overlay);
            if (channelId == null) {
                String msg = "When adding a xsection click on a channel connection line. You clicked on some other line?";
                eventBus.fireEvent(new MessageEvent(msg));
                return;
            }
            XSection xsection = ModelUtils.createXSection(channelId, 0.5, 1000.0);
            mapPanel.getChannelManager().getChannels().getChannel(channelId).addXSection(xsection);
        }
    }
}
