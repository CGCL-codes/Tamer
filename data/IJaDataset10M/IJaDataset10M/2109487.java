package org.nakedobjects.plugins.dnd.view;

import org.nakedobjects.plugins.dnd.drawing.Bounds;
import org.nakedobjects.plugins.dnd.drawing.Location;

public class InteractionSpy {

    private int actionCount;

    private String damagedArea;

    private int event;

    private final String label[][] = new String[2][20];

    private final InteractionSpyWindow spyWindow;

    private final String[] trace = new String[60];

    private int traceIndex;

    private boolean isVisible;

    public InteractionSpy(final InteractionSpyWindow spyWindow) {
        this.spyWindow = spyWindow;
    }

    public void addAction(final String action) {
        if (isVisible) {
            set(actionCount++, "Action", action);
        }
    }

    public void addDamagedArea(final Bounds bounds) {
        if (isVisible) {
            damagedArea += bounds + "; ";
            set(7, "Damaged areas", damagedArea);
        }
    }

    public void addTrace(final String message) {
        if (isVisible && traceIndex < trace.length) {
            trace[traceIndex] = message;
            traceIndex++;
        }
    }

    public void addTrace(final View view, final String message, final Object object) {
        if (isVisible && traceIndex < trace.length) {
            trace[traceIndex] = view.getClass().getName() + " " + message + ": " + object;
            traceIndex++;
        }
    }

    public void close() {
        if (isVisible) {
            spyWindow.close();
            isVisible = false;
        }
    }

    public void reset() {
        if (isVisible) {
            event++;
            traceIndex = 0;
            actionCount = 8;
            damagedArea = "";
            setDownAt(null);
            for (int i = actionCount; i < label[0].length; i++) {
                label[0][i] = null;
                label[1][i] = null;
            }
        }
    }

    private void set(final int index, final String label, final Object debug) {
        if (spyWindow != null) {
            this.label[0][index] = debug == null ? null : label + ":";
            this.label[1][index] = debug == null ? null : debug.toString();
            spyWindow.display(event, this.label, trace, traceIndex);
        }
    }

    public void setAbsoluteLocation(final Location absoluteLocation) {
        if (isVisible) {
            set(6, "Absolute view location", absoluteLocation);
        }
    }

    public void setDownAt(final Location downAt) {
        if (isVisible) {
            set(0, "Down at", downAt);
        }
    }

    public void setLocationInView(final Location internalLocation) {
        if (isVisible) {
            set(3, "Relative mouse location", internalLocation);
        }
    }

    public void setLocationInViewer(final Location mouseLocation) {
        if (isVisible) {
            set(1, "Mouse location", mouseLocation);
        }
    }

    public void setOver(final Object data) {
        if (isVisible) {
            set(2, "Mouse over", data);
        }
    }

    public void setType(final ViewAreaType type) {
        if (isVisible) {
            set(4, "Area type", type);
        }
    }

    public void setViewLocation(final Location locationWithinViewer) {
        if (isVisible) {
            set(5, "View location", locationWithinViewer);
        }
    }

    public void open() {
        if (!isVisible) {
            spyWindow.open();
            isVisible = true;
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void redraw(final String redrawArea, final int redrawCount) {
        set(8, "Redraw", "#" + redrawCount + "  " + redrawArea);
        damagedArea = "";
    }
}
