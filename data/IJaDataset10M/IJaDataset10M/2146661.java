package jat.attitude.util;

import jat.util.FileUtil;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <P>
 * The JTextFieldPanel provides an efficient way of creating and organizing
 * TextField objects. The class extends JPanel and is therefore a JPanel itself.
 * And all the components constructed in this class is swing components.
 * The main objective of this class is to create input fields with labels 
 * identifying the fields.
 * <P>
 * JTextFieldPanel uses BoxLayout to construct a JPanel with 1 row or column of 
 * JTextField components along with labels.
 *
 * @author 	Noriko Takada
 * @version 	Last Modified: 08/12003
 */
public class JTextFieldPanel extends JPanel {

    Font fancyFont = new Font("Serif", Font.BOLD | Font.ITALIC, 32);

    Font titlef = new Font("Dialog", Font.BOLD, 16);

    Font boldf = new Font("Dialog", Font.BOLD, 12);

    Font italicf = new Font("Dialog", Font.ITALIC, 12);

    Font normalf = new Font("Dialog", Font.PLAIN, 12);

    private static JFrame theFrame;

    /**
	  * @param		direction	(int)1-> X_AXIS, 2-> Y_AXIS
	  * @param		title		(String) Title of the panel
	  * @param		labels		(String[]) Labels of the corresponding TextFields
	  * @param		fields		(JTextField[]) TextFields to be laid out
	  * @param		c			(Color)	Color of the panel
	  */
    public JTextFieldPanel(int direction, String title, String labels[], JTextField fields[], Color c) {
        super();
        int length = labels.length;
        if (direction == 1) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            int panelRowWidth = length * 100 + length * 5 + 5 + 5;
            int panelRowHeight = 30;
            Dimension rowSize = new Dimension(panelRowWidth, panelRowHeight);
            this.setMaximumSize(rowSize);
            this.setPreferredSize(rowSize);
            this.setMinimumSize(rowSize);
        } else {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            int panelColumnWidth = 110;
            int panelColumnHeight = (length + 1) * 25 + length * 5 + 5;
            Dimension columnSize = new Dimension(panelColumnWidth, panelColumnHeight);
            this.setMaximumSize(columnSize);
            this.setPreferredSize(columnSize);
            this.setMinimumSize(columnSize);
        }
        this.setBackground(c);
        this.setBorder(BorderFactory.createTitledBorder(title));
        for (int i = 0; i < length; ++i) {
            int fieldWidth = 50;
            int fieldHeight = 25;
            JPanel unitPanel = new JPanel();
            unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.X_AXIS));
            Dimension unitSize = new Dimension(2 * fieldWidth, fieldHeight);
            unitPanel.setMaximumSize(unitSize);
            unitPanel.setPreferredSize(unitSize);
            unitPanel.setMinimumSize(unitSize);
            unitPanel.setBackground(c);
            if (direction == 1) unitPanel.setAlignmentY(CENTER_ALIGNMENT); else unitPanel.setAlignmentX(LEFT_ALIGNMENT);
            Dimension fieldSize = new Dimension(fieldWidth, fieldHeight);
            fields[i].setMaximumSize(fieldSize);
            fields[i].setPreferredSize(fieldSize);
            fields[i].setMinimumSize(fieldSize);
            JLabel label = new JLabel(labels[i], Label.RIGHT);
            unitPanel.add(Box.createHorizontalGlue());
            unitPanel.add(label);
            unitPanel.add(fields[i]);
            this.add(Box.createRigidArea(new Dimension(5, 5)));
            this.add(unitPanel);
        }
        this.add(Box.createRigidArea(new Dimension(5, 5)));
        this.setAlignmentX(LEFT_ALIGNMENT);
    }

    /**
	 * Demonstrates the use of JTextFieldPanel class
	 * @param	args	(String[])	Argument
	 */
    public static void main(String[] args) {
        String images_path = FileUtil.getClassFilePath("jat.attitude.thesis", "AttitudeSimulator") + "images/";
        ImageIcon domburi = new ImageIcon(images_path + "Domburi7.gif");
        String title = "";
        String hobby[] = { "w1", "hobby2", "hobby3" };
        JTextField hobbyfields[] = new JTextField[3];
        hobbyfields[0] = new JTextField(5);
        hobbyfields[1] = new JTextField(5);
        hobbyfields[2] = new JTextField(5);
        JTextFieldPanel theJPanel = new JTextFieldPanel(2, title, hobby, hobbyfields, Color.pink);
        theFrame = new JFrame();
        theFrame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setSize(750, 550);
        outerPanel.add(theJPanel);
        theFrame.getContentPane().add(outerPanel, BorderLayout.CENTER);
        theFrame.setTitle("JTextFieldPanel");
        Dimension frameSize = theJPanel.getPreferredSize();
        theFrame.setSize(750, 550);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        theFrame.setLocation((d.width - theFrame.getSize().width) / 2, (d.height - theFrame.getSize().height) / 2);
        theFrame.setVisible(true);
    }
}
