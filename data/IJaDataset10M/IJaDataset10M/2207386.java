package de.rockon.fuzzy.simulation.view.frames;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import mdes.slick.sui.Frame;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import mdes.slick.sui.event.ChangeEvent;
import mdes.slick.sui.event.ChangeListener;
import de.rockon.fuzzy.controller.model.listener.IModelObserver;
import de.rockon.fuzzy.controller.model.listener.IModelSubject;
import de.rockon.fuzzy.controller.util.factories.IconFactory;
import de.rockon.fuzzy.simulation.cases.BaseSimulation;

/**
 * Superklasse f�r alle Frames in einer Simulationsumgebung
 */
public abstract class FuzzyFrame extends Frame implements ActionListener, ChangeListener, IModelObserver {

    /** Die Simulation */
    protected BaseSimulation parent;

    /** the icon */
    protected Image icon = null;

    private static final String DEFAULT_ICON_PATH = IconFactory.ICON_ABOUT;

    /**
	 * Konstruktor
	 * 
	 * @param parent
	 *            Die zugeh�rige Simulation
	 */
    public FuzzyFrame(String title, BaseSimulation parent) {
        super(title);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.3f));
        this.parent = parent;
        this.parent.registerObserver(this);
        getTitleBar().remove(getCloseButton());
        setFrameIcon(DEFAULT_ICON_PATH);
        initFrame();
        reset();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /** Adapter Methoden f�r die implemetierten Schnittstellen */
    @Override
    public void addEvent(IModelSubject subject, Object addItem) {
    }

    @Override
    public void changeEvent(IModelSubject subject, Object changeItem) {
    }

    /** Initialisiert alle Frame Elemente */
    public abstract void initFrame();

    /** Custom Render Methode */
    public abstract void paint(Graphics g);

    @Override
    public void removeEvent(IModelSubject subject, Object remItem) {
    }

    /** Setzt den Frame auf Standardwerte zur�ck */
    public abstract void reset();

    /**
	 * @param iconPath
	 *            setzt das FrameIcon
	 */
    public void setFrameIcon(final String iconPath) {
        try {
            icon = new Image(iconPath);
            setWindowIcon(icon);
        } catch (Exception e) {
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
    }
}
