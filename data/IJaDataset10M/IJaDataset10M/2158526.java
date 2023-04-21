package org.qtitools.mathassess.tools.qticasbridge.types;

import org.qtitools.mathassess.tools.qticasbridge.ASCIIMathMLHelper;
import org.qtitools.mathassess.tools.qticasbridge.maxima.QTIMaximaSession;

/**
 * Trivial wrapper representing a <tt>MathsContent</tt> value.
 * <p>
 * You should fill one of these in when passing such values <tt>into</tt> the QTI/CAS layer.
 * The fields in this Object directly mirror the fields in the spec for <tt>MathsContent</tt>
 * variables (albeit with slightly different names).
 * <p>
 * Note that we are deviating from QTI slightly here by making this type explicit, rather
 * than implicitly equating it with a "record" cardinality value.
 * 
 * <h2>Usage Notes</h2>
 * 
 * <ul>
 *   <li>
 *     This hopefully mirrors a similar kind of structure in your calling code that encapsulates
 *     the various fields in MathsContent values.
 *   </li>
 *   <li>
 *     You should be able to populate one of these from an existing MathsContent value in "your"
 *     code by copying fields over.
 *   </li>
 *   <li>
 *     When the QTI/CAS code returns MathsContent values to you, it will actually return an
 *     instance of the more verbose subclass {@link MathsContentOutputValueWrapper}. You can
 *     feel free to ignore the additional fields in that subclass when extracting MathsContent
 *     fields.
 *   </li>
 *   <li>
 *     You should never find yourself having to mess around with XML to fill one of these in.
 *     Methods in {@link QTIMaximaSession} cope with creating these from Maxima outputs and
 *     {@link ASCIIMathMLHelper} makes it easy to populate instances of these values from
 *     raw ASCIIMath input/output.
 *   </li>
 * </ul> 
 *
 * @author  David McKain
 * @version $Revision: 2428 $
 */
public class MathsContentValueWrapper implements ValueWrapper {

    /**
     * Raw ASCIIMath input (called <tt>CandidateInput</tt> in our spec document).
     * <p>
     * This should be non-null if this MathsContent value was produced by an ASCIIMathML
     * input.
     * 
     * @see ASCIIMathMLHelper
     */
    protected String asciiMathInput;

    /** 
     * Tidied up Presentation MathML (called <tt>PMathML</tt> in our spec document).
     */
    protected String pMathML;

    /**
     * Content MathML form obtained by up-conversion process (called <tt>CMath</tt> in our spec
     * document).
     */
    protected String cMathML;

    /** 
     * Up-converted form suitable for Maxima input.
     * <p>
     * You MUST fill this in when creating a new wrapper
     * for the {@link MathsContentSource#STUDENT_MATH_ENTRY_INTERACTION} case.
     * <p>
     * This will ALWAYS be filled in when returning MathsContent from {@link QTIMaximaSession}
     * in the {@link MathsContentSource#CAS_OUTPUT} case UNLESS the up-conversion process
     * fails, in which case it will be null.
     */
    protected String maximaInput;

    public final String getAsciiMathInput() {
        return asciiMathInput;
    }

    public final void setAsciiMathInput(String asciiMathInput) {
        this.asciiMathInput = asciiMathInput;
    }

    public final String getPMathML() {
        return pMathML;
    }

    public final void setPMathML(String pmathML) {
        this.pMathML = pmathML;
    }

    public String getCMathML() {
        return cMathML;
    }

    public void setCMathML(String cmathML) {
        this.cMathML = cmathML;
    }

    public final String getMaximaInput() {
        return maximaInput;
    }

    public final void setMaximaInput(String maximaInput) {
        this.maximaInput = maximaInput;
    }

    public final ValueCardinality getCardinality() {
        return ValueCardinality.MATHS_CONTENT;
    }

    public final boolean isNull() {
        return pMathML == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(asciiMathInput=" + asciiMathInput + ",pMathML=" + pMathML + ",cMathML=" + cMathML + ",maximaInput=" + maximaInput + ")";
    }
}
