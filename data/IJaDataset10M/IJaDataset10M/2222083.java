package gebruikersInterfaceLaag.buttons;

import gebruikersInterfaceLaag.ImageCreator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class Button1Player extends BaseButton {

    String label;

    public Button1Player(String label) {
        super(null, new Dimension(279, 326));
        super.setIdleImage("images/1player_idle.png");
        super.setOverImage("images/1player_over.png");
    }
}
