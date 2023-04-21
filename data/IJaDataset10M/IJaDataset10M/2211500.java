package gebruikersInterfaceLaag.buttons;

import gebruikersInterfaceLaag.ImageCreator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class ButtonNav extends BaseButton {

    String label;

    public ButtonNav(String label) {
        super(label, new Dimension(180, 30));
        Font buttonFont = new Font("Century Gothic", 1, 18);
        super.setIdleState("images/nav_button_idle_s1.png", new Color(204, 204, 51), buttonFont);
        super.setOverState("images/nav_button_over_s1.png", new Color(204, 255, 51), buttonFont);
        super.setClickState("images/nav_button_click_s1.png", new Color(204, 153, 51), buttonFont);
        super.setDisabledState("images/nav_button_disabled_s1.png", new Color(102, 102, 102), buttonFont);
        super.setYalignment(22);
    }
}
