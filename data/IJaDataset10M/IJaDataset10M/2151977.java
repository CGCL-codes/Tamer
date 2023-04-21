package shieh.pnn.nets;

import java.util.Iterator;
import shieh.pnn.core.ConnGroup;
import shieh.pnn.core.Connection;
import shieh.pnn.core.Layer;
import shieh.pnn.core.LearnAlgoUnnormalized;
import shieh.pnn.core.Network;
import shieh.pnn.core.Param;
import shieh.pnn.core.Projection;
import shieh.pnn.core.ProjectionType;
import shieh.pnn.core.Unit;
import shieh.pnn.core.WtType;

public class NetPFCBGLoop extends Network {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8654781601561726170L;

    public static final String NAME_PFCBG_NET = "PFC-BG loop";

    public static final String NAME_PFC1_LAYER = "PFC-I";

    public static final String NAME_PFC2_LAYER = "PFC-II";

    public static final String NAME_STRIATUM_LAYER = "Striatum";

    public Layer layerPFC1, layerPFC2, layerStriatum;

    protected Projection projPFC1Str, projStrLat, projStrPFC2;

    int nPFCUnits, nBGWidth, nBGHeight;

    public NetPFCBGLoop(String label, int nPFCUnits, int nBGWidth, int nBGHeight) {
        super(label);
        this.nPFCUnits = nPFCUnits;
        this.nBGWidth = nBGWidth;
        this.nBGHeight = nBGHeight;
    }

    @Override
    public void initNetwork() {
        super.initNetwork();
        layerPFC1 = new Layer(this, NAME_PFC1_LAYER, nPFCUnits);
        addLayer(layerPFC1, true);
        layerPFC1.setDecay(Param.decay_rate);
        layerPFC2 = new Layer(this, NAME_PFC2_LAYER, nPFCUnits);
        addLayer(layerPFC2, true);
        layerStriatum = new Layer(this, NAME_STRIATUM_LAYER, nBGWidth, nBGHeight);
        addLayer(layerStriatum, true);
        layerStriatum.setActBounds(0, 1);
        projPFC1Str = new Projection(this, layerPFC1, layerStriatum, ProjectionType.ONE_TO_MANY);
        projPFC1Str.setWtType(WtType.UNIFORM, 1D);
        projPFC1Str.connect();
        projStrLat = new Projection(this, layerStriatum, layerStriatum, ProjectionType.MASK);
        projStrLat.setWtType(WtType.UNIFORM, -10D);
        projStrLat.setMask(new int[][] { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } });
        projStrLat.connect();
        projStrLat.setLearner(LearnAlgoUnnormalized.getInstance());
        projStrLat.setWtBounds(-10D, 0);
        projStrLat.setLRate(.1, .1);
        projStrPFC2 = new Projection(this, layerStriatum, layerPFC2, ProjectionType.USER);
        simulateStrPFC2Mapping();
        simulateLateralHebbian();
    }

    protected void simulateStrPFC2Mapping() {
        Iterator<Unit> pfc2Iter = projStrPFC2.to.iterator();
        for (ConnGroup cgo : projPFC1Str.cggo) {
            Unit pfc2Unit = pfc2Iter.next();
            int nConnections = cgo.size();
            for (Connection c : cgo) {
                Unit striatum = c.to;
                projStrPFC2.addConnection(striatum, pfc2Unit, WtType.UNIFORM, 1D / nConnections);
            }
        }
    }

    /**
	 * This function prunes lateral inhibition on the item itself
	 */
    protected void simulateLateralHebbian() {
        for (ConnGroup cgo : projPFC1Str.cggo) {
            for (Connection c : cgo) {
                Unit unit = c.to;
                ConnGroup outgoings = projStrLat.cggo.fromUnit(unit);
                for (Connection conn : outgoings) if (cgo.toUnit(conn.to) != null) conn.wt = 0D;
            }
        }
    }

    @Override
    public void init() {
        super.init();
        simulateLateralHebbian();
    }
}
