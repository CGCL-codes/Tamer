package gtkwire.listener.connector;

import gtkwire.*;
import gtkwire.widget.GtkWidget;
import gtkwire.listener.FilteringGTKMessageListener;
import gtkwire.listener.MenuSelectionListener;

public class MenuSelectionListenerFilter extends FilteringGTKMessageListener {

    public MenuSelectionListenerFilter(MenuSelectionListener l) {
        super(l);
        addFilteringWidgetType(GtkWidget.WT_GtkCheckMenuItem);
        addFilteringWidgetType(GtkWidget.WT_GtkMenuItem);
        addFilteringWidgetType(GtkWidget.WT_GtkImageMenuItem);
        addFilteringSignal(GTKWireSignal.ACTIVATE);
        setMatchMode(this.MUST_MATCH_ALL);
    }

    public void filteredMessage(GTKWireMessage msg) {
        ((MenuSelectionListener) listener).menuItemSelected(msg);
    }
}
