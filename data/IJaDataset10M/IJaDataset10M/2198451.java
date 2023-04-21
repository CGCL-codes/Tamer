package de.webmines.client.gamebrowser;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class GameItem implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String name;

    @Persistent
    private String width;

    @Persistent
    private String height;

    @Persistent
    private String mines;

    public GameItem() {
        this.name = "Noob";
        this.width = "10";
        this.height = "10";
        this.mines = "10";
    }

    public GameItem(GameItem item) {
        this.name = item.name;
        this.width = item.width;
        this.height = item.height;
        this.mines = item.mines;
    }

    public GameItem(String name, String width, String height, String mines) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.mines = mines;
    }

    public String getName() {
        return name;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getMines() {
        return mines;
    }

    public void setMines(String mines) {
        this.mines = mines;
    }
}
