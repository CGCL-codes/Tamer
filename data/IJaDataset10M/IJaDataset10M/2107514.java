package bioweka.normalizers;

import java.beans.PropertyEditorManager;
import weka.gui.GenericObjectEditor;
import bioweka.core.Cloneable;
import bioweka.core.properties.components.AbstractCloneableComponentProperty;

/**
 * Property for handling normalizers.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.4 $
 */
public class NormalizerProperty extends AbstractCloneableComponentProperty implements NormalizerHandler {

    /**
     * The unique class identifier.
     */
    private static final long serialVersionUID = 3258411729170937654L;

    /**
     * The option flag to set the normalizer on command line.
     */
    public static final String NORMALIZER_OPTION_FLAG = "N";

    /**
     * The description of the normalizer property.
     */
    public static final String NORMALIZER_TIP_TEXT = "Sets the normalizer.";

    /**
     * The class type of normalizers.
     */
    public static final Class NORMALIZER_CLASS_TYPE = Normalizer.class;

    /**
     * The name of the normalizer property. 
     */
    public static final String NORMALIZER_PROPERTY_NAME = "normalizer";

    static {
        PropertyEditorManager.registerEditor(NORMALIZER_CLASS_TYPE, GenericObjectEditor.class);
    }

    /**
     * The normalizer.
     */
    private Normalizer normalizer = null;

    /**
     * The default normalizer.
     */
    private Normalizer defaultNormalizer = null;

    /**
     * Initializes the normalizer property.
     * @param defaultNormalizer the default and initial normalizer.
     * @throws NullPointerException if <code>defaultNormalizer</code>
     * is <code>null</code>.
     */
    public NormalizerProperty(Normalizer defaultNormalizer) throws NullPointerException {
        super(NORMALIZER_PROPERTY_NAME);
        if (defaultNormalizer == null) {
            throw new NullPointerException("defaultNormalizer is null.");
        }
        this.normalizer = defaultNormalizer;
        this.defaultNormalizer = (Normalizer) ((Cloneable) defaultNormalizer).clone();
    }

    /**
     * {@inheritDoc}
     */
    protected final Object getComponent() {
        return normalizer;
    }

    /**
     * {@inheritDoc}
     */
    protected final void setComponent(Object component) throws Exception {
        setNormalizer((Normalizer) component);
    }

    /**
     * {@inheritDoc}
     */
    protected Object defaultComponent() {
        return defaultNormalizer;
    }

    /**
     * {@inheritDoc}
     */
    protected Class classType() {
        return NORMALIZER_CLASS_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    public Normalizer getNormalizer() {
        return normalizer;
    }

    /**
     * {@inheritDoc}
     */
    public void setNormalizer(Normalizer normalizer) throws Exception {
        if (normalizer == null) {
            throw new NullPointerException("normalizer is null.");
        }
        change(normalizer);
        this.normalizer = normalizer;
    }

    /**
     * {@inheritDoc}
     */
    public String normalizerTipText() {
        return NORMALIZER_TIP_TEXT;
    }

    /**
     * {@inheritDoc}
     */
    protected String optionFlag() {
        return NORMALIZER_OPTION_FLAG;
    }

    /**
     * {@inheritDoc}
     */
    public String tipText() {
        return NORMALIZER_TIP_TEXT;
    }
}
