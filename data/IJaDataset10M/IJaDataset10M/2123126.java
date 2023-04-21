package view;

import game_model.position;
import javax.swing.*;
import data_model.gambler;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author MK
 */
public class game_hand_panel extends JPanel {

    private static final long serialVersionUID = 1L;

    gambler owner;

    boolean flipped = true;

    ArrayList<card_view> hand = new ArrayList<card_view>();

    ArrayList<position> hand_pos = new ArrayList<position>();

    public game_hand_panel(gambler g) {
        this.setLayout(new GridLayout(8, 1));
        this.setPreferredSize(new Dimension(100, 100));
        this.setOpaque(false);
        owner = g;
        for (int i = 0; i < owner.getDeck().size(); i++) {
            try {
                position puff = new position("hand");
                puff.setOwner(owner);
                puff.setOwn(owner.getDeck().get(i));
                hand.add(new card_view("hand", puff));
                this.add(hand.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void flip() {
        for (int i = 0; i < owner.getDeck().size(); i++) {
            hand.get(i).flip();
            flipped = !flipped;
        }
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void disposeCard(card_view c) {
        for (int i = 0; i < owner.getDeck().size(); i++) {
            if (hand.get(i).equals(c)) this.remove(hand.get(i));
        }
    }
}
