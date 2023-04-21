package es.caib.zkib.jxpath;

/**
 * A delegate of {@link JXPathContext} that implements the XPath <code>"key()"</code>
 * function.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 1.1 $ $Date: 2009-04-03 08:13:14 $
 */
public interface KeyManager {

    /**
     * Find a node by key/value.
     *
     * @param context to search
     * @param keyName String
     * @param keyValue String
     * @return Pointer
     */
    Pointer getPointerByKey(JXPathContext context, String keyName, String keyValue);
}
