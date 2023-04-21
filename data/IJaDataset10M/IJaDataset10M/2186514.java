package panelgui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**A resize panel is basically just a JPanel that you are able to resize - it also
 * allows you to split it up into another JPanel.  It also allows you to pop out
 * whatever view you currently have selected into a new window.
 * 
 * @author Robert Middleton
 *
 */
@SuppressWarnings("serial")
public class ResizePanel extends JPanel implements MouseListener, ActionListener {

    private JComponent center;

    private JComboBox type;

    private CardLayout centerLayout;

    private JPanel context;

    public static final Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    public static final String POP_OUT = "pop out";

    public static int ctr = 0;

    private AddToResizePanel[] res;

    private ResizePanel left;

    private ResizePanel right;

    private JSplitPane s;

    private boolean useScrollPane;

    public ResizePanel(AddToResizePanel... r) {
        this(false, r);
    }

    public ResizePanel(boolean useScrollPane, AddToResizePanel... r) {
        super(new BorderLayout(), true);
        this.useScrollPane = useScrollPane;
        centerLayout = new CardLayout();
        center = new JPanel(centerLayout);
        super.add(center, BorderLayout.CENTER);
        res = r;
        addContextPane();
        for (int x = 0; x < res.length; x++) {
            res[x].addToPanel(this);
        }
    }

    private void addContextPane() {
        context = new JPanel(new BorderLayout());
        type = new JComboBox();
        JButton popOut = new JButton("->");
        popOut.getFont().deriveFont((float) 2.0);
        popOut.setActionCommand(POP_OUT);
        context.add(type, BorderLayout.WEST);
        context.add(popOut, BorderLayout.EAST);
        context.setBorder(border);
        popOut.addActionListener(this);
        type.addActionListener(this);
        super.add(context, BorderLayout.SOUTH);
    }

    /**set the selected index.  Used generally after calling the constructor, to
	 * tell what to show
	 * 
	 * @param selected
	 */
    public void setSelected(int selected) {
        type.setSelectedIndex(selected);
        centerLayout.show(center, (String) type.getSelectedItem());
    }

    public void add(Component p, Object name) {
        type.addItem(name);
        if (useScrollPane) {
            JScrollPane s = new JScrollPane(p);
            s.addMouseListener(this);
            center.add(s, name);
        } else {
            JPanel temp = new JPanel();
            temp.setLayout(new GridLayout(1, 1));
            temp.add(p);
            temp.addMouseListener(this);
            center.add(temp, name);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        int b2 = arg0.getModifiers();
        b2 &= InputEvent.BUTTON3_MASK;
        if (b2 != 0) {
            JPopupMenu popup = new JPopupMenu();
            JMenuItem splitHorizontal = new JMenuItem("Split Horizontal");
            JMenuItem splitVertical = new JMenuItem("Split Vertical");
            JMenuItem joinLT = new JMenuItem("Join Left/Top");
            JMenuItem joinRB = new JMenuItem("Join Right/Bottom");
            final MouseEvent e = arg0;
            splitHorizontal.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    divideHorizontal();
                }
            });
            splitVertical.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    divideVertical();
                }
            });
            joinLT.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    joinPanelsLeftTop();
                }
            });
            joinRB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    joinPanelsRightBottom();
                }
            });
            popup.add(splitHorizontal);
            popup.add(splitVertical);
            popup.add(joinLT);
            popup.add(joinRB);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals(POP_OUT)) {
            JFrame f = new JFrame("Sandbox " + ++ctr);
            ResizePanel r = new ResizePanel(res);
            r.setSelected(type.getSelectedIndex());
            f.add(r);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setSize(500, 500);
            f.setVisible(true);
        }
        centerLayout.show(center, (String) type.getSelectedItem());
    }

    private void divideHorizontal() {
        right = new ResizePanel(res);
        left = new ResizePanel(res);
        right.setSelected(type.getSelectedIndex());
        left.setSelected(type.getSelectedIndex());
        s = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        removeAll();
        center.add(s, "center");
        super.add(center, BorderLayout.CENTER);
        centerLayout.show(center, "center");
        Container c = getParent();
        while (c != null) {
            c.repaint();
            c = c.getParent();
        }
    }

    private void divideVertical() {
        right = new ResizePanel(res);
        left = new ResizePanel(res);
        right.setSelected(type.getSelectedIndex());
        left.setSelected(type.getSelectedIndex());
        s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, left, right);
        removeAll();
        center.add(s, "center");
        super.add(center, BorderLayout.CENTER);
        centerLayout.show(center, "center");
    }

    /**Call to join two panels using the left panel
	 * 
	 */
    public void joinPanelsLeftTop() {
        try {
            ((ResizePanel) (getParent().getParent().getParent())).realJoinLeftTop();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Not enough panels", "Not enough panels", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**Does the actual joining of two elements
	 * 
	 */
    private void realJoinLeftTop() {
        removeAll();
        center.add(left, "center");
        super.add(center, BorderLayout.CENTER);
        centerLayout.show(center, "center");
    }

    public void joinPanelsRightBottom() {
        try {
            ((ResizePanel) (getParent().getParent().getParent())).realJoinRightBottom();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Not enough panels", "Not enough panels", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realJoinRightBottom() {
        removeAll();
        center.add(right, "center");
        super.add(center, BorderLayout.CENTER);
        centerLayout.show(center, "center");
    }
}
