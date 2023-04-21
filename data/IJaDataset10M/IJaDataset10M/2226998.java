package sizepanel;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;

/**
 * <p>Title: Movable Mouse Panel</p>
 * <p>Description: A panel that enlarges and tells surrounding panels to sift</p>
 * <p>Copyright: Copyright (c) Group Xtreme 2004</p>
 * @author Brendan Kowitz
 * @version 1.01
 */
public class MPanel extends JPanel {

    public static final int MOVE_LEFT = 0;

    public static final int MOVE_RIGHT = 1;

    public static final int MOVE_LCENTRE = 2;

    public static final int MOVE_RCENTRE = 7;

    public static final int MOVE_DONE = 3;

    public static final int SIZE_GROW = 4;

    public static final int SIZE_SMALL = 5;

    public static final int SIZE_DONE = 6;

    protected Rectangle oRect;

    protected XYConstraints xr;

    protected ImageIcon picture = null;

    protected JLabel lblPicture = null;

    public int value = 0;

    protected int sizelimit = 10;

    protected int sizeupto = 0;

    protected int moveupto = 0;

    private boolean goBig = false;

    private boolean goBack = false;

    private int movestate;

    private int sizestate;

    protected MPanel previous = null;

    protected MPanel next = null;

    private java.util.Timer timer = null;

    class jPanel_mouseAdapter extends java.awt.event.MouseAdapter {

        MPanel adaptee;

        jPanel_mouseAdapter(MPanel adaptee) {
            this.adaptee = adaptee;
        }

        public void mouseEntered(MouseEvent e) {
            adaptee.jPanel_mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            adaptee.jPanel_mouseExited(e);
        }

        public void mouseClicked(MouseEvent e) {
            adaptee.jPanel_MouseClicked(e);
        }
    }

    public MPanel(XYConstraints con) {
        this.addMouseListener(new jPanel_mouseAdapter(this));
        movestate = MOVE_DONE;
        sizestate = SIZE_DONE;
        timer = new java.util.Timer();
        timer.schedule(new TimerElement(this), 100);
        xr = con;
        this.setBackground(new java.awt.Color(0, 0, 0));
    }

    public void addImage(Image img) {
        picture = new ImageIcon(img);
        lblPicture = new JLabel(picture);
        this.setLayout(new BorderLayout());
        this.add(lblPicture, BorderLayout.NORTH);
        lblPicture.setSize(xr.getWidth(), xr.getHeight() - 5);
        sizeupto = 5;
        sizestate = SIZE_SMALL;
        this.updateUI();
        this.invalidate();
    }

    public void makeActive() {
        this.setBackground(new java.awt.Color(255, 0, 0));
    }

    public void makeNotActive() {
        this.setBackground(new java.awt.Color(0, 0, 0));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newval) {
        value = newval;
    }

    class TimerElement extends TimerTask {

        MPanel Own;

        public TimerElement(MPanel owner) {
            Own = owner;
        }

        public void run() {
            Own.timer.schedule(new TimerElement(Own), 20);
            if (Own.sizestate == SIZE_GROW) {
                moveupto = 0;
                if (previous != null) previous.setMoveState(MOVE_LEFT);
                if (next != null) next.setMoveState(MOVE_RIGHT);
                if (sizeupto < sizelimit) {
                    Own.setLocation(xr.getX() - sizeupto + moveupto, xr.getY() - sizeupto);
                    Own.setSize(xr.getWidth() + (sizeupto * 2), xr.getHeight() + (sizeupto * 2));
                    if (lblPicture != null) {
                        lblPicture.setSize(xr.getWidth() + (sizeupto * 2), xr.getHeight() + (sizeupto * 2) - 5);
                    }
                    sizeupto++;
                } else {
                    Own.sizestate = SIZE_DONE;
                }
            } else if (Own.sizestate == SIZE_SMALL) {
                movestate = MOVE_LCENTRE;
                if (sizeupto > 0) {
                    Own.setLocation(xr.getX() - sizeupto + moveupto, xr.getY() - sizeupto);
                    Own.setSize(xr.getWidth() + (sizeupto * 2), xr.getHeight() + (sizeupto * 2));
                    if (lblPicture != null) {
                        lblPicture.setSize(xr.getWidth() + (sizeupto * 2), xr.getHeight() + (sizeupto * 2) - 5);
                    }
                    sizeupto--;
                } else {
                    Own.sizestate = SIZE_DONE;
                }
            }
            switch(Own.movestate) {
                case MOVE_LEFT:
                    if (moveupto > 0 - xr.getWidth() / 1.7) {
                        Own.setLocation(xr.getX() + moveupto - sizeupto, xr.getY() - sizeupto);
                        moveupto += -2;
                    } else Own.movestate = MOVE_DONE;
                    break;
                case MOVE_RIGHT:
                    if (moveupto < xr.getWidth() / 1.7) {
                        Own.setLocation(xr.getX() + moveupto - sizeupto, xr.getY() - sizeupto);
                        moveupto += 2;
                    } else Own.movestate = MOVE_DONE;
                    break;
                case MOVE_LCENTRE:
                case MOVE_RCENTRE:
                    if (moveupto > 0) {
                        Own.setLocation(xr.getX() + moveupto + sizeupto, xr.getY() - sizeupto);
                        moveupto += -1;
                    } else if (moveupto < 0) {
                        Own.setLocation(xr.getX() + moveupto + sizeupto, xr.getY() - sizeupto);
                        moveupto += 1;
                    } else Own.movestate = MOVE_DONE;
                    break;
            }
        }
    }

    public void setPreviousPanel(MPanel prev) {
        previous = prev;
    }

    public MPanel getPrevious() {
        return previous;
    }

    public void setNextPanel(MPanel next1) {
        next = next1;
    }

    void jPanel_MouseClicked(MouseEvent e) {
    }

    void jPanel_mouseEntered(MouseEvent e) {
        sizestate = SIZE_GROW;
        movestate = MOVE_LCENTRE;
        if (previous != null) previous.setMoveState(MOVE_LEFT);
        if (next != null) next.setMoveState(MOVE_RIGHT);
    }

    void jPanel_mouseExited(MouseEvent e) {
        sizestate = SIZE_SMALL;
        movestate = MOVE_LCENTRE;
        if (previous != null) previous.setMoveState(MOVE_LCENTRE);
        if (next != null) next.setMoveState(MOVE_RCENTRE);
    }

    public void setMoveState(int newstate) {
        movestate = newstate;
        if (newstate == MOVE_LEFT) {
            if (previous != null) {
                previous.setMoveState(MOVE_LEFT);
            }
        } else if (newstate == MOVE_RIGHT) {
            if (next != null) {
                next.setMoveState(MOVE_RIGHT);
            }
        } else if (newstate == MOVE_LCENTRE) {
            if (previous != null) {
                previous.setMoveState(MOVE_LCENTRE);
            }
        } else if (newstate == MOVE_RCENTRE) {
            if (next != null) {
                next.setMoveState(MOVE_RCENTRE);
            }
        }
    }
}
