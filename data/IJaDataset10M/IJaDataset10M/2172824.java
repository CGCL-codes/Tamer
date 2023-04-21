package home.jes.ui.jazz.converters;

import home.jes.ui.jazz.Converter;
import org.jdom.Attribute;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.StringTokenizer;

/**
 * The ColorConverter class defines a Converter that turns the Strings into a Color object
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.0 $
 *
 * @see java.awt.Color
 * @since LiquidOffice 3.0
 *
 * <h3>Examples for Valid XML attribute notations:</h3>
 * <pre><b>The following example show valid xml attributes to create Color objects:</b><br>
 * <ul>
 * <li>background="3399CC"</li>
 * <li>background="red"</li>
 * <li>foreground="991144"</li>
 * </ul>
 * </pre>
 */
public class ColorConverter implements Converter {

    /**
   * Returns a <code>java.awt.Color</code> runtime object
   * @param type <code>Class</code> not used
   * @param attr <code>Attribute</code> value needs to provide a String
   * @return runtime type is subclass of <code>java.awt.Color</code>
   */
    public Object convert(final Class type, final Attribute attr) {
        if (attr != null) {
            try {
                Field field = Color.class.getField(attr.getValue());
                if (Color.class.equals(field.getType()) && Modifier.isStatic(field.getModifiers())) return field.get(Color.class);
            } catch (NoSuchFieldException e) {
            } catch (SecurityException e) {
            } catch (IllegalAccessException e) {
            }
            StringTokenizer st = new StringTokenizer(attr.getValue(), ",");
            if (1 == st.countTokens()) {
                try {
                    return new Color(Integer.parseInt(st.nextToken().trim(), 16));
                } catch (NumberFormatException e) {
                    System.err.println(e.getLocalizedMessage());
                    return null;
                }
            }
            int[] para = Util.ia(st);
            if (4 <= para.length) return new Color(para[0], para[1], para[2], para[3]);
            if (3 <= para.length) return new Color(para[0], para[1], para[2]);
            if (1 <= para.length) return new Color(para[0]);
        }
        return null;
    }
}
