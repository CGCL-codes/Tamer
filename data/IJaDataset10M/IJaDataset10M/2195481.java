package pl.bristleback.scrumtable.vo;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 23.04.11 14:05 <br/>
 *
 * @author Paweł Machowski
 */
public class Widget implements Comparable<Widget> {

    private static Logger log = Logger.getLogger(Widget.class.getName());

    private String id;

    private Position position;

    private String noteName;

    public Widget() {
    }

    public Widget(String newId, String noteName) {
        this.id = newId;
        this.noteName = noteName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    @Override
    public int compareTo(Widget widget) {
        return this.id.compareTo(widget.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        if (id != null ? !id.equals(widget.id) : widget.id != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
