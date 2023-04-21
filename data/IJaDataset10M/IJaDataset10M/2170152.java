package net.ar.guia.managers.windows;

import java.lang.reflect.*;
import java.util.*;
import net.ar.guia.*;
import net.ar.guia.helpers.*;
import net.ar.guia.own.interfaces.*;

/**
 * Implementacion simple de un administrador de ventanas; tiene la capacidad de
 * apilar las ventanas que se van abriendo para poder trabajar con ventanas de
 * tipo "modal".
 * 
 * @author Fernando Damian Petrola
 */
public class DefaultWindowManager implements WindowManager {

    public static final String AFTER_HIDE_METHOD_NAME = "theAfterHideMethodName";

    public static final String OPENER_WINDOW = "theOpenerWindow";

    public static final String WINDOW_MANAGER_PROPERTY = "theWindowManager";

    protected Stack theStack;

    protected Hashtable theOpenersTable;

    protected WindowComponent currentWindow;

    protected void showNormal(Object aWindowToShow) {
    }

    protected void hideNormal(Object aWindow) {
    }

    protected WindowManager getWindowManager(Object anOpenerWindow) {
        VisualComponent theComponent = (VisualComponent) anOpenerWindow;
        return (WindowManager) theComponent.getClientProperty(WINDOW_MANAGER_PROPERTY);
    }

    protected void setWindowManager(Object aWindow, WindowManager aWindowManager) {
        VisualComponent theComponent = (VisualComponent) aWindow;
        theComponent.putClientProperty(WINDOW_MANAGER_PROPERTY, aWindowManager);
    }

    protected WindowComponent getNewVisualWindowFor(Object aWindowToShow) {
        return (WindowComponent) aWindowToShow;
    }

    protected boolean isWindowModal(Object aWindowToShow) {
        return false;
    }

    protected boolean isWindowEquals(Object aComponent, WindowComponent theWindowWrapper) {
        return aComponent.equals(theWindowWrapper);
    }

    protected Object getRealWindow(Object aWindow) {
        return aWindow;
    }

    public DefaultWindowManager() {
        theStack = new Stack();
        theOpenersTable = new Hashtable();
    }

    public void show(WindowComponent aWindow, boolean isModal) {
        if (!isModal) theStack.clear(); else addModalWindowExecuteMethod(aWindow);
        setCurrentWindow(aWindow);
    }

    protected void addModalWindowExecuteMethod(WindowComponent aWindow) {
        Object theMethod = aWindow.getClientProperty(AFTER_HIDE_METHOD_NAME);
        if (theMethod != null) theOpenersTable.put(aWindow.getName(), theMethod);
    }

    public void hide(WindowComponent aWindow) {
        WindowComponent foundWindow = findWindowWithId(aWindow.getName());
        if (existOpennedWindows() && foundWindow != null) {
            int indexOfWindow = theStack.indexOf(foundWindow);
            if (indexOfWindow > 0) {
                WindowComponent parentWindow = (WindowComponent) theStack.get(indexOfWindow - 1);
                executeMethodForModalClosing(aWindow, parentWindow);
                setCurrentWindow(parentWindow);
            } else setCurrentWindow((WindowComponent) theStack.peek());
            theStack.remove(foundWindow);
        }
        if (!existOpennedWindows()) {
            theStack.push(aWindow);
            setCurrentWindow(aWindow);
        }
    }

    protected void executeMethodForModalClosing(WindowComponent aWindow, WindowComponent aParentWindow) {
        String theMethodName = (String) theOpenersTable.get(aWindow.getName());
        if (theMethodName != null) callReturnMethod(aParentWindow, aWindow, theMethodName);
    }

    public WindowComponent getCurrentWindow() {
        if (!existOpennedWindows()) return null; else return currentWindow;
    }

    public void setCurrentWindow(WindowComponent aWindow) {
        setWindowManager(aWindow, this);
        currentWindow = findWindowWithId(aWindow.getName());
        if (currentWindow == null) {
            currentWindow = aWindow;
            theStack.push(aWindow);
        }
    }

    public WindowComponent findWindowWithId(Object aName) {
        for (Iterator i = theStack.iterator(); i.hasNext(); ) {
            WindowComponent theWindow = (WindowComponent) i.next();
            if (GuiaFramework.getInstance().getComponentNameManager().getUniqueId(theWindow).equals(aName)) return theWindow;
        }
        return null;
    }

    public void replaceCurrentWindow(WindowComponent aReplaceWindow) {
        theStack.set(theStack.indexOf(currentWindow), aReplaceWindow);
        setCurrentWindow(aReplaceWindow);
    }

    public void showChildWindow(Object anOpenerWindow, Object aWindowToShow) {
        showAndExecute(anOpenerWindow, aWindowToShow, null);
    }

    protected void callReturnMethod(WindowComponent aParentWindow, WindowComponent aWindow, String theMethodName) {
        Object theWindow = getRealWindow(aWindow);
        Object theOpenerWindow = getRealWindow(aParentWindow);
        callMethod(theMethodName, theWindow, theOpenerWindow);
    }

    protected void callMethod(String theMethodName, Object theWindow, Object theOpenerWindow) {
        try {
            Method theMethod = theOpenerWindow.getClass().getMethod(theMethodName, new Class[] { theWindow.getClass() });
            if (theMethod != null) theMethod.invoke(theOpenerWindow, new Object[] { theWindow });
        } catch (Exception e) {
            throw new GuiaException("Cannot invoke the method after window hide", e);
        }
    }

    public void showAndExecute(Object anOpenerWindow, Object aWindowToShow, String aMethodName) {
        WindowManager theWindowManager = getWindowManager(anOpenerWindow);
        setWindowManager(aWindowToShow, theWindowManager);
        if (theWindowManager != null) {
            WindowComponent theWindowToShowWrapper = getNewVisualWindowFor(aWindowToShow);
            if (aMethodName != null) theWindowToShowWrapper.putClientProperty(AFTER_HIDE_METHOD_NAME, aMethodName);
            theWindowToShowWrapper.putClientProperty(OPENER_WINDOW, anOpenerWindow);
            theWindowManager.show(theWindowToShowWrapper, isWindowModal(aWindowToShow));
        } else {
            showNormal(aWindowToShow);
            if (aMethodName != null) callMethod(aMethodName, aWindowToShow, anOpenerWindow);
        }
    }

    public void hide(Object aWindow) {
        WindowManager theWindowManager = getWindowManager(aWindow);
        if (theWindowManager != null) {
            WindowComponent theFoundWindow = theWindowManager.findWindow(aWindow);
            if (theFoundWindow != null) theWindowManager.hide(theFoundWindow);
        } else hideNormal(aWindow);
    }

    public WindowComponent findWindow(Object aComponent) {
        for (Iterator i = theStack.iterator(); i.hasNext(); ) {
            WindowComponent theWindowWrapper = (WindowComponent) i.next();
            if (isWindowEquals(aComponent, theWindowWrapper)) return theWindowWrapper;
        }
        return null;
    }

    public WindowComponent getWindow(String aWindowId) {
        WindowComponent foundWindow = null;
        if (existOpennedWindows()) if (aWindowId != null) foundWindow = findWindowWithId(aWindowId);
        return foundWindow;
    }

    private boolean existOpennedWindows() {
        return !theStack.isEmpty();
    }
}
