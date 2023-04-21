package practicecreator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class GeneratorMain {

    private static final long serialVersionUID = -4136627763691235191L;

    public static Dimension SCREENSIZE = null;

    public GeneratorMain() {
    }

    public static void main(String[] args) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        SCREENSIZE = toolkit.getScreenSize();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(SCREENSIZE.width, SCREENSIZE.height - 40));
        frame.setLayout(new BorderLayout());
        frame.add(new XMLGeneratorPanel(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
