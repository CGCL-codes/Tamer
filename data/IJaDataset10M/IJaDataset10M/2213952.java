package ag.ion.bion.officelayer.draw.shapes.properties;

/**
 * @see <a href=
 *      "http://api.openoffice.org/docs/common/ref/com/sun/star/drawing/ShadowProperties.html">
 *      OpenOffice documentation </a>
 * 
 * @author Sebastian Patschorke
 * 
 */
public interface IShadowProperties {

    public static final String SHADOW = "Shadow";

    public static final String COLOR = "ShadowColor";

    public static final String TRANSPARENCE = "ShadowTransparence";

    public static final String X_DISTANCE = "ShadowXDistance";

    public static final String Y_DISTANCE = "ShadowYDistance";

    public void setShadow(boolean visible);

    public void setShadowDistance(int x, int y);
}
