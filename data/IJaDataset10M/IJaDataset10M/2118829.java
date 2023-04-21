package pl.voidsystems.yajf.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Image extends Component {

    /**
     * @uml.property  name="image_path"
     */
    protected String image_path = "";

    /**
     * @uml.property  name="alt"
     */
    protected String alt = "";

    /**
     * @uml.property  name="width"
     */
    protected int width = 0;

    /**
     * @uml.property  name="height"
     */
    protected int height = 0;

    public Image(String name, String path) {
        super(name);
        this.image_path = path;
    }

    @Override
    public Element paintAt(Element element) throws PaintException {
        final Document doc = element.getOwnerDocument();
        final Element image = doc.createElement("image");
        image.setAttribute("path", this.getImagePath());
        image.setAttribute("alt", this.getAlt());
        image.setAttribute("width", "" + this.width);
        image.setAttribute("height", "" + this.height);
        return image;
    }

    public String getImagePath() {
        return this.image_path;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }

    /**
     * Getter of the alt property
     * @return  Returns the alt.
     * @uml.property  name="alt"
     */
    public String getAlt() {
        return this.alt;
    }

    /**
     * Setter of the alt property
     * @param alt  The alt to set.
     * @uml.property  name="alt"
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     * Getter of the height property
     * @return  Returns the height.
     * @uml.property  name="height"
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Setter of the height property
     * @param height  The height to set.
     * @uml.property  name="height"
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter of the width property
     * @return  Returns the width.
     * @uml.property  name="width"
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Setter of the width property
     * @param width  The width to set.
     * @uml.property  name="width"
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
