package de.frewert.vboxj.gui.swing;

import java.util.NoSuchElementException;
import de.frewert.vboxj.vbox.MessageInfo;
import de.frewert.vboxj.gui.swing.third_party.TableSorter;

/**
 * <pre>
 * Copyright (C) 2000, 2001, 2003, 2005 Carsten Frewert. All Rights Reserved.
 * 
 * The VBox/J package (de.frewert.vboxj.*) is distributed under
 * the terms of the Artistic license.
 * </pre>
 * @author Carsten Frewert
 * &lt;<a href="mailto:frewert@users.sourceforge.net">
 * frewert@users.sourceforge.net</a>&gt;
 * @version $Revision: 1.5 $
 */
class MessageTableSorter extends TableSorter {

    static final long serialVersionUID = -2236889481138445172L;

    protected int[] reverseIdx;

    /**
     * Create a new MessageTableSorter.
     */
    MessageTableSorter() {
        reverseIdx = new int[0];
    }

    /**
     * Create a new MessageTableSorter.
     * @param model the model to sort on
     */
    MessageTableSorter(final MessageTableModel model) {
        super(model);
    }

    MessageInfo getMessageInfoAt(final int row) {
        MessageTableModel model = (MessageTableModel) getModel();
        return model.getMessageInfoAt(indexes[row]);
    }

    int getRow(final MessageInfo msg) throws NoSuchElementException {
        MessageTableModel model = (MessageTableModel) getModel();
        int row = model.getRow(msg);
        return reverseIdx[row];
    }

    /**
     * Set the model the model to sort on
     * @param model the model to sort on
     */
    void setModel(final MessageTableModel model) {
        super.setModel(model);
    }

    public void reallocateIndexes() {
        super.reallocateIndexes();
        reverseIdx = new int[indexes.length];
        System.arraycopy(indexes, 0, reverseIdx, 0, indexes.length);
    }

    public void sort(final Object sender) {
        super.sort(sender);
        reverseIdx = transpose(indexes, reverseIdx);
    }

    protected int[] transpose(final int[] src, int[] dest) {
        if (dest == null || (src.length != dest.length)) {
            dest = new int[src.length];
        }
        for (int i = 0; i < src.length; i++) {
            dest[src[i]] = i;
        }
        return dest;
    }
}
