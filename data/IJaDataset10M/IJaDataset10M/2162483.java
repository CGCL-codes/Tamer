package CADI.Viewer.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * This class creates a new window where to display information. It is similar
 * to a JDialog.
 * <p>
 * Usage example:<br>
 * &nbsp; constructor<br>
 * &nbsp; display<br>  
 *  
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.0 2007-2012/12/09
 */
public class DisplayInfoFrame extends JFrame implements MouseListener, KeyListener {

    /**
	 * Reference to the parent window.
	 */
    private JFrame parent = null;

    /**
	 * Width of the window.
	 */
    private int width = 0;

    /**
	 * Height of the window.
	 */
    private int height = 0;

    /**
	 * Title of the window.
	 */
    private String title = "";

    /**
	 * Message with the information to show.
	 */
    private String msg = null;

    /**
	 * Button accept to close the window
	 */
    private JButton buttonOK;

    /**
	 * Is the scroll pane where the message will be displayed.
	 */
    JScrollPane scroller = null;

    /**
	 * Constructor.
	 * <p>
	 * If the <code>width</code> and <code>height</code> are <code>0</code> or
	 * smaller, the window preferred size is fit to the size preferred
	 * components. 
	 * 
	 * @param parent reference to the parent window.
	 * @param width preferred width of the window.
	 * @param height preferred height of the window.
	 * @param title title of the window.
	 */
    public DisplayInfoFrame(JFrame parent, int width, int height, String title) {
        super();
        this.parent = parent;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
	 * Is used to display a message.
	 * 
	 * @param msg the message to be displayed.
	 */
    public void display(String msg) {
        this.msg = msg;
        createAndShowGUI();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == buttonOK) {
            setVisible(false);
            dispose();
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if ((keyCode == KeyEvent.VK_ENTER) || (keyCode == KeyEvent.VK_ESCAPE)) {
            setVisible(false);
            dispose();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
	 * Creates a graphic user interface and show the text.
	 */
    private void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        setTitle(title);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = width > screenSize.width ? screenSize.width : width;
        height = height > screenSize.height ? screenSize.height : height;
        if ((width > 0) || (height > 0)) {
            Dimension dims = new Dimension(width, height);
            setSize(dims);
            setPreferredSize(dims);
            setMinimumSize(dims);
            setMaximumSize(dims);
        }
        int borderSize = 10;
        JLabel label = new JLabel();
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setText(msg);
        scroller = new JScrollPane(label);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
        buttonOK = new JButton("OK");
        Dimension bDims = new Dimension(75, 25);
        buttonOK.setSize(bDims);
        buttonOK.setPreferredSize(bDims);
        buttonOK.setMinimumSize(bDims);
        buttonOK.setMaximumSize(bDims);
        buttonOK.addMouseListener(this);
        buttonOK.addKeyListener(this);
        buttonOK.setFocusable(true);
        Box buttonBox = Box.createHorizontalBox();
        Dimension boxDims = new Dimension(bDims.width + 2 * borderSize, bDims.height + 2 * borderSize);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(buttonOK);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.setSize(boxDims);
        buttonBox.setPreferredSize(boxDims);
        buttonBox.setMinimumSize(boxDims);
        buttonBox.setMaximumSize(boxDims);
        buttonBox.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));
        getContentPane().add(scroller, BorderLayout.CENTER);
        getContentPane().add(buttonBox, BorderLayout.SOUTH);
        if (parent != null) {
            Point parentLocation = parent.getLocation();
            Dimension parentSize = parent.getSize();
            Point center = new Point(parentLocation.x + (parentSize.width / 2), parentLocation.y + (parentSize.height / 2));
            int xLocation = center.x - width / 2;
            int yLocation = center.y - height / 2;
            if ((xLocation >= 0) && (yLocation >= 0)) {
                setLocation(xLocation, yLocation);
            } else {
                setLocationRelativeTo(parent);
            }
        } else {
            setLocationByPlatform(true);
        }
        setResizable(true);
        pack();
        setVisible(true);
    }
}
