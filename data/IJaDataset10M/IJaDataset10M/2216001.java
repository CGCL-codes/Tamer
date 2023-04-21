package apollo.gui.menus;

import apollo.gui.*;
import apollo.gui.genomemap.ScaleView;
import apollo.util.*;
import apollo.datamodel.*;
import apollo.dataadapter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.net.*;

public class ScaleMenu extends JPopupMenu {

    ScaleView view;

    Point pos;

    JMenuItem colouring;

    JMenuItem guideline;

    public ScaleMenu(ScaleView view, Point pos) {
        super("Scale operations");
        this.view = view;
        this.pos = pos;
        menuInit();
    }

    public void menuInit() {
        colouring = add(view.getColouringAction());
    }
}
