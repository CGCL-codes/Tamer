package shieh.pnn.nets;

import shieh.pnn.core.*;

public class NetMultiDetectors extends Network {

    /**
	 * 
	 */
    private static final long serialVersionUID = 579649087284692855L;

    public static final String NAME_MDETECTOR_NET = "Multiple Detectors";

    public static final String NAME_INPUT_LAYER = "Input";

    public static final String NAME_ACTIVATORS_LAYER = "Activators";

    protected int nUnits, nDetectors;

    public NetGatedRepDetector detector[];

    protected NetRepDetector netSwitchDetector;

    protected Layer layerInput, layerShutter;

    protected Layer shutterInh, actInh;

    protected HolderLayer layerActivators;

    protected NetControlledChain netChain;

    public NetMultiDetectors(String label, ItemSet items, int nDetectors) {
        this(label, items.size(), nDetectors);
        this.items = items;
    }

    public NetMultiDetectors(String label, Layer layerInput, int nDetectors) {
        this(label, layerInput.size(), nDetectors);
        this.layerInput = layerInput;
    }

    public NetMultiDetectors(String label, int nUnits, int nDetectors) {
        super(label);
        this.nUnits = nUnits;
        this.nDetectors = nDetectors;
        detector = new NetGatedRepDetector[nDetectors];
    }

    public int getCountSize() {
        return detector[0].netRepCount.nMaxCount;
    }

    @Override
    public void initNetwork() {
        super.initNetwork();
        if (layerInput == null) {
            layerInput = new Layer(this, NAME_INPUT_LAYER, nUnits);
            addLayer(layerInput, true);
        }
        netChain = new NetControlledChain(NetControlledChain.NAME_CHAIN_NET, nDetectors);
        netChain.initNetwork();
        addSubnetwork(netChain);
        netSwitchDetector = new NetRepDetector(NetRepDetector.NAME_REPDETECT_NET, layerInput);
        netSwitchDetector.initNetwork();
        addSubnetwork(netSwitchDetector);
        layerShutter = new Layer(this, "Input Shutter", nUnits);
        shutterInh = new Layer(this, "Shutter Inh", 1);
        shutterInh.setBias(1);
        actInh = new Layer(this, "Activate Inh", 1);
        actInh.setBias(1);
        addLayer(layerShutter);
        addLayer(shutterInh);
        addLayer(actInh);
        for (int i = 0; i < nDetectors; i++) {
            detector[i] = new NetGatedRepDetector(getNameGatedDetector(i), layerShutter);
            detector[i].initNetwork();
            addSubnetwork(detector[i]);
        }
        layerActivators = new HolderLayer(this, NAME_ACTIVATORS_LAYER);
        for (int i = 0; i < nDetectors; i++) {
            Layer activate = detector[i].getExportedLayer(NetGatedRepDetector.NAME_ACTIVATE_LAYER);
            layerActivators.addUnit(activate.units.get(0));
        }
        addLayer(layerActivators);
        Projection projChainActivate = new Projection(this, netChain.getExportedLayer(NetControlledChain.NAME_STATES_LAYER), layerActivators, ProjectionType.ONE_TO_ONE);
        projChainActivate.setWtType(WtType.UNIFORM, 1D);
        projChainActivate.connect();
        Projection projSwChain = new Projection(this, netSwitchDetector.getExportedLayer(NetRepDetector.NAME_CHGDETECT_LAYER), ((BiState) netChain.getExportedObject(NetControlledChain.NAME_GOSIGNAL_BISTATE)).layer, ProjectionType.ONE_TO_ONE);
        projSwChain.setWtType(WtType.UNIFORM, 1D);
        projSwChain.connect();
        Projection projMemShutter = new Projection(this, netSwitchDetector.getExportedLayer(NetBuffer.NAME_MEM_LAYER), layerShutter, ProjectionType.ONE_TO_ONE);
        projMemShutter.setWtType(WtType.UNIFORM, 1D);
        projMemShutter.connect();
        Projection projShutterInh = new Projection(this, shutterInh, layerShutter, ProjectionType.ALL_TO_ALL);
        projShutterInh.setWtType(WtType.UNIFORM, -1D);
        projShutterInh.connect();
        projShutterInh.setDelay(1);
        Projection projShutterGates = new Projection(this, actInh, layerActivators, ProjectionType.ALL_TO_ALL);
        projShutterGates.setWtType(WtType.UNIFORM, -1D);
        projShutterGates.connect();
        Projection projNewShutter = new Projection(this, netSwitchDetector.getExportedLayer(NetBuffer.NAME_NEWITEM_LAYER), shutterInh, ProjectionType.ALL_TO_ALL);
        projNewShutter.setWtType(WtType.UNIFORM, -1D);
        projNewShutter.connect();
        projNewShutter.setDelay(5);
        Projection projChgAct = new Projection(this, netSwitchDetector.getExportedLayer(NetRepDetector.NAME_CHGDETECT_LAYER), actInh, ProjectionType.ALL_TO_ALL);
        projChgAct.setWtType(WtType.UNIFORM, -1D);
        projChgAct.connect();
        projChgAct.setDelay(4);
    }

    protected String getNameGatedDetector(int i) {
        return String.format("Detector %d", i);
    }
}
