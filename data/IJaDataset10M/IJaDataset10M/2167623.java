package tjacobs.ui;

import java.awt.AWTEventMulticaster;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import tjacobs.ui.drag.Draggable;

/**
 * A component that draws a little speaking blurb with a string inside of it
 * 
 * This is the heavyweight version
 */
public class AWTQuote extends Canvas {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    String mTxt;

    String mContent = "";

    ActionListener mListener;

    boolean mXORMode = false;

    private Window mPopup;

    Container mParent;

    Component mTop;

    static Robot sRobot;

    static {
        try {
            sRobot = new Robot();
        } catch (AWTException ex) {
        }
    }

    static final Dimension DEFAULT_SIZE = new Dimension(200, 250);

    static Dimension PopupSize = DEFAULT_SIZE;

    public AWTQuote(Container parent, Component top) {
        super();
        mParent = parent;
        mTop = top;
        init();
    }

    public AWTQuote(String s, Container parent, Component top) {
        this(parent, top);
        setText(s);
    }

    public AWTQuote(GraphicsConfiguration arg0) {
        super(arg0);
        init();
    }

    private void init() {
        MouseAdapter ma = new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                if (mListener != null) {
                    ActionEvent ev = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, mTxt);
                    ;
                    mListener.actionPerformed(ev);
                }
            }

            public void mousePressed(MouseEvent me) {
                mXORMode = true;
                repaint();
            }

            public void mouseReleased(MouseEvent me) {
                mXORMode = false;
                repaint();
            }
        };
        addMouseListener(ma);
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                showComment(false);
            }
        };
        addActionListener(al);
    }

    public void showComment(boolean editable) {
        if (mPopup != null) {
            mPopup.setVisible(true);
            return;
        }
        Window w = SwingUtilities.getWindowAncestor(AWTQuote.this);
        JDialog d = null;
        if (w instanceof Frame) {
            d = new JDialog((Frame) w, mTxt, false);
        } else {
            if (w instanceof Dialog) {
                d = new JDialog((Dialog) w, mTxt, false);
            }
        }
        mPopup = d;
        d.setUndecorated(true);
        new Draggable(d);
        Container cp = d.getContentPane();
        cp.setLayout(new BorderLayout());
        JScrollPane spane = new JScrollPane();
        JTextPane tp = new JTextPane();
        spane.getViewport().setView(tp);
        tp.setEditorKit(new HTMLEditorKit());
        tp.setText(mContent);
        tp.setEditable(editable);
        cp.add(spane, BorderLayout.CENTER);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton dismiss = new JButton("Dismiss");
        dismiss.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                SwingUtilities.getWindowAncestor((Component) ae.getSource()).dispose();
                mPopup = null;
                AWTQuote.this.getParent().remove(AWTQuote.this);
            }
        });
        p.add(dismiss);
        cp.add(p, BorderLayout.SOUTH);
        Point pt = AWTQuote.this.getLocationOnScreen();
        d.setBounds(pt.x + getWidth() + 10, pt.y, PopupSize.width, PopupSize.height);
        d.setVisible(true);
    }

    public void setText(String txt) {
        mTxt = txt;
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle rect = this.getBounds();
        Point loc = getLocationOnScreen();
        rect.x = loc.x + rect.width;
        rect.y = loc.y;
        Image im = sRobot.createScreenCapture(rect);
        g.drawImage(im, 0, 0, null);
        if (mXORMode) {
            g.setXORMode(Color.BLACK);
        }
        int width = getWidth();
        int height = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);
        g.setColor(getForeground());
        if (mXORMode) {
            g.setXORMode(Color.WHITE);
        }
        int wid = width / 10;
        int ht = height / 10;
        g.drawArc(wid / 2, ht / 2, wid * 9, ht * 9, 0, -30);
        g.drawArc(wid / 2, ht / 2, wid * 9, ht * 9, 0, 300);
        int ptx1 = (int) (width / 2 + wid * 18 / 4.0 * (Math.cos(30 * Math.PI / 180)));
        int pty1 = (int) (height / 2 + ht * 18 / 4.0 * (Math.sin(30 * Math.PI / 180)));
        int ptx2 = (int) (width / 2 + wid * 18 / 4.0 * (Math.cos(60 * Math.PI / 180)));
        int pty2 = (int) (height / 2 + ht * 18 / 4.0 * (Math.sin(60 * Math.PI / 180)));
        g.drawLine(ptx1, pty1, width, height);
        g.drawLine(ptx2, pty2, width, height);
        if (mTxt != null) {
            int x = (int) ((width / 2) * (1 - Math.sqrt(2) / 2));
            int y = (int) ((height / 2) + (height / 2 * Math.sqrt(2) / 2));
            int sqwidth = (int) (width * Math.sqrt(2) / 2);
            int sqheight = (y - height / 2) * 2;
            System.out.println("x = " + x + " y = " + y);
            Font f = g.getFont();
            FontMetrics fm = g.getFontMetrics();
            float pt = f.getSize();
            while (fm.stringWidth(mTxt) < sqwidth && fm.getHeight() < sqheight) {
                pt += 2;
                f = f.deriveFont(pt);
                fm = g.getFontMetrics(f);
            }
            int x_diff = sqwidth - fm.stringWidth(mTxt);
            int y_diff = sqheight - fm.getHeight();
            g.setFont(f);
            g.drawString(mTxt, x + x_diff / 2, y - y_diff / 2 - fm.getHeight() / 5);
        }
    }

    public void addActionListener(ActionListener a) {
        if (mListener == null) mListener = a; else AWTEventMulticaster.add(a, mListener);
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String s) {
        mContent = s;
    }

    public static void setPopupSize(Dimension d) {
        PopupSize = d;
    }
}
