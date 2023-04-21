package com.hack23.cia.web.views.common;

import java.util.HashMap;
import java.util.Iterator;
import com.hack23.cia.web.viewfactory.api.common.ModelAndView;
import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;

/**
 * The Class Navigator.
 */
@SuppressWarnings("serial")
public class Navigator extends CustomComponent {

    /**
	 * The Interface NavigableApplication.
	 */
    public interface NavigableApplication {

        /**
		 * Creates the new window.
		 * 
		 * @return the window
		 */
        Window createNewWindow();
    }

    /**
	 * The Interface View.
	 */
    public interface View extends Component {

        /**
		 * Gets the warning for navigating from.
		 * 
		 * @return the warning for navigating from
		 */
        String getWarningForNavigatingFrom();

        /**
		 * Inits the.
		 * 
		 * @param navigator
		 *            the navigator
		 * @param application
		 *            the application
		 */
        void init(Navigator navigator, Application application);

        /**
		 * Navigate to.
		 * 
		 * @param requestedDataId
		 *            the requested data id
		 */
        void navigateTo(String requestedDataId);

        /**
		 * Process.
		 * 
		 * @param modelAndView
		 *            the model and view
		 */
        void process(ModelAndView modelAndView);
    }

    /**
	 * Gets the window.
	 * 
	 * @param application
	 *            the application
	 * @param name
	 *            the name
	 * @param superGetWindow
	 *            the super get window
	 * 
	 * @return the window
	 */
    public static Window getWindow(final NavigableApplication application, final String name, final Window superGetWindow) {
        if (superGetWindow != null) {
            return superGetWindow;
        }
        Window w = application.createNewWindow();
        w.setName(name);
        ((Application) application).addWindow(w);
        w.open(new ExternalResource(w.getURL()));
        return w;
    }

    /** The class to uri. */
    private HashMap<Class<?>, String> classToUri = new HashMap<Class<?>, String>();

    /** The class to view. */
    private HashMap<Class<?>, View> classToView = new HashMap<Class<?>, View>();

    /** The current fragment. */
    private String currentFragment = "";

    /** The current view. */
    private View currentView = null;

    /** The layout. */
    private VerticalLayout layout = new VerticalLayout();

    /** The main view uri. */
    private String mainViewUri = null;

    /** The uri fragment util. */
    private UriFragmentUtility uriFragmentUtil = new UriFragmentUtility();

    /** The uri to class. */
    private HashMap<String, Class<?>> uriToClass = new HashMap<String, Class<?>>();

    /**
	 * Instantiates a new navigator.
	 */
    public Navigator() {
        layout.setSizeFull();
        setSizeFull();
        layout.addComponent(uriFragmentUtil);
        setCompositionRoot(layout);
        uriFragmentUtil.addListener(new FragmentChangedListener() {

            public void fragmentChanged(FragmentChangedEvent source) {
                Navigator.this.fragmentChanged();
            }
        });
    }

    /**
	 * Adds the view.
	 * 
	 * @param uri
	 *            the uri
	 * @param viewClass
	 *            the view class
	 */
    public final void addView(final String uri, final Class<?> viewClass) {
        if (!View.class.isAssignableFrom(viewClass)) {
            throw new IllegalArgumentException("viewClass must implemenent Navigator.View");
        }
        if (uri == null || viewClass == null || uri.length() == 0) {
            throw new IllegalArgumentException("viewClass and uri must be non-null and not empty");
        }
        if (uriToClass.containsKey(uri)) {
            if (uriToClass.get(uri) == viewClass) {
                return;
            }
            throw new IllegalArgumentException(uriToClass.get(uri).getName() + " is already mapped to '" + uri + "'");
        }
        if (classToUri.containsKey(viewClass)) {
            throw new IllegalArgumentException("Each view class can only be added to Navigator with one uri");
        }
        if (uri.indexOf('/') >= 0 || uri.indexOf('#') >= 0) {
            throw new IllegalArgumentException("Uri can not contain # or / characters");
        }
        uriToClass.put(uri, viewClass);
        classToUri.put(viewClass, uri);
        if (getMainView() == null) {
            setMainView(uri);
        }
    }

    /**
	 * Confirmed move to new view.
	 * 
	 * @param requestedDataId
	 *            the requested data id
	 * @param newView
	 *            the new view
	 * @param warn
	 *            the warn
	 */
    private void confirmedMoveToNewView(final String requestedDataId, final View newView, final String warn) {
        VerticalLayout lo = new VerticalLayout();
        lo.setMargin(true);
        lo.setSpacing(true);
        lo.setWidth("400px");
        final Window wDialog = new Window("Warning", lo);
        wDialog.setModal(true);
        final Window main = getWindow();
        main.addWindow(wDialog);
        lo.addComponent(new Label(warn));
        lo.addComponent(new Label("If you do not want to navigate away from the current screen, press Cancel."));
        Button cancel = new Button("Cancel", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                uriFragmentUtil.setFragment(currentFragment, false);
                main.removeWindow(wDialog);
            }
        });
        Button cont = new Button("Continue", new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                main.removeWindow(wDialog);
                moveTo(newView, requestedDataId, false);
            }
        });
        HorizontalLayout h = new HorizontalLayout();
        h.addComponent(cancel);
        h.addComponent(cont);
        h.setSpacing(true);
        lo.addComponent(h);
        lo.setComponentAlignment(h, "r");
    }

    /**
	 * Fragment changed.
	 */
    private void fragmentChanged() {
        String newFragment = uriFragmentUtil.getFragment();
        if ("".equals(newFragment)) {
            newFragment = mainViewUri;
        }
        int i = newFragment.indexOf('/');
        String uri = i < 0 ? newFragment : newFragment.substring(0, i);
        final String requestedDataId = i < 0 || i + 1 == newFragment.length() ? null : newFragment.substring(i + 1);
        if (uriToClass.containsKey(uri)) {
            final View newView = getOrCreateView(uri);
            String warn = currentView == null ? null : currentView.getWarningForNavigatingFrom();
            if (warn != null && warn.length() > 0) {
                confirmedMoveToNewView(requestedDataId, newView, warn);
            } else {
                moveTo(newView, requestedDataId, false);
            }
        } else {
            uriFragmentUtil.setFragment(currentFragment, false);
        }
    }

    /**
	 * Gets the current view.
	 * 
	 * @return the current view
	 */
    public final View getCurrentView() {
        return currentView;
    }

    /**
	 * Gets the main view.
	 * 
	 * @return the main view
	 */
    public final String getMainView() {
        return mainViewUri;
    }

    /**
	 * Gets the or create view.
	 * 
	 * @param uri
	 *            the uri
	 * 
	 * @return the or create view
	 */
    private View getOrCreateView(final String uri) {
        Class<?> newViewClass = uriToClass.get(uri);
        if (!classToView.containsKey(newViewClass)) {
            try {
                View view = (View) newViewClass.newInstance();
                view.init(this, getApplication());
                classToView.put(newViewClass, view);
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        final View v = classToView.get(newViewClass);
        return v;
    }

    /**
	 * Gets the uri.
	 * 
	 * @param viewClass
	 *            the view class
	 * 
	 * @return the uri
	 */
    public final String getUri(final Class<?> viewClass) {
        return classToUri.get(viewClass);
    }

    /**
	 * Gets the view class.
	 * 
	 * @param uri
	 *            the uri
	 * 
	 * @return the view class
	 */
    public final Class<?> getViewClass(final String uri) {
        return uriToClass.get(uri);
    }

    /**
	 * Move to.
	 * 
	 * @param v
	 *            the v
	 * @param requestedDataId
	 *            the requested data id
	 * @param noFragmentSetting
	 *            the no fragment setting
	 */
    @SuppressWarnings("unchecked")
    private void moveTo(final View v, final String requestedDataId, final boolean noFragmentSetting) {
        currentFragment = classToUri.get(v.getClass());
        if (requestedDataId != null) {
            currentFragment += "/" + requestedDataId;
        }
        if (!noFragmentSetting && !currentFragment.equals(uriFragmentUtil.getFragment())) {
            uriFragmentUtil.setFragment(currentFragment, false);
        }
        Component removeMe = null;
        for (Iterator<Component> i = layout.getComponentIterator(); i.hasNext(); ) {
            Component c = i.next();
            if (c != uriFragmentUtil) {
                removeMe = c;
            }
        }
        if (removeMe != null) {
            layout.removeComponent(removeMe);
        }
        layout.addComponent(v);
        layout.setExpandRatio(v, 1.0F);
        v.navigateTo(requestedDataId);
        currentView = v;
    }

    /**
	 * Navigate to.
	 * 
	 * @param viewClass
	 *            the view class
	 */
    public final void navigateTo(final Class<?> viewClass) {
        String uri = getUri(viewClass);
        if (uri != null) {
            navigateTo(uri);
        }
    }

    /**
	 * Navigate to.
	 * 
	 * @param uri
	 *            the uri
	 */
    public final void navigateTo(final String uri) {
        uriFragmentUtil.setFragment(uri);
    }

    /**
	 * Removes the view.
	 * 
	 * @param uri
	 *            the uri
	 */
    public final void removeView(String uri) {
        Class<?> c = uriToClass.get(uri);
        if (c != null) {
            uriToClass.remove(uri);
            classToUri.remove(c);
            if (getMainView() == null || getMainView().equals(getMainView())) {
                if (uriToClass.size() == 0) {
                    mainViewUri = null;
                } else {
                    setMainView(uriToClass.keySet().iterator().next());
                }
            }
        }
    }

    /**
	 * Sets the current view.
	 * 
	 * @param currentView
	 *            the new current view
	 */
    public final void setCurrentView(final View currentView) {
        this.currentView = currentView;
    }

    /**
	 * Sets the main view.
	 * 
	 * @param mainViewUri
	 *            the new main view
	 */
    public final void setMainView(final String mainViewUri) {
        if (uriToClass.containsKey(mainViewUri)) {
            this.mainViewUri = mainViewUri;
            if (currentView == null) {
                moveTo(getOrCreateView(mainViewUri), null, true);
            }
        } else {
            throw new IllegalArgumentException("No view with given uri can be found in the navigator");
        }
    }
}
