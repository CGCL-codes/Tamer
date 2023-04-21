package edu.oasis.domain;

public class DomainChangedEvent {

    DomainChangedEvent(Item item, ChangeType command) {
        this.item = item;
        this.changeType = command;
    }

    @SuppressWarnings("unchecked")
    DomainChangedEvent(WebteachCollection collection, Item item, ChangeType command) {
        this.collection = collection;
        this.item = item;
        this.changeType = command;
    }

    @SuppressWarnings("unchecked")
    DomainChangedEvent(ChangeType command, WebteachCollection collection) {
        this.changeType = command;
        this.collection = collection;
    }

    @SuppressWarnings("unchecked")
    protected WebteachCollection collection;

    protected Item item;

    protected ChangeType changeType;

    @SuppressWarnings("unchecked")
    public WebteachCollection getCollection() {
        return collection;
    }

    public Item getItem() {
        return item;
    }

    public ChangeType getChangeType() {
        return changeType;
    }
}
