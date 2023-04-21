package gtkwire.listener.connector;

import gtkwire.*;
import gtkwire.widget.GtkWidget;
import gtkwire.listener.FilteringGTKMessageListener;
import gtkwire.listener.EntryListener;

public class EntryListenerFilter extends FilteringGTKMessageListener {

    public EntryListenerFilter(EntryListener l) {
        super(l);
        addFilteringWidgetType(GtkWidget.WT_GtkEntry);
        addFilteringWidgetType(GtkWidget.WT_GtkSpinButton);
        setMatchMode(this.MUST_MATCH_NAME_AND_WIDGET);
    }

    public void filteredMessage(GTKWireMessage msg) {
        ((EntryListener) listener).entryContentsChanged(msg);
    }
}
