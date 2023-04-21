package com.innovative.main;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import com.innovative.main.actions.Perspective;
import com.innovative.main.panels.StatusPanel;

/**
 * A container for {@link DocumentComponent}s to display and interact with from the {@link MainFrame} window
 *
 * @param <T> 
 * @see com.innovative.main.MainFrame
 * @author Dylon Edwards
 * @since 0.2
 */
public abstract class MainComponent<T extends DocumentCanvas<?, ?>> extends JPanel {

    private static final long serialVersionUID = -2786734484658521252L;

    /** Holds the MainFrame object which instantiated this MainComponent object */
    protected static final MainFrame frame = MainFrame.getInstance();

    /** Holds the {@link DocumentCanvas}s contained in this MainComponent */
    protected final List<T> canvases = new ArrayList<T>();

    /** 
	 * Holds the {@link com.innovative.main.actions.MainAction} associated 
	 * with this MainComponent 
	 */
    private final Perspective<? extends MainComponent<T>> perspective;

    /**
	 * Holds the current value to display in {@link com.innovative.main.panels.StatusPanel#scaleCombo}
	 */
    private Object value = StatusPanel.FIT_PAGE;

    /**
	 * Constructs a MainComponent super class to simplify handling common tasks within classes directly related to the
	 * MainFrame
	 *
	 * @param frame
	 * @param name
	 * @param icon
	 * @param mnemonic 
	 * @param desc
	 * @param accel
	 * @throws IllegalArgumentException When unitSystem is invalid
	 * @throws NullPointerException When mainFrame is null
	 */
    public MainComponent(final String name, final Icon icon, final String desc, final Integer mnemonic, final KeyStroke accel) {
        perspective = new Perspective<MainComponent<T>>(frame, this, name, icon, desc, mnemonic, accel);
    }

    /**
	 * Fetches and returns the {@link #mainFrame}
	 *
	 * @return The MainFrame object from which this MainComponent object was instantiated
	 */
    public MainFrame getFrame() {
        return frame;
    }

    /**
	 * Sets the value to display in {@link com.innovative.main.panels.StatusPanel#scaleCombo}
	 *
	 *
	 * @param value
	 */
    public void setValue(final Object value) {
        assert value != null : "value may not be null";
        this.value = value;
    }

    /**
	 * Returns the value to display in {@link com.innovative.main.panels.StatusPanel#scaleCombo}
	 *
	 * @return {@link #value}
	 */
    public Object getValue() {
        return value;
    }

    /**
	 * Fetches and returns the {@link #perspective} associated with this MainComponent
	 *
	 * @return The {@link #perspective} attribute
	 */
    public Perspective<? extends MainComponent<T>> getPerspective() {
        return perspective;
    }

    /**
	 * Sets the {@link #documentCanvas} for this instance of MainComponent
	 *
	 * @param canvas The {@link DocumentCanvas} attribute of this MainComponent
	 * @throws NullPointerException When documentCanvas is null
	 */
    public void addCanvas(final T canvas) {
        assert canvas != null : "documentCanvas may not be null";
        canvases.add(canvas);
    }

    /**
	 *
	 * @param canvas
	 */
    protected void removeCanvas(T canvas) {
        canvases.remove(canvas);
    }

    /**
	 *
	 * @return
	 */
    public int getCanvasCount() {
        return canvases.size();
    }

    /**
	 *
	 * @param index
	 * @return
	 */
    public T getCanvas(int index) {
        return canvases.get(index);
    }

    /**
	 * Fetches and returns the {@link #documentCanvas} of this instance of MainComponent
	 *
	 * @param array
	 * @return The {@link #documentCanvas} attribute
	 */
    public T[] getCanvases(T[] array) {
        return canvases.toArray(array);
    }
}
