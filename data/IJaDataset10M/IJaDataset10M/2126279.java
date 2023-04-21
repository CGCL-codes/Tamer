package de.dfki.lt.signalproc.window;

/**
 * @author Marc Schr&ouml;der
 */
public class DynamicWindow implements InlineDataProcessor {

    protected int windowType;

    /**
	 * An inline data processor applying a window of the requested type to the
	 * data. The window length will always be equal to the data length.
	 */
    public DynamicWindow(int windowType) {
        this.windowType = windowType;
    }

    public double[] values(int len) {
        Window w = Window.get(windowType, len);
        return w.window;
    }

    /**
	 * apply a window of the specified type, with length len, to the data.
	 */
    public void applyInline(double[] data, int off, int len) {
        Window w = Window.get(windowType, len);
        w.applyInline(data, off, len);
    }
}
