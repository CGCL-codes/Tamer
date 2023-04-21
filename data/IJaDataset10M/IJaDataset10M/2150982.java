package com.magicpwd._util;

import com.magicpwd._comp.BtnLabel;
import com.magicpwd._cons.ConsEnv;
import java.awt.Dimension;

/**
 *
 * @author Amon
 */
public class Bean {

    private static javax.swing.ImageIcon bi_NoneIcon;

    private static java.util.Map<Integer, java.awt.image.BufferedImage> mp_LogoIcon;

    public static void setText(BtnLabel c, String t) {
        int i = -1;
        if (t.length() > 0) {
            i = t.indexOf(ConsEnv.CHAR_ALT_KEY);
            if (i >= 0) {
                t = t.replace(ConsEnv.CHAR_ALT_KEY, "");
                if (t.length() > i) {
                    c.setMnemonic(t.charAt(i));
                }
            }
        }
        if (!(i == 0 && t.length() == 1)) {
            c.setText(t);
            if (i >= 0) {
                c.setText(t);
                c.setDisplayedMnemonicIndex(i);
            }
        }
    }

    /**
     * 设置按钮的显示文本
     * @param c
     * @param wText
     * @param isHash
     */
    public static void setText(javax.swing.AbstractButton c, String t) {
        int i = -1;
        if (t.length() > 0) {
            i = t.indexOf(ConsEnv.CHAR_ALT_KEY);
            if (i >= 0) {
                t = t.replace(ConsEnv.CHAR_ALT_KEY, "");
                if (t.length() > i) {
                    c.setMnemonic(t.charAt(i));
                }
            }
        }
        if (!(i == 0 && t.length() == 1)) {
            c.setText(t);
            if (i >= 0) {
                c.setDisplayedMnemonicIndex(i);
            }
        }
    }

    public static void setText(javax.swing.JLabel c, String t) {
        int i = -1;
        if (t.length() > 0) {
            i = t.indexOf(ConsEnv.CHAR_ALT_KEY);
            if (i >= 0) {
                t = t.replace(ConsEnv.CHAR_ALT_KEY, "");
                if (t.length() > i) {
                    c.setDisplayedMnemonic(t.charAt(i));
                }
            }
        }
        if (!(i == 0 && t.length() == 1)) {
            c.setText(t);
            if (i >= 0) {
                c.setDisplayedMnemonicIndex(i);
            }
        }
    }

    public static void setTips(javax.swing.JComponent c, String t) {
        if ("".equals(t)) {
            t = null;
        }
        c.setToolTipText(t);
    }

    public static void registerPopupMenuAction(final javax.swing.JList list, final javax.swing.JPopupMenu menu) {
        if (list == null || menu == null) {
            return;
        }
        list.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                listPupupMenu(e, list, menu);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                listPupupMenu(e, list, menu);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                listPupupMenu(e, list, menu);
            }
        });
    }

    private static void listPupupMenu(java.awt.event.MouseEvent e, javax.swing.JList list, javax.swing.JPopupMenu menu) {
        if (e.isPopupTrigger()) {
            int row = list.locationToIndex(e.getPoint());
            if (row > -1) {
                list.setSelectedIndex(row);
            }
            menu.show(list, e.getX(), e.getY());
        }
    }

    public static void registerPopupMenuAction(final javax.swing.JTable table, final javax.swing.JPopupMenu menu) {
        if (table == null || menu == null) {
            return;
        }
        table.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tablePupupMenu(e, table, menu);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                tablePupupMenu(e, table, menu);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                tablePupupMenu(e, table, menu);
            }
        });
    }

    private static void tablePupupMenu(java.awt.event.MouseEvent e, javax.swing.JTable table, javax.swing.JPopupMenu menu) {
        if (e.isPopupTrigger()) {
            int row = table.rowAtPoint(e.getPoint());
            if (row > -1) {
                table.setRowSelectionInterval(row, row);
            }
            menu.show(table, e.getX(), e.getY());
        }
    }

    public static void registerKeyStrokeAction(javax.swing.JComponent component, javax.swing.KeyStroke stroke, javax.swing.Action action, String command, int condition) {
        command = (command != null) ? command : ((action != null) ? action.getValue(javax.swing.Action.NAME) + "" : "");
        javax.swing.InputMap inputMap = component.getInputMap(condition);
        if (inputMap != null) {
            inputMap.put(stroke, command);
            javax.swing.ActionMap actionMap = component.getActionMap();
            if (actionMap != null) {
                actionMap.put(command, action);
            }
        }
    }

    public static void closeReader(java.io.Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception exp) {
                Logs.exception(exp);
            }
        }
    }

    public static void closeWriter(java.io.Writer writer) {
        if (writer != null) {
            try {
                writer.flush();
            } catch (Exception exp) {
                Logs.exception(exp);
            }
            try {
                writer.close();
            } catch (Exception exp) {
                Logs.exception(exp);
            }
        }
    }

    public static void closeStream(java.io.InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception exp) {
                Logs.exception(exp);
            }
        }
    }

    public static void closeStream(java.io.OutputStream stream) {
        if (stream != null) {
            try {
                stream.flush();
            } catch (Exception exp) {
                Logs.exception(exp);
            }
            try {
                stream.close();
            } catch (Exception exp) {
                Logs.exception(exp);
            }
        }
    }

    public static synchronized javax.swing.ImageIcon getNone() {
        if (bi_NoneIcon == null) {
            bi_NoneIcon = new javax.swing.ImageIcon(createNone(16));
        }
        return bi_NoneIcon;
    }

    public static synchronized java.awt.image.BufferedImage getLogo(int size) {
        if (mp_LogoIcon == null) {
            mp_LogoIcon = new java.util.HashMap<Integer, java.awt.image.BufferedImage>();
        }
        java.awt.image.BufferedImage logo = mp_LogoIcon.get(size);
        if (logo == null) {
            try {
                java.io.InputStream stream = Util.class.getResourceAsStream(ConsEnv.RES_ICON + Char.lPad(Integer.toHexString(size), 4, '0') + ".png");
                logo = javax.imageio.ImageIO.read(stream);
                stream.close();
            } catch (Exception exp) {
                Logs.exception(exp);
                logo = createLogo(size);
            }
            mp_LogoIcon.put(size, logo);
        }
        return logo;
    }

    public static javax.swing.ImageIcon readIcon(String path) {
        if (!Char.isValidate(path)) {
            return null;
        }
        Logs.log("Icon:" + path);
        java.io.InputStream stream = null;
        try {
            stream = File.open4Read(path);
            return stream != null ? new javax.swing.ImageIcon(javax.imageio.ImageIO.read(stream)) : null;
        } catch (Exception exp) {
            Logs.exception(exp);
            return null;
        } finally {
            closeStream(stream);
        }
    }

    private static java.awt.image.BufferedImage createNone(int size) {
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        return bi;
    }

    private static java.awt.image.BufferedImage createLogo(int size) {
        java.awt.image.BufferedImage bi = createNone(size);
        return bi;
    }

    public static void loadJar(java.io.File... files) throws Exception {
        if (files == null || files.length < 1) {
            return;
        }
        java.net.URLClassLoader loader = (java.net.URLClassLoader) ClassLoader.getSystemClassLoader();
        java.lang.reflect.Method method = java.net.URLClassLoader.class.getDeclaredMethod("addURL", java.net.URL.class);
        method.setAccessible(true);
        for (java.io.File file : files) {
            method.invoke(loader, file.toURI().toURL());
        }
    }

    public static void centerForm(java.awt.Window form, java.awt.Window root) {
        Dimension d = (root != null ? root.getSize() : java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        Dimension s = form.getSize();
        form.setLocation((d.width - s.width) >> 1, (d.height - s.height) >> 1);
        form.setLocationRelativeTo(root);
    }
}
