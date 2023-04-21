package bioweka.aligners;

import java.beans.PropertyEditorManager;
import weka.gui.GenericObjectEditor;
import bioweka.core.Cloneable;
import bioweka.core.properties.components.AbstractCloneableComponentProperty;

/**
 * Property for aligners.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.5 $
 */
public class AlignerProperty extends AbstractCloneableComponentProperty implements AlignerHandler {

    /**
     * Unique class identifier.
     */
    private static final long serialVersionUID = 3257565118220087349L;

    /**
     * The description of the aligner property.
     */
    public static final String ALIGNER_TIP_TEXT = "Sets the aligner.";

    /**
     * The option flag to set the aligner property on command line.
     */
    public static final String ALIGNER_OPTION_FLAG = "A";

    /**
     * The class type of aligners.
     */
    public static final Class ALIGNER_CLASS_TYPE = Aligner.class;

    /**
     * The name of the aligner property.
     */
    public static final String ALIGNER_PROPERTY_NAME = "aligner";

    static {
        PropertyEditorManager.registerEditor(Aligner.class, GenericObjectEditor.class);
    }

    /**
     * The aligner.
     */
    private Aligner aligner = null;

    /**
     * The default aligner.
     */
    private Aligner defaultAligner = null;

    /**
     * Initializes the aligner property.
     * @param defaultAligner the default and initial aligner.
     * @throws NullPointerException if <code>defaultAligner</code> is 
     * <code>null</code>.
     */
    public AlignerProperty(Aligner defaultAligner) throws NullPointerException {
        super(ALIGNER_PROPERTY_NAME);
        if (defaultAligner == null) {
            throw new NullPointerException("defaultAligner is null.");
        }
        this.aligner = defaultAligner;
        this.defaultAligner = (Aligner) ((Cloneable) defaultAligner).clone();
    }

    /**
     * {@inheritDoc}
     */
    public Aligner getAligner() {
        return aligner;
    }

    /**
     * {@inheritDoc}
     */
    public void setAligner(Aligner aligner) throws NullPointerException, Exception {
        if (aligner == null) {
            throw new NullPointerException("aligner is null");
        }
        change(aligner);
        this.aligner = aligner;
    }

    /**
     * {@inheritDoc}
     */
    public String alignerTipText() {
        return ALIGNER_TIP_TEXT;
    }

    /**
     * {@inheritDoc}
     */
    protected Object getComponent() {
        return aligner;
    }

    /**
     * {@inheritDoc}
     */
    protected void setComponent(Object component) throws Exception {
        setAligner((Aligner) component);
    }

    /**
     * {@inheritDoc}
     */
    protected Class classType() {
        return ALIGNER_CLASS_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    protected Object defaultComponent() {
        return defaultAligner;
    }

    /**
     * {@inheritDoc}
     */
    protected String optionFlag() {
        return ALIGNER_OPTION_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    public String tipText() {
        return ALIGNER_TIP_TEXT;
    }
}
