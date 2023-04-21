package shieh.pnn.wm;

import shieh.pnn.core.Layer;
import shieh.pnn.core.Network;
import shieh.pnn.core.Param;
import shieh.pnn.core.Projection;
import shieh.pnn.core.ProjectionType;
import shieh.pnn.core.Unit;
import shieh.pnn.core.WtType;

/**
 * Tokenizer Layer
 * @author Danke Shieh
 *
 * This layer simulates the neural version for faster simulation. 
 * 
 */
public class LayerTokenizerMath extends Layer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 330160807270992695L;

    public static final String NAME_TOKENIZER_LAYER = "Tokenizer (Math)";

    protected Layer layerInput = null;

    protected int nCount, nSize;

    public LayerTokenizerMath(Network model, int nCount, Layer layerInput) {
        super(model, NAME_TOKENIZER_LAYER, nCount * layerInput.size());
        this.layerInput = layerInput;
        this.nCount = nCount;
        this.nSize = layerInput.size();
        setDecay(Param.decay_rate);
        setDisplayHeight(nCount);
        setDisplayWidth(nSize);
        Projection proj = new Projection(model, layerInput, this, ProjectionType.USER);
        proj.setWtType(WtType.UNIFORM, 0D);
        proj.connect();
    }

    @Override
    public void step() {
        int i, j;
        i = 0;
        for (Unit u : layerInput.units) {
            if (u.out >= Param.significant_input) {
                j = 0;
                while (j < nCount && getUnitAt(i, j).out >= Param.significant_input) j++;
                if (j < nCount) getUnitAt(i, j).setExtInput(u.out, u.phase, 1);
            }
            i++;
        }
        super.step();
    }

    @Override
    public void reset() {
        super.reset();
    }
}
