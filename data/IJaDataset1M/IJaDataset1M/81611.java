package com.openclub.ui.dialog.outing;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.javizy.ui.widget.AutoDialog;
import org.javizy.ui.widget.DateListRenderer;
import org.javizy.ui.widget.HoursListModelAndRenderer;
import org.javizy.ui.widget.calendar.IDateEditor;
import org.javizy.ui.widget.calendar.JCalendar;
import org.javizy.utils.JavizyToolBox;
import com.openclub.locale.Localization;

public class AddOutingDialog extends AutoDialog implements PropertyChangeListener {

    private JCalendar calendar = new JCalendar(true, true);

    private JList datesList = new JList();

    private JList hoursList = new JList();

    public AddOutingDialog(JDialog frame) {
        super(frame, true, Localization.getString(Localization.OK), null, Localization.getString(Localization.CANCEL));
        initialize();
    }

    private void initialize() {
        Container container = getContent();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();
        calendar.setLocale(Localization.getLocale());
        calendar.setMinSelectableDate(Calendar.getInstance().getTime());
        calendar.getDayChooser().setAllowMultiSelection(true);
        calendar.getDayChooser().addPropertyChangeListener(IDateEditor.DAY, this);
        constr.gridheight = 1;
        constr.weightx = 1;
        constr.weighty = 1;
        constr.fill = GridBagConstraints.VERTICAL;
        constr.gridx = 0;
        constr.gridy = 0;
        constr.gridwidth = 2;
        container.add(JavizyToolBox.frameWith(calendar, Localization.getString(Localization.PRESELECT_SOME) + " " + Localization.getString(Localization.DATES)), constr);
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 0;
        constr.gridy = 1;
        constr.gridwidth = 1;
        constr.weighty = 10;
        constr.weightx = 2;
        container.add(JavizyToolBox.frameWith(new JScrollPane(datesList), Localization.getString(Localization.DATES)), constr);
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 1;
        constr.gridy = 1;
        constr.gridwidth = 1;
        constr.weighty = 10;
        constr.weightx = 1;
        container.add(JavizyToolBox.frameWith(new JScrollPane(hoursList), Localization.getString(Localization.HOURS)), constr);
        super.setSize(500, 700);
        JavizyToolBox.centerOnScreen(this);
        HoursListModelAndRenderer rendModel = new HoursListModelAndRenderer(8, 20, 2);
        hoursList.setFixedCellHeight(12);
        hoursList.setModel(rendModel);
        hoursList.setCellRenderer(rendModel);
        hoursList.addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent arg0) {
                if (arg0.isControlDown() || arg0.isAltDown() || arg0.isAltGraphDown() || arg0.isShiftDown()) {
                    if (arg0.getWheelRotation() > 0) {
                        ((HoursListModelAndRenderer) (hoursList.getModel())).increment();
                    } else if (arg0.getWheelRotation() < 0) {
                        ((HoursListModelAndRenderer) (hoursList.getModel())).decrement();
                    }
                }
            }
        });
        hoursList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                updatereturn();
            }
        });
        datesList.setCellRenderer(new DateListRenderer());
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        Object[] array = calendar.getDayChooser().getDays().toArray();
        datesList.setListData(array);
        datesList.setSelectionInterval(0, array.length - 1);
        updatereturn();
    }

    public void updatereturn() {
        Object[] array = datesList.getSelectedValues();
        int[] rows = hoursList.getSelectedIndices();
        for (Object date : array) {
            for (int row : rows) {
                Date newDate = ((HoursListModelAndRenderer) (hoursList.getModel())).setDate((Date) date, row);
                System.err.println(newDate);
            }
        }
    }

    public List<Date> getSelectedDates() {
        List<Date> retList = new ArrayList<Date>();
        Object[] array = datesList.getSelectedValues();
        int[] rows = hoursList.getSelectedIndices();
        for (Object date : array) {
            for (int row : rows) {
                Date newDate = ((HoursListModelAndRenderer) (hoursList.getModel())).setDate((Date) date, row);
                retList.add(newDate);
            }
        }
        return retList;
    }

    protected boolean okButtonPressed() {
        return true;
    }
}
