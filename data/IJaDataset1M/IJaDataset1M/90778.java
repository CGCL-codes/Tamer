package org.opencdspowered.opencds.ui.util;

import org.opencdspowered.opencds.core.util.DesktopIntegration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.nio.channels.*;

/**
 * The interface util class.
 *
 * @author  Lars 'Levia' Wesselius
*/
public class Util {

    private static Timer m_Timer;

    private static File m_File;

    private static FileChannel m_Channel;

    private static FileLock m_Lock;

    /**
     * Create .lock file. This ensure only one application instance exists.
     */
    public static void lock() {
        try {
            m_File = new File(".lock");
            m_Channel = new RandomAccessFile(m_File, "rw").getChannel();
            m_Lock = m_Channel.tryLock();
            if (null == m_Lock) System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove .lock file. Delete .lock file once user stop the application.
     */
    public static void unlock() {
        try {
            m_Lock.release();
            m_Channel.close();
            m_File.delete();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JLabel createPanelHeader(String text) {
        JLabel header = new JLabel("   " + text);
        header.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        header.setOpaque(true);
        header.setMinimumSize(new Dimension(500, 25));
        header.setMaximumSize(new Dimension(500, 25));
        header.setBackground(new Color(49, 106, 197));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        return header;
    }

    /**
     * Linkifies a label. This method sets the color, and changes it's cursor
     *  to the standard link cursor.
     *
     * @param   label       The label to linkify.
     * @param   underline   Whether to underline the link or not.
    */
    public static void linkify(JLabel label, boolean underline) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(Color.blue);
        if (underline) {
            label.setText("<html><u>" + label.getText() + "</u></html>");
        }
    }

    /**
     * Linkifies a label. This method sets the color, and changes it's cursor
     *  to the standard link cursor, and tries to open the link when clicked.
     *
     * @param   label       The label to linkify.
     * @param   underline   Whether to underline the link or not.
     * @param   link        Opens this link when clicked.
    */
    public static void linkify(JLabel label, boolean underline, final String link) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(Color.blue);
        if (underline) {
            label.setText("<html><u>" + label.getText() + "</u></html>");
        }
        label.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent event) {
                DesktopIntegration.browse(java.net.URI.create(link));
            }

            public void mousePressed(MouseEvent event) {
            }

            public void mouseReleased(MouseEvent event) {
            }

            public void mouseExited(MouseEvent event) {
            }

            public void mouseEntered(MouseEvent event) {
            }
        });
    }

    /**
     * Linkifies a label. This method sets the color, and changes it's cursor
     *  to the standard link cursor, and tries to open the link when clicked.
     *
     * @param   label       The label to linkify.
     * @param   underline   Whether to underline the link or not.
     * @param   colorize    Whether to colorize the link or not.
     * @param   link        Opens this link when clicked.
    */
    public static void linkify(JLabel label, boolean underline, boolean colorize, final String link) {
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (colorize) {
            label.setForeground(Color.blue);
        }
        if (underline) {
            label.setText("<html><u>" + label.getText() + "</u></html>");
        }
        label.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent event) {
                DesktopIntegration.browse(java.net.URI.create(link));
            }

            public void mousePressed(MouseEvent event) {
            }

            public void mouseReleased(MouseEvent event) {
            }

            public void mouseExited(MouseEvent event) {
            }

            public void mouseEntered(MouseEvent event) {
            }
        });
    }

    /**
     * Flashes a component in given color. The border of the component flashes
     *  to the color given and is then set back to it's old border. It does
     *  this 7 times, and changes it's state every 150 miliseconds.
     *
     * @param   comp    The component to flash.
     * @param   color   The color to use when flashing.
    */
    public static void flashComponent(final JComponent comp, final Color color) {
        final Border oldBorder = comp.getBorder();
        m_Timer = new Timer(150, new ActionListener() {

            private int count = 0;

            public void actionPerformed(ActionEvent evt) {
                if (count != 7) {
                    if (!comp.getBorder().equals(oldBorder)) {
                        ++count;
                        comp.setBorder(oldBorder);
                    } else {
                        comp.setBorder(BorderFactory.createLineBorder(color));
                    }
                } else {
                    m_Timer.stop();
                }
            }
        });
        m_Timer.start();
    }
}
