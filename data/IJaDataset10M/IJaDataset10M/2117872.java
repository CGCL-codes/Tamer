package com.organic.maynard.outliner.menus.file;

import com.organic.maynard.outliner.menus.*;
import com.organic.maynard.outliner.*;
import com.organic.maynard.outliner.guitree.*;
import java.awt.event.*;
import javax.swing.*;
import org.xml.sax.*;

public class NewFileMenuItem extends AbstractOutlinerMenuItem implements ActionListener, GUITreeComponent {

    public void startSetup(Attributes atts) {
        super.startSetup(atts);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        OutlinerDocument doc = new OutlinerDocument("");
        Outliner.menuBar.windowMenu.changeToWindow(doc);
    }
}
