package com.explosion.expf.dragdrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * @author Stephen Cowx
 * 
 * Used to pass responsibility for rendering Transferable objects back up to the 
 * components that understand them rather than the reusable components
 * that are displaying them
 * 
 * Created: 23-Sep-2005 00:47:43 
 */
public interface DropTargetListenerPlugin {

    public DataFlavor[] getSupportedDataFlavors();

    public String render(Transferable transferable);

    public boolean isReplace();
}
