package spacefaring.ui.game;

import static spacefaring.ui.layout.FlowTechnique.Direction.Down;
import static spacefaring.ui.layout.FlowTechnique.Direction.Left;
import static spacefaring.ui.layout.FlowTechnique.Direction.Right;
import static spacefaring.ui.layout.FlowTechnique.Direction.Up;
import static spacefaring.util.PointUtility.newIP;
import static spacefaring.ui.controls.SelectionMode.*;
import java.util.List;
import java.util.Vector;
import spacefaring.Spacefaring;
import spacefaring.events.ClickEvent;
import spacefaring.events.Event;
import spacefaring.events.Listener;
import spacefaring.events.SelectEvent;
import spacefaring.game.InfoDatabase;
import spacefaring.game.starsystem.Planet;
import spacefaring.game.starsystem.Starsystem;
import spacefaring.ui.ContainerView;
import spacefaring.ui.ViewController;
import spacefaring.ui.controls.ButtonView;
import spacefaring.ui.controls.TileScrollView;
import spacefaring.ui.layout.FlowTechnique;
import static spacefaring.util.Err.*;

/**
 * A window showing multiple planets for the user to choose from.
 * 
 * @author astrometric
 */
public class PlanetPickerView extends GameWindowView implements Listener {

    protected ButtonView okbutton;

    protected TileScrollView<Planet> planettilescrollview;

    protected Listener selectionlistener;

    /**
     * Creates a new instance of PlanetPickerView
     */
    public PlanetPickerView(Spacefaring sfinstance) {
        super("PlanetPickerView", sfinstance, 490, 390);
        setDesiredSize(490, 390);
        setPos(20, 20);
        setTitle("- Planet Picker -");
        setupChildren();
    }

    /**
     * Creates the controls and adds them to the window.
     */
    private void setupChildren() {
        setLayoutTechnique(new FlowTechnique(Down, Right, 0));
        ContainerView leftcon = new ContainerView("leftcon", new FlowTechnique(Right, Down, 0), newIP(110, 330), 0, 5, 0, 0);
        ContainerView rightupcon = new ContainerView("rightupcon", new FlowTechnique(Down, Right, 0), newIP(160, 110), 0, 5, 0, 0);
        ContainerView rightdowncon = new ContainerView("rightdowncon", new FlowTechnique(Left, Up, 4), newIP(160, 20), 0, 0, 0, 0);
        planettilescrollview = new TileScrollView<Planet>("planettilescrollview", 1, 3, SINGLE);
        leftcon.addChild(planettilescrollview);
        ContainerView leftlabelcon = new ContainerView("leftlabelcon", new FlowTechnique(Down, Right, 2), newIP(40, 100), 0, 0, 0, 0);
        ContainerView rightlabelcon = new ContainerView("rightlabelcon", new FlowTechnique(Down, Right, 2), newIP(120, 100), 0, 0, 0, 0);
        okbutton = rightdowncon.addNewSmallButton("OK", this);
        rightupcon.addChild(leftlabelcon);
        rightupcon.addChild(rightlabelcon);
        addChild(leftcon);
        addChild(rightupcon);
        addChild(rightdowncon);
    }

    /**
     * Displays the PlanetPickerView window and associates a listener to it.
     * The listener will be informed when a planet is selection by the user by
     * clicking the OK button. 
     */
    public void show(Listener l) {
        argcheckNull(l, "l");
        selectionlistener = l;
        super.show();
        update();
    }

    @Override
    public void adaptToConfig(ViewController.GUIConfiguration guicon) {
        update();
    }

    public void updateDisplay() {
        update();
    }

    private void update() {
        if (!isVisible()) return;
        updateContent();
        positionAndStrechThisAndChildren(getPosition(), getSize());
    }

    /**
     * Sets the values of the controls to the current planet.
     */
    private void updateContent() {
        InfoDatabase infodb = sf.game.currentplayer.infodb;
        Vector<Starsystem> systems = infodb.exploredstarsystems;
        Vector<Planet> targetplanets = new Vector<Planet>();
        for (Starsystem starsystem : systems) {
            for (Planet planet : starsystem.planets) {
                if (planet.isHabitable()) {
                    targetplanets.add(planet);
                }
                if (planet.hasHabitableMoon()) {
                    for (Planet moon : planet.moons) {
                        if (moon.isHabitable()) {
                            targetplanets.add(moon);
                        }
                    }
                }
            }
        }
        planettilescrollview.clear();
        planettilescrollview.addItems(targetplanets);
    }

    /**
     * Returns the selected planet. This method returns null if no planet was
     * selected.
     */
    public Planet getSelectedPlanet() {
        List<Planet> sel = planettilescrollview.getSelectedItems();
        if (sel.size() == 0) {
            return null;
        } else {
            return sel.get(0);
        }
    }

    /**
     * Handles the click events for the buttons.
     * @param evetype
     * @param eventsource
     */
    @Override
    public void processEvent(Event e, Object eventsource) {
        if (e instanceof ClickEvent) {
            if (eventsource == okbutton) {
                clickOk();
            }
        }
    }

    /**
      * Closes window and fires select event (only if planet was picked). 
      */
    private void clickOk() {
        if (planettilescrollview.getSelectionCount() > 0) {
            selectionlistener.processEvent(new SelectEvent(), this);
        }
        close();
    }
}
