package de.jaret.util.ui.timebars.swt.renderer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.Printer;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;

/**
 * Interface describing a renderer that is called to do global rendering on the timebarviewer.
 * 
 * @author Peter Kliem
 * @version $Id: GlobalAssistantRenderer.java 800 2008-12-27 22:27:33Z kliem $
 */
public interface GlobalAssistantRenderer {

    /**
     * Will be called before the interval rendering starts.
     * 
     * @param delegate delegate that calls
     * @param gc GC
     * @param printing true for printing
     */
    void doRenderingBeforeIntervals(TimeBarViewerDelegate delegate, GC gc, boolean printing);

    /**
     * Will be called after the intervals have been rendered.
     * 
     * @param delegate delegate that calls
     * @param gc GC
     * @param printing true for printing
     */
    void doRenderingLast(TimeBarViewerDelegate delegate, GC gc, boolean printing);

    /**
     * Dispose whatever has been allocated.
     * 
     */
    void dispose();

    /**
     * Produce a renderer for printing.
     * 
     * @param printer printer to use
     * @return configured renderer for printing
     */
    GlobalAssistantRenderer createPrintRenderer(Printer printer);
}
