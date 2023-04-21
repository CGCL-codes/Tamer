package universe.client.gui;

import universe.common.database.Index;

/**
 * ScreenObject.java
 *
 *
 * Created: Thu Jun 10 13:24:51 1999
 *
 * @author Shalon Wood
 * @version $Id: ScreenObject.java,v 1.1 2003/04/03 00:20:54 sstarkey Exp $
 */
public class ScreenObject {

    ScreenObjectCapable objectForScreen;

    /**
	   * Get the value of objectForScreen.
	   * @return Value of objectForScreen.
	   */
    public ScreenObjectCapable getObjectForScreen() {
        return objectForScreen;
    }

    /**
	   * Set the value of objectForScreen.
	   * @param v  Value to assign to objectForScreen.
	   */
    public void setObjectForScreen(ScreenObjectCapable v) {
        this.objectForScreen = v;
    }

    ScreenObjectHandler handler;

    /**
	   * Get the value of handler.
	   * @return Value of handler.
	   */
    public ScreenObjectHandler getHandler() {
        return handler;
    }

    /**
	   * Set the value of handler.
	   * @param v  Value to assign to handler.
	   */
    public void setHandler(ScreenObjectHandler v) {
        this.handler = v;
    }

    Index id;

    /**
	   * Get the value of id.
	   * @return Value of id.
	   */
    public Index getId() {
        return id;
    }

    public ScreenObject(ScreenObjectCapable o, ScreenObjectHandler soh) {
        objectForScreen = o;
        handler = soh;
        id = o.getID();
    }
}
