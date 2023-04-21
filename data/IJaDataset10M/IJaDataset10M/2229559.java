package es.caib.zkib.jxpath.util;

import es.caib.zkib.jxpath.BasicNodeSet;
import es.caib.zkib.jxpath.ExtendedKeyManager;
import es.caib.zkib.jxpath.JXPathContext;
import es.caib.zkib.jxpath.KeyManager;
import es.caib.zkib.jxpath.NodeSet;
import es.caib.zkib.jxpath.Pointer;
import es.caib.zkib.jxpath.ri.InfoSetUtil;

/**
 * Utility class.
 *
 * @author Matt Benson
 * @since JXPath 1.3
 * @version $Revision: 1.1 $ $Date: 2009-04-03 08:13:15 $
 */
public class KeyManagerUtils {

    /**
     * Adapt KeyManager to implement ExtendedKeyManager.
     */
    private static class SingleNodeExtendedKeyManager implements ExtendedKeyManager {

        private KeyManager delegate;

        /**
         * Create a new SingleNodeExtendedKeyManager.
         * @param delegate KeyManager to wrap
         */
        public SingleNodeExtendedKeyManager(KeyManager delegate) {
            this.delegate = delegate;
        }

        public NodeSet getNodeSetByKey(JXPathContext context, String key, Object value) {
            Pointer pointer = delegate.getPointerByKey(context, key, InfoSetUtil.stringValue(value));
            BasicNodeSet result = new BasicNodeSet();
            result.add(pointer);
            return result;
        }

        public Pointer getPointerByKey(JXPathContext context, String keyName, String keyValue) {
            return delegate.getPointerByKey(context, keyName, keyValue);
        }
    }

    /**
     * Get an ExtendedKeyManager from the specified KeyManager.
     * @param keyManager to adapt, if necessary
     * @return <code>keyManager</code> if it implements ExtendedKeyManager
     *         or a basic single-result ExtendedKeyManager that delegates to
     *         <code>keyManager</code>.
     */
    public static ExtendedKeyManager getExtendedKeyManager(KeyManager keyManager) {
        return keyManager instanceof ExtendedKeyManager ? (ExtendedKeyManager) keyManager : new SingleNodeExtendedKeyManager(keyManager);
    }
}
