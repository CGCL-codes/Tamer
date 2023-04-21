package coopnetclient.utils;

import coopnetclient.frames.components.ui.CustomScrollBarUI;
import coopnetclient.frames.listeners.TabbedPaneColorChangeListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.text.JTextComponent;

public class Colorizer {

    private static Color bgColor, fgColor, btnbgColor, tfbgColor, disabledColor, selectionColor, borderColor;

    private static Vector<Component> toExecuteCustomCodeIn;

    private static final int BTN_DIFF = 20;

    private static final int TF_DIFF = 10;

    private static final int DISABLED_DIFF = 75;

    private static Image frameIcon = Toolkit.getDefaultToolkit().getImage("data/icons/coopnet.png");

    static {
        initColors();
    }

    public static Color getBackgroundColor() {
        return bgColor;
    }

    public static Color getForegroundColor() {
        return fgColor;
    }

    public static Color getButtonBackgroundColor() {
        return btnbgColor;
    }

    public static Color getTextfieldBackgroundColor() {
        return tfbgColor;
    }

    public static Color getDisabledColor() {
        return disabledColor;
    }

    public static Color getSelectionColor() {
        return selectionColor;
    }

    public static Color getBorderColor() {
        return borderColor;
    }

    public static void colorize(Container root) {
        if (root instanceof JFrame) {
            JFrame frame = (JFrame) root;
            frame.setIconImage(frameIcon);
        }
        if (getCurrentLAFisSupportedForColoring() == false || root == null || (!Settings.getColorizeBody() && (root instanceof JFileChooser || root instanceof JDialog))) {
            return;
        }
        toExecuteCustomCodeIn = new Vector<Component>();
        if (root instanceof JFrame) {
            JFrame frame = (JFrame) root;
            toExecuteCustomCodeIn.add(frame);
            enableEffectsRecursively(frame.getContentPane());
            if (frame.getJMenuBar() != null) {
                enableEffectsRecursively(frame.getJMenuBar());
            }
        } else {
            enableEffectsRecursively(root);
        }
        executeCustomCode();
    }

    public static void initColors() {
        if (Settings.getColorizeBody()) {
            bgColor = coopnetclient.utils.Settings.getBackgroundColor();
            fgColor = coopnetclient.utils.Settings.getForegroundColor();
            selectionColor = coopnetclient.utils.Settings.getSelectionColor();
            borderColor = coopnetclient.utils.Settings.getForegroundColor();
        } else {
            bgColor = null;
            fgColor = null;
            selectionColor = null;
            borderColor = null;
        }
        if (bgColor != null) {
            int[] rgb = new int[3];
            rgb[0] = bgColor.getRed();
            rgb[1] = bgColor.getGreen();
            rgb[2] = bgColor.getBlue();
            for (int i = 0; i < rgb.length; i++) {
                if (rgb[i] >= 128) {
                    rgb[i] -= BTN_DIFF;
                } else {
                    rgb[i] += BTN_DIFF;
                }
            }
            btnbgColor = new Color(rgb[0], rgb[1], rgb[2]);
            rgb[0] = bgColor.getRed();
            rgb[1] = bgColor.getGreen();
            rgb[2] = bgColor.getBlue();
            for (int i = 0; i < rgb.length; i++) {
                if (rgb[i] >= 128) {
                    rgb[i] -= TF_DIFF;
                } else {
                    rgb[i] += TF_DIFF;
                }
            }
            tfbgColor = new Color(rgb[0], rgb[1], rgb[2]);
            rgb[0] = bgColor.getRed();
            rgb[1] = bgColor.getGreen();
            rgb[2] = bgColor.getBlue();
            for (int i = 0; i < rgb.length; i++) {
                if (rgb[i] >= 128) {
                    rgb[i] -= DISABLED_DIFF;
                } else {
                    rgb[i] += DISABLED_DIFF;
                }
            }
            disabledColor = new Color(rgb[0], rgb[1], rgb[2]);
        } else {
            btnbgColor = null;
            tfbgColor = null;
            disabledColor = null;
        }
        modifyUI();
    }

    public static void initLAF() {
        try {
            if (Settings.getUseNativeLookAndFeel()) {
                throw new Exception("Invoking catch stuff!");
            } else {
                String selectedLAF = Settings.getSelectedLookAndFeel();
                UIManager.LookAndFeelInfo infos[] = UIManager.getInstalledLookAndFeels();
                String selectedLAFClass = null;
                for (int i = 0; i < infos.length; i++) {
                    if (selectedLAF.equals(infos[i].getName())) {
                        selectedLAFClass = infos[i].getClassName();
                        break;
                    }
                }
                if (selectedLAFClass != null) {
                    UIManager.setLookAndFeel(selectedLAFClass);
                } else {
                    throw new Exception("LAF not found!");
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("SystemLAF not found! Will revert to CrossPlatformLAF.");
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception exc) {
                    System.err.println("CrossPlatformLAF not found!?");
                    ex.printStackTrace();
                }
            }
        }
    }

    public static boolean getCurrentLAFisSupportedForColoring() {
        if (UIManager.getLookAndFeel().getName().equals("Metal")) {
            return true;
        }
        return false;
    }

    public static boolean getLAFisSupportedForColoring(String LAFName) {
        if (LAFName.equals("Metal")) {
            return true;
        }
        return false;
    }

    private static void enableEffectsRecursively(Container root) {
        if (!(root instanceof JButton) && !(root instanceof JTextComponent)) {
            root.setForeground(fgColor);
            root.setBackground(bgColor);
        }
        toExecuteCustomCodeIn.add(root);
        Component[] components;
        if (root instanceof JMenuBar) {
            JMenuBar mbar = (JMenuBar) root;
            components = new Component[mbar.getMenuCount()];
            for (int i = 0; i < components.length; i++) {
                components[i] = mbar.getMenu(i);
            }
        } else if (root instanceof JMenu) {
            JMenu m = (JMenu) root;
            components = m.getMenuComponents();
        } else {
            components = root.getComponents();
        }
        for (Component c : components) {
            if (c instanceof JList) {
                JList lst = (JList) c;
                if (bgColor != null) {
                    lst.setSelectionBackground(selectionColor);
                    lst.setSelectionForeground(fgColor);
                } else {
                    lst.setSelectionBackground((Color) UIManager.get("List.selectionBackground"));
                    lst.setSelectionForeground((Color) UIManager.get("List.selectionForeground"));
                }
            } else if (c instanceof JTable) {
                JTable tbl = (JTable) c;
                tbl.setUI(new BasicTableUI());
                if (bgColor != null) {
                    tbl.setSelectionBackground(selectionColor);
                    tbl.setSelectionForeground(fgColor);
                } else {
                    tbl.setSelectionBackground((Color) UIManager.get("Table.selectionBackground"));
                    tbl.setSelectionForeground((Color) UIManager.get("Table.selectionForeground"));
                }
            } else if (c instanceof JMenu) {
                JMenu m = (JMenu) c;
                m.setUI(new BasicMenuUI());
            } else if (c instanceof JMenuItem) {
                if (c instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem) c;
                    cbmi.setUI(new BasicCheckBoxMenuItemUI());
                } else {
                    JMenuItem mi = (JMenuItem) c;
                    mi.setUI(new BasicMenuItemUI());
                }
            } else if (c instanceof JTabbedPane) {
                JTabbedPane tp = (JTabbedPane) c;
                tp.setUI(new MetalTabbedPaneUI());
                ChangeListener[] listeners = tp.getChangeListeners();
                for (ChangeListener cl : listeners) {
                    if (cl instanceof TabbedPaneColorChangeListener) {
                        tp.removeChangeListener(cl);
                        if (tp.getTabCount() > 0) {
                            for (int i = 0; i < tp.getTabCount(); i++) {
                                tp.setBackgroundAt(i, null);
                            }
                        }
                    }
                }
                if (bgColor != null) {
                    tp.addChangeListener(new TabbedPaneColorChangeListener(tp));
                }
            } else if (c instanceof JTextComponent) {
                JTextComponent tc = (JTextComponent) c;
                if (c instanceof JTextField) {
                    if (!(c.getParent().getClass().getName().contains("JSpinner"))) {
                        tc.setBorder(javax.swing.BorderFactory.createLineBorder(borderColor));
                    }
                }
                if (bgColor != null) {
                    tc.setForeground(fgColor);
                    tc.setBackground(tfbgColor);
                    tc.setCaretColor(fgColor);
                    tc.setSelectedTextColor(fgColor);
                    tc.setSelectionColor(selectionColor);
                } else {
                    tc.setForeground((Color) UIManager.get("TextArea.foreground"));
                    tc.setBackground((Color) UIManager.get("TextArea.background"));
                    tc.setCaretColor((Color) UIManager.get("TextArea.caretForeground"));
                    tc.setSelectedTextColor((Color) UIManager.get("TextArea.selectionForeground"));
                    tc.setSelectionColor((Color) UIManager.get("TextArea.selectionBackground"));
                }
            } else if (c instanceof JPanel) {
                JPanel pnl = (JPanel) c;
                Border b = pnl.getBorder();
                if (b != null && b instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder) b;
                    tb.setTitleColor(fgColor);
                    tb.setBorder(javax.swing.BorderFactory.createLineBorder(borderColor));
                }
            } else if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                sp.setBorder(javax.swing.BorderFactory.createLineBorder(borderColor));
            } else if (c instanceof JScrollBar) {
                JScrollBar sb = (JScrollBar) c;
                if (bgColor != null) {
                    sb.setUI(new CustomScrollBarUI());
                } else {
                    sb.setUI(new MetalScrollBarUI());
                }
            } else if (c instanceof JButton) {
                JButton btn = (JButton) c;
                btn.setForeground(fgColor);
                btn.setBackground(btnbgColor);
            } else if (c instanceof JComboBox) {
                JComboBox cb = (JComboBox) c;
                cb.setBorder(javax.swing.BorderFactory.createLineBorder(borderColor));
                cb.updateUI();
            } else if (c instanceof JSplitPane) {
                JSplitPane sp = (JSplitPane) c;
                setSplitPaneDividerColor(sp, bgColor);
            } else if (c instanceof JSpinner) {
                JSpinner spnr = (JSpinner) c;
                spnr.setBorder(BorderFactory.createLineBorder(borderColor));
            }
            toExecuteCustomCodeIn.add(c);
            if (c instanceof JList) {
                JList lst = (JList) c;
                if (lst.getComponentPopupMenu() != null) {
                    enableEffectsRecursively(lst.getComponentPopupMenu());
                }
            }
            if (c instanceof Container) {
                enableEffectsRecursively((Container) c);
            }
        }
    }

    private static void executeCustomCode() {
        for (int i = 0; i < toExecuteCustomCodeIn.size(); i++) {
            Component obj = toExecuteCustomCodeIn.get(i);
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals("customCodeForColorizer")) {
                    try {
                        m.invoke(obj);
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private static void modifyUI() {
        UIManager.put("TabbedPane.background", bgColor);
        UIManager.put("TabbedPane.borderHightlightColor", null);
        UIManager.put("TabbedPane.contentAreaColor", bgColor);
        UIManager.put("TabbedPane.darkShadow", null);
        UIManager.put("TabbedPane.focus", bgColor);
        UIManager.put("TabbedPane.foreground", fgColor);
        UIManager.put("TabbedPane.highlight", null);
        UIManager.put("TabbedPane.light", null);
        UIManager.put("TabbedPane.selectHighlight", null);
        UIManager.put("TabbedPane.selected", bgColor);
        UIManager.put("TabbedPane.shadow", null);
        UIManager.put("TabbedPane.tabAreaBackground", null);
        UIManager.put("TabbedPane.unselectedBackground", selectionColor);
        UIManager.put("Menu.acceleratorForeground", bgColor);
        UIManager.put("Menu.acceleratorSelectionForeground", fgColor);
        UIManager.put("Menu.background", bgColor);
        UIManager.put("Menu.disabledForeground", disabledColor);
        UIManager.put("Menu.foreground", fgColor);
        UIManager.put("Menu.selectionBackground", selectionColor);
        UIManager.put("Menu.selectionForeground", fgColor);
        UIManager.put("MenuItem.acceleratorForeground", null);
        UIManager.put("MenuItem.acceleratorSelectionForeground", null);
        UIManager.put("MenuItem.background", bgColor);
        UIManager.put("MenuItem.disabledForeground", disabledColor);
        UIManager.put("MenuItem.foreground", fgColor);
        UIManager.put("MenuItem.selectionBackground", selectionColor);
        UIManager.put("MenuItem.selectionForeground", fgColor);
        UIManager.put("TableHeader.background", bgColor);
        UIManager.put("TableHeader.focusCellBackground", null);
        UIManager.put("TableHeader.foreground", fgColor);
        UIManager.put("Table.background", bgColor);
        UIManager.put("Table.dropCellBackground", null);
        UIManager.put("Table.dropLineColor", null);
        UIManager.put("Table.dropLineShortColor", null);
        UIManager.put("Table.focusCellBackground", null);
        UIManager.put("Table.focusCellForeground", null);
        UIManager.put("Table.foreground", fgColor);
        UIManager.put("Table.gridColor", null);
        UIManager.put("Table.selectionBackground", btnbgColor);
        UIManager.put("Table.selectionForeground", fgColor);
        UIManager.put("Table.sortIconColor", fgColor);
        UIManager.put("TitledBorder.titleColor", fgColor);
        UIManager.put("PopupMenu.background", bgColor);
        UIManager.put("PopupMenu.foreground", fgColor);
        UIManager.put("ComboBox.background", btnbgColor);
        UIManager.put("ComboBox.buttonBackground", btnbgColor);
        UIManager.put("ComboBox.buttonDarkShadow", null);
        UIManager.put("ComboBox.buttonHighlight", null);
        UIManager.put("ComboBox.buttonShadow", null);
        UIManager.put("ComboBox.disabledBackground", btnbgColor);
        UIManager.put("ComboBox.disabledForeground", disabledColor);
        UIManager.put("ComboBox.foreground", fgColor);
        UIManager.put("ComboBox.selectionBackground", selectionColor);
        UIManager.put("ComboBox.selectionForeground", fgColor);
        UIManager.put("Spinner.background", bgColor);
        UIManager.put("Spinner.foreground", fgColor);
        UIManager.put("ToolTip.background", tfbgColor);
        UIManager.put("ToolTip.backgroundInactive", tfbgColor);
        UIManager.put("ToolTip.foreground", fgColor);
        UIManager.put("ToolTip.foregroundInactive", disabledColor);
        UIManager.put("Button.background", btnbgColor);
        UIManager.put("Button.darkShadow", null);
        UIManager.put("Button.disabledText", disabledColor);
        UIManager.put("Button.disabledToolBarBorderBackground", disabledColor);
        UIManager.put("Button.focus", null);
        UIManager.put("Button.foreground", fgColor);
        UIManager.put("Button.highlight", null);
        UIManager.put("Button.light", null);
        UIManager.put("Button.select", selectionColor);
        UIManager.put("Button.shadow", null);
        UIManager.put("Button.toolBarBorderBackground", borderColor);
        UIManager.put("Panel.background", bgColor);
        UIManager.put("Panel.foreground", fgColor);
        UIManager.put("OptionPane.background", bgColor);
        UIManager.put("OptionPane.errorDialog.border.background", borderColor);
        UIManager.put("OptionPane.errorDialog.titlePane.background", bgColor);
        UIManager.put("OptionPane.errorDialog.titlePane.foreground", fgColor);
        UIManager.put("OptionPane.errorDialog.titlePane.shadow", null);
        UIManager.put("OptionPane.foreground", fgColor);
        UIManager.put("OptionPane.messageForeground", fgColor);
        UIManager.put("OptionPane.questionDialog.border.background", borderColor);
        UIManager.put("OptionPane.questionDialog.titlePane.background", bgColor);
        UIManager.put("OptionPane.questionDialog.titlePane.foreground", fgColor);
        UIManager.put("OptionPane.questionDialog.titlePane.shadow", null);
        UIManager.put("OptionPane.warningDialog.border.background", borderColor);
        UIManager.put("OptionPane.warningDialog.titlePane.background", bgColor);
        UIManager.put("OptionPane.warningDialog.titlePane.foreground", fgColor);
        UIManager.put("OptionPane.warningDialog.titlePane.shadow", null);
    }

    private static void setSplitPaneDividerColor(JSplitPane splitPane, Color newDividerColor) {
        SplitPaneUI splitUI = splitPane.getUI();
        if (splitUI instanceof BasicSplitPaneUI) {
            BasicSplitPaneDivider div = ((BasicSplitPaneUI) splitUI).getDivider();
            assert div != null;
            Border divBorder = div.getBorder();
            Border newBorder = null;
            Border colorBorder = null;
            class BGBorder implements Border {

                private Color color;

                private final Insets NO_INSETS = new Insets(0, 0, 0, 0);

                private BGBorder(Color color) {
                    this.color = color;
                }

                Rectangle r = new Rectangle();

                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    g.setColor(color);
                    g.fillRect(x, y, width, height);
                    if (c instanceof Container) {
                        Container cont = (Container) c;
                        for (int i = 0, n = cont.getComponentCount(); i < n; i++) {
                            Component comp = cont.getComponent(i);
                            comp.getBounds(r);
                            Graphics tmpg = g.create(r.x, r.y, r.width, r.height);
                            comp.paint(tmpg);
                            tmpg.dispose();
                        }
                    }
                }

                @Override
                public Insets getBorderInsets(Component c) {
                    return NO_INSETS;
                }

                @Override
                public boolean isBorderOpaque() {
                    return true;
                }
            }
            colorBorder = new BGBorder(newDividerColor);
            if (divBorder == null) {
                newBorder = colorBorder;
            } else {
                newBorder = BorderFactory.createCompoundBorder(null, colorBorder);
            }
            div.setBorder(newBorder);
        }
    }
}
