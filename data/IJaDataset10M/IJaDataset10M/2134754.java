package ieditor.model.shape;

import ieditor.model.InfoPageElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.swt.graphics.Image;

/**
 * A rectangular shape.
 * @author Elias Volanakis
 */
@XmlRootElement(name = "alphaRegion")
public class IAlphaRegion extends InfoPageElement {

    private static final long serialVersionUID = 4312196855709094018L;

    private int transparency;

    @XmlAttribute
    public int getTransparency() {
        return transparency;
    }

    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }

    public Image getIcon() {
        return null;
    }
}
