package net.saim.game.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Player's verification of a semantic web link.
 * Holds link id and player's verification.
 */
public class Verification implements IsSerializable {

    private int id;

    private int selection;

    private int[] array = new int[2];

    public Verification() {
    }

    public Verification(int id, int selection) {
        this.id = id;
        this.selection = selection;
        this.array[0] = id;
        this.array[1] = selection;
    }

    public Verification(int[] selection) {
        this.id = selection[0];
        this.selection = selection[1];
        this.array = selection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int[] getArray() {
        return this.array;
    }
}
