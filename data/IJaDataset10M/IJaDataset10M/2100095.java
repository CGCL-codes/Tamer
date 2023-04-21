package simjistwrapper.factories.jsnodefactory;

import jist.swans.Constants;
import jist.swans.field.Field;
import jist.swans.field.Placement;
import jist.swans.mac.Mac802_11;
import jist.swans.mac.MacAddress;
import jist.swans.mac.MacDumb;
import jist.swans.misc.Location;
import jist.swans.misc.Mapper;
import jist.swans.net.NetAddress;
import jist.swans.net.NetIp;
import jist.swans.net.PacketLoss;
import jist.swans.radio.RadioInfo;
import jist.swans.radio.RadioNoise;
import jist.swans.radio.RadioNoiseAdditive;
import jist.swans.radio.RadioNoiseIndep;
import simjistwrapper.exceptions.*;
import simjistwrapper.factories.jsnodefactory.JSNodeChecker;
import simjistwrapper.utils.realstruct.IParameter;
import simjistwrapper.utils.realstruct.Order;
import simjistwrapper.utils.simstruct.*;
import org.w3c.dom.*;

public class JSNodeFactory {

    private static Document document = null;

    public static void init(Document locDoc) {
        document = locDoc;
    }

    public static void createNodes(Field field, Location.Location2D bounds, OrderList orders) throws SemanticException, SimRuntimeException {
        Element element = document.getDocumentElement();
        NodeList nodeModels = element.getElementsByTagName(SimParamList.NodeModel);
        for (int i = 0; i < orders.size(); i++) {
            Order order = (Order) (orders.get(i));
            Node nodeModelXML = getNodeModel(nodeModels, order.getModelNbre());
            if (nodeModelXML != null) for (int j = 0; j < order.getQtity(); j++) createJSNode(field, bounds, nodeModelXML, computeId(order.getModelNbre(), j)); else throw new SemanticException("getNodeModel() returned a null object");
        }
    }

    private static int computeId(int nodeModelNbre, int instance) {
        int count = 0;
        int plop = instance;
        if (plop == 0) count = 1; else while (plop > 0) {
            plop = plop / 10;
            count++;
        }
        int answer = (int) (nodeModelNbre * Math.pow(10, (double) count) + instance);
        return answer;
    }

    private static Node getNodeModel(NodeList nodeModels, int id) {
        Node answer = null;
        for (int i = 0; i < nodeModels.getLength(); i++) {
            Node nodeModel = nodeModels.item(i);
            if (Integer.valueOf(((Element) nodeModel).getAttribute("id")).intValue() == id) answer = nodeModel;
        }
        return answer;
    }

    private static JSNode createJSNode(Field field, Location.Location2D bounds, Node XMLNode, int id) throws SemanticException, SimRuntimeException {
        JSNodeModel nodeModel = createNodeModel(XMLNode, bounds);
        String francFileName = getFrancFileName(XMLNode);
        nodeModel.showAll();
        Location loc = null;
        if (nodeModel.positiontype.equals("random")) {
            Placement placement = new Placement.Random(bounds);
            loc = placement.getNextLocation();
        } else if (nodeModel.positiontype.equals("deterministic")) loc = new Location.Location2D(nodeModel.posx, nodeModel.posy); else throw new SemanticException("An error occured in JSNodeFactory");
        RadioNoise radio = createRadio(nodeModel, id);
        MacDumb macDumb = null;
        Mac802_11 mac802_11 = null;
        if (nodeModel.macmodel.equals("dumb")) macDumb = new MacDumb(new MacAddress(id), radio.getRadioInfo()); else if (nodeModel.macmodel.equals("802_11")) mac802_11 = new Mac802_11(new MacAddress(id), radio.getRadioInfo()); else throw new SimRuntimeException("Neither macDumb or Mac802.11 layer has been created");
        NetAddress netAddr = new NetAddress(id);
        Mapper protMap = createMapper(nodeModel);
        PacketLoss plin = createPacketLoss(nodeModel, "in");
        PacketLoss plout = createPacketLoss(nodeModel, "out");
        NetIp net = new NetIp(netAddr, protMap, plin, plout);
        JSNode app = new JSNode(id, loc.getX(), loc.getY(), francFileName);
        field.addRadio(radio.getRadioInfo(), radio.getProxy(), loc);
        field.startMobility(radio.getRadioInfo().getUnique().getID());
        radio.setFieldEntity(field.getProxy());
        if (macDumb != null) {
            radio.setMacEntity(macDumb.getProxy());
            macDumb.setRadioEntity(radio.getProxy());
            byte intid = net.addInterface(macDumb.getProxy());
            macDumb.setNetEntity(net.getProxy(), intid);
        } else if (mac802_11 != null) {
            radio.setMacEntity(mac802_11.getProxy());
            mac802_11.setRadioEntity(radio.getProxy());
            byte intid = net.addInterface(mac802_11.getProxy());
            mac802_11.setNetEntity(net.getProxy(), intid);
        } else throw new SimRuntimeException("Neither macDumb or Mac802.11 layer has been created");
        net.setProtocolHandler(Constants.NET_PROTOCOL_UDP, app.getNetProxy());
        app.setNetEntity(net.getProxy());
        app.getAppProxy().run(null);
        return app;
    }

    private static JSNodeModel createNodeModel(Node XMLNode, Location bounds) throws SemanticException {
        int id = Integer.valueOf(((Element) XMLNode).getAttribute(SimParamList.id)).intValue();
        JSNodeModel nodeModel = new JSNodeModel(id);
        NodeList layers = XMLNode.getChildNodes();
        for (int j = 1; j < layers.getLength(); j++) {
            Node currentLayer = layers.item(j);
            if (currentLayer.getNodeName().equals(SimParamList.position)) {
                JSNodeChecker.checkPosition(currentLayer);
                nodeModel.setParameter(JSNodeChecker.check(SimParamList.positiontype, ((Element) currentLayer).getAttribute(SimParamList.positiontype)));
                NodeList positions = currentLayer.getChildNodes();
                if (positions.getLength() == 2) {
                    int posx = Integer.valueOf(positions.item(0).getFirstChild().getNodeValue()).intValue();
                    int posy = Integer.valueOf(positions.item(1).getFirstChild().getNodeValue()).intValue();
                    JSNodeChecker.checkPositionWithinBounds(new Location.Location2D(posx, posy), bounds);
                    nodeModel.setParameter(JSNodeChecker.check(positions.item(0).getNodeName(), Integer.toString(posx)));
                    nodeModel.setParameter(JSNodeChecker.check(positions.item(1).getNodeName(), Integer.toString(posy)));
                }
            }
            NodeList params = currentLayer.getChildNodes();
            for (int k = 0; k < params.getLength(); k++) {
                Node currentParam = params.item(k);
                IParameter param = JSNodeChecker.check(currentParam.getNodeName(), currentParam.getFirstChild().getNodeValue());
                nodeModel.setParameter(param);
            }
        }
        return nodeModel;
    }

    private static RadioNoise createRadio(JSNodeModel model, int id) {
        RadioNoise radio = null;
        RadioInfo.RadioInfoShared sharedRadioParameters = RadioInfo.createShared(model.frequency, model.bandwidth, model.transmitpower, model.antennagain, model.sensitivity_mW, model.threshold_mW, model.fieldtemperature_K, model.thermalfactor, model.ambientnoise_mW);
        if (model.radiomodel.equals("additive")) radio = new RadioNoiseAdditive(id, sharedRadioParameters, (float) model.SNRThreshold); else if (model.radiomodel.equals("independant")) radio = new RadioNoiseIndep(id, sharedRadioParameters, (float) model.SNRThreshold);
        return radio;
    }

    private static Mapper createMapper(JSNodeModel model) {
        Mapper protMap = new Mapper(Constants.NET_PROTOCOL_MAX);
        protMap.mapToNext(Constants.NET_PROTOCOL_UDP);
        return protMap;
    }

    private static PacketLoss createPacketLoss(JSNodeModel model, String pl) throws SimRuntimeException {
        if (pl.equals("in")) {
            if (model.packetlossin.equals("zero")) return new PacketLoss.Zero(); else if (model.packetlossin.equals("uniform")) return new PacketLoss.Uniform(model.probaPl); else throw new SimRuntimeException("An error occured during the packet loss creation");
        } else if (pl.equals("out")) {
            if (model.packetlossin.equals("zero")) return new PacketLoss.Zero(); else if (model.packetlossin.equals("uniform")) return new PacketLoss.Uniform(model.probaPl); else throw new SimRuntimeException("An error occured during the packet loss creation");
        } else throw new SimRuntimeException("An error occured during the packet loss creation");
    }

    private static String getFrancFileName(Node nodeModel) {
        return ((Element) nodeModel).getElementsByTagName("FrancStack").item(0).getFirstChild().getNodeValue();
    }
}
