package de.jmulti.proc;

import com.jstatcom.model.JSCData;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCTypeDef;
import com.jstatcom.model.JSCTypes;
import de.jmulti.tools.ModelTypes;

/**
 * This GAUSS command computes an impulse response function
 * for the selected model according to the parametrization
 * and the number of periods. The result of this call
 * is stored to the symbol table if one has been set.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig</a>
 */
public final class IRAFuncCall extends GaussPCall {

    /**
	 * Forecast error impulse responses.
	 */
    public static final JSCTypeDef IR_FERR = new JSCTypeDef("ir_ferr", JSCTypes.NARRAY, "Forecast error impulse responses.");

    /**
	 * Accumulated forecast error impulse responses.
	 */
    public static final JSCTypeDef IR_FERR_AC = new JSCTypeDef("ir_ferr_ac", JSCTypes.NARRAY, "Accumulated forecast error impulse responses.");

    /**
	 * Orthogonal impulse responses.
	 */
    public static final JSCTypeDef IR_ORTH = new JSCTypeDef("ir_orth", JSCTypes.NARRAY, "Orthogonal impulse responses.");

    /**
	 * Accumulated orthogonal impulse responses.
	 */
    public static final JSCTypeDef IR_ORTH_AC = new JSCTypeDef("ir_orth_ac", JSCTypes.NARRAY, "Accumulated orthogonal impulse responses.");

    /**
	 * SVAR/SVEC impulse responses.
	 */
    public static final JSCTypeDef IR_SVAR = new JSCTypeDef("ir_svar", JSCTypes.NARRAY, "SVAR/SVEC impulse responses.");

    /**
	* Accumulated SVAR/SVEC impulse responses.
	*/
    public static final JSCTypeDef IR_SVAR_AC = new JSCTypeDef("ir_svar_ac", JSCTypes.NARRAY, "Accumulated SVAR/SVEC impulse responses.");

    private int periods = 20;

    private JSCNArray yCoeff = null;

    private JSCNArray structMatrix = null;

    private JSCNArray covar = null;

    private ModelTypes modelType = ModelTypes.VAR;

    /**
	 * IRAFuncCall constructor comment.
	 */
    public IRAFuncCall(int periods, JSCNArray yCoeff, JSCNArray structMatrix, JSCNArray covar, ModelTypes modelType) {
        super();
        setName("Compute IR function for " + modelType);
        this.periods = periods;
        this.yCoeff = yCoeff;
        this.structMatrix = structMatrix;
        this.covar = covar;
        this.modelType = modelType;
    }

    /**
	 * @see ProcCall
	 */
    protected void runCode() {
        if (modelType == ModelTypes.SVAR || modelType == ModelTypes.SVEC) {
            if (getSymbolTable() != null) {
                getSymbolTable().get(IR_SVAR).clear();
                getSymbolTable().get(IR_SVAR_AC).clear();
            }
            JSCData[] retArg = new JSCData[] { IR_SVAR.getInstance(), IR_SVAR_AC.getInstance() };
            engine().call("ComputeIRF_svar_var", new JSCData[] { yCoeff, new JSCInt("periods", periods), structMatrix }, retArg);
            if (getSymbolTable() != null) getSymbolTable().set(retArg);
            return;
        }
        if (modelType == ModelTypes.VAR || modelType == ModelTypes.VECM) {
            if (getSymbolTable() != null) {
                getSymbolTable().get(IR_FERR).clear();
                getSymbolTable().get(IR_FERR_AC).clear();
                getSymbolTable().get(IR_ORTH).clear();
                getSymbolTable().get(IR_ORTH_AC).clear();
            }
            JSCData[] retArg = new JSCData[] { IR_FERR.getInstance(), IR_FERR_AC.getInstance(), IR_ORTH.getInstance(), IR_ORTH_AC.getInstance() };
            engine().call("var_impulseResponses", new JSCData[] { structMatrix, yCoeff, covar, new JSCInt("periods", periods), new JSCInt("fmt", 1) }, retArg);
            if (getSymbolTable() != null) getSymbolTable().set(retArg);
            return;
        }
    }
}
