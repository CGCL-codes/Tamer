package jp.ac.shizuoka.cs.panda.mmm.mr3.actions;

import java.awt.event.*;
import javax.swing.*;
import jp.ac.shizuoka.cs.panda.mmm.mr3.jgraph.*;
import jp.ac.shizuoka.cs.panda.mmm.mr3.util.*;

/**
 * @author takeshi morita
 */
public class RemoveAction extends AbstractAction {

    private RDFGraph graph;

    private GraphManager gmanager;

    private static final String TITLE = Translator.getString("Action.Remove.Text");

    private static final ImageIcon ICON = Utilities.getImageIcon(Translator.getString("Action.Remove.Icon"));

    public RemoveAction(RDFGraph g, GraphManager gm) {
        super(TITLE, ICON);
        graph = g;
        gmanager = gm;
        putValue(SHORT_DESCRIPTION, TITLE);
    }

    public void actionPerformed(ActionEvent e) {
        gmanager.initRemoveAction(graph);
        gmanager.removeAction();
    }
}
