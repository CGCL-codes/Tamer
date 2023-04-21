package org.wahlzeit.handlers;

import org.wahlzeit.model.*;
import org.wahlzeit.webparts.*;

/**
 * 
 * @author driehle
 *
 */
public interface WebPageHandler extends WebPartHandler {

    /**
	 * 
	 */
    public WebPart makeWebPart(UserSession ctx);
}
