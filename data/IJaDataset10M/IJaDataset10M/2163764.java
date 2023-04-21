package org.systemsbiology.PIPE2.client.view;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.DefaultHandlerRegistration;
import com.google.gwt.event.shared.GwtEvent;
import org.systemsbiology.PIPE2.client.PIPElets.PIPElet;
import java.util.ArrayList;

public class BroadcastButtonPanel extends Grid implements SourcesChangeEvents {

    private Button broadcastButton;

    private ListBox dataSourceLB;

    private ListBox targetsLB;

    private ChangeListenerCollection changeListeners;

    public BroadcastButtonPanel() {
        super(3, 2);
        targetsLB = new ListBox(false);
        dataSourceLB = new ListBox(false);
        dataSourceLB.setWidth("250px");
        broadcastButton = new Button("Broadcast", new ClickListener() {

            public void onClick(Widget widget) {
                raiseChangeEvent();
            }
        });
        HTML html1 = new HTML("Data: ");
        HTML html2 = new HTML("Target: ");
        setWidget(0, 0, html1);
        setWidget(1, 0, html2);
        setWidget(0, 1, dataSourceLB);
        setWidget(1, 1, targetsLB);
        setWidget(2, 1, broadcastButton);
    }

    private void raiseChangeEvent() {
        changeListeners.fireChange(this);
    }

    /**
	 * Adds a listener interface to receive change events.
	 *
	 * @param listener the listener interface to add
	 */
    public void addChangeListener(ChangeListener listener) {
        if (this.changeListeners == null) this.changeListeners = new ChangeListenerCollection();
        changeListeners.add(listener);
    }

    /**
	 * Removes a previously added listener interface.
	 *
	 * @param listener the listener interface to remove
	 */
    public void removeChangeListener(ChangeListener listener) {
        if (this.changeListeners != null) changeListeners.remove(listener);
    }

    public String getDataSource() {
        if (dataSourceLB.getItemCount() > 0) return dataSourceLB.getItemText(dataSourceLB.getSelectedIndex()); else return "";
    }

    public String getTarget() {
        if (targetsLB.getItemCount() > 0) return targetsLB.getItemText(targetsLB.getSelectedIndex()); else return "";
    }

    public void addDataSource(String newSource) {
        dataSourceLB.addItem(newSource);
    }

    public void setSources(ArrayList<String> newSources) {
        String currentSelection = getDataSource();
        dataSourceLB.clear();
        int currentSelectionsNewIdx = 0;
        for (int i = 0; i < newSources.size(); i++) {
            String newSource = newSources.get(i);
            dataSourceLB.addItem(newSource);
            if (newSource.equals(currentSelection)) currentSelectionsNewIdx = i;
        }
        dataSourceLB.setSelectedIndex(currentSelectionsNewIdx);
    }

    public void removeDataSource(String dataSource) {
        for (int i = 0; i < targetsLB.getItemCount(); i++) {
            if (targetsLB.getItemText(i).equals(dataSource)) {
                targetsLB.removeItem(i);
                break;
            }
        }
    }

    public void setTargets(ArrayList<String> newTargets) {
        String currentSelection = getTarget();
        targetsLB.clear();
        int currentSelectionsNewIdx = 0;
        for (int i = 0; i < newTargets.size(); i++) {
            String newTarget = newTargets.get(i);
            targetsLB.addItem(newTarget);
            if (newTarget.equals(currentSelection)) currentSelectionsNewIdx = i;
        }
        targetsLB.setSelectedIndex(currentSelectionsNewIdx);
    }

    public void addTarget(String newTarget) {
        targetsLB.addItem(newTarget);
    }

    public void removeTarget(String target) {
        for (int i = 0; i < targetsLB.getItemCount(); i++) {
            if (targetsLB.getItemText(i).equals(target)) {
                targetsLB.removeItem(i);
                break;
            }
        }
    }

    public void setSelectedDataSource(String selectSource) {
        for (int i = 0; i < dataSourceLB.getItemCount(); i++) if (dataSourceLB.getItemText(i).equals(selectSource)) dataSourceLB.setItemSelected(i, true);
    }
}
