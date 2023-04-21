package com.magicpwd._comp;

import com.magicpwd._cons.ConsEnv;
import com.magicpwd._util.Logs;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

/**
 * @author Amon
 * 
 */
public class BtnLabel extends JLabel {

    private String command;

    private static BufferedImage defImage;

    private static BufferedImage ovrImage;

    private static BufferedImage prsImage;

    private static BufferedImage disImage;

    private BufferedImage curImage;

    private java.awt.event.ActionListener listener;

    static {
        try {
            java.io.InputStream fis = BtnLabel.class.getResourceAsStream(ConsEnv.RES_ICON + "button_d.png");
            defImage = ImageIO.read(fis);
            fis.close();
        } catch (Exception exp) {
            Logs.exception(exp);
        }
    }

    public BtnLabel() {
        curImage = defImage;
        setHorizontalAlignment(CENTER);
        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thisMouseEntered(evt);
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                thisMousePressed(evt);
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fireActionPerformed();
                curImage = ovrImage;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                curImage = defImage;
                repaint();
            }
        });
    }

    public void addActionListener(final java.awt.event.ActionListener listener) {
        this.listener = listener;
        getActionMap().put("MagicPwdEvent", new javax.swing.AbstractAction() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionPerformed();
            }
        });
    }

    public void setActionCommand(String command) {
        this.command = command;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (disImage == null) {
            try {
                java.io.InputStream fis = BtnLabel.class.getResourceAsStream(ConsEnv.RES_ICON + "button_x.png");
                disImage = ImageIO.read(fis);
                fis.close();
            } catch (Exception exp) {
                disImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Logs.exception(exp);
            }
        }
    }

    public void setMnemonic(char mnemonic) {
        int vk = (int) mnemonic;
        if (vk >= 'a' && vk <= 'z') {
            vk -= ('a' - 'A');
        }
        setMnemonic(vk);
    }

    public void setMnemonic(int mnemonic) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_MASK), "MagicPwdEvent");
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        java.awt.Dimension size = super.getPreferredSize();
        size.width += 4;
        size.height += 3;
        return size;
    }

    @Override
    public java.awt.Dimension getSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (!isEnabled()) {
            paintBackground(g2d, disImage);
            return;
        }
        paintBackground(g2d, curImage);
        super.paintComponent(g);
    }

    private void paintBackground(Graphics2D g2d, BufferedImage image) {
        int gapx = 3;
        int gapy = 3;
        int bw = getWidth();
        int bh = getHeight();
        int iw = image.getWidth();
        int ih = image.getHeight();
        g2d.drawImage(image, 0, 0, gapx, gapy, 0, 0, gapx, gapy, this);
        g2d.drawImage(image, bw - gapx, 0, bw, gapy, iw - gapx, 0, iw, gapy, this);
        g2d.drawImage(image, gapx, 0, bw - gapx, gapy, gapx, 0, iw - gapx, gapy, this);
        g2d.drawImage(image, 0, gapy, gapx, bh - gapy, 0, gapy, gapx, ih - gapy, this);
        g2d.drawImage(image, bw - gapx, gapy, bw, bh - gapy, iw - gapx, gapy, iw, ih - gapy, this);
        g2d.drawImage(image, gapx, gapy, bw - gapx, bh - gapy, gapx, gapy, iw - gapx, ih - gapy, this);
        g2d.drawImage(image, 0, bh - gapy, gapx, bh, 0, ih - gapy, gapx, ih, this);
        g2d.drawImage(image, bw - gapx, bh - gapy, bw, bh, iw - gapx, ih - gapy, iw, ih, this);
        g2d.drawImage(image, gapx, bh - gapy, bw - gapx, bh, gapx, ih - gapy, iw - gapx, ih, this);
    }

    private void fireActionPerformed() {
        if (listener != null) {
            java.awt.event.ActionEvent e = new java.awt.event.ActionEvent(this, 0, command);
            e.getActionCommand();
            listener.actionPerformed(e);
        }
    }

    private void thisMouseEntered(java.awt.event.MouseEvent evt) {
        if (ovrImage == null) {
            try {
                java.io.InputStream fis = BtnLabel.class.getResourceAsStream(ConsEnv.RES_ICON + "button_e.png");
                ovrImage = ImageIO.read(fis);
                fis.close();
            } catch (Exception exp) {
                ovrImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Logs.exception(exp);
            }
        }
        curImage = ovrImage;
    }

    private void thisMousePressed(java.awt.event.MouseEvent evt) {
        if (prsImage == null) {
            try {
                java.io.InputStream fis = BtnLabel.class.getResourceAsStream(ConsEnv.RES_ICON + "button_p.png");
                prsImage = ImageIO.read(fis);
                fis.close();
            } catch (Exception exp) {
                prsImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Logs.exception(exp);
            }
        }
        curImage = prsImage;
    }
}
