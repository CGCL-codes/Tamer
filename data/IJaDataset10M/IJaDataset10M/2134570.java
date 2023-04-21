package chrriis.udoc.ui.toolbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import chrriis.udoc.ui.ClassPane;

public abstract class ToolBarMenu extends JLabel implements ItemSelectable {

    protected static final Color TOOLTIP_FOREGROUND_COLOR = new Color(74, 42, 167);

    protected boolean isOn;

    protected boolean isOver;

    protected ClassPane classPane;

    protected Icon iconOn;

    protected Icon iconOff;

    protected ToolBarMenu(Icon iconOn, Icon iconOff, ClassPane classPane) {
        super(iconOff);
        this.iconOn = iconOn;
        this.iconOff = iconOff;
        this.classPane = classPane;
        setBackground(getSelectionColor());
        setHorizontalAlignment(JLabel.CENTER);
        addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                int modifiers = e.getModifiers();
                if ((modifiers & MouseEvent.BUTTON1_MASK) != 0 || (modifiers & MouseEvent.BUTTON2_MASK) != 0 || (modifiers & MouseEvent.BUTTON3_MASK) != 0) {
                    return;
                }
                isOver = true;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setOpaque(true);
                if (!isOn) {
                    setToolTipVisible(e, true);
                }
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                isOver = false;
                setCursor(null);
                setOpaque(isOn);
                if (!isOn) {
                    setToolTipVisible(e, false);
                }
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }
                setOn(!isOn);
                if (!isOn) {
                    setToolTipVisible(e, true);
                }
            }
        });
        addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                ToolBarMenu.this.classPane.closeMenus();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JPanel contentPane = new JPanel(new BorderLayout(0, 0));
                    String title = getContentTitle();
                    if (title != null && title.length() > 0) {
                        JLabel titleLabel = new JLabel(title);
                        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
                        titleLabel.setHorizontalAlignment(JLabel.CENTER);
                        titleLabel.setBorder(UIManager.getBorder("TextField.border"));
                        titleLabel.setBackground(getSelectionColor());
                        titleLabel.setOpaque(true);
                        contentPane.add(titleLabel, BorderLayout.NORTH);
                    }
                    Component popupMenuContent = createPopupMenuContent();
                    contentPane.add(popupMenuContent, BorderLayout.SOUTH);
                    addMenu(contentPane);
                    popupMenuContent.requestFocus();
                }
            }
        });
    }

    protected void setToolTipVisible(MouseEvent e, boolean isVisible) {
        if (isVisible) {
            String title = getContentTitle();
            if (title != null && title.length() > 0) {
                JLabel titleLabel = new JLabel(title);
                titleLabel.setForeground(TOOLTIP_FOREGROUND_COLOR);
                titleLabel.setOpaque(true);
                titleLabel.setBackground(classPane.getClassComponentPane().getBackground());
                classPane.showTooltip(titleLabel, ToolBarMenu.this, (getWidth() - titleLabel.getPreferredSize().width) / 2, getHeight() + 20);
            }
            return;
        }
        classPane.hideTooltip();
    }

    protected void addMenu(JComponent content) {
        JPanel contentPane = new JPanel(new BorderLayout(0, 0)) {

            public void invalidate() {
                super.invalidate();
                if (isExpanded) {
                    validate();
                }
            }

            public void validate() {
                setSize(this.getPreferredSize());
                super.validate();
            }

            public void hide() {
                super.hide();
                setOn(false);
            }

            boolean isExpanded;

            public void reshape(int x, int y, int w, int h) {
                super.reshape(x, y, w, h);
                Dimension preferredSize = this.getPreferredSize();
                isExpanded = w == preferredSize.width && h == preferredSize.height;
            }
        };
        content.addMouseListener(new MouseAdapter() {
        });
        contentPane.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(content, BorderLayout.CENTER);
        classPane.showMenu(contentPane, ToolBarMenu.this, 0, getHeight());
    }

    public Object[] getSelectedObjects() {
        return isOn ? new Object[] { this } : null;
    }

    protected void fireItemStateChanged(ItemEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(event);
            }
        }
    }

    public void addItemListener(ItemListener l) {
        listenerList.add(ItemListener.class, l);
    }

    public void removeItemListener(ItemListener l) {
        listenerList.remove(ItemListener.class, l);
    }

    protected void setOn(boolean isOn) {
        if (this.isOn == isOn) {
            return;
        }
        this.isOn = isOn;
        setOpaque(isOver);
        setIcon(isOn ? iconOn : iconOff);
        fireItemStateChanged(new ItemEvent(ToolBarMenu.this, ItemEvent.ITEM_STATE_CHANGED, ToolBarMenu.this, isOn ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
    }

    public Dimension getPreferredSize() {
        return new Dimension(50, super.getPreferredSize().height);
    }

    protected String getContentTitle() {
        return null;
    }

    protected abstract Component createPopupMenuContent();

    protected abstract Color getSelectionColor();
}
