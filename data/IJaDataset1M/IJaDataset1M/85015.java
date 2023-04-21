package com.rapidminer.gui.operatortree.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.rapidminer.gui.operatortree.OperatorTree;
import com.rapidminer.gui.tools.IconSize;
import com.rapidminer.gui.tools.SwingTools;

/**
 * Start the corresponding action.
 * 
 * @author Ingo Mierswa
 */
public class ExpandAllAction extends AbstractAction {

    private static final long serialVersionUID = -1779888083256884608L;

    private static final String ICON_NAME = "zoom_in.png";

    private static final Icon[] ICONS = new Icon[IconSize.values().length];

    static {
        int counter = 0;
        for (IconSize size : IconSize.values()) {
            ICONS[counter++] = SwingTools.createIcon(size.getSize() + "/" + ICON_NAME);
        }
    }

    private OperatorTree operatorTree;

    public ExpandAllAction(OperatorTree operatorTree, IconSize size) {
        super("Expand Tree", ICONS[size.ordinal()]);
        putValue(SHORT_DESCRIPTION, "Expands the complete operator tree");
        putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_X));
        this.operatorTree = operatorTree;
    }

    public void actionPerformed(ActionEvent e) {
        this.operatorTree.expandAll();
    }
}
