package com.leclercb.taskunifier.gui.components.views;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.MainView;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ViewList implements ListChangeSupported, PropertyChangeSupported {

    public static final String PROP_CURRENT_VIEW = "currentView";

    private static ViewList INSTANCE = null;

    public static ViewList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewList();
        }
        return INSTANCE;
    }

    private ListChangeSupport listChangeSupport;

    private PropertyChangeSupport propertyChangeSupport;

    private List<ViewItem> views;

    private ViewItem currentView;

    private ViewItem mainTaskView;

    private ViewItem mainNoteView;

    private ViewItem mainCalendarView;

    public ViewList() {
        this.listChangeSupport = new ListChangeSupport(this);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.views = new ArrayList<ViewItem>();
        this.mainTaskView = new ViewItem(ViewType.TASKS, Translations.getString("general.tasks"), ImageUtils.getResourceImage("task.png", 16, 16), MainFrame.getInstance().getFrameId(), false);
        this.mainNoteView = new ViewItem(ViewType.NOTES, Translations.getString("general.notes"), ImageUtils.getResourceImage("note.png", 16, 16), MainFrame.getInstance().getFrameId(), false);
        this.mainCalendarView = new ViewItem(ViewType.CALENDAR, Translations.getString("general.calendar"), ImageUtils.getResourceImage("calendar.png", 16, 16), MainFrame.getInstance().getFrameId(), false);
        this.addView(this.mainTaskView);
        this.addView(this.mainNoteView);
        this.addView(this.mainCalendarView);
        this.setCurrentView(this.mainTaskView);
    }

    public void initializeMainViews(MainView mainView) {
        this.mainTaskView.setView(new DefaultTaskView(mainView));
        this.mainNoteView.setView(new DefaultNoteView(mainView));
        this.mainCalendarView.setView(new DefaultCalendarView(mainView));
    }

    public ViewItem getMainTaskView() {
        return this.mainTaskView;
    }

    public ViewItem getMainNoteView() {
        return this.mainNoteView;
    }

    public ViewItem getMainCalendarView() {
        return this.mainCalendarView;
    }

    public ViewItem getCurrentView() {
        return this.currentView;
    }

    public void setCurrentView(ViewItem currentView) {
        this.setCurrentView(currentView, false);
    }

    public void setCurrentView(ViewItem currentView, boolean force) {
        CheckUtils.isNotNull(currentView);
        if (!force && currentView.equals(this.currentView)) return;
        if (!this.views.contains(currentView)) throw new RuntimeException("View list doesn't contain view \"" + currentView.getLabel() + "\"");
        ViewItem oldCurrentView = this.currentView;
        if (force) oldCurrentView = null;
        this.currentView = currentView;
        this.propertyChangeSupport.firePropertyChange(PROP_CURRENT_VIEW, oldCurrentView, currentView);
    }

    public int getIndexOf(ViewItem view) {
        return this.views.indexOf(view);
    }

    public ViewItem getView(int index) {
        return this.views.get(index);
    }

    public ViewItem[] getViews() {
        return this.views.toArray(new ViewItem[0]);
    }

    public int getViewCount() {
        return this.views.size();
    }

    public void addView(ViewItem view) {
        CheckUtils.isNotNull(view);
        if (this.views.contains(view)) return;
        this.views.add(view);
        int index = this.views.indexOf(view);
        this.listChangeSupport.fireListChange(ListChangeEvent.VALUE_ADDED, index, view);
    }

    public void removeView(ViewItem view) {
        CheckUtils.isNotNull(view);
        if (!view.isRemovable()) throw new RuntimeException("You cannot remove this view");
        int index = this.views.indexOf(view);
        if (this.views.remove(view)) {
            if (view.equals(this.currentView)) this.setCurrentView(this.mainTaskView);
            this.listChangeSupport.fireListChange(ListChangeEvent.VALUE_REMOVED, index, view);
        }
    }

    @Override
    public void addListChangeListener(ListChangeListener listener) {
        this.listChangeSupport.addListChangeListener(listener);
    }

    @Override
    public void removeListChangeListener(ListChangeListener listener) {
        this.listChangeSupport.removeListChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
