package org.mbari.vars.knowledgebase.ui;

import org.mbari.vars.knowledgebase.model.SectionInfo;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * <p><!-- Class description --></p>
 *
 * @version    $Id: SectionInfoList.java 3 2005-10-27 16:20:12Z hohonuuli $
 * @author     <a href="http://www.mbari.org">Monterey Bay Aquarium Research Institute</a>
 */
public class SectionInfoList extends JPanel {

    private static final Font LINE_1_FONT = new Font("Serif", Font.BOLD, 14);

    private static final Font LINE_2_FONT = new Font("Serif", Font.ITALIC, 14);

    private static final Color normalBackground = (Color) UIManager.get("TextField.background");

    private static final Color normalForeground = (Color) UIManager.get("TextField.foreground");

    private static final Color selectionBackground = (Color) UIManager.get("TextField.selectionBackground");

    private static final Color selectionForeground = (Color) UIManager.get("TextField.selectionForeground");

    private int currentItemIndex;

    private Map items;

    private List listeners;

    private Border selectionBorder = BorderFactory.createLineBorder(selectionForeground, 1);

    private Border normalBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    /**
     * Constructs ...
     *
     */
    public SectionInfoList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        items = new TreeMap();
        currentItemIndex = -1;
        listeners = new LinkedList();
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param sectionInfo
     */
    public void addSectionInfo(SectionInfo sectionInfo) {
        addSectionInfo(sectionInfo, false);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param sectionInfo
     * @param batch
     */
    private void addSectionInfo(SectionInfo sectionInfo, boolean batch) {
        if (items.containsKey(sectionInfo)) {
            return;
        }
        StringBuffer buf = new StringBuffer();
        buf.append(sectionInfo.getHeader());
        buf.append(" : ");
        buf.append(sectionInfo.getLabel());
        String line_1 = buf.toString();
        String line_2 = sectionInfo.getInformation();
        SectionInfoLinesPanel panel = new SectionInfoLinesPanel(line_1, line_2);
        items.put(sectionInfo, panel);
        if (!batch) {
            update();
            panel.select();
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param listener
     */
    public void addSectionInfoListListener(SectionInfoListListener listener) {
        listeners.add(listener);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void clearSelection() {
        deselectAllItems();
        currentItemIndex = -1;
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void deselectAllItems() {
        Iterator valuesIterator = items.values().iterator();
        while (valuesIterator.hasNext()) {
            SectionInfoLinesPanel panel = (SectionInfoLinesPanel) valuesIterator.next();
            panel.deselect();
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param sectionInfo
     */
    private void notifyListeners(SectionInfo sectionInfo) {
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext()) {
            SectionInfoListListener listener = (SectionInfoListListener) iterator.next();
            listener.selectionChanged(sectionInfo);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param sectionInfo
     *
     * @return
     */
    public boolean removeSectionInfo(SectionInfo sectionInfo) {
        boolean removed = (items.remove(sectionInfo) != null);
        if (removed) {
            update();
        }
        return removed;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param listener
     */
    public void removeSectionInfoListListener(SectionInfoListListener listener) {
        listeners.remove(listener);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param itemIndex
     */
    private void selectItem(int itemIndex) {
        int maxIndex = items.size() - 1;
        if (itemIndex < 0) {
            itemIndex = 0;
        } else if (maxIndex < itemIndex) {
            itemIndex = maxIndex;
        }
        if (currentItemIndex != itemIndex) {
            SectionInfo selectedSectionInfo = null;
            currentItemIndex = itemIndex;
            Iterator keyIterator = items.keySet().iterator();
            while (keyIterator.hasNext()) {
                SectionInfo key = (SectionInfo) keyIterator.next();
                SectionInfoLinesPanel panel = (SectionInfoLinesPanel) items.get(key);
                if (panel.getItemIndex() == currentItemIndex) {
                    panel.select();
                    selectedSectionInfo = key;
                } else {
                    panel.deselect();
                }
            }
            notifyListeners(selectedSectionInfo);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param sectionInfos
     */
    public void setList(SectionInfo[] sectionInfos) {
        items.clear();
        currentItemIndex = -1;
        for (int i = 0; i < sectionInfos.length; i++) {
            addSectionInfo(sectionInfos[i], true);
        }
        update();
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void update() {
        removeAll();
        int itemCount = 0;
        Iterator valuesIterator = items.values().iterator();
        while (valuesIterator.hasNext()) {
            SectionInfoLinesPanel panel = (SectionInfoLinesPanel) valuesIterator.next();
            add(panel);
            panel.deselect();
            panel.setItemIndex(itemCount++);
        }
    }

    class SectionInfoLinesPanel extends JPanel {

        private int itemIndex;

        private JTextArea line_1_Area;

        private JTextArea line_2_Area;

        /**
         * Constructs ...
         *
         *
         * @param line_1
         * @param line_2
         */
        public SectionInfoLinesPanel(String line_1, String line_2) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            line_1_Area = new JTextArea(line_1);
            line_1_Area.setEditable(false);
            line_1_Area.setFont(LINE_1_FONT);
            line_2_Area = new JTextArea(line_2);
            line_2_Area.setFont(LINE_2_FONT);
            line_2_Area.setLineWrap(true);
            line_2_Area.setWrapStyleWord(true);
            line_2_Area.setEditable(false);
            FocusListener focusListener = new LinesFocusListener();
            line_1_Area.addFocusListener(focusListener);
            line_2_Area.addFocusListener(focusListener);
            KeyListener keyListener = new LinesKeyListener();
            line_1_Area.addKeyListener(keyListener);
            line_2_Area.addKeyListener(keyListener);
            add(line_1_Area);
            add(line_2_Area);
            deselect();
        }

        /**
         * <p><!-- Method description --></p>
         *
         */
        public void deselect() {
            line_1_Area.setBackground(normalBackground);
            line_1_Area.setForeground(normalForeground);
            line_2_Area.setBackground(normalBackground);
            line_2_Area.setForeground(normalForeground);
            setBorder(normalBorder);
        }

        /**
         * <p><!-- Method description --></p>
         *
         *
         * @return
         */
        public int getItemIndex() {
            return itemIndex;
        }

        /**
         * <p><!-- Method description --></p>
         *
         */
        public void select() {
            line_1_Area.setBackground(selectionBackground);
            line_1_Area.setForeground(selectionForeground);
            line_2_Area.setBackground(selectionBackground);
            line_2_Area.setForeground(selectionForeground);
            setBorder(selectionBorder);
            line_2_Area.requestFocus();
        }

        /**
         * <p><!-- Method description --></p>
         *
         *
         * @param itemIndex
         */
        public void setItemIndex(int itemIndex) {
            this.itemIndex = itemIndex;
        }

        class LinesFocusListener implements FocusListener {

            /**
             * <p><!-- Method description --></p>
             *
             *
             * @param event
             */
            public void focusGained(FocusEvent event) {
                selectItem(itemIndex);
            }

            /**
             * <p><!-- Method description --></p>
             *
             *
             * @param event
             */
            public void focusLost(FocusEvent event) {
            }
        }

        class LinesKeyListener extends KeyAdapter {

            /**
             * <p><!-- Method description --></p>
             *
             *
             * @param event
             */
            public void keyPressed(KeyEvent event) {
                int keyCode = event.getKeyCode();
                if ((keyCode == KeyEvent.VK_UP) || (keyCode == KeyEvent.VK_KP_UP)) {
                    selectItem(itemIndex - 1);
                }
                if ((keyCode == KeyEvent.VK_DOWN) || (keyCode == KeyEvent.VK_KP_DOWN)) {
                    selectItem(itemIndex + 1);
                }
                if (keyCode == KeyEvent.VK_PAGE_UP) {
                    selectItem(0);
                }
                if (keyCode == KeyEvent.VK_PAGE_DOWN) {
                    selectItem(Integer.MAX_VALUE);
                }
            }
        }
    }
}
