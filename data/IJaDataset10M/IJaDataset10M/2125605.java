package JavaOrc;

import JavaOrc.BlueInterface.DiagramDataModelShell;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import JavaOrc.ui.ToolPalette;
import JavaOrc.ui.FlatMenuBar;
import JavaOrc.ui.DiagramContainer;
import JavaOrc.DeviceLibrary.DeviceLibraryEditor;
import diagram.Diagram;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @class JavaOrc
 *
 * @date 12-16-2004
 * @author Eric Crahen, krcpa
 * @version 1.0
 *
 */
public class JavaOrcComponent extends JComponent {

    private DiagramContainer container;

    public JavaOrcComponent() {
        container = new DiagramContainer();
        DeviceLibraryEditor deviceLibrary = new DeviceLibraryEditor();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, container, deviceLibrary);
        splitPane.setResizeWeight(0.8);
        ToolPalette palette = new ToolPalette(container, deviceLibrary);
        FlatMenuBar menuBar = new FlatMenuBar();
        container.updateMenus(menuBar);
        palette.updateMenus(menuBar);
        updateMenus(menuBar);
        Container content = this;
        content.setLayout(new BorderLayout());
        content.add(menuBar, BorderLayout.NORTH);
        content.add(palette, BorderLayout.WEST);
        content.add(splitPane);
    }

    /**
   * Update the JMenuBar before its installed. Add exit option, etc.
   *
   * @param JMenuBar
   */
    public void updateMenus(FlatMenuBar menuBar) {
        JMenu menu = menuBar.getMenu("File");
        menu.add(new JSeparator(), -1);
        menu.add(new JMenuItem(new QuitAction()), -1);
        menu = menuBar.getHelpMenu();
        menu.add(new JMenuItem(new AboutAction()), -1);
    }

    /**
   * @class QuitAction
   */
    class QuitAction extends AbstractAction {

        QuitAction() {
            super("Quit");
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
   * @class AboutAction
   */
    class AboutAction extends AbstractAction {

        JComponent about = new JLabel("<HTML>Created By: <B>krcpa</B><HTML>", JLabel.CENTER);

        AboutAction() {
            super("About");
        }

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showOptionDialog(null, about, "About", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] { "OK" }, null);
        }
    }

    public void setDiagramModel(DiagramDataModelShell diagDataModelShell) {
        container.setDataShell(diagDataModelShell);
    }

    public static void main(String[] args) {
        JavaOrcComponent jc = new JavaOrcComponent();
        try {
            JFrame frame = new JFrame();
            frame.getContentPane().add(jc);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setBounds(dim.width / 8, dim.height / 8, dim.width * 3 / 4, dim.height * 3 / 4);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(0);
        }
    }
}
