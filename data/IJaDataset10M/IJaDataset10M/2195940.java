package be.vds.jtbtaskplanner.client.view.core;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import be.vds.jtbtaskplanner.client.actions.PlanningApplActionsContoller;
import be.vds.jtbtaskplanner.client.core.GlossaryManagerFacade;
import be.vds.jtbtaskplanner.client.core.PlanningManagerFacade;
import be.vds.jtbtaskplanner.client.core.UserPreferences;
import be.vds.jtbtaskplanner.client.core.event.LogBookEvent;
import be.vds.jtbtaskplanner.client.swing.component.glass.AlphaGlassPane;
import be.vds.jtbtaskplanner.client.util.ResourceManager;

public class PlanningApplFrame extends JFrame implements Observer {

    private static final long serialVersionUID = -6897808431393389767L;

    private Logger LOGGER = Logger.getLogger(PlanningApplFrame.class);

    private PlanningApplPanel planningDockPanel;

    private PlanningManagerFacade planningManagerFacade;

    private GlossaryManagerFacade glossaryManagerFacade;

    private PlanningApplActionsContoller actions;

    private AlphaGlassPane glassPane;

    public PlanningApplFrame(PlanningManagerFacade logBookManagerFacade, GlossaryManagerFacade glossaryManagerFacade) {
        this.planningManagerFacade = logBookManagerFacade;
        this.glossaryManagerFacade = glossaryManagerFacade;
        logBookManagerFacade.addObserver(this);
        init();
    }

    public PlanningApplPanel getLogbookDockPanel() {
        return planningDockPanel;
    }

    private void init() {
        actions = new PlanningApplActionsContoller(this, planningManagerFacade, glossaryManagerFacade);
        adaptTitle();
        this.setIconImage(ResourceManager.getInstance().getImageIcon("diver16.png").getImage());
        this.getContentPane().add(createContentPanel());
        this.setGlassPane(createGlasspane());
        this.setJMenuBar(createJMenuBar());
    }

    private Component createGlasspane() {
        glassPane = new AlphaGlassPane(0.65f);
        return glassPane;
    }

    private JMenuBar createJMenuBar() {
        PlanningApplMenuBar bar = new PlanningApplMenuBar(this, actions);
        return bar;
    }

    private Component createContentPanel() {
        planningDockPanel = new PlanningApplPanel(planningManagerFacade, glossaryManagerFacade, actions);
        return planningDockPanel;
    }

    /**
	 * Closes the window and remove itself from the Application.
	 */
    public void closeWindow() {
        planningDockPanel.saveLayout();
        saveFramePosition();
        this.dispose();
        LOGGER.info("Window Closed");
        firePropertyChange("windowClosed", this, null);
    }

    private void saveFramePosition() {
        UserPreferences up = UserPreferences.getInstance();
        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            up.setWindowWidth(0);
            up.setWindowHeigth(0);
            up.setWindowTop(0);
            up.setWindowLeft(0);
            LOGGER.debug("Cancel Windows Position as it is maximized");
        } else {
            Point p = getLocationOnScreen();
            int w = getWidth();
            int h = getHeight();
            int l = (int) p.getX();
            int t = (int) p.getY();
            up.setWindowWidth(w);
            up.setWindowHeigth(h);
            up.setWindowLeft(l);
            up.setWindowTop(t);
            LOGGER.debug("Set Windows param to " + w + "*" + h + " / x:" + l + " y:" + t);
        }
        up.savePreferences(false);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            LOGGER.debug("Closing the frame using the OS window close button");
            closeWindow();
        } else {
            super.processWindowEvent(e);
        }
    }

    public PlanningManagerFacade getLogBookManagerFacade() {
        return planningManagerFacade;
    }

    public void adaptLanguage() {
        planningDockPanel.adaptLanguage();
    }

    public void showView(String viewKey) {
        planningDockPanel.showView(viewKey);
    }

    public void changePerspective(int perspective) {
        planningDockPanel.changePerspective(perspective);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof LogBookEvent) {
            LogBookEvent event = (LogBookEvent) arg;
            if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED) || event.getType().equals(LogBookEvent.LOGBOOK_META_SAVED) || event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
                adaptTitle();
            }
        }
    }

    private void adaptTitle() {
    }

    public void setGlassPaneActive(final boolean b) {
        if (b) {
            this.setGlassPane(glassPane);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                glassPane.setVisible(b);
            }
        });
    }
}
