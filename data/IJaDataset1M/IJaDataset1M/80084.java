package jaxlib.closure;

/**
 * TODO: comment
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: FloatFunction.java 1044 2004-04-06 16:37:29Z joerg_wassmer $
 */
public abstract class FloatFunction extends Object implements FloatTransformer {

    protected FloatFunction() {
        super();
    }

    public abstract float apply(float e);
}
