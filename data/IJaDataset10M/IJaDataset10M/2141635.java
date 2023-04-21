package org.swing.datechooser.view;

import org.swing.datechooser.beans.DateChooserPanel;
import org.swing.datechooser.controller.DateChooseController;
import org.swing.datechooser.model.DateChoose;
import org.swing.datechooser.view.appearance.AppearancesList;
import org.swing.datechooser.view.appearance.ViewAppearance;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Date selection panel. DateChooserPanel simply extends it and provides
 * some simple configuration methods.<br>
 * ������ ��� ������ ����. ��������������� ��������� ������ ��������� ��
 * � ������������� ��� ������� ������� ��������� �������.
 *
 * @author Androsov Vadim
 * @since 1.0
 */
public class CalendarPane extends JPanel implements PropertyChangeListener {

    private GridPane gp;

    private AbstractNavigatePane[] navigPanes;

    private DateChoose model;

    private Locale locale;

    private int currentNavigateIndex = 0;

    private boolean todayButtonVisible = false;

    private JButton todayButton;

    private String[] shortMonth;

    private Calendar calendar;

    public void initialize(DateChoose model, DateChooseController controller) {
        gp = new GridPane();
        setLayout(new BorderLayout());
        todayButton = new JButton(getTodayText());
        todayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                getModel().setSelectedDate(calendar);
            }
        });
        navigPanes = new AbstractNavigatePane[] { new ComboNavigatePane(), new ButtonNavigatePane() };
        todayButton.setFont(navigPanes[1].getFont());
        setModel(model);
        gp.setModel(model);
        setLocale(Locale.getDefault());
        gp.setController(controller);
        if (todayButtonVisible) add(todayButton, BorderLayout.SOUTH);
        add(gp, BorderLayout.CENTER);
        setCurrentNavigateIndex(0);
        add(getCurrentNavigPane(), BorderLayout.NORTH);
    }

    private AbstractNavigatePane getCurrentNavigPane() {
        return navigPanes[getCurrentNavigateIndex()];
    }

    private void setAllNavigCurrentModel() {
        for (int i = 0; i < navigPanes.length; i++) {
            navigPanes[i].setModel(getModel());
        }
    }

    public void reInitialize(DateChoose model, DateChooseController controller) {
        if (gp == null) return;
        setModel(model);
        gp.setModel(model);
        gp.setController(controller);
    }

    public CalendarPane(DateChoose model, DateChooseController controller) {
        initialize(model, controller);
    }

    public CalendarPane() {
    }

    public DateChoose getModel() {
        return model;
    }

    public void setModel(DateChoose model) {
        DateChoose oldModel = getModel();
        if (getModel() != null) {
            getModel().removePropertyChangeListener(this);
        }
        this.model = model;
        setAllNavigCurrentModel();
        getModel().addPropertyChangeListener(this);
        firePropertyChange("model", oldModel, model);
    }

    public Font getNavigateFont() {
        return getCurrentNavigPane().getFont();
    }

    public void setNavigateFont(Font font) {
        Font oldFont = getNavigateFont();
        for (int i = 0; i < navigPanes.length; i++) {
            navigPanes[i].setFont(font);
        }
        updateUI();
        firePropertyChange(DateChooserPanel.PROPERTY_NAVIG_FONT, oldFont, font);
    }

    public ViewAppearance getCurrentCellAppearance() {
        return gp.getAppearance();
    }

    public AppearancesList getAppearancesList() {
        return gp.getAppearanceList();
    }

    public void setAppearancesList(AppearancesList aList) {
        AppearancesList oldView = getAppearancesList();
        gp.setAppearanceList(aList);
        firePropertyChange(DateChooserPanel.PROPERTY_VIEW, oldView, aList);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        if ((getLocale() != null) && (getLocale().equals(locale))) return;
        Locale oldLocale = getLocale();
        this.locale = locale;
        model.setLocale(locale);
        gp.setLocale(locale);
        for (AbstractNavigatePane pane : navigPanes) {
            pane.setLocale(locale);
        }
        DateFormatSymbols dateSymbols = new DateFormatSymbols(getLocale());
        shortMonth = dateSymbols.getShortMonths();
        firePropertyChange(DateChooserPanel.PROPERTY_LOCALE, oldLocale, locale);
    }

    public int getCurrentNavigateIndex() {
        return currentNavigateIndex;
    }

    public void setCurrentNavigateIndex(int currentNavigateIndex) {
        int newPaneIndex = 0;
        if ((currentNavigateIndex >= 0) && (currentNavigateIndex < navigPanes.length)) {
            newPaneIndex = currentNavigateIndex;
        }
        if (newPaneIndex != getCurrentNavigateIndex()) {
            int oldIndex = getCurrentNavigateIndex();
            remove(getCurrentNavigPane());
            this.currentNavigateIndex = currentNavigateIndex;
            add(getCurrentNavigPane(), BorderLayout.NORTH);
            revalidate();
            getCurrentNavigPane().setEnabled(getModel().isEnabled());
            firePropertyChange(DateChooserPanel.PROPERTY_NAVIG_PANE, oldIndex, newPaneIndex);
        }
    }

    public WeekDaysStyle getWeekStyle() {
        return gp.getWeekStyle();
    }

    public void setWeekStyle(WeekDaysStyle weekStyle) {
        WeekDaysStyle oldStyle = getWeekStyle();
        gp.setWeekStyle(weekStyle);
        firePropertyChange(DateChooserPanel.PROPERTY_WEEK_STYLE, oldStyle, weekStyle);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        DateChoose model = getModel();
        gp.setEnabled(model.isEnabled());
        AbstractNavigatePane navig = getCurrentNavigPane();
        navig.setNothingSelectEnabled(model.isNothingAllowed());
        navig.setEnabled(model.isEnabled());
    }

    public void setGridBackground(Color color) {
        gp.setBackground(color);
    }

    public Color getGridBackground() {
        return gp.getBackground();
    }

    public boolean isTodayButtonVisible() {
        return todayButtonVisible;
    }

    public void setTodayButtonVisible(boolean todayButtonVisible) {
        final boolean oldTodayButtonVisible = isTodayButtonVisible();
        this.todayButtonVisible = todayButtonVisible;
        if (todayButtonVisible != oldTodayButtonVisible) {
            if (todayButtonVisible) {
                add(todayButton, BorderLayout.SOUTH);
            } else {
                remove(todayButton);
            }
            revalidate();
            todayButton.setEnabled(getModel().isEnabled());
            firePropertyChange(DateChooserPanel.PROPERTY_TODAY_PANE, oldTodayButtonVisible, todayButtonVisible);
        }
    }

    public String getTodayText() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy ");
        calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Requests that this <code>Component</code> gets the input focus.
     * Refer to {@link java.awt.Component#requestFocus()
     * Component.requestFocus()} for a complete description of
     * this method.
     * <p/>
     * Note that the use of this method is discouraged because
     * its behavior is platform dependent. Instead we recommend the
     * use of {@link #requestFocusInWindow() requestFocusInWindow()}.
     * If you would like more information on focus, see
     * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html">
     * How to Use the Focus Subsystem</a>,
     * a section in <em>The Java Tutorial</em>.
     *
     * @see java.awt.Component#requestFocusInWindow()
     * @see java.awt.Component#requestFocusInWindow(boolean)
     * @since 1.4
     */
    @Override
    public void requestFocus() {
        gp.requestFocus();
    }

    public void setFocusableForComboElements(boolean focusable) {
        todayButton.setFocusable(focusable);
        gp.setFocusable(false);
    }
}
