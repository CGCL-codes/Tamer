package fr.inria.zvtm.master.gui;

import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import fr.inria.zvtm.common.gui.MainEventHandler;
import fr.inria.zvtm.common.gui.NavigationManager;
import fr.inria.zvtm.common.gui.PCursor;
import fr.inria.zvtm.common.gui.Viewer;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.VImage;
import fr.inria.zvtm.master.MasterMain;

/**
 * The effective graphic part of the compositor.
 * @see Viewer
 * @author Julien Altieri
 *
 */
public class MasterViewer extends Viewer {

    private CursorMultiplexer cursorMultiplexer;

    private Bouncer bouncer;

    public MasterViewer() {
        init();
        bouncer = new Bouncer();
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        getView().getFrame().setLocation((int) (e.getCenterPoint().x - getView().getFrame().getWidth() * 1. / 2), (int) (e.getCenterPoint().y - getView().getFrame().getHeight() * 1. / 2));
        getMainEventListener().setViewer(this);
        cursorMultiplexer = new CursorMultiplexer(this);
        ged = new GEDMultiplexer();
        this.listenMultiplexer.addListerner(ged);
    }

    @Override
    public MainEventHandler makeEventHandler() {
        return new MasterMainEventHandler(null);
    }

    @Override
    public void initNavigation() {
        nm = new NavigationManager(this);
        nm.setCamera(mCamera);
        getNavigationManager().getCamera().setAltitude(-80);
    }

    @Override
    protected void addBackground() {
        ImageIcon img = (new ImageIcon("src/main/java/fr/inria/zvtm/resources/bg.jpg"));
        Glyph g = new VImage(img.getImage());
        mSpace.addGlyph(g);
    }

    /**
	 * 
	 * @return The related {@link CursorMultiplexer}
	 */
    public CursorMultiplexer getCursorMultiplexer() {
        return cursorMultiplexer;
    }

    /**
	 * 
	 * @return The {@link VirtualSpace} in which the cursors are moving
	 */
    public VirtualSpace getCursorSpace() {
        return cursorSpace;
    }

    /**
	 * @return The related {@link Bouncer}.
	 */
    public Bouncer getBouncer() {
        return bouncer;
    }

    /**
	 * Updates and broadcasts the wall's zvtm bounds.
	 */
    public void sendViewUpgrade() {
        if (MasterMain.SMALLMODE) {
            PCursor.wallBounds = getView().getVisibleRegion(mCamera);
        } else {
            double a = (mCamera.focal + mCamera.altitude) / mCamera.focal;
            PCursor.wallBounds[0] = -a * (4 * 2760 - 100) + cursorCamera.vx;
            PCursor.wallBounds[2] = a * (4 * 2760 - 100) + cursorCamera.vx;
            PCursor.wallBounds[1] = a * (2 * 1840 - 120) + cursorCamera.vy;
            PCursor.wallBounds[3] = -a * (2 * 1840 - 120) + cursorCamera.vy;
        }
        bouncer.sendViewUpgrade(PCursor.wallBounds);
    }
}
