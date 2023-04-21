package javaaxp.core.service.model.document.page;

public interface IImageBrush extends IPageResource {

    /**
	 * Gets the value of the imageBrushTransform property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link CTCPTransform }
	 *     
	 */
    public abstract ITransform getImageBrushTransform();

    /**
	 * Gets the value of the imageSource property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
    public abstract String getImageSource();

    /**
	 * Gets the value of the opacity property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Double }
	 *     
	 */
    public abstract double getOpacity();

    /**
	 * Gets the value of the tileMode property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link STTileMode }
	 *     
	 */
    public abstract STTileMode getTileMode();

    /**
	 * Gets the value of the transform property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
    public abstract String getTransform();

    /**
	 * Gets the value of the viewbox property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
    public abstract String getViewbox();

    /**
	 * Gets the value of the viewboxUnits property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link STViewUnits }
	 *     
	 */
    public abstract STViewUnits getViewboxUnits();

    /**
	 * Gets the value of the viewport property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
    public abstract String getViewport();

    /**
	 * Gets the value of the viewportUnits property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link STViewUnits }
	 *     
	 */
    public abstract STViewUnits getViewportUnits();
}
