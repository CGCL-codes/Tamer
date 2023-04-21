package eteg.sinon.core;

/**
 * <p>
 *     Class that represents positioning steps inside a page in Sinon.
 * </p>
 *
 * <p>
 *     The properties of instances of this class are:
 * </p>
 *
 * <ul>
 *     <li>
 *         <code>type</code>: a <code>String</code> containing the
 *         positioning step. The current set of positioning step types are:
 *         {@link #JUMP}, {@link #FROM}, and {@link #TO}.
 *     </li>
 *     <li>
 *         <code>reference</code>: string used as reference to this
 *         positioning step. It is searched inside the page text.
 *         Loop positioning steps does not have a reference.
 *     </li>
 *     <li>
 *         <code>direction</code>: search direction. The possible values
 *         are {@link #FORWARD} and {@link #BACKWARD}.
 *         Default: {@link #FORWARD}.
 *     </li>
 *     <li>
 *         <code>before</code>: string used to specify a position after which
 *         the reference cannot be found. If the reference is found after
 *         the <code>before</code> string, a failure happens.
 *     </li>
 *     <li>
 *         <code>failIfFound</code>: if <code>true</code>, this positioning
 *         steps is considered a failure if the reference is found.
 *         Default: false.
 *     </li>
 *     <li>
 *         <code>mustTrimReference</code>:
 *         Tells if the reference must be trimmed (methodo
 *         <code>String.trim</code> before used. Default: true.
 *     </li>
 * </ul>
 *
 * @author <a href="mailto:thiagohp at users.sourceforge.net">Thiago H. de Paula Figueiredo</a>
 * @author Last modified by $Author: thiagohp $
 * @version $Revision: 1.2 $
 */
public class PositioningStep implements Step {

    /**
     * Constant that defines the jump positioning step type.
     */
    public static final String JUMP = "jump";

    /**
     * Constant that defines the from positioning step type.
     */
    public static final String FROM = "from";

    /**
     * Constant that defines the to positioning step type.
     */
    public static final String TO = "to";

    /**
     * Constant that defines the loop positioning step type.
     */
    public static final String LOOP = "loop";

    /**
     * Constants used to specify a forward search.
     */
    public static final boolean FORWARD = true;

    /**
     * Constants used to specify a backward search.
     */
    public static final boolean BACKWARD = false;

    /**
     * Positioning step type.
     */
    private String type;

    /**
     * String used as reference to this positioning step.
     */
    private String reference;

    /**
     * String que marca uma posi��o a partir da qual ocorr�ncias da
     * refer�ncias ser�o consideradas inv�lidas.
     */
    private String before;

    /**
     * Se <code>true</code>, este passo falha caso a refer�ncia
     * seja encontrada.
     * Default : false
     */
    private boolean failIfFound;

    /**
     * Dire��o da procura. Deve-se utilizar as constantes {@link #FORWARD} e
     * {@link #BACKWARD}.
     */
    private boolean direction;

    /**
     * Tells if the reference must be trimmed (methodo <code>String.trim</code>
     * before used. Default: true.
     */
    private boolean mustTrimReference;

    /**
     * Constructor without parameters.
     */
    public PositioningStep() {
        type = null;
        reference = null;
        direction = FORWARD;
        before = null;
        failIfFound = false;
        mustTrimReference = true;
    }

    /**
     * Returns the value of the <code>before</code> property.
     * @return a <code>String</code>.
     */
    public String getBefore() {
        return before;
    }

    /**
     * Sets the value of the <code>before</code> property.
     * @param before the new <code>before</code> value.
     */
    public void setBefore(String before) {
        this.before = before;
    }

    /**
     * Returns the value of the <code>type</code> property.
     * @return a <code>String</code>.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the <code>type</code> property.
     * @param type the new <code>type</code> value.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the value of the <code>reference</code> property.
     * @return a <code>String</code>.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the <code>reference</code> property.
     * @param reference the new <code>reference</code> value.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Returns the value of the <code>direction</code> property.
     * @return {@link #FORWARD} or {@link #BACKWARD}.
     */
    public boolean getDirection() {
        return direction;
    }

    /**
     * Sets the value of the <code>direction</code> property.
     * @param {@link #FORWARD} or {@link #BACKWARD}.
     */
    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    /**
     * Returns the value of the <code>failIfFound</code> property.
     * @return a <code>boolean</code>.
     */
    public boolean isFailIfFound() {
        return failIfFound;
    }

    /**
     * Sets the value of the <code>failIfFound</code> property.
     * @param failIfFound the new <code>failIfFound</code> value.
     */
    public void setFailIfFound(boolean failIfFound) {
        this.failIfFound = failIfFound;
    }

    /**
     * Returns the value of the <code>mustTrimReference</code> property.
     * @return a <code>String</code>.
     */
    public boolean isMustTrimReference() {
        return mustTrimReference;
    }

    /**
     * Sets the value of the <code>mustTrimReference</code> property.
     * @param mustTrimReference the new <code>mustTrimReference</code> value.
     */
    public void setMustTrimReference(boolean mustTrimReference) {
        this.mustTrimReference = mustTrimReference;
    }

    /**
     * Returns <code>false</code>.
     * @return <code>false</code>
     */
    public boolean isDataExtraction() {
        return false;
    }

    /**
     * Returns <code>true</code>.
     * @return <code>true</code>
     */
    public boolean isPositioningStep() {
        return true;
    }

    /**
     * Diz se este passo � um loop.
     * @return <code>true</code> se este passo � um loop,
     * <code>false</code> caso contr�rio.
     */
    public boolean isLoop() {
        return false;
    }

    /**
     * Returns <code>false</code>.
     * @return false.
     */
    public boolean isAction() {
        return false;
    }

    /**
     * Tells if the type of this positioning step is <code>from</code>.
     * @return <code>true</code> if the type of this positioning step is
     * <code>from</code>, <code>false</code> otherwise.
     */
    public boolean isFrom() {
        return type.equals(FROM);
    }

    /**
     * Tells if the type of this positioning step is <code>to</code>.
     * @return <code>true</code> if the type of this positioning step is
     * <code>to</code>, <code>false</code> otherwise.
     */
    public boolean isTo() {
        return type.equals(TO);
    }

    /**
     * Tells if the type of this positioning step is <code>jump</code>.
     * @return <code>true</code> if the type of this positioning step is
     * <code>jump</code>, <code>false</code> otherwise.
     */
    public boolean isJump() {
        return type.equals(JUMP);
    }
}
