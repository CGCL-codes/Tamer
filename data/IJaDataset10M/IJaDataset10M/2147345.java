package org.eclipsecon.gwt.chattr.client;

public class UserRemoveEvent extends ChatEvent {

    private User user;

    public UserRemoveEvent() {
    }

    public UserRemoveEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void accept(ChatEventVisitor visitor) {
        visitor.visit(this);
    }
}
