package net.sf.doolin.app.sc.client.field;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.doolin.app.sc.client.bean.GameBean;
import net.sf.doolin.app.sc.client.service.ItemIdSelector;
import net.sf.doolin.app.sc.client.util.GUIHistoryUtils;
import net.sf.doolin.app.sc.game.type.HistoryItem;
import net.sf.doolin.app.sc.game.type.HistoryItemLevel;
import net.sf.doolin.app.sc.game.type.ItemId;
import net.sf.doolin.gui.field.AbstractFieldDescriptor;
import net.sf.doolin.gui.field.Field;
import net.sf.doolin.gui.field.support.SimpleField;
import net.sf.doolin.gui.service.IconSize;
import net.sf.doolin.gui.util.GUIStrings;
import net.sf.doolin.gui.view.GUIView;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FieldHistory extends AbstractFieldDescriptor<GameBean> {

    protected class HistoryCellRenderer extends JPanel implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        private JLabel labelIcon;

        private JTextArea textArea;

        public HistoryCellRenderer() {
            setLayout(new FormLayout("32px,4px,fill:default:grow", "center:default:grow"));
            CellConstraints cc = new CellConstraints();
            this.labelIcon = new JLabel();
            add(this.labelIcon, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.CENTER));
            this.textArea = new JTextArea();
            this.textArea.setWrapStyleWord(true);
            this.textArea.setLineWrap(true);
            this.textArea.setFont(this.labelIcon.getFont());
            add(this.textArea, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.FILL));
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            HistoryItem item = (HistoryItem) value;
            String code = item.getMessageCode();
            String textKey = GUIHistoryUtils.getHistoryItemTextKey(code);
            String message = GUIStrings.get(textKey, (Object[]) item.getMessageParams());
            this.textArea.setText(message);
            String iconIDKey = GUIHistoryUtils.getHistoryItemIconKey(code);
            String iconID = GUIStrings.getIfPresent(iconIDKey);
            if (iconID != null) {
                Icon icon = getIconService().getIcon(iconID, IconSize.SMALL);
                this.labelIcon.setIcon(icon);
            } else {
                this.labelIcon.setIcon(null);
            }
            Color color;
            if (isSelected) {
                color = list.getSelectionBackground();
            } else {
                color = list.getBackground();
            }
            setBackground(color);
            this.labelIcon.setBackground(color);
            this.textArea.setBackground(color);
            Border border;
            if (item.getLevel() == HistoryItemLevel.SECTION) {
                border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK);
            } else {
                border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY);
            }
            setBorder(border);
            return this;
        }
    }

    private static final int ROW_HEIGHT = 32;

    private static final long serialVersionUID = 1L;

    @Override
    public Field<GameBean> createField(final GUIView<GameBean> view) {
        final JList list = new JList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(ROW_HEIGHT);
        list.setCellRenderer(new HistoryCellRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        Field<GameBean> field = new SimpleField<GameBean>(view, this, scrollPane);
        final EventList<HistoryItem> history = view.getViewData().getHistory();
        EventListModel<HistoryItem> historyModel = new EventListModel<HistoryItem>(history);
        list.setModel(historyModel);
        list.getModel().addListDataListener(new ListDataListener() {

            @Override
            public void contentsChanged(ListDataEvent e) {
                scrollToEnd();
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                scrollToEnd();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            private void scrollToEnd() {
                int rowIndex = list.getModel().getSize() - 1;
                list.ensureIndexIsVisible(rowIndex);
            }
        });
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = list.getSelectedIndex();
                    if (index >= 0) {
                        HistoryItem historyItem = history.get(index);
                        if (historyItem != null) {
                            ItemId associatedItemId = historyItem.getAssociatedItemId();
                            if (associatedItemId != null) {
                                selectAssociatedItem(view.getViewData(), associatedItemId);
                            }
                        }
                    }
                }
            }
        });
        return field;
    }

    protected void selectAssociatedItem(GameBean game, ItemId associatedItemId) {
        ItemIdSelector selector = game.getClientService().getItemIdSelector(associatedItemId.getType());
        if (selector != null) {
            selector.selectItem(game, associatedItemId.getId());
        }
    }
}
