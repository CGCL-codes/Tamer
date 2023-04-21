package snipsnap.util.collections;

/**
 * Transforms a object to another. Used by Collections.transform().
 * Original author <a href="mailto:mgrosze@web.de">Michael Gro&szlig;e</a>
 *
 * @author stephan
 * @version $Id:Transformer.java 1859 2006-08-08 15:10:07 +0200 (Tue, 08 Aug 2006) leo $
 */
public interface Transformer {

    /**
   * Transforms an object to another
   *
   * @param obj object to transform
   * @return transformed object
   */
    public Object transform(Object obj);
}
