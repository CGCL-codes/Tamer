package org.wings;

import java.net.URL;
import org.wings.plaf.*;
import org.wings.io.Device;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 6 $
 */
public class SHRef extends SButton {

    private static final String cgClassID = "HRefCG";

    /**
     * TODO: documentation
     */
    protected String ref = null;

    /**
     * TODO: documentation
     *
     */
    public SHRef() {
        setFormComponent(false);
    }

    /**
     * TODO: documentation
     *
     * @param text
     */
    public SHRef(String text) {
        super(text);
        setFormComponent(false);
    }

    public SHRef(String text, String ref) {
        super(text);
        setReference(ref);
        setFormComponent(false);
    }

    /**
     * TODO: documentation
     *
     * @param f
     */
    public void setFormComponent(boolean f) {
        super.setFormComponent(false);
    }

    /**
     * TODO: documentation
     *
     * @param url
     */
    public void setReference(String url) {
        ref = url;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getReference() {
        return ref;
    }

    /**
     * Returns the name of the CGFactory class that generates the
     * look and feel for this component.
     *
     * @return "HRefCG"
     * @see SComponent#getCGClassID
     * @see CGDefaults#getCG
     */
    public String getCGClassID() {
        return cgClassID;
    }
}
