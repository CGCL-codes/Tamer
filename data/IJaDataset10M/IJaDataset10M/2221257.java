package shieh.pnn.nets;

import shieh.pnn.core.*;

public class NetGatedRepDetector extends NetRepDetector {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2393835203323772488L;

    public static final String NAME_REPCOUNT_LAYER = "RepCount";

    public static final String NAME_SEPCOUNT_LAYER = "SepCount";

    public static final String NAME_ACTIVATE_LAYER = "Activate";

    public static final String NAME_REP_INDICATOR_LAYER = "Repetition Indicator";

    protected Layer layerActivate;

    protected Layer layerRepIndicator;

    protected NetCounter netRepCount;

    protected NetCounter netSepCount;

    public NetGatedRepDetector(String label, Layer layerInput) {
        super(label, layerInput);
    }

    @Override
    public NetBuffer createBuffer() {
        return new NetGatedBuffer(NetGatedBuffer.NAME_GATEDBUFFER_NET, layerInput);
    }

    public BiState getGate() {
        if (netBuffer == null) return null;
        String gateName = ((NetGatedBuffer) netBuffer).getGateName();
        return (BiState) netBuffer.getExportedObject(gateName);
    }

    @Override
    public void initNetwork() {
        super.initNetwork();
        layerActivate = new Layer(this, NAME_ACTIVATE_LAYER, 1);
        netRepCount = new NetCounter("RepCount");
        netRepCount.initNetwork();
        netSepCount = new NetCounter("SepCount");
        netSepCount.initNetwork();
        layerRepIndicator = new Layer(this, NAME_REP_INDICATOR_LAYER, 1);
        addLayer(layerRepIndicator);
        addLayer(layerActivate, true);
        addSubnetwork(netRepCount);
        addSubnetwork(netSepCount);
        getGate().setDefaultState(true);
        Projection projRepIndicator = new Projection(this, layerRepetition, layerRepIndicator, ProjectionType.ONE_TO_ONE);
        projRepIndicator.setWtType(WtType.UNIFORM, 1D);
        projRepIndicator.connect();
        projRepIndicator.setDelay(0);
        Projection projActGate = new Projection(this, layerActivate, getGate().layer, ProjectionType.ONE_TO_ONE);
        projActGate.setWtType(WtType.UNIFORM, -1D);
        projActGate.connect();
        Projection projActRepCount = new Projection(this, layerActivate, netRepCount.getExportedLayer(NetCounter.NAME_COUNTER_LAYER), ProjectionType.ALL_TO_ALL);
        projActRepCount.setWtType(WtType.UNIFORM, -10D);
        projActRepCount.connect();
        Projection projActSepCount = new Projection(this, layerActivate, netSepCount.getExportedLayer(NetCounter.NAME_COUNTER_LAYER), ProjectionType.ALL_TO_ALL);
        projActSepCount.setWtType(WtType.UNIFORM, -10D);
        projActSepCount.connect();
        Projection projRepCount = new Projection(this, layerRepetition.getMainGroup(), netRepCount.getExportedLayer(NetCounter.NAME_INPUT_LAYER).groups.get(0), ProjectionType.ONE_TO_ONE);
        projRepCount.setWtType(WtType.UNIFORM, 1D);
        projRepCount.connect();
        Projection projSepCount = new Projection(this, layerChange.getMainGroup(), netSepCount.getExportedLayer(NetCounter.NAME_INPUT_LAYER).groups.get(0), ProjectionType.ONE_TO_ONE);
        projSepCount.setWtType(WtType.UNIFORM, 1D);
        projSepCount.connect();
    }
}
