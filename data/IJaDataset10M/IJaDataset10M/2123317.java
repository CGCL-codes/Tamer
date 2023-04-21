package pt.isel.meic.agendaagent.ontology.impl;

import jade.util.leap.*;
import pt.isel.meic.agendaagent.ontology.*;

/**
* Protege name: Event
* @author OntologyBeanGenerator v4.1
* @version 2011/09/1, 01:51:12
*/
public class DefaultEvent implements Event {

    private static final long serialVersionUID = 1653532723714014168L;

    private String _internalInstanceName = null;

    public DefaultEvent() {
        this._internalInstanceName = "";
    }

    public DefaultEvent(String instance_name) {
        this._internalInstanceName = instance_name;
    }

    public String toString() {
        return _internalInstanceName;
    }

    /**
   * Protege name: eventStatus
   */
    private EventStatus eventStatus;

    public void setEventStatus(EventStatus value) {
        this.eventStatus = value;
    }

    public EventStatus getEventStatus() {
        return this.eventStatus;
    }

    /**
   * Brief title for the event
   * Protege name: title
   */
    private String title;

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return this.title;
    }

    /**
   * Location of the event or nearby necessities such as parking. If a <gd:where> element is specified at the feed level, but there's no <gd:where> element at the entry level, then the entries inherit the feed-level <gd:where> value.
   * Protege name: where
   */
    private Where where;

    public void setWhere(Where value) {
        this.where = value;
    }

    public Where getWhere() {
        return this.where;
    }

    /**
   * Protege name: transparency
   */
    private Transparency transparency;

    public void setTransparency(Transparency value) {
        this.transparency = value;
    }

    public Transparency getTransparency() {
        return this.transparency;
    }

    /**
   * Longer description of the event.
   * Protege name: content
   */
    private String content;

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    /**
   * Protege name: visibility
   */
    private Visibility visibility;

    public void setVisibility(Visibility value) {
        this.visibility = value;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    /**
   * Protege name: extendedProperty
   */
    private List extendedProperty = new ArrayList();

    public void addExtendedProperty(ExtendedProperty elem) {
        extendedProperty.add(elem);
    }

    public boolean removeExtendedProperty(ExtendedProperty elem) {
        boolean result = extendedProperty.remove(elem);
        return result;
    }

    public void clearAllExtendedProperty() {
        extendedProperty.clear();
    }

    public Iterator getAllExtendedProperty() {
        return extendedProperty.iterator();
    }

    public List getExtendedProperty() {
        return extendedProperty;
    }

    public void setExtendedProperty(List l) {
        extendedProperty = l;
    }

    /**
   * Protege name: when
   */
    private When when;

    public void setWhen(When value) {
        this.when = value;
    }

    public When getWhen() {
        return this.when;
    }

    /**
   * Person who created this event.
   * Protege name: author
   */
    private jade.core.AID author;

    public void setAuthor(jade.core.AID value) {
        this.author = value;
    }

    public jade.core.AID getAuthor() {
        return this.author;
    }

    /**
   * Comments feed.
   * Protege name: comments
   */
    private String comments;

    public void setComments(String value) {
        this.comments = value;
    }

    public String getComments() {
        return this.comments;
    }

    /**
   * Protege name: originalEvent
   */
    private OriginalEvent originalEvent;

    public void setOriginalEvent(OriginalEvent value) {
        this.originalEvent = value;
    }

    public OriginalEvent getOriginalEvent() {
        return this.originalEvent;
    }

    /**
   * Links.
   * Protege name: link
   */
    private List link = new ArrayList();

    public void addLink(String elem) {
        link.add(elem);
    }

    public boolean removeLink(String elem) {
        boolean result = link.remove(elem);
        return result;
    }

    public void clearAllLink() {
        link.clear();
    }

    public Iterator getAllLink() {
        return link.iterator();
    }

    public List getLink() {
        return link;
    }

    public void setLink(List l) {
        link = l;
    }

    /**
   * Categories
   * Protege name: category
   */
    private List category = new ArrayList();

    public void addCategory(String elem) {
        category.add(elem);
    }

    public boolean removeCategory(String elem) {
        boolean result = category.remove(elem);
        return result;
    }

    public void clearAllCategory() {
        category.clear();
    }

    public Iterator getAllCategory() {
        return category.iterator();
    }

    public List getCategory() {
        return category;
    }

    public void setCategory(List l) {
        category = l;
    }

    /**
   * People associated with the event: organizer, attendees, speakers, performers, etc.
   * Protege name: who
   */
    private List who = new ArrayList();

    public void addWho(Who elem) {
        who.add(elem);
    }

    public boolean removeWho(Who elem) {
        boolean result = who.remove(elem);
        return result;
    }

    public void clearAllWho() {
        who.clear();
    }

    public Iterator getAllWho() {
        return who.iterator();
    }

    public List getWho() {
        return who;
    }

    public void setWho(List l) {
        who = l;
    }

    /**
   * Represents the dates and times when a recurring event takes place.
   * Protege name: recurrence
   */
    private String recurrence;

    public void setRecurrence(String value) {
        this.recurrence = value;
    }

    public String getRecurrence() {
        return this.recurrence;
    }
}
