package com.finchsync.http;

/**
 * 
 * $Author: $
 * <p>
 * $Revision: $
 */
public interface HttpSessionBindingListener {

    /**
	 * Is called, after a value is bound to the session.
	 * 
	 * @param session SyncSession object , to which the value is bound.
	 * @param name The name, with which the value is bound to the session.
	 */
    void valueBound(HttpSession session, String name);

    /**
	 * Is called, after a value is unbound from the session.
	 * 
	 * @param session SyncSession object, from which the value was unbound.
	 * @param name The name, with which the value was bound to the session.
	 */
    void valueUnbound(HttpSession session, String name);
}
