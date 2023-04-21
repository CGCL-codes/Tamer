package org.tranche.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.tranche.gui.util.GUIUtil;

/**
 * <p>Applies custom Tranche look to limited subset of JOptionPane interface.</p>
 * @author Bryan E. Smith - bryanesmith@gmail.com
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class GenericOptionPane {

    /**
     * Gives the user options to choose from.
     * @param parentComponent If null, will be centered on screen. Otherwise, will be centered over the parent component.
     */
    public static int showConfirmDialog(Component parentComponent, Object message) {
        return showConfirmDialog(parentComponent, message, "Select an Option", JOptionPane.YES_NO_CANCEL_OPTION);
    }

    /**
     * Gives the user options to choose from.
     * @param parentComponent If null, will be centered on screen. Otherwise, will be centered over the parent component.
     */
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        return showConfirmDialog(parentComponent, message, title, optionType, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Gives the user options to choose from.
     * @param parentComponent If null, will be centered on screen. Otherwise, will be centered over the parent component.
     */
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        return showConfirmDialog(parentComponent, message, title, optionType, messageType, new ImageIcon(Styles.IMAGE_FRAME_ICON));
    }

    /**
     * Gives the user options to choose from.
     * @param parentComponent If null, will be centered on screen. Otherwise, will be centered over the parent component.
     */
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon) {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GenericScrollPane scroll = new GenericScrollPane();
        scroll.add(panel);
        final GenericPopupFrame popup = new GenericPopupFrame(title, panel);
        GridBagConstraints gbc = new GridBagConstraints();
        ImagePanel imagePanel = null;
        boolean isImageFail = false;
        try {
            String image = "/org/tranche/gui/image/dialog-warning-100x100.gif";
            switch(messageType) {
                case JOptionPane.INFORMATION_MESSAGE:
                    image = "/org/tranche/gui/image/dialog-information-100x100.gif";
                    break;
                case JOptionPane.WARNING_MESSAGE:
                    image = "/org/tranche/gui/image/dialog-warning-100x100.gif";
                    break;
                case JOptionPane.ERROR_MESSAGE:
                    image = "/org/tranche/gui/image/dialog-error-100x100.gif";
                    break;
            }
            BufferedImage img = ImageIO.read(GenericOptionPane.class.getResourceAsStream(image));
            imagePanel = new ImagePanel(img);
            imagePanel.setSize(100, 100);
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(imagePanel, gbc);
            gbc.ipadx = 0;
            gbc.ipady = 0;
        } catch (Exception ex) {
            System.err.println("Failed to load image for GenericOptionPane: " + ex.getMessage());
            ex.printStackTrace();
            isImageFail = true;
        }
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JTextArea msgArea = new JTextArea(message.toString());
        msgArea.setEditable(false);
        msgArea.setWrapStyleWord(true);
        msgArea.setLineWrap(false);
        msgArea.setFont(Styles.FONT_14PT);
        msgArea.setMargin(new Insets(15, 15, 15, 15));
        msgArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(msgArea);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        final int[] optionClicked = { JOptionPane.CANCEL_OPTION };
        if (optionType == JOptionPane.OK_CANCEL_OPTION) {
            JButton ok = new JButton(" OK ");
            ok.setFont(Styles.FONT_14PT_BOLD);
            ok.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    optionClicked[0] = JOptionPane.OK_OPTION;
                    popup.dispose();
                }
            });
            Border insideBorder = ok.getBorder();
            ok.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE), insideBorder));
            buttonPanel.add(ok);
        }
        if (optionType == JOptionPane.OK_CANCEL_OPTION || optionType == JOptionPane.YES_NO_CANCEL_OPTION) {
            JButton btn = new JButton(" Cancel ");
            btn.setFont(Styles.FONT_14PT_BOLD);
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    optionClicked[0] = JOptionPane.CANCEL_OPTION;
                    popup.dispose();
                }
            });
            Border insideBorder = btn.getBorder();
            btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE), insideBorder));
            buttonPanel.add(btn);
        }
        if (optionType == JOptionPane.YES_NO_OPTION || optionType == JOptionPane.YES_NO_CANCEL_OPTION) {
            JButton btn = new JButton(" Yes ");
            btn.setFont(Styles.FONT_14PT_BOLD);
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    optionClicked[0] = JOptionPane.YES_OPTION;
                    popup.dispose();
                }
            });
            Border insideBorder = btn.getBorder();
            btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE), insideBorder));
            buttonPanel.add(btn);
        }
        if (optionType == JOptionPane.YES_NO_OPTION || optionType == JOptionPane.YES_NO_CANCEL_OPTION) {
            JButton btn = new JButton(" No ");
            btn.setFont(Styles.FONT_14PT_BOLD);
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    optionClicked[0] = JOptionPane.NO_OPTION;
                    popup.dispose();
                }
            });
            Border insideBorder = btn.getBorder();
            btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, Color.WHITE), insideBorder));
            buttonPanel.add(btn);
        }
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(buttonPanel);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(Box.createVerticalStrut(10));
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(rightPanel, gbc);
        panel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        msgArea.setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        popup.setBackground(Color.WHITE);
        popup.setAlwaysOnTop(true);
        if (message.toString().length() > 100) {
            msgArea.setFont(Styles.FONT_14PT);
        }
        popup.pack();
        if (parentComponent == null) {
            popup.center();
        } else {
            popup.setLocationRelativeTo(parentComponent);
        }
        popup.setVisible(true);
        popup.setFocusable(true);
        if (!SwingUtilities.isEventDispatchThread()) {
            while (popup.isVisible()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            System.err.println("Don't call GenericOptionPane from the Event Dispatch Thread!");
        }
        return optionClicked[0];
    }

    /**
     * Brings up an information-message dialog titled "Message".
     * @param parentComponent If null, will be centered on screen. Otherwise, will be centered over the parent component.
     */
    public static void showMessageDialog(Component parentComponent, Object message) {
        showMessageDialog(parentComponent, message, "Please note", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @param parentComponent If null, will be centered on screen. Otherwise, will be centered over the parent component.
     */
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GenericScrollPane scroll = new GenericScrollPane();
        scroll.add(panel);
        final GenericPopupFrame popup = new GenericPopupFrame(title, panel);
        GridBagConstraints gbc = new GridBagConstraints();
        ImagePanel imagePanel = null;
        try {
            String image = "/org/tranche/gui/image/dialog-warning-100x100.gif";
            switch(messageType) {
                case JOptionPane.INFORMATION_MESSAGE:
                    image = "/org/tranche/gui/image/dialog-information-100x100.gif";
                    break;
                case JOptionPane.WARNING_MESSAGE:
                    image = "/org/tranche/gui/image/dialog-warning-100x100.gif";
                    break;
                case JOptionPane.ERROR_MESSAGE:
                    image = "/org/tranche/gui/image/dialog-error-100x100.gif";
                    break;
            }
            BufferedImage img = ImageIO.read(GenericOptionPane.class.getResourceAsStream(image));
            imagePanel = new ImagePanel(img);
            imagePanel.setSize(100, 100);
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(imagePanel, gbc);
            gbc.ipadx = 0;
            gbc.ipady = 0;
        } catch (Exception ex) {
            System.err.println("Failed to load image for GenericOptionPane: " + ex.getMessage());
            ex.printStackTrace();
        }
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JTextArea msgArea = new JTextArea(message.toString());
        msgArea.setEditable(false);
        msgArea.setWrapStyleWord(true);
        msgArea.setLineWrap(false);
        msgArea.setFont(Styles.FONT_14PT);
        msgArea.setMargin(new Insets(15, 15, 15, 15));
        msgArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(msgArea);
        JButton ok = new JButton(" OK ");
        ok.setFont(Styles.FONT_14PT_BOLD);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                popup.dispose();
            }
        });
        ok.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(ok);
        ok.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(Box.createVerticalStrut(10));
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(rightPanel, gbc);
        panel.setBackground(Color.WHITE);
        rightPanel.setBackground(Color.WHITE);
        msgArea.setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        popup.setBackground(Color.WHITE);
        popup.setAlwaysOnTop(true);
        if (message.toString().length() > 100) {
            msgArea.setFont(Styles.FONT_14PT);
        }
        popup.pack();
        if (parentComponent == null) {
            GUIUtil.centerOnScreen(popup);
        } else {
            popup.setLocationRelativeTo(parentComponent);
        }
        popup.setVisible(true);
        popup.setFocusable(true);
        popup.addKeyListener(new KeyAdapter() {

            /** Handle the key-pressed event from the text field. */
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == e.VK_ENTER) {
                    popup.dispose();
                }
            }
        });
        if (!SwingUtilities.isEventDispatchThread()) {
            while (popup.isVisible()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            System.err.println("Don't call GenericOptionPane from the Event Dispatch Thread!");
        }
    }
}
