package jasperdesign.ui.jrtree;

import javax.swing.tree.*;
import java.util.*;
import net.sf.jasperreports.engine.base.*;
import net.sf.jasperreports.engine.*;

public class JRRectangleTreeNode extends JRElementTreeNode {

    public JRRectangleTreeNode(TreeNode n, JRBaseReport r, JRRectangle aElement) {
        super(n, r, aElement);
    }

    public String toString() {
        String s = "Rectangle";
        return s;
    }

    private JRRectangle getRectangle() {
        return (JRRectangle) element;
    }
}
