package com.ramp.microswing;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import com.ramp.microswing.behaviour.ComponentBehaviour;
import com.ramp.microswing.plaf.LookAndFeel;
import com.ramp.microswing.plaf.UIManager;

public class JComboBox extends JComponent {

    protected static int ARROW_COLOR;

    protected static int SELECTOR_COLOR;

    protected static int BORDERI_COLOR;

    protected static int BORDERII_COLOR;

    private Vector items;

    private int selectedIndex;

    private int oldHeight;

    private int initialIndex;

    private Object selectedItem;

    private JButton button;

    private JScrollBar scroll;

    private int contentHeight;

    public static int MAX_VISIBLE_ITEMS = 4;

    public JComboBox() {
        editModeSupport = true;
        editMode = false;
        items = new Vector();
        button = new JButton("");
        updateUI();
    }

    public void paintComponent(Graphics g, int x, int y) {
        g.setColor(background);
        g.fillRect(x, y, width, height);
        g.setFont(font);
        paintComponentBorder(g, x, y);
        if (selectedItem instanceof String) {
            g.setColor(foreground);
            g.drawString((String) selectedItem, x + 2, y + 2, Graphics.LEFT | Graphics.TOP);
        } else if (selectedItem instanceof Component) {
            Component comp = (Component) selectedItem;
            comp.setLocation(x + 2, y + 2);
            comp.paint(g);
        }
        button.focus = focus;
        g.setClip(x + width - button.width, this.y, button.getWidth(), button.getHeight());
        button.x = (x + width) - button.width;
        button.y = y;
        button.paint(g);
        g.setColor(ARROW_COLOR);
        Graphics2D.fillTriangle(g, button.x + (button.getWidth() / 2), button.y + (button.getHeight() / 2) + 3, 3, 3, Graphics2D.HEAD_DOWN);
    }

    private void paintItems(Graphics g, int x, int y) {
        if (editMode) {
            int size = items.size();
            int count = 0;
            height = oldHeight;
            int _y = y + 4;
            for (int i = initialIndex; i < size; i++) {
                Object obj = items.elementAt(i);
                if (obj instanceof String) {
                    height += font.getHeight();
                    _y += font.getHeight();
                    paintSelection(i, _y, g);
                    g.setClip(x, _y, width + 4, height + 2);
                    g.setColor(foreground);
                    g.drawString((String) obj, x + 2, _y, Graphics.LEFT | Graphics.TOP);
                } else if (obj instanceof Component) {
                    Component comp = (Component) obj;
                    height += comp.height;
                    _y += comp.height;
                    paintSelection(i, _y, g);
                    g.setClip(x, _y, width + 4, height + 2);
                    comp.setLocation(x + 2, _y);
                    comp.paint(g);
                }
                count++;
                if (count >= MAX_VISIBLE_ITEMS) {
                    break;
                }
            }
            if (scroll == null) {
                scroll = new JScrollBar(width, height - oldHeight, contentHeight, x, y + oldHeight);
            }
            if (scroll != null) {
                scroll.setY(y + oldHeight);
                scroll.paint(g);
            }
        } else {
            if (selectedItem instanceof String) {
                height = font.getHeight() + 4;
                oldHeight = height;
            } else if (selectedItem instanceof Component) {
                height = ((Component) selectedItem).height;
                oldHeight = height;
            }
            scroll = null;
        }
    }

    protected void paintComponentBorder(Graphics g, int x, int y) {
        g.setClip(x, y, width + 2, height + 2);
        g.setColor(BORDERI_COLOR);
        g.drawRect(x + 1, y + 1, width - 2, height - 2);
        g.setColor(BORDERII_COLOR);
        g.drawRect(x, y, width, height);
    }

    private void paintSelection(int i, int y, Graphics g) {
        if (i == selectedIndex) {
            g.setColor(SELECTOR_COLOR);
            g.fillRect(x, y, width, font.getHeight());
            MSFThreadController.moveMouse(x + width - button.width, y, MSFThreadController.MOUSE_POINTER);
        }
    }

    protected int keyPressed(int key) {
        switch(getGameAction(key)) {
            case Canvas.FIRE:
                if (editMode) {
                    selectedItem = items.elementAt(selectedIndex);
                    height = oldHeight;
                    editMode = false;
                } else {
                    initialIndex = 0;
                    selectedIndex = 0;
                    editMode = true;
                }
                break;
            case Canvas.UP:
                selectedIndex -= (selectedIndex > 0) ? 1 : 0;
                if (selectedIndex < initialIndex) {
                    initialIndex--;
                    if (scroll != null) {
                        scroll.move(oldHeight);
                    }
                }
                break;
            case Canvas.DOWN:
                selectedIndex += (selectedIndex < items.size() - 1) ? 1 : 0;
                if (selectedIndex > MAX_VISIBLE_ITEMS - 1 && initialIndex < items.size() - MAX_VISIBLE_ITEMS) {
                    initialIndex++;
                    if (scroll != null) {
                        scroll.move(-oldHeight);
                    }
                }
                break;
        }
        return editMode ? 1 : 0;
    }

    protected int keyReleased(int key) {
        return super.keyReleased(key);
    }

    protected void paint(Graphics g) {
        if (visible) {
            g.setClip(x, y, width + 5, height);
            if (behaviour == null) {
                paintComponent(g, x, y);
            } else {
                if (button.behaviour == null) {
                    try {
                        button.behaviour = (ComponentBehaviour) behaviour.getClass().newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                button.paintBehaviour(g);
                paintBehaviour(g);
            }
            paintItems(g, x, y);
        }
    }

    public void addItem(String str) {
        int objw = 0;
        int objh = 0;
        objw = font.stringWidth(str) + button.width + 4;
        objh = font.getHeight() + 4;
        width = (objw > width) ? objw + button.width : width;
        height = (objh > height) ? height = objh + button.height : height;
        if (selectedItem == null) {
            selectedItem = str;
        }
        contentHeight += objh;
        items.addElement(str);
        oldHeight = height;
    }

    public void addItem(Component component) {
        int objw = 0;
        int objh = 0;
        objw = component.width + button.width + 4;
        objh = component.height + 4;
        width = (objw > width) ? objw + button.width : width;
        height = (objh > height) ? height = objh + button.height : height;
        if (selectedItem == null) {
            selectedItem = component;
        }
        contentHeight += objh;
        items.addElement(component);
        oldHeight = height;
    }

    protected void calcule() {
        button.width = 18;
        button.height = font.getHeight() + 4;
    }

    protected void updateUI() {
        ARROW_COLOR = UIManager.getColor(LookAndFeel.comboboxArrowColor);
        SELECTOR_COLOR = UIManager.getColor(LookAndFeel.comboboxSelectorColor);
        BORDERI_COLOR = UIManager.getColor(LookAndFeel.comboboxBorder1Color);
        BORDERII_COLOR = UIManager.getColor(LookAndFeel.comboboxBorder2Color);
        font = UIManager.getFont(LookAndFeel.comboboxFont);
        foreground = UIManager.getColor(LookAndFeel.comboboxForeground);
        background = UIManager.getColor(LookAndFeel.comboboxBackground);
        calcule();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = selectedItem;
    }
}
