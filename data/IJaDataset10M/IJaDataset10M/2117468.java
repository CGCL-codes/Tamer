package quizcards;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class SoundImage extends JPanel implements ActionListener, ImageObserver, MouseListener, MouseMotionListener {

    private JMenuItem changeImage = new JMenuItem("Change Image File...");

    private JMenuItem changeSound = new JMenuItem("Change Sound File...");

    private Vector curxvec = null;

    private Vector curyvec = null;

    private JMenuItem deleteImage = new JMenuItem("Remove Image");

    private JMenuItem deleteSound = new JMenuItem("Remove Sound");

    private boolean edit_mode;

    public Image image;

    public String imageFile;

    private JPopupMenu imageMenu = new JPopupMenu();

    private boolean isVisible = true;

    private int lastx, lasty;

    private JMenuItem playSound = new JMenuItem("Play Sound");

    private static Image sound;

    public String soundFile;

    private boolean soundFlag = false;

    private JPopupMenu soundMenu = new JPopupMenu();

    private MediaTracker tracker;

    private final Vector xstrokes = new Vector();

    private final Vector ystrokes = new Vector();

    public SoundImage() {
        setBackground(Color.white);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        imageMenu.add(changeImage);
        imageMenu.add(deleteImage);
        soundMenu.add(playSound);
        soundMenu.add(changeSound);
        soundMenu.add(deleteSound);
        tracker = new MediaTracker(this);
        changeImage.addActionListener(this);
        changeSound.addActionListener(this);
        deleteImage.addActionListener(this);
        deleteSound.addActionListener(this);
        playSound.addActionListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        UndoableEditEvent e;
        Object source = event.getSource();
        Stack stack = QuizCards.w.stack;
        if (source == changeImage) {
            FileDialog fd = new FileDialog(QuizCards.w, "Choose an image file");
            fd.setFile(imageFile);
            fd.show();
            if (fd.getFile() != null) {
                e = new UndoableEditEvent(this, new EditImage(this, imageFile, fd.getFile()));
                stack.undoableEditHappened(e);
                imageFile = fd.getFile();
                image = getToolkit().getImage(imageFile);
                tracker.addImage(image, 1);
                try {
                    tracker.waitForID(1);
                } catch (InterruptedException e2) {
                }
                tracker.removeImage(image);
                repaint();
            }
        } else if (source == deleteImage) {
            e = new UndoableEditEvent(this, new EditImage(this, imageFile, ""));
            stack.undoableEditHappened(e);
            imageFile = "";
            repaint();
        } else if (source == changeSound) {
            FileDialog fd = new FileDialog(QuizCards.w, "Choose a sound file");
            fd.setFile(soundFile);
            fd.show();
            if (fd.getFile() != null) {
                e = new UndoableEditEvent(this, new EditSound(this, soundFile, fd.getFile()));
                stack.undoableEditHappened(e);
                soundFile = fd.getFile();
            }
        } else if (source == deleteSound) {
            e = new UndoableEditEvent(this, new EditSound(this, soundFile, ""));
            stack.undoableEditHappened(e);
            soundFile = "";
        } else {
            playSound();
        }
    }

    public boolean imageUpdate(Image im, int flags, int x, int y, int width, int height) {
        if ((flags & (ImageObserver.FRAMEBITS | ImageObserver.ALLBITS)) > 0) {
            repaint();
            return false;
        }
        return true;
    }

    public void mouseClicked(MouseEvent event) {
        if (isVisible) {
            Dimension d = getSize();
            if (!edit_mode && soundFlag && event.getX() >= d.width - 32 && event.getX() <= d.width - 16 && event.getY() >= d.height - 32 && event.getY() <= d.height - 16) {
                playSound();
            }
        } else if (event.getClickCount() > 1) {
            xstrokes.removeAllElements();
            ystrokes.removeAllElements();
            curxvec = null;
            curyvec = null;
            repaint();
        }
    }

    public void mouseDragged(MouseEvent event) {
        if (!isVisible) {
            int x, y;
            x = event.getX();
            y = event.getY();
            curxvec.addElement(new Integer(x));
            curyvec.addElement(new Integer(y));
            getGraphics().drawLine(lastx, lasty, x, y);
            lastx = x;
            lasty = y;
        }
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseMoved(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
        if (edit_mode) {
            Dimension d = getSize();
            int x = event.getX();
            int y = event.getY();
            if (x >= d.width - 32 && x <= d.width - 16 && y >= d.height - 32 && y <= d.height - 16) {
                if (soundFlag) {
                    if (soundFile == null || soundFile.length() == 0) {
                        deleteSound.setEnabled(false);
                        playSound.setEnabled(false);
                    } else {
                        deleteSound.setEnabled(true);
                        playSound.setEnabled(true);
                    }
                    soundMenu.show(this, x, y);
                }
            } else {
                if (imageFile == null || imageFile.length() == 0) {
                    deleteImage.setEnabled(false);
                } else {
                    deleteImage.setEnabled(true);
                }
                imageMenu.show(this, x, y);
            }
        } else if (!isVisible) {
            curxvec = new Vector();
            curyvec = new Vector();
            xstrokes.addElement(curxvec);
            ystrokes.addElement(curyvec);
            lastx = event.getX();
            lasty = event.getY();
            curxvec.addElement(new Integer(lastx));
            curyvec.addElement(new Integer(lasty));
        }
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        if (isVisible) {
            Image i = createImage(d.width, d.height);
            Graphics ig = i.getGraphics();
            if (sound == null) sound = getToolkit().getImage(ClassLoader.getSystemResource("sound.gif"));
            ig.setColor(getBackground());
            ig.fillRect(0, 0, d.width, d.height);
            if (imageFile.length() > 0 && image != null) {
                int halfheight = image.getHeight(this) / 2;
                int halfwidth = image.getWidth(this) / 2;
                ig.drawImage(image, d.width / 2 - halfwidth, d.height / 2 - halfheight, this);
            }
            if (soundFlag && (edit_mode || (soundFile != null && soundFile.length() > 0))) ig.drawImage(sound, d.width - 32, d.height - 32, this);
            paintBorder(ig);
            g.drawImage(i, 0, 0, this);
        } else {
            Enumeration xe = xstrokes.elements();
            Enumeration ye = ystrokes.elements();
            g.setColor(getBackground());
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(getForeground());
            while (xe.hasMoreElements()) {
                int x, y;
                int lastx = -1, lasty = -1;
                Enumeration xe2 = ((Vector) xe.nextElement()).elements();
                Enumeration ye2 = ((Vector) ye.nextElement()).elements();
                while (xe2.hasMoreElements()) {
                    x = ((Integer) xe2.nextElement()).intValue();
                    y = ((Integer) ye2.nextElement()).intValue();
                    if (lastx != -1) g.drawLine(lastx, lasty, x, y);
                    lastx = x;
                    lasty = y;
                }
            }
        }
    }

    public boolean playSound() {
        if (soundFile != null && soundFile.length() > 0) {
            try {
                AudioApp ac = new AudioApp(soundFile);
                ac.play();
            } catch (IOException e) {
            }
            return true;
        } else {
            return false;
        }
    }

    public void setEditMode(boolean edit_mode) {
        this.edit_mode = edit_mode;
    }

    public void setImage(Image image, String imageFile) {
        this.image = image;
        this.imageFile = imageFile;
        repaint();
    }

    public void setSound(String soundFile) {
        this.soundFile = soundFile;
    }

    public void setSoundFlag(boolean soundFlag) {
        this.soundFlag = soundFlag;
    }

    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;
        if (!isVisible) {
            xstrokes.removeAllElements();
            ystrokes.removeAllElements();
            curxvec = null;
            curyvec = null;
        }
        repaint();
    }

    public void update(Graphics g) {
        paint(g);
    }
}
