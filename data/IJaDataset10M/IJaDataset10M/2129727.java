package com.lunarshades.samsara.ui.actions;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import com.lunarshades.samsara.Raider;
import com.lunarshades.samsara.ui.ListingDialog;
import com.lunarshades.samsara.ui.RosterTableModel;
import com.lunarshades.samsara.ui.Utils;
import com.lunarshades.samsara.ui.fonts.Fonts;

/**
 */
public class KarmaAwardAction extends KarmaChangeAction implements ListSelectionListener {

    public KarmaAwardAction(RosterTableModel<? extends Raider> model, JTable table) {
        super(NAME);
        mTable = table;
        mModel = model;
        URL smallIconUrl = getClass().getResource("/com/lunarshades/samsara/ui/icons/addBean16.gif");
        putValue(Action.SMALL_ICON, new ImageIcon(smallIconUrl));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(Action.SHORT_DESCRIPTION, SHORT_TEXT);
        setEnabled(!table.getSelectionModel().isSelectionEmpty());
        table.getSelectionModel().addListSelectionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = mTable.getSelectedRows();
        if (selectedRows.length > 0) {
            initComponent();
            if (mModelRowMap == null) {
                mModelRowMap = new HashMap<Raider, Integer>(mModel.getRowCount());
            } else {
                mModelRowMap.clear();
            }
            synchronized (mModel) {
                for (int selectedRow : selectedRows) {
                    selectedRow = mTable.convertRowIndexToModel(selectedRow);
                    Raider value = mModel.getRowAt(selectedRow);
                    if (!value.pickup()) {
                        if (mDialog.addToBatch(value)) {
                            mModelRowMap.put(value, selectedRow);
                        }
                    }
                }
            }
        }
        mDialog.show();
        if (!mDialog.isCanceled()) {
            try {
                int addQty = Utils.makeInt(mQuantityField.getText());
                if (addQty > 0) {
                    Set<Raider> raiders = mDialog.getBatch();
                    for (Raider r : raiders) {
                        r.karma().create(addQty);
                        int modelRow = mModelRowMap.get(r);
                        mModel.fireTableRowsUpdated(modelRow, modelRow);
                        fireKarmaAdded(r, addQty);
                    }
                }
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void initComponent() {
        if (mDialog == null) {
            JPanel mInputComponent = new JPanel();
            mInputComponent.setLayout(new FlowLayout(FlowLayout.LEADING));
            JLabel amountLabel = new JLabel("Amount:", SwingConstants.LEFT);
            amountLabel.setFont(amountLabel.getFont().deriveFont(Font.BOLD));
            mQuantityField = new JTextField(cDocumentModel, "", 5);
            mQuantityField.setFont(Fonts.getDerivedNumberFont(mQuantityField.getFont()));
            mInputComponent.add(amountLabel);
            mInputComponent.add(mQuantityField);
            mDialog = new ListingDialog<Raider>("Award Karma", "Affected persons:", mTable.getTopLevelAncestor(), mInputComponent);
            InputMap inputMap = mQuantityField.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap actionMap = mQuantityField.getActionMap();
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), this);
            actionMap.put(this, mDialog.CommitAction);
        }
        mDialog.clearBatch();
        mQuantityField.setText(null);
        mQuantityField.requestFocusInWindow();
    }

    public void valueChanged(ListSelectionEvent e) {
        setEnabled(!mTable.getSelectionModel().isSelectionEmpty());
    }

    private static final Document cDocumentModel = new PlainDocument() {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (Utils.isNumberText(str)) {
                super.insertString(offs, str, a);
            }
        }
    };

    private static final String NAME = "Award Karma";

    private static final String SHORT_TEXT = "Award karma to selected entries";

    private final JTable mTable;

    private final RosterTableModel<?> mModel;

    private ListingDialog<Raider> mDialog;

    private JTextField mQuantityField;

    private Map<Raider, Integer> mModelRowMap;
}
