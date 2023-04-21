package cz.hdf.cdnavigator.gui.util;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import cz.hdf.gui.HCheckBox;
import cz.hdf.gui.HComboCheckBox;
import cz.hdf.i18n.I18N;

/**
 * DOCUMENT ME!
 *
 * @author hunter
 */
public class LanguagesComboCheckBox extends HComboCheckBox {

    /** Logger. Hierarchy is set to name of this class. */
    Logger logger = Logger.getLogger(this.getClass().getName());

    ListCellRenderer renderer = new ListCellRenderer() {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (index == -1) {
                String record = "";
                for (int i = 0; i < list.getModel().getSize(); i++) {
                    HCheckBox checkBox = (HCheckBox) list.getModel().getElementAt(i);
                    if (checkBox.isSelected()) {
                        if (!record.equals("")) {
                            record += " ";
                        }
                        record += I18N.getLanguageCode(checkBox.getNotLocalizeText());
                    }
                }
                logger.finer("render main item: " + record);
                if (record.equals("")) {
                    record = " ";
                }
                JLabel label = new JLabel(record);
                return label;
            } else {
                JCheckBox item = (JCheckBox) value;
                if (isSelected) {
                    item.setBackground(list.getSelectionBackground());
                    item.setForeground(list.getSelectionForeground());
                } else {
                    item.setBackground(list.getBackground());
                    item.setForeground(list.getForeground());
                }
                logger.finer("render list item: " + item.getText());
                return item;
            }
        }
    };

    public LanguagesComboCheckBox() {
        super();
        String[] langs = I18N.getAllLanguagesNames();
        Arrays.sort(langs);
        ActionListener[] lsnrs = getActionListeners();
        for (int i = 0; i < lsnrs.length; i++) {
            removeActionListener(lsnrs[i]);
        }
        for (int i = 0; i < langs.length; i++) {
            addItem(new HCheckBox(langs[i]));
        }
        setRenderer(renderer);
        for (int i = 0; i < lsnrs.length; i++) {
            addActionListener(lsnrs[i]);
        }
    }

    /**
   * TODO
   *
   * @param items TODO
   */
    public LanguagesComboCheckBox(HCheckBox[] items) {
        super(items);
        setRenderer(renderer);
    }

    public void setSelectedItems(String[] items) {
        if (items == null) items = new String[0];
        String[] names = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            names[i] = I18N.getLanguageName(items[i]);
            if (names[i] == null) {
                names[i] = items[i];
            }
        }
        super.setSelectedItems(names);
    }

    public String[] getSelectedItems() {
        String[] names = super.getSelectedItems();
        String[] codes = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            codes[i] = I18N.getLanguageCode(names[i]);
            if (codes[i] == null) {
                codes[i] = names[i];
            }
        }
        return codes;
    }
}
