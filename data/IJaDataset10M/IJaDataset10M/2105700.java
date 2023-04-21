package foa.preview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Fabio Giannetti
 * @version 0.0.1
 */
public class ModifyBrickAction extends AbstractAction {

    private Container c;

    private String group;

    private String brickName;

    private String brickGroup;

    public ModifyBrickAction(Container c, String brickName, String brickGroup) {
        super("Modify Brick...", null);
        this.c = c;
        this.brickName = brickName;
        this.brickGroup = brickGroup;
    }

    public void actionPerformed(ActionEvent e) {
        ((PreviewFrame) c).getBrickDirector().modifyBrick(brickName, brickGroup);
    }
}
