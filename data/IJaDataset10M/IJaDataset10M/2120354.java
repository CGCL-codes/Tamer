package dot.chimera;

import java.util.LinkedList;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JToolBar;
import oscript.data.Value;
import dot.exceptions.ProgrammingErrorException;

/**
 * The abstract base class for all plugins.  This class provides an easy to
 * use framework for adding modules to <i>chimera</i>, and a mechanism to
 * manage resources created by this plugin.
 * <p>
 * The plugin class itself should be a lightweight container for all the
 * resources created and/or provided by the plugin.  The plugin class will
 * be instantiated at startup regardless of whether the plugin is actually
 * started.  As much as possible, all resources created by a plugin should
 * not be created until the the plugin is started.  This can be accomplish
 * by using managed resources.
 * <p>
 * The {@link Resource} mechanism is used for managing resources created by
 * this plugin.  A plugin is active (ie. running) whenever it has installed
 * resources, and it is in-active (ie. stopped) whenever all it's resources
 * have been uninstalled.  There are two types of resources that can be 
 * created (see {@link #addResource}, <i>managed</i>, and <i>unmanaged</i>.  
 * A managed  resources is automatically {@link Resource#install}ed when the 
 * plugin is started, and {@link Resource#uninstall}ed when the plugin is 
 * stopped.  An unmanaged resource is {@link Resource#install}ed when it
 * is added ({@link #addResource}), and {@link Resource#uninstall}ed when
 * it is removed ({@link #removeResource}).  Adding an unmanaged resource
 * will cause the plugin to become active, if it wasn't already active, and
 * removing an unmanaged resource could cause the plugin to become inactive
 * if there are no more unmanaged resources.
 * 
 * @author Rob Clark
 * @version 0.1
 */
public abstract class Plugin {

    /**
   * The main application.
   */
    protected Main main;

    /**
   * The name of this plugin instance.
   */
    private String name;

    /**
   * The list of unmanaged resources.
   */
    private LinkedList unmanagedResourceList = new LinkedList();

    /**
   * The list of managed resources.
   */
    private LinkedList managedResourceList = new LinkedList();

    /**
   * List of services provided by plugin.
   */
    private LinkedList serviceFactoryList = new LinkedList();

    /**
   * Class Constructor.
   * 
   * @param main         the main application
   * @param name         the name of this plugin instance
   */
    public Plugin(Main main, String name) {
        this.main = main;
        this.name = name;
        addResource(new Resource(true) {

            public void install() {
                startHook();
            }

            public void uninstall() {
                stopHook();
            }
        });
        addResource(new Resource(true) {

            private boolean installed = false;

            public void install() {
                if (installed) throw new ProgrammingErrorException("already installed");
                Plugin.this.main.debug(1, "starting plugin: \"" + getName() + "\"");
                installed = true;
            }

            public void uninstall() {
                if (!installed) throw new ProgrammingErrorException("not installed");
                Plugin.this.main.debug(1, "stopping plugin: \"" + getName() + "\"");
                installed = false;
            }
        });
    }

    /**
   * Get the name of this plugin instance.
   * 
   * @return the name
   */
    public String getName() {
        return name;
    }

    /**
   * Because the garbage collection of a service instance (among other 
   * things) is used to determine if a {@link Plugin} should be stopped, the
   * {@link Registry} cannot hold a reference to the service itself. Instead 
   * the service is registered ({@link Plugin#addServiceFactory}) via a 
   * <code>ServiceFactory</code> which is used by the <code>Registry</code> 
   * to create an instance of the {@link Service} on demand.  The service-
   * factory tracks (via a weak-reference) the lifecycle of the service
   * instance, and uses a {@link Resource} to prevent the plugin from being
   * inactive as long as the service instance is in use.
   * <p>
   * Conceptually this class is an inner-class of <code>Plugin</code>, but
   * in order to keep the file sizes managed, all of it's functionality is
   * actually delegated to {@link ServiceFactoryImpl}.  This is all subject
   * to change, and once the deprecated methods are removed from Plugin,
   * it is possible that the implementation gets moved into this class and
   * ServiceFactoryImpl goes away.
   */
    public abstract class ServiceFactory extends ServiceFactoryImpl {

        protected ServiceFactory() {
            super(Plugin.this);
        }
    }

    /**
   * Register a service provided by this plugin.  The serviced provided by
   * the plugin should be registered by the plugin's constructor, rather
   * than by the {@link #start} method, so that the registry can determine
   * which services are provided by which plugin.
   * <p>
   * A service is registered via a {@link ServiceFactory}, which should be
   * the one to actually create an instance of the service.  This is done
   * this way because the registry tracks the instances of a service, and
   * adds/removes managed resources to when a service instance is created/
   * GC'd.  If the registry, or any other code, where to hold a reference
   * to the service itself, rather than the service-factory, the service
   * would never get garbage collected.
   * <p>
   * The only reason this method is <code>public</code> is for the benefit
   * of plugins implemented as script.  The way the ObjectScript interpreter
   * works, script code only has access to public methods of a class.  This
   * method should be treated as if it were <code>protected</code>.
   * 
   * @param sh           a factory to a service provided by this plugin
   */
    public final synchronized void registerServiceFactory(ServiceFactory sh) {
        serviceFactoryList.add(sh);
    }

    /**
   * This is provided to make life easy for script code... script code can
   * simply pass in a function that returns a service, rather than having
   * to pass in an instance of {@link ServiceFactory}.
   * 
   * @param fxn          a script function that takes no args and returns
   *    a instance of a service
   */
    public final void registerServiceFactory(final Value fxn) {
        registerServiceFactory(new ServiceFactory() {

            protected Service createService() {
                return (Service) (fxn.callAsFunction(new Value[0]).castToJavaObject());
            }
        });
    }

    /**
   * @deprecated use <code>registerServiceFactory</code> instead.
   */
    public final void registerService(final Service s) {
        main.warning(getName(), "registerService deprecated");
        registerServiceFactory(new ServiceFactory() {

            protected Service createService() {
                return s;
            }
        });
    }

    /**
   * Used by registry to determine services provided by plugin.
   */
    Iterator getServiceFactories() {
        return serviceFactoryList.iterator();
    }

    /**
   * A view-resource adds a view, when installed, and removes the view
   * when uninstalled.
   */
    public class ViewResource extends Resource {

        private ViewFactory vh;

        private View view = null;

        private Runnable closeRunnable = new Runnable() {

            public void run() {
                removeResource(ViewResource.this);
            }
        };

        /**
     * Create a <code>ViewResource</code>
     * 
     * @param vh           the view-factory
     * @param managed      is this a managed view
     */
        public ViewResource(ViewFactory vh, boolean managed) {
            super(managed);
            this.vh = vh;
        }

        /**
     * Create a <code>ViewResource</code>.  This constructor is provided as 
     * a convenience for script code, so script code can simply pass in a 
     * function that returns a {@link View}, rather than having to pass in
     * an instance of {@link ViewFactory}.
     * 
     * @param fxn          the script function that takes no args and 
     *    returns a view instance
     * @param managed      is this a managed view
     */
        public ViewResource(final Value fxn, boolean managed) {
            this(new ViewFactory() {

                public View createView() {
                    return (View) (fxn.callAsFunction(new Value[0]).castToJavaObject());
                }
            }, managed);
        }

        /**
     * Create a <code>ViewResource</code> for a view.  Since a view is
     * potentially not lightweight, this constructor only allows you
     * to create an unmanaged resource.  To create a managed resouce,
     * use the constructor that takes a {@link ViewFactory} (or a script
     * function that returns a view).
     * 
     * @param view         the view
     */
        public ViewResource(final View view) {
            this(new ViewFactory() {

                public View createView() {
                    return view;
                }
            }, false);
        }

        /**
     * install the view
     */
        public void install() {
            if (view != null) throw new ProgrammingErrorException("ViewResource already install()ed!");
            view = vh.createView();
            view.setPlugin(Plugin.this);
            view.addCloseRunnable(closeRunnable);
            main.getWindowManager().getDock(Dock.getDockName(view)).addView(view);
        }

        /**
     * uninstall the view that was installed
     */
        public void uninstall() {
            if (view == null) throw new ProgrammingErrorException("ViewResource already uninstall()ed!");
            view.getDock().removeView(view);
            view.setPlugin(null);
            view.removeCloseRunnable(closeRunnable);
            view = null;
        }

        public String toString() {
            return "view";
        }
    }

    /**
   * In order to create a managed ViewResource, we need to create the
   * resource without creating the view itself.  To accomplish this,
   * we use a ViewFactory, whose {@link #createView} method is not
   * called until the resource is installed.
   */
    public interface ViewFactory {

        public View createView();
    }

    /**
   * @deprecated use addResource( new ViewResource(view) )
   */
    public void addView(View view) {
        main.warning(getName(), "addView deprecated");
        addResource(new ViewResource(view));
    }

    /**
   * @deprecated use removeResource( new ViewResource(view) )
   */
    public synchronized void removeView(View view) {
        main.warning(getName(), "removeView deprecated");
        removeResource(new ViewResource(view));
    }

    /**
   * A tool-bar-resource adds a tool-bar when installed, and removes when
   * uninstalled.
   */
    public class ToolBarResource extends Resource {

        private ToolBarFactory tbf;

        private JToolBar tb;

        /**
     * Class Constructor, which can be used to defer creating
     * the toolbar until this resource is installed.
     * 
     * @param tbf          the tool-bar-factory
     * @param managed      is this a managed resource
     */
        public ToolBarResource(ToolBarFactory tbf, boolean managed) {
            super(managed);
            this.tbf = tbf;
        }

        /**
     * Class Constructor, which can be used to defer creating
     * the toolbar until this resource is installed.
     * 
     * @param fxn          the script function that takes no args and
     *    returns a tool-bar instance
     * @param managed      is this a managed resource
     */
        public ToolBarResource(final Value fxn, boolean managed) {
            this(new ToolBarFactory() {

                public JToolBar createToolBar() {
                    return (JToolBar) (fxn.callAsFunction(new Value[0]).castToJavaObject());
                }
            }, managed);
        }

        /**
     * Class Constructor.
     * 
     * @param tb           the tool-bar
     * @param managed      is this a managed view
     */
        public ToolBarResource(final JToolBar tb, boolean managed) {
            this(new ToolBarFactory() {

                public JToolBar createToolBar() {
                    return tb;
                }
            }, managed);
        }

        public void install() {
            if (tb != null) throw new ProgrammingErrorException("ToolBarResource already install()ed!");
            tb = tbf.createToolBar();
            main.getWindowManager().addToolBar(tb);
        }

        public void uninstall() {
            if (tb == null) throw new ProgrammingErrorException("ToolBarResource already uninstall()ed!");
            main.getWindowManager().removeToolBar(tb);
            tb = null;
        }

        public String toString() {
            return "tool-bar";
        }
    }

    /**
   * In order to create the ToolBarResource without creating the
   * JToolBar itself, a ToolBarResource is created with a reference
   * to a ToolBarFactory which is called on demand to create the
   * tool-bar.
   */
    public interface ToolBarFactory {

        public JToolBar createToolBar();
    }

    /**
   * @deprecated use addResource( new ToolBarResource( toolBar, true ) )
   */
    public synchronized void addToolBar(JToolBar toolBar) {
        main.warning(getName(), "addToolBar deprecated");
        addResource(new ToolBarResource(toolBar, true));
    }

    /**
   * @deprecated use removeResource( new ToolBarResource( toolBar, true ) )
   */
    public synchronized void removeToolBar(JToolBar toolBar) {
        main.warning(getName(), "removeToolBar deprecated");
        removeResource(new ToolBarResource(toolBar, true));
    }

    /**
   * A MenuBarItemResource adds to / removes from the system wide menubar.
   */
    public class MenuBarItemResource extends Resource {

        private String path;

        private Action a;

        /**
     * Class Constructor.
     * 
     * @param path       which sub-menu the item should go under
     * @param a          the menu bar action to add.
     */
        public MenuBarItemResource(String path, Action a, boolean managed) {
            super(managed);
            this.path = path;
            this.a = a;
        }

        public void install() {
            main.getWindowManager().addMenuBarItem(path, a);
        }

        public void uninstall() {
            main.getWindowManager().removeMenuBarItem(path, a);
        }

        public int hashCode() {
            return path.hashCode() ^ a.hashCode();
        }

        public boolean equals(Object obj) {
            return ((obj instanceof MenuBarItemResource) && path.equals(((MenuBarItemResource) obj).path) && a.equals(((MenuBarItemResource) obj).a));
        }

        public String toString() {
            return "[menu-bar-item: " + path + "]";
        }
    }

    /**
   * @deprecated use addResource( new MenuBarItemResource( path, a, true ) )
   */
    public synchronized void addMenuBarItem(String path, Action a) {
        main.warning(getName(), "addMenuBarItem deprecated");
        addResource(new MenuBarItemResource(path, a, true));
    }

    /**
   * @deprecated use removeResource( new MenuBarItemResource( path, a, true ) )
   */
    public synchronized void removeMenuBarItem(String path, Action a) {
        main.warning(getName(), "removeMenuBarItem deprecated");
        removeResource(new MenuBarItemResource(path, a, true));
    }

    /**
   * Add a resource.  If the resource is unmanaged, this could cause the
   * plugin to become active if it is not already.  This does nothing if
   * the resource has already been added.
   * 
   * @param r            the resource to add
   * @see #removeResource
   */
    public synchronized void addResource(Resource r) {
        boolean wasActive = isActive();
        LinkedList l;
        if (r.isManaged()) l = managedResourceList; else l = unmanagedResourceList;
        if (!l.contains(r)) {
            main.debug(1, getName() + ": addResource: r=" + r + ", managed=" + r.isManaged());
            l.add(r);
            if (!wasActive && isActive()) {
                main.debug(1, getName() + ": installing resources");
                Object[] rcrs = managedResourceList.toArray();
                for (int i = 0; i < rcrs.length; i++) ((Resource) (rcrs[i])).install();
            }
            if (!r.isManaged() || isActive()) r.install();
        }
    }

    /**
   * Remove a resource.  If the resource is unmanged, and there no more
   * unmanaged resources, this will cause the plugin to become inactive.
   * This does nothing if the resource has not already been added.
   * 
   * @param r            the resource to add
   * @see #addResource
   */
    public synchronized void removeResource(Resource r) {
        boolean wasActive = isActive();
        LinkedList l;
        if (r.isManaged()) l = managedResourceList; else l = unmanagedResourceList;
        if (l.remove(r)) {
            main.debug(1, getName() + ": removeResource: r=" + r + ", managed=" + r.isManaged());
            r.uninstall();
            if (wasActive && !isActive()) {
                main.debug(1, getName() + ": uninstalling resources");
                Object[] rcrs = managedResourceList.toArray();
                for (int i = 0; i < rcrs.length; i++) ((Resource) (rcrs[i])).uninstall();
            }
        }
    }

    /**
   * @deprecated use a managed Resource
   */
    public void startHook() {
    }

    /**
   * @deprecated use a managed Resource
   */
    public void stopHook() {
    }

    /**
   * Determine if this plugin is currently active.  An active plugin is one
   * for which the {@link #start} method has been called since the last time
   * {@link #stop} has been called.
   * 
   * @see #start
   * @see #stop
   */
    public final boolean isActive() {
        return unmanagedResourceList.size() > 0;
    }
}
