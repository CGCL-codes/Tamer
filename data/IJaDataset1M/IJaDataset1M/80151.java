package com.scottandjoe.texasholdem.gameplay;

import com.scottandjoe.texasholdem.chips.Chips;
import com.scottandjoe.texasholdem.cards.FullHand;
import javax.swing.ImageIcon;

/**
 *
 * @author Scott DellaTorre
 * @author Joe Stein
 */
public class Player {

    private boolean active = true;

    private Chips chips;

    private int id;

    private ImageIcon image;

    private FullHand hand;

    private final String username;

    public Player(String u, ImageIcon img, int chipCount) {
        this.id = -1;
        image = img;
        username = u;
        chips = new Chips(chipCount);
    }

    public Player(int id, String u, ImageIcon img, int chipCount) {
        this.id = id;
        image = img;
        username = u;
        chips = new Chips(chipCount);
    }

    public Player(int id, String u, ImageIcon img, Chips c) {
        this.id = id;
        image = img;
        username = u;
        chips = c;
    }

    public Chips getChips() {
        return chips;
    }

    public int getID() {
        return id;
    }

    public ImageIcon getImage() {
        return image;
    }

    public FullHand getHand() {
        return hand;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        active = isActive;
    }

    public void setHand(FullHand fh) {
        hand = fh;
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
