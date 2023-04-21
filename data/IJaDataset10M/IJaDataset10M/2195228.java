package snakefarm;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Gergo
 */
public class ButtonsPanel extends Panel {

    final GraphicMain graphicMain;

    public ButtonsPanel(final GraphicMain graphicMain) {
        this.graphicMain = graphicMain;
        setLayout(new GridLayout(1, 2));
        Panel temp;
        Button b;
        temp = new Panel();
        b = new Button("Exit");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphicMain.exit();
            }
        });
        temp.add(b);
        add(temp);
        temp = new Panel();
        b = new Button("New game");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphicMain.newGame();
            }
        });
        temp.add(b);
        add(temp);
    }
}
