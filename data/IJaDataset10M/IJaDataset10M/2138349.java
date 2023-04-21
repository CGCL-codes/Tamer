package eu.livotov.tpt.gui.widgets;

import com.vaadin.ui.Component;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import java.util.*;

/**
 * Provides lightweight multi-view component, that can hold an unlimited number of actual UI
 * components, each associated with it's unique name and only one component can be displayed at a
 * time, providing also fast switching between views.
 * <p/>
 * <p>TPTMultiView also allow to use view names, combined with the view parameters, separated by a
 * '/' (slash) chatacter. In combination with view component that implements TPTView interface this
 * will cause the parameters to be passed to a view that is switched on. This feature spicifically
 * done for work with the combination of URIFragmentUtility, allowing not only to control your views
 * from an URL programmatically or by a end user but also pass custom parameters from an URL to a
 * view each time it is become active.
 */
public class TPTMultiView extends VerticalLayout implements UriFragmentUtility.FragmentChangedListener {

    /**
     * Internal map, where we'll store all registered views
     */
    private Map<String, Component> views = new HashMap<String, Component>(10);

    /**
     * Map to store lazy-loading views. If actual view instance is not present in the "views" map,
     * TPTView will try to find and instantiate it on activation from delayedViews map.
     */
    private Map<String, Class<? extends Component>> delayedViews = new HashMap<String, Class<? extends Component>>(10);

    /**
     * Name of currently displayed view
     */
    private String currentView;

    /**
     * URI manager, that tracks browser address bar changes and automatically switches the view, if
     * enabled.
     */
    private UriFragmentUtility uriManager = new UriFragmentUtility();

    /**
     * URI manager activation flag. When set to <code>false</code>, uri changes will not cause the
     * current view to switch.
     */
    private boolean uriManagerEnabled = false;

    /**
     * Last fragment, which was set by an URI Fragment Utility. Used to track and eliminate duplicate view activation events
     */
    private String lastChangedFragment = "";

    /**
     * Contains a failsafe view, to which view manager should switch in case actual view name will be
     * invalid or view will not exists. If failsafe view is null - an IllegalArgumentException will be thrown on
     * attempt to switch to non existing view. When a failsafe view is activated, the non-existed view name is passed there
     * as a view parameter. Original parameter (if were present) will be passed slash-separated after the view name: nonexview/param
     */
    private String failsafeView;

    /**
     * Creates a multiview with uri manager disabled. View is based on VerticalLayout, sets it's
     * witdh and height to 100% and does not contain any margins and borders.
     */
    public TPTMultiView() {
        super();
        setSizeFull();
        setMargin(false);
        setSpacing(false);
    }

    /**
     * Creates a multiview, optionaly activating or deactivating the uri manager. View is based on
     * VerticalLayout, sets it's witdh and height to 100% and does not contain any margins and
     * borders.
     *
     * @param uriManagerEnabled enables or disabled the URI manager. Enabling the URI manager will
     *                          cause this component to automatically switch the current view if URI
     *                          text in the browser is changed.
     */
    public TPTMultiView(boolean uriManagerEnabled) {
        this();
        this.uriManagerEnabled = uriManagerEnabled;
        if (this.uriManagerEnabled) {
            uriManager.setWidth("0px");
            uriManager.setHeight("0px");
            uriManager.addListener(this);
            addComponent(uriManager);
        }
    }

    /**
     * Sets the failsafe view name. When you try to switch to a non-existing view and failsafe view name
     * is defined, view manager will switch to it (keeping your view parameters as well). If no filesafe
     * view name is set - an IllegalArgumentException is will be thrown in attempt to go to non existing view.
     * If failsafe view name will also not exists, then IllegalArgumentException will be thrown.
     *
     * @param name view name to switch to in case actual view is not found.
     */
    public void setFailsafeViewName(String name) {
        failsafeView = name;
    }

    /**
     * Provides the current failsafe view name (null by default) set by a setFailsafeViewName
     *
     * @return current failsafe view name
     */
    public String getFailsafeViewName() {
        return failsafeView;
    }

    /**
     * Registers a new view. If this is a first view, it also becomes an active, e.g. it will be
     * displayed. If this is not a first view - no active view will be changed, you'll need to call
     * switchView(...) to show this view.
     * <p/>
     * <p>If you'll try to add a view with duplicate name, an IllegalArgumentException will be
     * thrown.
     *
     * @param viewName unique name of this view
     * @param view     actual component of a view
     * @return self instance in order to simplify adding multiple views at one line.
     */
    @SuppressWarnings("unchecked")
    public TPTMultiView addView(String viewName, Component view) {
        if (!isViewAvailable(viewName)) {
            views.put(viewName, view);
            delayedViews.put(viewName, (Class<? extends Component>) view.getClass());
            fireViewAttachedMessage(view);
            if (views.size() == 1) {
                switchView(viewName);
            }
            return this;
        }
        throw new IllegalArgumentException(String.format("View %s already exists. Use updateView() if you want to replace it with the new component.", viewName));
    }

    /**
     * Adds a lazy loading view. When this view will be called for activation for the first time,
     * TPTView will instantiate the given class by calling default constructor. Please note, that in
     * event you add a lazy view, you'll not receive 'viewAttached' event until view is activated.
     * Once activated, you'll receive viewAttached event first and then immideately receive a
     * viewActivated event as a second one.
     *
     * @param viewName  view name
     * @param viewClass class, that represnets the view. Class must have a default constructor.
     */
    public void addView(String viewName, Class<? extends Component> viewClass) {
        if (!isViewAvailable(viewName)) {
            views.put(viewName, null);
            delayedViews.put(viewName, viewClass);
        } else {
            throw new IllegalArgumentException(String.format("View %s already exists. Use updateView() if you want to replace it with the new component.", viewName));
        }
    }

    /**
     * Replaces an old view with the new one. If you replaces an active view, it will be updated on
     * the display as well. <p>If you'll try to replace a view with that is not yet exists, an
     * IllegalArgumentException will be thrown.
     *
     * @param viewName view name to replace
     * @param view     new view component
     * @return self instance to simplify replacing multiple views at one line
     */
    @SuppressWarnings("unchecked")
    public TPTMultiView replaceView(String viewName, Component view) {
        if (isViewAvailable(viewName)) {
            fireViewRemovedMessage(views.get(currentView));
            if (currentView != null && currentView.equalsIgnoreCase(viewName)) {
                switchView(null);
                views.put(viewName, view);
                delayedViews.put(viewName, (Class<? extends Component>) view.getClass());
                fireViewAttachedMessage(view);
                switchView(viewName);
            } else {
                views.put(viewName, view);
                delayedViews.put(viewName, (Class<? extends Component>) view.getClass());
                fireViewAttachedMessage(view);
            }
            return this;
        }
        throw new IllegalArgumentException(String.format("View %s does not exists. If you want to add a new view, use method addView()", viewName));
    }

    /**
     * Removes view. If  you remove an active view, the display become blank and no active view will
     * exists anymore, so you'll have to call switchView to display a new one. <p>If you'll try to
     * remove a view that is not exists, an IllegalArgumentException will be thrown.
     *
     * @param viewName view name to remove.
     * @return self instance in order to simplify multiple removes at one line.
     */
    public TPTMultiView removeView(String viewName) {
        if (isViewAvailable(viewName)) {
            fireViewRemovedMessage(views.get(viewName));
            if (currentView != null && currentView.equalsIgnoreCase(viewName)) {
                switchView(null);
            }
            views.remove(viewName);
            delayedViews.remove(viewName);
            return this;
        }
        throw new IllegalArgumentException(String.format("View %s does not exists and thus cannot be removed.", viewName));
    }

    /**
     * Switches the active view to a new one, causing display to update. You may also switch to a
     * null view in order to clear the display. <p>If you'll try to switch to a view that does not
     * exists, an IllegalArgumentException will be thrown in casd URI manager is not enabled or
     * noting will happen otherwise.
     *
     * @param viewId new active view name or name, combined with the view parameters, separated by a
     *               slash character, for instance: <b>myview</b> equals <b>myview/12345</b> but in
     *               second case, string 12345 will be passed to a view as viewActivated method
     *               parameter, in case the view object being swithced to implements TPTView
     *               interface. If not - custom parameter will be simply ignored.
     */
    public void switchView(String viewId) {
        final String viewName = (!views.containsKey(getPureViewName(viewId)) && failsafeView != null) ? failsafeView : getPureViewName(viewId);
        final String viewParameters = getViewParameters(viewId);
        if (currentView != null) {
            fireViewDeactivatedMessage(views.get(currentView), viewId);
        }
        if (viewId == null) {
            removeAllComponents();
            currentView = null;
        } else if (views.containsKey(viewName)) {
            if (views.get(viewName) == null) {
                try {
                    views.put(viewName, delayedViews.get(viewName).newInstance());
                    fireViewAttachedMessage(views.get(viewName));
                } catch (Throwable e) {
                    throw new RuntimeException("Cannot activate lazy view: " + e.getMessage(), e);
                }
            }
            if (!viewName.equals(currentView)) {
                removeAllComponents();
                addComponent(views.get(viewName));
                setExpandRatio(views.get(viewName), 1.0f);
            }
            fireViewActivatedMessage(views.get(viewName), currentView, viewParameters);
            currentView = viewName;
        } else {
            if (!uriManagerEnabled) {
                throw new IllegalArgumentException(String.format("View %s does not exists.", viewName));
            }
        }
        if (uriManagerEnabled) {
            lastChangedFragment = viewId;
            uriManager.setFragment(viewId);
        }
    }

    /**
     * Checks if the specified view exists in the component
     *
     * @param viewName view name to check
     * @return TRUE, if the specified view name exists
     */
    public boolean isViewAvailable(String viewName) {
        return views.containsKey(getPureViewName(viewName));
    }

    /**
     * Checks, if the specified view available and active
     *
     * @param viewName view nameto chexk
     * @return TRUE, if the specified view name exists and a view, associated with it is active,
     *         e.g. displayed ont he screen.
     */
    public boolean isViewActive(String viewName) {
        return isViewAvailable(viewName) && currentView != null && currentView.equalsIgnoreCase(viewName);
    }

    /**
     * Get the actual view component by the view name
     *
     * @param viewName view name.
     * @return view component, associated with the specified name. If non-existing name is given, an
     *         IllegalArgumentException will be thrown.
     */
    public Component getView(String viewName) {
        if (isViewAvailable(viewName)) {
            return views.get(viewName);
        }
        throw new IllegalArgumentException(String.format("View %s does not exists.", viewName));
    }

    /**
     * Provides name of the currently active view
     *
     * @return name of the view being active
     */
    public String getCurrentViewName() {
        return currentView;
    }

    /**
     * Provides the actual component of the current view
     *
     * @return component of the current view
     */
    public Component getCurrentView() {
        return views.get(currentView);
    }

    /**
     * Removes all components but uri manager. This is done in order to protect uri manager from
     * accidental removals from a multiview as the removal will breal uri change detection. If
     * you'll need to remove the uri manager for some reason, please use explicit removal.
     */
    @Override
    public void removeAllComponents() {
        Iterator<Component> components = getComponentIterator();
        List<Component> componentsToRemove = new ArrayList<Component>(2);
        while (components.hasNext()) {
            final Component component = components.next();
            if (component != uriManager) {
                componentsToRemove.add(component);
            }
        }
        for (Component component : componentsToRemove) {
            removeComponent(component);
        }
        componentsToRemove.clear();
    }

    /**
     * Resolves view name by the actual view component instance.
     *
     * @param view view component instance to resolve name of
     * @return view name of the given component or null, if no such component found in the list of
     *         registered views.
     */
    public String getViewName(Component view) {
        Collection<String> currentViews = Collections.unmodifiableCollection(views.keySet());
        for (String key : currentViews) {
            if (views.get(key).equals(view)) {
                return key;
            }
        }
        return null;
    }

    public void fragmentChanged(UriFragmentUtility.FragmentChangedEvent fragmentChangedEvent) {
        if (uriManagerEnabled && fragmentChangedEvent.getUriFragmentUtility().getFragment() != null) {
            final String fragment = fragmentChangedEvent.getUriFragmentUtility().getFragment();
            if (!fragment.equals(lastChangedFragment)) {
                lastChangedFragment = fragment;
                switchView(fragment);
            }
        }
    }

    /**
     * An optional interface, your component representing a single view may implement. If interface
     * is implemented, your view will receive its lifecycle events, see interface methods below. You
     * do not need to register your view as a listener, just implement the interface and this is
     * enough.
     */
    public interface TPTView {

        /**
         * Called when view is activated, e.g. becomes visible to the user.
         *
         * @param parameters     optional view parameters, that may come from who called the method
         *                       switchView
         * @param previousViewId ID of the previous view. Useful for building navigation or
         *                       historical browsing
         */
        public void viewActivated(String previousViewId, String parameters);

        /**
         * Called when view is deactivated, e.g. becomes hidden from a user. This usually happens if
         * another view was swithced on.
         *
         * @param newViewId ID of the view that came at top
         */
        public void viewDeactivated(String newViewId);

        /**
         * Called when a view is attached to a multiview component, e.g. when new view was added by
         * invoking addView method.
         */
        public void viewAttached();

        /**
         * Called when a view is removed from a multiview component, e.g. when view is removed by
         * invoking a removeView method
         */
        public void viewRemoved();
    }

    /**
     * Checks if view component implements the TPTView interface and if so, invokes the appropriate
     * method to notify a view on event happened
     *
     * @param view       view being activated (showing)
     * @param parameters parameters, passed to a view (if any)
     */
    protected void fireViewActivatedMessage(Component view, String previousViewId, String parameters) {
        if (view instanceof TPTView) {
            ((TPTView) view).viewActivated(previousViewId, parameters);
        }
    }

    /**
     * Checks if view component implements the TPTView interface and if so, invokes the appropriate
     * method to notify a view on event happened
     *
     * @param view view being deactivated (hidden)
     */
    protected void fireViewDeactivatedMessage(Component view, String newViewId) {
        if (view instanceof TPTView) {
            ((TPTView) view).viewDeactivated(newViewId);
        }
    }

    /**
     * Checks if view component implements the TPTView interface and if so, invokes the appropriate
     * method to notify a view on event happened
     *
     * @param view new view being added to a multiview component (added)
     */
    protected void fireViewAttachedMessage(Component view) {
        if (view instanceof TPTView) {
            ((TPTView) view).viewAttached();
        }
    }

    /**
     * Checks if view component implements the TPTView interface and if so, invokes the appropriate
     * method to notify a view on event happened
     *
     * @param view view being removed from a multiview component (removed)
     */
    protected void fireViewRemovedMessage(Component view) {
        if (view instanceof TPTView) {
            ((TPTView) view).viewRemoved();
        }
    }

    /**
     * Extracts the pure name from the view name, combined with the parameters. Parameters are
     * separated from a view name by a slash character.
     *
     * @param viewName view  name or view name,combined with the view parameters
     * @return always the pure view name without parameters
     */
    protected String getPureViewName(String viewName) {
        if (viewName == null) {
            return "";
        }
        if (!viewName.contains("/")) {
            return viewName;
        } else {
            return viewName.substring(0, viewName.indexOf("/"));
        }
    }

    /**
     * Extracts the parameters from a view name if it was combined with such parameters.
     *
     * @param viewName view name or view name, combined with the view parameters
     * @return parameters , extracted from the view name if it was combined with the parameters. If
     *         no parameters present, an empty string will be returned.
     */
    protected String getViewParameters(String viewName) {
        if (viewName == null || !viewName.contains("/")) {
            return "";
        } else {
            return viewName.substring(viewName.indexOf("/") + 1, viewName.length());
        }
    }
}
