package jbreport.xrl;

/**
 * 
 * @author Grant Finnemore
 * @version $Revision: 1.1 $
 */
public interface XRLStylesheet extends XRLComposite {

    /** The stylesheet is for HTML renderers */
    public static final int ST_HTML = 0x01;

    /**
    * Returns the type of the stylesheet. This type is given by the ST_ 
    * enumerated constants.
    */
    public int getStylesheetType();
}
