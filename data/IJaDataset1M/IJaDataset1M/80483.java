package de.fzi.injectj.util.log;

/**
 * A basic progress event
 * 
 * @author Sebastian Mies
 */
public class ProgressEvent {

    public static final int BEGIN_TASK = 40;

    public static final int END_TASK = 50;

    public static final int START = 10;

    public static final int END = 20;

    public static final int PROGRESS = 30;

    protected Logging source;

    protected int type;

    public ProgressEvent(Logging source, int type) {
        this.source = source;
        this.type = type;
    }

    /**
	 * @return Returns the source.
	 */
    public Logging getSource() {
        return source;
    }

    /**
	 * @return Returns the type.
	 */
    public int getType() {
        return type;
    }
}
