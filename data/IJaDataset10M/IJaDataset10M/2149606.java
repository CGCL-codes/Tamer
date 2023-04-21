package net.benojt.display;

/**
 * an interface for displays that buffer the data to be displayed. 
 * such displays can re-color without  re-rendering
 * @author felfe
 *
 */
public interface BufferedDisplay {

    /**
	 * redraw the display with data from the buffers.
	 */
    void reColor();
}
