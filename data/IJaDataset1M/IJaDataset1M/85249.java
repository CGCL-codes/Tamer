package org.akrogen.tkui.core.dbel.objectproviders;

/**
 * {@link IObjectProvider} is used to retrieve Object from one context.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public interface IObjectProvider {

    /**
	 * Retrieve object registered into one context with <code>key</code>.
	 * 
	 * @param key
	 * @return
	 */
    public Object findObject(Object key);
}
