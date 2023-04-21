package edu.byu.ece.bitwidth.ptolemy.actor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ptolemy.actor.TypedAtomicActor;
import ptolemy.data.BooleanToken;
import ptolemy.data.IntToken;
import ptolemy.data.Token;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.ChangeListener;
import ptolemy.kernel.util.ChangeRequest;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.Workspace;
import edu.byu.ece.bitwidth.ptolemy.BitwidthDirector;
import edu.byu.ece.bitwidth.ptolemy.data.AffineArithmeticToken;
import edu.byu.ece.bitwidth.ptolemy.data.QuantizationErrorToken;
import edu.byu.ece.bitwidth.ptolemy.data.RangePair;
import edu.byu.ece.bitwidth.ptolemy.data.RangeToken;
import edu.byu.ece.bitwidth.ptolemy.data.SimulationErrorToken;
import edu.byu.ece.bitwidth.ptolemy.strategies.ProbabilisticAffineErrorModel;
import edu.byu.ece.bitwidth.ptolemy.strategies.Simulation;

public abstract class BitwidthActor extends TypedAtomicActor {

    private ChangeListener listener = new ChangeListener() {

        @Override
        public void changeFailed(ChangeRequest change, Exception exception) {
            System.out.println("Change failed");
        }

        @Override
        public void changeExecuted(ChangeRequest change) {
        }
    };

    ;

    ;

    public BitwidthActor(CompositeEntity container, String name) throws IllegalActionException, NameDuplicationException {
        super(container, name);
        fractionalWidth = new Parameter(this, "fractionalWidth");
        fractionalWidth.setTypeAtMost(BaseType.INT);
        fractionalWidth.setToken("32");
        fractionalWidth.addChangeListener(listener);
        fixedWidth = new Parameter(this, "fixedWidth");
        fixedWidth.setTypeEquals(BaseType.BOOLEAN);
        fixedWidth.setToken("false");
        persistentWidth = new Parameter();
        persistentWidth.setTypeAtMost(BaseType.INT);
        persistentWidth.setToken(fractionalWidth.getToken());
    }

    public abstract double getCost();

    /**
	 * Stores the bitwidth during a competition so that it can be restored afterwards
	 */
    private final Parameter persistentWidth;

    /**
	 * Quantizer caches the range token, so that the range does not need to be recomputed each time. 
	 */
    private RangeToken range;

    /**
	 * Quantizer chaches the error for reporting purposes
	 */
    private RangeToken error;

    public Parameter fractionalWidth;

    public Parameter fixedWidth;

    private RangePair errorRange;

    private RangePair rangeRange;

    @Override
    public Object clone(Workspace workspace) throws CloneNotSupportedException {
        BitwidthActor newObject = (BitwidthActor) super.clone(workspace);
        newObject.fractionalWidth = new Parameter();
        try {
            newObject.fractionalWidth.setTypeAtMost(BaseType.INT);
            newObject.fractionalWidth.setToken("32");
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        return newObject;
    }

    public void savePrecision() {
        try {
            persistentWidth.setToken(fractionalWidth.getToken());
        } catch (IllegalActionException e) {
            throw new RuntimeException("Error Checking on fractionalWidth failed.  Cannot save value.");
        }
    }

    public void loadPrecision() {
        try {
            fractionalWidth.setToken(persistentWidth.getToken());
        } catch (IllegalActionException e) {
            throw new RuntimeException("Error Checking failed: bad value in persistenWidth, cannot restore.");
        }
    }

    public void setPrecision(int precision) throws IllegalActionException {
        if (isFixed()) throw new IllegalActionException("Cannot change precision of the fixed operator " + getName());
        fractionalWidth.setToken(precision + "");
    }

    public boolean isFixed() {
        boolean ret = false;
        try {
            BooleanToken bt = (BooleanToken) fixedWidth.getToken();
            ret = bt.booleanValue();
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int getPrecision() {
        int ret = QuantizationErrorToken.FLOATINGPOINT;
        IntToken t;
        try {
            t = ((IntToken) fractionalWidth.getToken());
            ret = t.intValue();
        } catch (IllegalActionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public Token quantize(Token t) throws IllegalActionException {
        Token token = null;
        if (t instanceof RangeToken) {
            range = (RangeToken) t;
            token = t;
        } else if (t instanceof QuantizationErrorToken) {
            QuantizationErrorToken eToken = (QuantizationErrorToken) t;
            RangeToken newRange;
            if (t instanceof SimulationErrorToken) {
                newRange = eToken.getRangeToken();
            } else newRange = range;
            IntToken precisionToken = (IntToken) BaseType.INT.convert(fractionalWidth.getToken());
            int precision = precisionToken.intValue();
            int maxPrecision = eToken.getPrecision();
            if (maxPrecision < precision && precision > 1 && maxPrecision > 1) {
                System.out.print(".");
                precision = maxPrecision;
                setPrecision(maxPrecision);
            }
            if (precision != maxPrecision) token = eToken.quantize(newRange, precision); else {
                token = eToken.newToken(range, precision, eToken.getErrorToken());
            }
            error = ((QuantizationErrorToken) token).getErrorToken();
            BitwidthDirector director = (BitwidthDirector) getDirector();
            if (director.getErrorModel() instanceof ProbabilisticAffineErrorModel) {
                error = ((AffineArithmeticToken) error).getProbabilisticAffineToken();
            }
            if (eToken instanceof SimulationErrorToken) {
                rangeRange.update(((QuantizationErrorToken) token).doubleValue());
                errorRange.update(((QuantizationErrorToken) token).getMaxError());
            }
        } else {
            token = ((BitwidthDirector) getDirector()).newTokenInstance(t);
        }
        return token;
    }

    public String toString() {
        String widthString = "(BadToken)";
        try {
            IntToken precision = (IntToken) BaseType.INT.convert(fractionalWidth.getToken());
            widthString = "" + precision.intValue();
        } catch (IllegalActionException e) {
        }
        String ret = getFullName() + "\t@" + widthString;
        if (range != null) {
            String rangeString = range.toRangeString();
            ret += "\tRange: " + rangeString;
        }
        if (error != null) {
            String errorString = error.toRangeString();
            ret += "\tError: " + errorString;
        }
        return ret;
    }

    public RangeToken getError() {
        if (((BitwidthDirector) getDirector()).getCurrentStrategy() instanceof Simulation) return errorRange.getRange(); else return error;
    }

    private Log logger = LogFactory.getLog(this.getClass());

    public RangeToken getRange() {
        if (((BitwidthDirector) getDirector()).getCurrentStrategy() instanceof Simulation) return rangeRange.getRange(); else return range;
    }

    public void initialize() {
        errorRange = new RangePair();
        rangeRange = new RangePair();
    }
}
