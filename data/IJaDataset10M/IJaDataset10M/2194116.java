package org.dcm4che2.code;

/**
 * CID 3713 TIMI Flow Characteristics.
 *
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Rev: 13502 $ $Date:: 2010-06-09#$
 * @since Jun 2, 2010
 */
public class TIMIFlowCharacteristics {

    /** (0: No Perfusion, SRT[R-0037E], "106-0, 107-0") */
    public static final String Ten601070 = "0: No Perfusion\\SRT[R-0037E]";

    /** (1: Penetration without Perfusion, SRT[R-0037F], "106-1, 107-1") */
    public static final String Ten611071 = "1: Penetration without Perfusion\\SRT[R-0037F]";

    /** (2: Partial Perfusion, SRT[R-00381], "106-2, 107-2") */
    public static final String Ten621072 = "2: Partial Perfusion\\SRT[R-00381]";

    /** (3: Complete Perfusion, SRT[R-00382], "106-3, 107-3") */
    public static final String Ten631073 = "3: Complete Perfusion\\SRT[R-00382]";
}
