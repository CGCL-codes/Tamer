package joelib.gui.molviewer.java3d;

import wsi.ra.tool.ResourceLoader;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.apache.log4j.Category;

/**
 * Description of the Class
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.6 $, $Date: 2004/08/31 14:23:19 $
 */
public class ViewerUsage extends JDialog implements ActionListener {

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.gui.molviewer.java3d.ViewerUsage");

    private static final String imageLocation = "joelib/data/images/joelib.gif";

    BorderLayout borderLayout1 = new BorderLayout();

    FlowLayout flowLayout1 = new FlowLayout();

    GridLayout gridLayout1 = new GridLayout();

    JButton button1 = new JButton();

    JLabel imageLabel = new JLabel();

    JPanel insetsPanel1 = new JPanel();

    JPanel insetsPanel3 = new JPanel();

    JPanel panel1 = new JPanel();

    JPanel panel2 = new JPanel();

    /**
     *Constructor for the ViewerUsage object
     *
     * @param parent  Description of the Parameter
     */
    public ViewerUsage(Frame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
    }

    /**
     *Close the dialog on a button event
     *
     * @param e  Description of the Parameter
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            cancel();
        }
    }

    /**
     *Overridden so we can exit when window is closed
     *
     * @param e  Description of the Parameter
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    /**
     *Close the dialog
     */
    void cancel() {
        dispose();
    }

    /**
     *Component initialization
     *
     * @exception Exception  Description of the Exception
     */
    private void jbInit() throws Exception {
        URL image = this.getClass().getClassLoader().getSystemResource(imageLocation);
        if (image == null) {
            logger.error("Image not found at " + imageLocation);
        } else {
            imageLabel.setIcon(new ImageIcon(image));
        }
        this.setTitle("Usage");
        setResizable(false);
        panel1.setLayout(borderLayout1);
        JTextArea area1 = new JTextArea();
        area1.setEditable(false);
        int index = this.getClass().getName().lastIndexOf(".");
        String usageInfo = this.getClass().getName().substring(0, index);
        usageInfo = usageInfo.replace('.', '/');
        byte[] bytes = ResourceLoader.instance().getBytesFromResourceLocation(usageInfo + "/usage.txt");
        if (bytes == null) {
            logger.error("No usage info file found at " + usageInfo + "/usage.txt");
        } else {
            area1.setText(String.valueOf(bytes));
        }
        panel2.add(imageLabel, BorderLayout.NORTH);
        panel2.add(area1, BorderLayout.CENTER);
        insetsPanel1.setLayout(flowLayout1);
        gridLayout1.setRows(4);
        gridLayout1.setColumns(1);
        insetsPanel3.setLayout(gridLayout1);
        insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
        button1.setText("Ok");
        button1.addActionListener(this);
        this.getContentPane().add(panel1, null);
        insetsPanel1.add(button1, null);
        panel1.add(insetsPanel1, BorderLayout.SOUTH);
        panel1.add(panel2, BorderLayout.NORTH);
    }
}
