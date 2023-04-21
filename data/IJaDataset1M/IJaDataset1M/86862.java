package bw.note;

import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;
import bw.util.*;

public class Note implements Comparable, Serializable {

    static final long serialVersionUID = 877300956316495095L;

    public static final String NOTE_TAG = "note";

    public static final String TIME_ATTR = "time";

    private String _id = null;

    private long _time = -1;

    private String _text = "";

    private int _height = 0;

    private int _width = 0;

    private int _top = 0;

    private int _left = 0;

    private long _lastModified = 0L;

    public Note() {
        _time = DateFactory.getInstance().getDate().getTime();
        _lastModified = DateFactory.getInstance().getDate().getTime();
    }

    public Note(Date time) {
        _time = time.getTime();
    }

    public Note(String id) {
        _id = id;
    }

    public boolean isNew() {
        return (_id == null);
    }

    public String getId() {
        return _id;
    }

    public void setId(String s) {
        _id = s;
    }

    public Date getDate() {
        return new Date(_time);
    }

    public void setDate(Date time) {
        _time = time.getTime();
    }

    public long getTime() {
        return _time;
    }

    public void setTime(long time) {
        _time = time;
    }

    public void modified() {
        _lastModified = DateFactory.getInstance().getDate().getTime();
    }

    public long getLastModified() {
        return _lastModified;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }

    public boolean hasText() {
        return (_text != null && _text.length() > 0);
    }

    public int getHeight() {
        return _height;
    }

    public void setHeight(int h) {
        _height = h;
    }

    public void setWidth(int w) {
        _width = w;
    }

    public int getWidth() {
        return _width;
    }

    public void setTop(int t) {
        _top = t;
    }

    public int getTop() {
        return _top;
    }

    public void setLeft(int l) {
        _left = l;
    }

    public int getLeft() {
        return _left;
    }

    public boolean equals(Object obj) {
        return (this.getId().equals(((Note) obj).getId()));
    }

    public int compareTo(Object obj) {
        Note note = (Note) obj;
        Long me = new Long(getTime());
        Long you = new Long(note.getTime());
        return me.compareTo(you);
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<note ");
        buf.append("id=\"").append(getId()).append("\" ");
        GregorianCalendar cal = DateFactory.getInstance().getCalendar();
        cal.setTime(getDate());
        cal.setTimeZone(DateFactory.getInstance().getTimeZone());
        buf.append("year=\"").append(cal.get(Calendar.YEAR)).append("\" ");
        buf.append("month=\"").append(cal.get(Calendar.MONTH)).append("\" ");
        buf.append("date=\"").append(cal.get(Calendar.DAY_OF_MONTH)).append("\" ");
        buf.append("time=\"").append(Format.formatTime(getDate())).append("\" ");
        buf.append("height=\"").append(_height).append("\" ");
        buf.append("width=\"").append(_width).append("\" ");
        buf.append("top=\"").append(_top).append("\" ");
        buf.append("left=\"").append(_left);
        buf.append("\">");
        buf.append(Util.XMLEscape(getText()));
        buf.append("</note>");
        return buf.toString();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<note ");
        buf.append("id=\"").append(_id).append("\" ");
        buf.append("time=\"").append(_time);
        buf.append("\">");
        buf.append(_text);
        buf.append("</note>");
        return buf.toString();
    }
}
