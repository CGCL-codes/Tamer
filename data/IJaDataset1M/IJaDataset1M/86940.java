package org.glossitope.container.security;

import java.awt.Container;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glossitope.desklet.DeskletContainer;
import org.glossitope.container.wm.WindowManager;

/**
 *
 * @author cooper
 */
public class ContainerFactory {

    private static final Logger LOG = Logger.getLogger("org.glossitope");

    public static final String LOCATION_X = "org.glossitope.location.x";

    public static final String LOCATION_Y = "org.glossitope.location.y";

    private static final ContainerFactory instance = new ContainerFactory();

    private Container dock;

    private WindowManager wm;

    /** Creates a new instance of ContainerFactory */
    private ContainerFactory() {
        super();
    }

    public void cleanup(DefaultContext context) {
        if (context.hasDock()) {
            dock.remove(((DockContainer) context.getDockingContainer()).panel);
            dock.validate();
        }
        if (context.hasContainer()) {
            Point2D location = context.getContainer().getLocation();
            context.setPreference(ContainerFactory.LOCATION_X, Double.toString(location.getX()));
            context.setPreference(ContainerFactory.LOCATION_Y, Double.toString(location.getY()));
            wm.destroyContainer(context.getContainer());
        }
    }

    public DockContainer createDockContainer(DefaultContext context) {
        final DockContainer dock = new DockContainer();
        this.dock.add(dock.panel);
        DockSkinner.configureDockConatiner(dock);
        return dock;
    }

    public DeskletContainer createInternalFrameContainer(DefaultContext context) {
        DeskletContainer ifc = wm.createInternalContainer(context);
        try {
            Point point = new Point((int) Double.parseDouble(context.getPreference(ContainerFactory.LOCATION_X, "50")), (int) Double.parseDouble(context.getPreference(ContainerFactory.LOCATION_Y, "50")));
            System.out.println("Setting location: " + point.getX() + " " + point.getY());
            ifc.setLocation(point);
        } catch (NumberFormatException nfe) {
            LOG.log(Level.WARNING, "Excpetion reading screen position information.", nfe);
            ifc.setLocation(new Point(50, 50));
        }
        wm.showContainer(ifc);
        return ifc;
    }

    DeskletContainer createConfigContainer(DefaultContext defaultContext) {
        DeskletContainer dc = wm.createDialog(defaultContext.getContainer());
        return dc;
    }

    public static ContainerFactory getInstance() {
        return instance;
    }

    public void init(WindowManager wm, Container dock) {
        this.wm = wm;
        this.dock = dock;
    }
}
