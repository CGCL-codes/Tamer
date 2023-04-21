package utoopia.content.html;

/**
 * Representa al elemento HTML "body"
 * @author Jose
 */
public class Body extends Container {

    /**
	 * Constructor
	 */
    public Body() {
        super();
    }

    /**
	 * Constructor
	 * @param element Elemento a añadir al cuerpo
	 */
    public Body(Element element) {
        super(element);
    }

    @Override
    protected String getEmptyContent() {
        return "";
    }

    @Override
    protected void setAttributes() {
    }

    @Override
    protected String setTagName() {
        return "body";
    }
}
