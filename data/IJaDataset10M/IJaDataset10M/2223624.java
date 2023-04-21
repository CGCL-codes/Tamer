package chess;

import javax.swing.SwingUtilities;
import chess.UI.ChessWindow;
import chess.business.BusinessController;
import chess.dtos.PlayerDTO;

public class Main {

    BusinessController cc;

    PlayerDTO players[];

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new ChessWindow().setVisible(true);
            }
        });
    }
}
