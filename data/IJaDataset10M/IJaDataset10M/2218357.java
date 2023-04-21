package com.astrium.faceo.client.events.programming.sps2;

import com.google.gwt.event.shared.EventHandler;

/**
 * <B>SITE FACEO</B> <BR>
 * 
 * <P>
 * La classe ProgrammingDragAndDropHandler 
 * </P>
 * </P>
 * 
 * @author GR
 * @version 1.0, le 17/03/2010
 */
public interface ProgrammingDragAndDropHandler extends EventHandler {

    /**
	 * 
	 * 	 
	 * @param _event 
	 */
    public void onDrop(ProgrammingDragAndDropEvents _event);

    /**
	 * 
	 * 	 
	 * @param _event 
	 */
    public void onDropOut(ProgrammingDragAndDropEvents _event);

    /**
	 * 
	 * 	 
	 * @param _event 
	 */
    public void onDropOver(ProgrammingDragAndDropEvents _event);
}
