package test.xito.dazzle;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.xito.dazzle.widget.toolbar.Toolbar;

/**
 *
 * @author deane
 */
public class ToolBarTest {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Test ToolBar");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Toolbar toolBar = new Toolbar();
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}
