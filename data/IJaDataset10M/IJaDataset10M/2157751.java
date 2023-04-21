package test;

import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import model.AngaNode;
import vc.TechniqueContainerViewer;

public class TestTechniqueContainerViewer extends JFrame {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TestTechniqueContainerViewer t = new TestTechniqueContainerViewer();
        t.setVisible(true);
    }

    public TestTechniqueContainerViewer() throws HeadlessException {
        super("TestTechniqueContainerViewer");
        AngaNode asana = TestHelper.createAnga();
        TechniqueContainerViewer viewer = new TechniqueContainerViewer(asana);
        JScrollPane scrollpane = new JScrollPane(viewer);
        setSize(640, 100);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().add(scrollpane);
    }
}
