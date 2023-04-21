package org.gjt.gamp.ui;

import org.gjt.gamp.application.*;
import org.gjt.gamp.gamp.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
Display the properties for the currently selected object  
*/
public class PropertiesCommand extends Command {

    public void execute(MyView vp) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "Not done yet", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
