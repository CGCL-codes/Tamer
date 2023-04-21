package net.yura.mobile.gui.components;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.Graphics2D;
import net.yura.mobile.gui.Icon;
import net.yura.mobile.gui.KeyEvent;
import net.yura.mobile.gui.Midlet;
import net.yura.mobile.gui.border.Border;
import net.yura.mobile.gui.cellrenderer.ListCellRenderer;
import net.yura.mobile.gui.plaf.Style;

/**
 * @author Yura Mamyrin
 * @see javax.swing.JMenu
 */
public class Menu extends Button {

    private boolean useAnimation = true;

    private boolean open;

    private int slide = Graphics.BOTTOM;

    private int destX;

    private int destY;

    private Icon arrowDirection;

    public Menu() {
        popup = makePopup();
    }

    /**
         * @param string the text for the menu label
         * @see javax.swing.JMenu#JMenu(java.lang.String) JMenu.JMenu
         */
    public Menu(String string) {
        this();
        setText(string);
    }

    public void fireActionPerformed() {
        super.fireActionPerformed();
        setPopupMenuVisible(true);
    }

    /**
         * @param c The component to append to the menu
         * @see javax.swing.JMenu#add(java.awt.Component) JMenu.add
         */
    protected void addImpl(Component c, Object cons, int index) {
        getPopupMenu(popup).insert(c, index);
    }

    /**
         * @see javax.swing.JMenu#removeAll() JMenu.removeAll
         */
    public void removeAll() {
        getPopupMenu(popup).removeAll();
    }

    public Button findMneonicButton(int mnu) {
        return getPopupMenu(popup).findMneonicButton(mnu);
    }

    protected void workoutMinimumSize() {
        super.workoutMinimumSize();
        if (!isTopLevelMenu()) {
            width = width + (arrowDirection != null ? (arrowDirection.getIconWidth() + gap) : 0);
        }
    }

    public void paintComponent(Graphics2D g) {
        super.paintComponent(g);
        if (!isTopLevelMenu()) {
            if (arrowDirection != null) {
                arrowDirection.paintIcon(this, g, width - padding - arrowDirection.getIconWidth(), (height - arrowDirection.getIconHeight()) / 2);
            }
        }
    }

    public void setMenuRenderer(ListCellRenderer renderer) {
        getPopupMenu(popup).setCellRenderer(renderer);
    }

    public void updateUI() {
        super.updateUI();
        arrowDirection = (Icon) theme.getProperty("icon", Style.ALL);
    }

    /**
         * @see javax.swing.JMenu#addSeparator() JMenu.addSeparator
         */
    public void addSeparator() {
        add(makeSeparator());
    }

    /**
         * @see javax.swing.JMenu#getMenuComponents() JMenu.getMenuComponents
         */
    public Vector getMenuComponents() {
        MenuBar bar = getPopupMenu(popup);
        return bar.getItems();
    }

    /**
         * @see javax.swing.JSeparator
         */
    public static Component makeSeparator() {
        Label separator = new Label();
        separator.setPreferredSize(-1, 1);
        separator.setName("Separator");
        return separator;
    }

    /**
         * @see javax.swing.JPopupMenu
         */
    public static Window makePopup() {
        Window popup = new Window();
        popup.setCloseOnFocusLost(true);
        MenuBar menuItems = new MenuBar();
        menuItems.setLayoutOrientation(List.VERTICAL);
        menuItems.setLoop((!Boolean.FALSE.equals(DesktopPane.get("LOOP_MENU"))));
        popup.addWindowListener(menuItems);
        popup.add(new ScrollPane(menuItems));
        popup.setName("Menu");
        Button cancel = new Button((String) DesktopPane.get("cancelText"));
        cancel.setActionCommand(Frame.CMD_CLOSE);
        cancel.addActionListener(menuItems);
        cancel.setMnemonic(KeyEvent.KEY_END);
        popup.addCommand(cancel);
        if (Midlet.getPlatform() == Midlet.PLATFORM_ANDROID) {
            Button cancel2 = new Button((String) DesktopPane.get("menuText"));
            cancel2.setActionCommand(Frame.CMD_CLOSE);
            cancel2.addActionListener(menuItems);
            cancel2.setMnemonic(KeyEvent.KEY_MENU);
            popup.addCommand(cancel2);
        } else {
            menuItems.setUseSelectButton(true);
        }
        return popup;
    }

    public static MenuBar getPopupMenu(Window popup) {
        return (MenuBar) ((ScrollPane) popup.getComponents().firstElement()).getView();
    }

    /**
         * @see javax.swing.JMenu#setPopupMenuVisible(boolean) JMenu#setPopupMenuVisible
         */
    public void setPopupMenuVisible(boolean vis) {
        if (vis) {
            Component parent1 = getParent();
            if (parent1 instanceof MenuBar) {
                getPopupMenu(popup).owner = (MenuBar) parent1;
            }
            popup.setDesktopPane(getDesktopPane());
            setupSize(popup);
            Border insets = getInsets();
            positionMenuRelativeTo(popup, getXOnScreen() - insets.getLeft(), getYOnScreen() - insets.getTop(), getWidthWithBorder(), getHeightWithBorder(), isTopLevelMenu() ? Graphics.TOP : Graphics.RIGHT);
            setupSnap(popup);
            openMenuAtLocation();
        }
    }

    private boolean isTopLevelMenu() {
        Component parent1 = getParent();
        return !(parent1 instanceof MenuBar) || ((MenuBar) parent1).getLayoutOrientation() == List.HORIZONTAL;
    }

    public static void setupSize(Window window) {
        window.pack();
        DesktopPane dp = window.getDesktopPane();
        int w = window.getWidthWithBorder();
        int h = window.getHeightWithBorder();
        final int maxh = dp.getHeight() - dp.getMenuHeight() * 2;
        if (h > maxh) {
            h = maxh;
            w = w + extraWidth(window);
        }
        if (w > dp.getWidth()) {
            w = dp.getWidth();
        }
        Border insets = window.getInsets();
        window.setSize(w - insets.getLeft() - insets.getRight(), h - insets.getTop() - insets.getBottom());
    }

    public static void positionMenuRelativeTo(Window window, int x, int y, int width, int height, int direction) {
        DesktopPane dp = window.getDesktopPane();
        int w = window.getWidthWithBorder();
        int h = window.getHeightWithBorder();
        if (direction != Graphics.RIGHT) {
            int right = dp.getWidth() - x - width;
            boolean up = (y + height / 2 > dp.getHeight() / 2);
            if (up) {
                y = y - h;
            } else {
                y = y + height;
            }
            if (x > 0 && right == 0) {
                x = dp.getWidth() - w;
            }
        } else {
            x = x + width;
        }
        Border insets = window.getInsets();
        window.setLocation(x + insets.getLeft(), y + insets.getTop());
        window.makeVisible();
    }

    private static int extraWidth(Panel p) {
        Vector children = p.getComponents();
        for (int c = 0; c < children.size(); c++) {
            Component comp = (Component) children.elementAt(c);
            if (comp instanceof ScrollPane) {
                return ((ScrollPane) comp).getBarThickness();
            } else if (comp instanceof Panel) {
                int e = extraWidth((Panel) comp);
                if (e > 0) {
                    return e;
                }
            }
        }
        return 0;
    }

    private static void setupSnap(Window popup) {
        DesktopPane dp = DesktopPane.getDesktopPane();
        boolean left = popup.getXWithBorder() == 0;
        boolean top = popup.getYWithBorder() == 0;
        boolean right = popup.getXWithBorder() + popup.getWidthWithBorder() == dp.getWidth();
        boolean bottom = popup.getYWithBorder() + popup.getHeightWithBorder() == dp.getHeight();
        popup.snap = (left ? Graphics.LEFT : 0) | (top ? Graphics.TOP : 0) | (right ? Graphics.RIGHT : 0) | (bottom ? Graphics.BOTTOM : 0);
    }

    /**
         * there is no method for this in Swing, Swing uses:
         * popup.show(parent, (invokerSize.width - popupSize.width) / 2, (invokerSize.height - popupSize.height) / 2);
         */
    public void openMenuInCentre() {
        popup.pack();
        popup.setLocationRelativeTo(null);
        openMenuAtLocation();
    }

    private void openMenuAtLocation() {
        DesktopPane dp = getDesktopPane();
        if (useAnimation) {
            int x = popup.getXWithBorder();
            int y = popup.getYWithBorder();
            int w = popup.getInsets().getLeft();
            int h = popup.getInsets().getTop();
            if (slide == Graphics.BOTTOM) {
                popup.setLocation(w + x, h + dp.getHeight());
            }
            if (slide == Graphics.TOP) {
                popup.setLocation(w + x, h - popup.getHeightWithBorder());
            }
            if (slide == Graphics.RIGHT) {
                popup.setLocation(w + dp.getWidth(), h + y);
            }
            if (slide == Graphics.LEFT) {
                popup.setLocation(w - popup.getWidthWithBorder(), h + y);
            }
            int offsetX = popup.getX() - popup.getXWithBorder();
            int offsetY = popup.getY() - popup.getYWithBorder();
            destX = x + offsetX;
            destY = y + offsetY;
            open = false;
            dp.animateComponent(this);
        }
        dp.add(popup);
    }

    public void run() throws InterruptedException {
        try {
            int travelDistance = 0;
            if (slide == Graphics.BOTTOM) {
                travelDistance = popup.getY() - destY;
            } else if (slide == Graphics.TOP) {
                travelDistance = destY - popup.getY();
            } else if (slide == Graphics.RIGHT) {
                travelDistance = popup.getX() - destX;
            } else if (slide == Graphics.LEFT) {
                travelDistance = destX - popup.getX();
            }
            int menuMoveSpeed = travelDistance / 3 + 1;
            int step = menuMoveSpeed / 10 + 1;
            while (true) {
                menuMoveSpeed = menuMoveSpeed - step;
                if (menuMoveSpeed < step) {
                    menuMoveSpeed = step;
                }
                if (open) {
                } else {
                    int pX = popup.getX(), pY = popup.getY();
                    if (slide == Graphics.BOTTOM) {
                        pY = pY - menuMoveSpeed;
                        if (pY < destY) {
                            pY = destY;
                        }
                    } else if (slide == Graphics.TOP) {
                        pY = pY + menuMoveSpeed;
                        if (pY > destY) {
                            pY = destY;
                        }
                    } else if (slide == Graphics.RIGHT) {
                        pX = pX - menuMoveSpeed;
                        if (pX < destX) {
                            pX = destX;
                        }
                    } else if (slide == Graphics.LEFT) {
                        pX = pX + menuMoveSpeed;
                        if (pX > destX) {
                            pX = destX;
                        }
                    }
                    popup.getDesktopPane().repaintHole(popup);
                    popup.setLocation(pX, pY);
                    popup.repaint();
                    if (pY == destY && pX == destX) {
                        break;
                    }
                }
                wait(50);
            }
        } finally {
            if (open) {
            } else {
                open = true;
                popup.getDesktopPane().repaintHole(popup);
                popup.setLocation(destX, destY);
                popup.makeVisible();
                popup.repaint();
            }
        }
    }
}
