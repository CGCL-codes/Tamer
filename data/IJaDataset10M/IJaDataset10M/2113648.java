package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.GameManager;
import model.Player;
import model.Rules;
import view.SlotButton;

/**
 * Recebe qual casa do tabuleiro foi clicada e atribui isso à jogada do jogador
 * atual.
 */
public class PlayerListener implements ActionListener {

    private boolean valid = true;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (valid == false) return;
        SlotButton button = (SlotButton) e.getSource();
        Point pos = button.getPosition();
        GameManager game = GameManager.getInstance();
        if (!Rules.isValid(pos, game.getBoard())) return;
        Player now = game.getPlaysNow();
        now.setMove(pos);
        now.setReady(true);
        button.setType(now.getType());
        game.nextTurn();
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
