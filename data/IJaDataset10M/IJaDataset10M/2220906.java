package com.objectwave.uiWidget;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.objectwave.viewUtility.StretchLayout;
import com.objectwave.viewUtility.StretchLayoutConstraints;
import com.objectwave.utility.Sorter;
import com.objectwave.utility.SorterComparisonIF;
import com.objectwave.utility.StringifyIF;

/**
 * A two-list selection thingy.  Note that objects passed to this method are
 * expected to properly support the .equals comparison operator, which is used
 * to determine if a given choice exists on the left or right, and is used when
 * adding or removing items from the list.
 */
public class SimpleChoiceGui extends JPanel {

    private MyListModel choiceModel;

    private MyListModel chosenModel;

    private JList choiceList;

    private JList chosenList;

    private JScrollPane lhsScroll;

    private JScrollPane rhsScroll;

    private JButton addButton;

    private JButton removeButton;

    private JButton addAllButton;

    private JButton removeAllButton;

    private SorterComparisonIF comparison;

    private boolean chosenListWasModified = false;

    private class MyListModel extends AbstractListModel {

        StringifyIF stringifier;

        Vector data = new Vector();

        MyListModel() {
        }

        MyListModel(StringifyIF s) {
            stringifier = s;
        }

        public void setStringifier(StringifyIF s) {
            stringifier = s;
        }

        public StringifyIF getStringifier() {
            return stringifier;
        }

        public Object getElementAt(int idx) {
            Object o = data.elementAt(idx);
            return (stringifier == null) ? o : stringifier.toString(o);
        }

        /** Bypass any stringification.
		 */
        public Object getTrueElementAt(int idx) {
            return data.elementAt(idx);
        }

        public int size() {
            return data.size();
        }

        public void clear() {
            setData(new Vector());
        }

        public int getSize() {
            return data.size();
        }

        public Vector getData() {
            return data;
        }

        public void setData(Vector d) {
            data = d;
            fireContentsChanged(this, 0, data.size() - 1);
        }

        public void setData(Object[] d) {
            data = new Vector(d.length);
            for (int i = 0; i < d.length; ++i) data.addElement(d[i]);
            fireContentsChanged(this, 0, data.size() - 1);
        }

        public void appendData(Object[] d) {
            if (d.length == 0) return;
            int size = data.size();
            data.ensureCapacity(data.size() + d.length);
            for (int i = 0; i < d.length; ++i) data.addElement(d[i]);
            fireIntervalAdded(this, size, size + d.length);
        }

        public void removeData(Object[] d) {
            for (int i = 0; i < d.length; ++i) data.removeElement(d[i]);
            fireContentsChanged(this, 0, data.size() - 1);
        }
    }

    public SimpleChoiceGui() {
        init();
    }

    protected void addAllChoices() {
        choiceList.setSelectionInterval(0, Math.max(0, choiceModel.size() - 1));
        addChoices();
    }

    protected void addChoices() {
        int indices[] = choiceList.getSelectedIndices();
        Object selections[] = new Object[indices.length];
        for (int i = 0; i < indices.length; ++i) selections[i] = choiceModel.getTrueElementAt(indices[i]);
        chosenModel.appendData(selections);
        choiceModel.removeData(selections);
        choiceList.clearSelection();
    }

    public boolean chosenListWasModified() {
        return chosenListWasModified;
    }

    public JList getChoiceList() {
        return choiceList;
    }

    public Vector getChoices() {
        return choiceModel.getData();
    }

    public Vector getChosen() {
        return chosenModel.getData();
    }

    public JList getChosenList() {
        return chosenList;
    }

    public SorterComparisonIF getSorterComparison() {
        return comparison;
    }

    protected void hookupListeners() {
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addChoices();
                chosenListWasModified = true;
            }
        });
        addAllButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addAllChoices();
                chosenListWasModified = true;
            }
        });
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeChoices();
                chosenListWasModified = true;
            }
        });
        removeAllButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeAllChoices();
                chosenListWasModified = true;
            }
        });
    }

    protected void init() {
        chosenListWasModified = false;
        choiceModel = new MyListModel();
        choiceList = new JList(choiceModel);
        choiceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lhsScroll = new JScrollPane(choiceList);
        chosenModel = new MyListModel();
        chosenList = new JList(chosenModel);
        chosenList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        rhsScroll = new JScrollPane(chosenList);
        addButton = new JButton(">> ins");
        addButton.setOpaque(true);
        removeButton = new JButton("<< del");
        removeButton.setOpaque(true);
        addAllButton = new JButton("all");
        addAllButton.setOpaque(true);
        removeAllButton = new JButton("clear");
        removeAllButton.setOpaque(true);
        StretchLayout layout = new StretchLayout();
        setLayout(layout);
        StretchLayoutConstraints slc = new StretchLayoutConstraints();
        int btnX = 70, btnY = 20;
        slc.setScalars(5, -btnX / 2, 5 + btnY, btnX / 2);
        slc.setMultipliers(0, 0.5, 0, 0.5);
        layout.setConstraints(addButton, slc);
        slc.top = slc.bottom + 5;
        slc.bottom = slc.top + btnY;
        layout.setConstraints(removeButton, slc);
        slc.top = slc.bottom + 5;
        slc.bottom = slc.top + btnY;
        layout.setConstraints(addAllButton, slc);
        slc.top = slc.bottom + 5;
        slc.bottom = slc.top + btnY;
        layout.setConstraints(removeAllButton, slc);
        slc.setScalars(5, 5, -5, -5 - btnX / 2);
        slc.setMultipliers(0, 0, 1, 0.5);
        layout.setConstraints(lhsScroll, slc);
        slc.setScalars(5, 5 + btnX / 2, -5, -5);
        slc.setMultipliers(0, 0.5, 1, 1);
        layout.setConstraints(rhsScroll, slc);
        add(addButton);
        add(removeButton);
        add(addAllButton);
        add(removeAllButton);
        add(lhsScroll);
        add(rhsScroll);
        hookupListeners();
    }

    public static void main(String args[]) {
        System.out.println("Usage: all args become elements in the ChoicesGui");
        SimpleChoiceGui choice = new SimpleChoiceGui();
        choice.setSorterComparison(new SorterComparisonIF() {

            public int compare(Object lhs, Object rhs) {
                return ((String) lhs).compareTo((String) rhs);
            }
        });
        choice.setStringifier(new StringifyIF() {

            public String toString(Object o) {
                return ((String) o).toUpperCase();
            }
        });
        choice.setChoices(args);
        SimpleOkCancelDialog okCancel = new SimpleOkCancelDialog(null, "Test choice GUI", choice);
        okCancel.setBounds(100, 100, 300, 400);
        okCancel.setVisible(true);
        if (okCancel.isCancelled()) {
            System.out.println("Cancelled.");
        } else {
            Vector sel = choice.getChosen();
            System.out.println("Chosen " + sel.size() + " objects: ");
            for (int i = 0; i < sel.size(); ++i) {
                System.out.println("\t" + sel.elementAt(i));
            }
        }
        System.exit(0);
    }

    protected void removeAllChoices() {
        chosenList.setSelectionInterval(0, Math.max(0, chosenModel.size() - 1));
        removeChoices();
    }

    protected void removeChoices() {
        int indices[] = chosenList.getSelectedIndices();
        Object selections[] = new Object[indices.length];
        for (int i = 0; i < indices.length; ++i) selections[i] = chosenModel.getTrueElementAt(indices[i]);
        choiceModel.appendData(selections);
        chosenModel.removeData(selections);
        chosenList.clearSelection();
        if (comparison != null && choiceModel.size() != 0) {
            Object[] array = new Object[choiceModel.size()];
            choiceModel.getData().copyInto(array);
            setChoices(array);
        }
    }

    public void setChoices(Object[] ch) {
        setChoices(ch, false);
    }

    public void setChoices(Object[] ch, boolean okToModifyArray) {
        if (comparison != null && ch.length > 1) {
            if (!okToModifyArray) ch = (Object[]) ch.clone();
            Sorter.quickSort(ch, comparison);
        }
        choiceModel.setData(ch);
    }

    public void setChosen(Object[] ch) {
        chosenModel.appendData(ch);
        choiceModel.removeData(ch);
    }

    public void setSorterComparison(SorterComparisonIF c) {
        comparison = c;
    }

    public void setStringifier(StringifyIF s) {
        choiceModel.setStringifier(s);
        chosenModel.setStringifier(s);
    }
}
