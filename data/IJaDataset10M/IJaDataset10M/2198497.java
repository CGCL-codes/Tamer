package org.nomadpim.module.timetracking.activity;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ActivityOperationMenuItemFactory {

    private ActivityControllerImages images;

    public ActivityOperationMenuItemFactory(ActivityControllerImages images) {
        this.images = images;
    }

    public void createMenuItem(Menu menu, final ActivityOperation operation) {
        String label = operation.getActivity().getLabel();
        label = ActivityFacade.getLastLabelPart(label);
        MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
        menuItem.setText(getTypeLabel(operation.getType()) + " \"" + label + "\"");
        menuItem.setImage(getTypeImage(operation.getType()));
        menuItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                operation.run();
            }
        });
    }

    private Image getTypeImage(ActivityOperationType type) {
        switch(type) {
            case START:
                return images.getImageStart();
            case STOP:
                return images.getImageStop();
            case PAUSE:
                return images.getImagePause();
            default:
                throw new RuntimeException("unknown actitity operation type: " + type);
        }
    }

    private String getTypeLabel(ActivityOperationType type) {
        switch(type) {
            case START:
                return "Start";
            case STOP:
                return "Stop";
            case PAUSE:
                return "Pause";
            default:
                throw new RuntimeException("unknown actitity operation type: " + type);
        }
    }
}
