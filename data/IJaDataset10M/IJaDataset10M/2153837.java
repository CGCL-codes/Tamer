package jaxlib.attribute;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: Attributed.java 2695 2009-01-02 07:25:42Z joerg_wassmer $
 */
public interface Attributed {

    public <T> T getAttribute(Object key);
}
