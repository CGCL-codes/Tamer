package kr.ac.ssu.imc.whitehole.report.designer.items.rdtables;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.border.*;
import javax.swing.event.*;
import kr.ac.ssu.imc.whitehole.report.designer.*;
import kr.ac.ssu.imc.whitehole.report.designer.dialogs.*;
import kr.ac.ssu.imc.whitehole.report.designer.items.*;
import com.sun.java.swing.plaf.windows.*;

public class RDTablePopupButton extends JButton implements MouseListener, PopupMenuListener {

    private Component invoker;

    private JPopupMenu menu;

    private Vector vectorItems;

    private ImageIcon icon;

    private int selectedIndex = -1;

    private static BevelBorder tRaisedBevelBorder, tLoweredBevelBorder;

    private static EmptyBorder tEmptyBorder;

    static {
        tRaisedBevelBorder = new BevelBorder(BevelBorder.RAISED);
        tLoweredBevelBorder = new BevelBorder(BevelBorder.LOWERED);
        tEmptyBorder = new EmptyBorder(new Insets(2, 2, 2, 2));
    }

    public RDTablePopupButton(ImageIcon icon, Component invoker) {
        super(icon);
        setBorder(tEmptyBorder);
        setOpaque(false);
        this.invoker = invoker;
        this.icon = icon;
        menu = new JPopupMenu();
        menu.setLightWeightPopupEnabled(true);
        menu.addPopupMenuListener(this);
        addMouseListener(this);
    }

    /**
   * ����Ʈ �׼��� �����ϴ� �޼ҵ� �ݵ�� �������̵� �ؾ��Ѵ�.
   */
    public void defaultActionPerformed() {
    }

    /**
   * ������Ʈ�� �迭�� �޴��� ���Ѵ�.
   * @param items ������Ʈ�� �迭
   */
    public void addItems(Object[] items) {
        int length = items.length;
        if (vectorItems == null) vectorItems = new Vector(length, 1);
        for (int i = 0; i < length; i++) {
            vectorItems.addElement(items[i]);
        }
        for (int i = 0; i < length; i++) {
            if (vectorItems.elementAt(i) instanceof String) vectorItems.setElementAt(new JMenuItem((String) vectorItems.elementAt(i)), i); else if (vectorItems.elementAt(i) instanceof ImageIcon) vectorItems.setElementAt(new JMenuItem((ImageIcon) vectorItems.elementAt(i)), i); else if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) vectorItems.setElementAt((JCheckBoxMenuItem) vectorItems.elementAt(i), i); else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) vectorItems.setElementAt((JRadioButtonMenuItem) vectorItems.elementAt(i), i); else if (vectorItems.elementAt(i) instanceof JMenuItem) vectorItems.setElementAt((JMenuItem) vectorItems.elementAt(i), i);
        }
        for (int i = 0; i < length; i++) {
            if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) {
                menu.add((JCheckBoxMenuItem) vectorItems.elementAt(i));
                ((JCheckBoxMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) {
                menu.add((JRadioButtonMenuItem) vectorItems.elementAt(i));
                ((JRadioButtonMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JMenuItem) {
                menu.add((JMenuItem) vectorItems.elementAt(i));
                ((JMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            }
        }
    }

    /**
   *  ���͸� �޴��� ���� �߰��Ѵ�.
   *  @param items Vector�� items�� �޴� �г��� ���� �߰��Ѵ�.
   */
    public void addItems(Vector items) {
        if (vectorItems == null) {
            vectorItems = new Vector(items.size(), 1);
            for (int i = 0; i < items.size(); i++) {
                vectorItems.addElement(items.elementAt(i));
            }
        } else {
            for (int i = 0; i < items.size(); i++) {
                vectorItems.addElement(items.elementAt(i));
            }
        }
        for (int i = 0; i < vectorItems.size(); i++) {
            if (vectorItems.elementAt(i) instanceof String) vectorItems.setElementAt(new JMenuItem((String) vectorItems.elementAt(i)), i); else if (vectorItems.elementAt(i) instanceof ImageIcon) vectorItems.setElementAt(new JMenuItem((ImageIcon) vectorItems.elementAt(i)), i); else if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) vectorItems.setElementAt((JCheckBoxMenuItem) vectorItems.elementAt(i), i); else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) vectorItems.setElementAt((JRadioButtonMenuItem) vectorItems.elementAt(i), i); else if (vectorItems.elementAt(i) instanceof JMenuItem) vectorItems.setElementAt((JMenuItem) vectorItems.elementAt(i), i);
        }
        for (int i = 0; i < vectorItems.size(); i++) {
            if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) {
                menu.add((JCheckBoxMenuItem) vectorItems.elementAt(i));
                ((JCheckBoxMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) {
                menu.add((JRadioButtonMenuItem) vectorItems.elementAt(i));
                ((JRadioButtonMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JMenuItem) {
                menu.add((JMenuItem) vectorItems.elementAt(i));
                ((JMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            }
        }
    }

    /**
   * ������Ʈ �ϳ��� �޴��� ���Ѵ�.
   * @param anObject Object�� �޴� ������
   */
    public void addItem(Object anObject) {
        if (vectorItems == null) vectorItems = new Vector(1, 1);
        vectorItems.addElement(anObject);
        if (vectorItems.elementAt(vectorItems.size() - 1) instanceof String) {
            JMenuItem item = new JMenuItem((String) vectorItems.elementAt(vectorItems.size() - 1));
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    defaultActionPerformed();
                }
            });
            vectorItems.setElementAt(item, vectorItems.size() - 1);
        } else if (vectorItems.elementAt(vectorItems.size() - 1) instanceof ImageIcon) {
            JMenuItem item = new JMenuItem((ImageIcon) vectorItems.elementAt(vectorItems.size() - 1));
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    defaultActionPerformed();
                }
            });
            vectorItems.setElementAt(item, vectorItems.size() - 1);
        } else if (vectorItems.elementAt(vectorItems.size() - 1) instanceof JCheckBoxMenuItem) vectorItems.setElementAt((JCheckBoxMenuItem) vectorItems.elementAt(vectorItems.size() - 1), vectorItems.size() - 1); else if (vectorItems.elementAt(vectorItems.size() - 1) instanceof JRadioButtonMenuItem) vectorItems.setElementAt((JRadioButtonMenuItem) vectorItems.elementAt(vectorItems.size() - 1), vectorItems.size() - 1); else if (vectorItems.elementAt(vectorItems.size() - 1) instanceof JMenuItem) vectorItems.setElementAt((JMenuItem) vectorItems.elementAt(vectorItems.size() - 1), vectorItems.size() - 1);
        if (vectorItems.elementAt(vectorItems.size() - 1) instanceof JCheckBoxMenuItem) {
            menu.add((JCheckBoxMenuItem) vectorItems.elementAt(vectorItems.size() - 1));
            ((JCheckBoxMenuItem) (vectorItems.elementAt(vectorItems.size() - 1))).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    selectedIndex = vectorItems.indexOf(ae.getSource());
                }
            });
        } else if (vectorItems.elementAt(vectorItems.size() - 1) instanceof JRadioButtonMenuItem) {
            menu.add((JRadioButtonMenuItem) vectorItems.elementAt(vectorItems.size() - 1));
            ((JRadioButtonMenuItem) (vectorItems.elementAt(vectorItems.size() - 1))).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    selectedIndex = vectorItems.indexOf(ae.getSource());
                }
            });
        } else if (vectorItems.elementAt(vectorItems.size() - 1) instanceof JMenuItem) {
            menu.add((JMenuItem) vectorItems.elementAt(vectorItems.size() - 1));
            ((JMenuItem) (vectorItems.elementAt(vectorItems.size() - 1))).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    selectedIndex = vectorItems.indexOf(ae.getSource());
                }
            });
        }
    }

    /**
   * �־��� �ε����� ������Ʈ�� �ִ´�.
   * @param anObject Object�� ������
   * @param index �������� ���Ե� �ε���
   */
    public void insertItemAt(Object anObject, int index) {
        if (index != -1) vectorItems.insertElementAt(anObject, index); else vectorItems.addElement(anObject);
        for (int i = 0; i < vectorItems.size(); i++) {
            if (vectorItems.elementAt(i) instanceof String) vectorItems.setElementAt(new JMenuItem((String) vectorItems.elementAt(i)), i); else if (vectorItems.elementAt(i) instanceof ImageIcon) vectorItems.setElementAt(new JMenuItem((ImageIcon) vectorItems.elementAt(i)), i); else if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) vectorItems.setElementAt((JCheckBoxMenuItem) vectorItems.elementAt(i), i); else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) vectorItems.setElementAt((JRadioButtonMenuItem) vectorItems.elementAt(i), i); else if (vectorItems.elementAt(i) instanceof JMenuItem) vectorItems.setElementAt((JMenuItem) vectorItems.elementAt(i), i);
        }
        menu.removeAll();
        for (int i = 0; i < vectorItems.size(); i++) {
            if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) {
                menu.add((JCheckBoxMenuItem) vectorItems.elementAt(i));
                ((JCheckBoxMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) {
                menu.add((JRadioButtonMenuItem) vectorItems.elementAt(i));
                ((JRadioButtonMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JMenuItem) {
                menu.add((JMenuItem) vectorItems.elementAt(i));
                ((JMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            }
        }
    }

    /**
   * ��� �������� �����Ѵ�.
   */
    public void removeAllItems() {
        if (vectorItems != null) vectorItems.removeAllElements();
        if (menu != null) menu.removeAll();
    }

    /**
   * �־��� �ε����� �������� �����Ѵ�.
   * @param anIndex ������ �������� �ε���
   */
    public void removeItemAt(int anIndex) {
        if (vectorItems.elementAt(anIndex) != null) vectorItems.removeElementAt(anIndex);
        menu.removeAll();
        for (int i = 0; i < vectorItems.size(); i++) {
            if (vectorItems.elementAt(i) instanceof JCheckBoxMenuItem) {
                menu.add((JCheckBoxMenuItem) vectorItems.elementAt(i));
                ((JCheckBoxMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JRadioButtonMenuItem) {
                menu.add((JRadioButtonMenuItem) vectorItems.elementAt(i));
                ((JRadioButtonMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            } else if (vectorItems.elementAt(i) instanceof JMenuItem) {
                menu.add((JMenuItem) vectorItems.elementAt(i));
                ((JMenuItem) (vectorItems.elementAt(i))).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        selectedIndex = vectorItems.indexOf(ae.getSource());
                    }
                });
            }
        }
    }

    /**
   * �޴��г��� ���̾ƿ��� �����Ѵ�.
   * @param mgr LayoutManager Ÿ������ �޴��г��� ���̾ƿ�
   */
    public void setMenuLayout(LayoutManager mgr) {
        menu.setLayout(mgr);
    }

    /**
   * �޴��гο� �и��ڸ� �ִ´�.
   */
    public void addSeparator() {
        menu.addSeparator();
    }

    /**
   * �޴��гο� �����ִ� �������� ������ ��ȯ�Ѵ�.
   */
    public int getItemCount() {
        if (vectorItems == null) return 0; else return vectorItems.size();
    }

    /**
   * ���õ� �������� �ε����� ��ȯ�Ѵ�.
   */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
   * ���õ� �ε����� �����Ѵ�.
   * @param index ���õ� �������� �ε���
   */
    public void setSelectedIndex(int index) {
        if (index < -1 || index > vectorItems.size() - 1) return;
        selectedIndex = index;
    }

    /**
   * ���õ� �������� ��ȯ�Ѵ�.
   */
    public Object getSelectedItem() {
        return vectorItems.elementAt(selectedIndex);
    }

    /**
   * �־��� �ε����� �������� ��ȯ�Ѵ�.
   * @param index ����� �ϴ� �������� �ε���
   * @return Object���� �������� ��´�.
   */
    public Object getItemAt(int index) {
        return vectorItems.elementAt(index);
    }

    /**
   * ��ư�� ���¸� �����Ѵ�.
   * @param aFlag aFlag�� true�̸� �����ͺ��ϸ�, false�̸� �Ҵɻ���
   */
    public void setEditable(boolean aFlag) {
        if (aFlag == false) {
            removeMouseListener(this);
            setEnabled(false);
        }
        if (aFlag == true) {
            addMouseListener(this);
            setEnabled(true);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (menu != null && menu.getComponentCount() > 0) menu.show(invoker, (int) this.getBounds().getX(), (int) this.getBounds().getY() + (int) this.getBounds().getHeight());
    }

    public void mousePressed(MouseEvent e) {
        JButton tButton = (JButton) e.getSource();
        if (!tButton.isEnabled()) return;
        tButton.setBorder(tLoweredBevelBorder);
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        JButton tButton = (JButton) e.getSource();
        if (!tButton.isEnabled()) return;
        tButton.setBorder(tRaisedBevelBorder);
    }

    public void mouseExited(MouseEvent e) {
        if (menu.isShowing()) {
            JButton tButton = (JButton) e.getSource();
            if (!tButton.isEnabled()) return;
            tButton.setBorder(tLoweredBevelBorder);
        } else {
            JButton tButton = (JButton) e.getSource();
            if (!tButton.isEnabled()) return;
            tButton.setBorder(tEmptyBorder);
        }
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        setBorder(tEmptyBorder);
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    protected void processFocusEvent(FocusEvent e) {
    }
}
