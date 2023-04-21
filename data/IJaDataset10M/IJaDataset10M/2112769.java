package cz.cvut.fel.telefony.gui.util;

/**
 *
 * @author Frantisek Hradil
 */
public class Message {

    private Integer level;

    private String text;

    public Message(Integer level, String text) {
        this.level = level;
        this.text = text;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
