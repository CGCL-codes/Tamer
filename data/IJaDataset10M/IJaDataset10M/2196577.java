package org.ewm.clientManagement;

import java.util.LinkedList;
import java.util.List;
import gnu.x11.Window;
import gnu.x11.Window.Attributes;
import gnu.x11.event.Event;

public class DecorationContainer extends InteractionComponent {

    private List<InteractionComponent> childInteractionComponents;

    private List<ClientWindow> childClientWindows;

    public DecorationContainer(ClientWindow client) {
        super();
        childInteractionComponents = new LinkedList<InteractionComponent>();
        childClientWindows = new LinkedList<ClientWindow>();
        addClientWindow(client);
        Window.Attributes attributes = new Window.Attributes();
        attributes.set_background(client.getMainWindow().display.default_white);
        Window frame = new Window(client.getMainWindow().display.getRootWindow(), client.getX(), client.getY(), client.getWidth() + client.getInsets().left + client.getInsets().right, client.getHeight() + client.getInsets().top + client.getInsets().bottom);
        frame.create(0, attributes);
        frame.reparent(client.getMainWindow(), client.getInsets().left, client.getInsets().top);
        setMainWindow(frame);
        setX(frame.x);
        setY(frame.y);
        setWidth(frame.width);
        setHeight(frame.height);
        register(frame.id());
    }

    public void addClientWindow(ClientWindow clientWindow) {
        childClientWindows.add(clientWindow);
    }

    public List<ClientWindow> getChildClientWindows() {
        return childClientWindows;
    }

    public void addChildComponent(InteractionComponent windowContainer) {
        this.childInteractionComponents.add(windowContainer);
    }

    public List<InteractionComponent> getChildInteractionComponents() {
        return childInteractionComponents;
    }

    public void addChildWindow(InteractionComponent clientWindow) {
        this.childInteractionComponents.add(clientWindow);
    }

    public void setDecorationManager(DecorationManager decorationManager) {
        System.out.println("---setting decoration manager---");
        this.decorationManager = decorationManager;
        this.decorationManager.setWindowContainer(this);
    }

    public void addInteractionComponent(InteractionComponent interactionComponent) {
        interactionComponent.setParent(this);
    }

    @Override
    public void setMainWindow(Window window) {
        Attributes attr = new Window.Attributes();
        attr.set_event_mask(Event.BUTTON_PRESS_MASK | Event.BUTTON_RELEASE_MASK | Event.SUBSTRUCTURE_REDIRECT_MASK | Event.EXPOSURE_MASK | Event.ENTER_WINDOW_MASK | Event.LEAVE_WINDOW_MASK);
        window.change_attributes(attr);
        super.setMainWindow(window);
    }

    @Override
    public boolean isDominant() {
        return true;
    }
}
