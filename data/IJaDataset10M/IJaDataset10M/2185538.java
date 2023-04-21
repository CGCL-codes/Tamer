package PRISM.VRWGui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * 
 * @author Mauro Dragone
 */
public class BoxAlignmentDemo extends JPanel {

    public BoxAlignmentDemo() {
        super(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel buttonRow = new JPanel();
        buttonRow.add(createButtonRow(false));
        buttonRow.add(createButtonRow(true));
        tabbedPane.addTab("Altering alignments", buttonRow);
        JPanel labelAndComponent = new JPanel();
        labelAndComponent.add(createLabelAndComponent(false));
        labelAndComponent.add(createLabelAndComponent(true));
        tabbedPane.addTab("X alignment mismatch", labelAndComponent);
        JPanel buttonAndComponent = new JPanel();
        buttonAndComponent.add(createYAlignmentExample(false));
        buttonAndComponent.add(createYAlignmentExample(true));
        tabbedPane.addTab("Y alignment mismatch", buttonAndComponent);
        add(tabbedPane, BorderLayout.CENTER);
    }

    protected JPanel createButtonRow(boolean changeAlignment) {
        JButton button1 = new JButton("A JButton", createImageIcon("images/middle.gif"));
        button1.setVerticalTextPosition(AbstractButton.BOTTOM);
        button1.setHorizontalTextPosition(AbstractButton.CENTER);
        JButton button2 = new JButton("Another JButton", createImageIcon("images/geek-cght.gif"));
        button2.setVerticalTextPosition(AbstractButton.BOTTOM);
        button2.setHorizontalTextPosition(AbstractButton.CENTER);
        String title;
        if (changeAlignment) {
            title = "Desired";
            button1.setAlignmentY(BOTTOM_ALIGNMENT);
            button2.setAlignmentY(BOTTOM_ALIGNMENT);
        } else {
            title = "Default";
        }
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(button1);
        pane.add(button2);
        return pane;
    }

    protected JPanel createLabelAndComponent(boolean doItRight) {
        JPanel pane = new JPanel();
        JComponent component = new JPanel();
        Dimension size = new Dimension(150, 100);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        TitledBorder border = new TitledBorder(new LineBorder(Color.black), "A JPanel", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component.setBorder(border);
        JLabel label = new JLabel("This is a JLabel");
        String title;
        if (doItRight) {
            title = "Matched";
            label.setAlignmentX(CENTER_ALIGNMENT);
        } else {
            title = "Mismatched";
        }
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(label);
        pane.add(component);
        return pane;
    }

    protected JPanel createYAlignmentExample(boolean doItRight) {
        JPanel pane = new JPanel();
        String title;
        JComponent component1 = new JPanel();
        Dimension size = new Dimension(100, 50);
        component1.setMaximumSize(size);
        component1.setPreferredSize(size);
        component1.setMinimumSize(size);
        TitledBorder border = new TitledBorder(new LineBorder(Color.black), "A JPanel", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component1.setBorder(border);
        JComponent component2 = new JPanel();
        size = new Dimension(100, 50);
        component2.setMaximumSize(size);
        component2.setPreferredSize(size);
        component2.setMinimumSize(size);
        border = new TitledBorder(new LineBorder(Color.black), "A JPanel", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.black);
        component2.setBorder(border);
        if (doItRight) {
            title = "Matched";
        } else {
            component1.setAlignmentY(TOP_ALIGNMENT);
            title = "Mismatched";
        }
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(component1);
        pane.add(component2);
        return pane;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = BoxAlignmentDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("BoxAlignmentDemo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        BoxAlignmentDemo newContentPane = new BoxAlignmentDemo();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
