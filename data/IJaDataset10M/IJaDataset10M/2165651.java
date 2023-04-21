package jalview.gui;

import java.applet.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import jalview.datamodel.*;
import jalview.gui.event.*;
import javax.swing.*;

public class IdPanel extends JPanel implements MouseListener, MouseMotionListener {

    protected IdCanvas idCanvas;

    protected int offy;

    public int width;

    public int lastid;

    protected AlignViewport av;

    protected Controller controller;

    public IdPanel(AlignViewport av, Controller c) {
        this.av = av;
        this.controller = c;
        componentInit();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void componentInit() {
        idCanvas = new IdCanvas(av, controller);
        setLayout(new BorderLayout());
        add("Center", idCanvas);
        idCanvas.addMouseListener(this);
        idCanvas.addMouseMotionListener(this);
    }

    public Dimension minimumSize() {
        return idCanvas.minimumSize();
    }

    public Dimension preferredSize() {
        return idCanvas.preferredSize();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        try {
            int seq = av.getIndex(y);
            if (seq != lastid) {
                DrawableSequence pickedSeq = av.getAlignment().getDrawableSequenceAt(seq);
                controller.handleStatusEvent(new StatusEvent(this, "Sequence ID : " + pickedSeq.getName() + " (" + seq + ")", StatusEvent.INFO));
                if (av.getSelection().contains(pickedSeq)) {
                    System.out.println("Selected " + seq);
                    av.getSelection().removeElement(pickedSeq);
                    fireSequenceSelectionEvent(av.getSelection());
                } else {
                    av.getSelection().addElement(pickedSeq);
                    fireSequenceSelectionEvent(av.getSelection());
                }
                idCanvas.paintFlag = true;
                idCanvas.repaint();
            }
            lastid = seq;
        } catch (Exception ex) {
        }
        return;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void fireSequenceSelectionEvent(Selection sel) {
        controller.handleSequenceSelectionEvent(new SequenceSelectionEvent(this, sel));
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int seq = av.getIndex(y);
        if (Config.DEBUG) System.out.println("Y = " + y + " " + seq + " " + av.getAlignment().getDrawableSequenceAt(seq).getName());
        try {
            if ((e.getModifiers() & Event.META_MASK) != 0) {
                try {
                    if (seq != -1) {
                        String id = av.getAlignment().getDrawableSequenceAt(seq).getName();
                        if (id.indexOf("/") != -1) {
                            id = id.substring(0, id.indexOf("/"));
                        }
                    }
                    System.out.println("NOTE: META_MASK mouse press commented out");
                    System.out.println("NOTE: Browser code commented out");
                } catch (Exception ex) {
                    System.out.println("Exception : " + ex);
                }
            } else {
                lastid = seq;
                if (seq != -1) {
                    DrawableSequence pickedSeq = av.getAlignment().getDrawableSequenceAt(seq);
                    controller.handleStatusEvent(new StatusEvent(this, "Sequence ID : " + pickedSeq.getName() + " (" + seq + ")", StatusEvent.INFO));
                    if (av.getSelection().contains(pickedSeq)) {
                        av.getSelection().removeElement(pickedSeq);
                        fireSequenceSelectionEvent(av.getSelection());
                    } else {
                        av.getSelection().addElement(pickedSeq);
                        fireSequenceSelectionEvent(av.getSelection());
                    }
                }
            }
        } catch (Exception ex) {
        }
        return;
    }

    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (Config.DEBUG) System.out.println("NOTE: IdPanel mouseReleased code commented out");
        return;
    }
}
