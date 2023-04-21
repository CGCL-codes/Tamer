package org.vmasterdiff.gui.filediff.highlighter;

import java.util.List;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;
import org.vmasterdiff.gui.filediff.api.DiffHighlightModel;
import org.vmasterdiff.gui.filediff.api.DiffHighlighterModel;
import org.vmasterdiff.gui.filediff.api.HighlightEvent;
import org.vmasterdiff.gui.filediff.api.HighlightListener;
import org.vmasterdiff.gui.filediff.api.LinedDocument;
import org.vmasterdiff.gui.filediff.api.Positions;
import org.vmasterdiff.gui.filediff.highlighter.highlight.DefaultDiffHighlightModel;
import org.vmasterdiff.gui.filediff.misc.MergeStateMap;
import org.vmasterdiff.lib.TriChunk;
import biz.xsoftware.statemachine.State;
import biz.xsoftware.statemachine.StateMachineAction;
import biz.xsoftware.thread.Runner;

public class DefaultDiffHighlighterModel implements DiffHighlighterModel, HighlightListener {

    private EventListenerList listenerList = new EventListenerList();

    private LinedDocument parentDoc = null;

    private LinedDocument childADoc = null;

    private LinedDocument childBDoc = null;

    private LinedDocument mergedDoc = null;

    private List parentLines = null;

    private List childALines = null;

    private List childBLines = null;

    private List mergedLines = null;

    private int currentIndex = -1;

    private Vector highlights = new Vector();

    public void installDocuments(LinedDocument parentDoc, LinedDocument childADoc, LinedDocument childBDoc, LinedDocument mergedDoc) {
        if (parentDoc == null || childADoc == null || childBDoc == null || mergedDoc == null) throw new IllegalArgumentException("None of the parameters in this function can be null");
        this.parentDoc = parentDoc;
        this.childADoc = childADoc;
        this.childBDoc = childBDoc;
        this.mergedDoc = mergedDoc;
    }

    public void installLineNumberModels(List parentLines, List childALines, List childBLines, List mergedLines) {
        if (parentLines == null || childALines == null || childBLines == null || mergedLines == null) throw new IllegalArgumentException("None of the parameters in this function can be null");
        this.parentLines = parentLines;
        this.childALines = childALines;
        this.childBLines = childBLines;
        this.mergedLines = mergedLines;
    }

    public DiffHighlightModel addHighlight(Positions p, int diffType, State mergeState) throws BadLocationException {
        if (parentDoc == null || childADoc == null || childBDoc == null || mergedDoc == null) throw new IllegalArgumentException("installDocuments must be called before calling this function.");
        if (!TriChunk.isValidType(diffType)) throw new IllegalArgumentException("diffType is not valid.  It must be one of TriChunk.ADD_ADD,\n" + "TriChunk.SAME_CHANGE, etc");
        DefaultDiffHighlightModel m = new DefaultDiffHighlightModel(mergeState);
        m.setDiffType(diffType);
        m.addHighlightListener(this);
        m.installDocuments(parentDoc, childADoc, childBDoc, mergedDoc);
        m.setParentPositions(parentDoc.createPosition(p.getParentStartOffset()), parentDoc.createPosition(p.getParentEndOffset()));
        m.setChildAPositions(childADoc.createPosition(p.getChildAStartOffset()), childADoc.createPosition(p.getChildAEndOffset()));
        m.setChildBPositions(childBDoc.createPosition(p.getChildBStartOffset()), childBDoc.createPosition(p.getChildBEndOffset()));
        m.setMergedPositions(mergedDoc.createPosition(p.getMergedStartOffset()), mergedDoc.createPosition(p.getMergedEndOffset()));
        highlights.addElement(m);
        fireHighlightAdded(m);
        fireModelChange();
        return m;
    }

    public void removeHighlight(DiffHighlightModel m) {
        for (int i = 0; i < highlights.size(); i++) {
            if (m == highlights.get(i)) {
                highlights.remove(i);
                fireHighlightRemoved(m);
                fireModelChange();
            }
        }
    }

    public void removeAllHighlights() {
        Vector old = highlights;
        highlights = new Vector();
        for (int i = 0; i < old.size(); i++) fireHighlightRemoved((DiffHighlightModel) old.get(i));
        fireModelChange();
    }

    public int getNumberHighlights() {
        return highlights.size();
    }

    public int getCurrentHighlightIndex() {
        return currentIndex;
    }

    public DiffHighlightModel getHighlightModel(int index) {
        return (DiffHighlightModel) highlights.get(index);
    }

    public boolean hasNextDiff() {
        if (currentIndex < getNumberHighlights() - 1) return true;
        return false;
    }

    public boolean hasPrevDiff() {
        if (currentIndex > 0) return true;
        return false;
    }

    public void moveToNextDiff() {
        if (currentIndex >= 0) getHighlightModel(currentIndex).setSelected(false);
        if (currentIndex < getNumberHighlights() - 1) getHighlightModel(++currentIndex).setSelected(true);
    }

    public void moveToPrevDiff() {
        if (currentIndex > 0) {
            getHighlightModel(currentIndex).setSelected(false);
            getHighlightModel(--currentIndex).setSelected(true);
        }
    }

    public int getDiffType() {
        if (currentIndex < 0) return TriChunk.SAME_SAME;
        return getHighlightModel(currentIndex).getDiffType();
    }

    public void setLineNumbersOn(boolean on) {
        fireModelChange();
    }

    public void setIndiceOn(boolean on) {
        fireModelChange();
    }

    public boolean isLineNumbersOn() {
        return true;
    }

    public boolean isIndiceOn() {
        return true;
    }

    public boolean isMerging() {
        return true;
    }

    public boolean isAllDifferencesMerged() {
        for (int i = 0; i < getNumberHighlights(); i++) {
            if (getHighlightModel(i).getMergeState() == MergeStateMap.UNMERGED) return false;
        }
        return true;
    }

    public void fireMergeAction(StateMachineAction action) {
        getHighlightModel(currentIndex).fireMergeAction(action);
        fireModelChange();
    }

    public State getMergeState() {
        if (currentIndex < 0) return MergeStateMap.UNMERGABLE;
        return getHighlightModel(currentIndex).getMergeState();
    }

    public boolean hasNextUnmergedDiff() {
        boolean hasUnmergedDiff = false;
        for (int i = currentIndex + 1; i < getNumberHighlights(); i++) {
            if (getHighlightModel(i).getMergeState() == MergeStateMap.UNMERGED) {
                hasUnmergedDiff = true;
                break;
            }
        }
        return hasUnmergedDiff;
    }

    public boolean hasPrevUnmergedDiff() {
        boolean hasUnmergedDiff = false;
        for (int i = currentIndex - 1; i >= 0; i--) {
            if (getHighlightModel(i).getMergeState() == MergeStateMap.UNMERGED) {
                hasUnmergedDiff = true;
                break;
            }
        }
        return hasUnmergedDiff;
    }

    public void moveToNextUnmergedDiff() {
        for (int i = currentIndex + 1; i < getNumberHighlights(); i++) {
            DiffHighlightModel m = getHighlightModel(i);
            if (m.getMergeState() == MergeStateMap.UNMERGED) {
                if (currentIndex >= 0) {
                    getHighlightModel(currentIndex).setSelected(false);
                    fireHighlightChanged(getHighlightModel(currentIndex));
                }
                currentIndex = i;
                m.setSelected(true);
                fireModelChange();
                fireHighlightChanged(m);
                break;
            }
        }
    }

    public void moveToPrevUnmergedDiff() {
        for (int i = currentIndex - 1; i >= 0; i--) {
            DiffHighlightModel m = getHighlightModel(i);
            if (m.getMergeState() == MergeStateMap.UNMERGED) {
                getHighlightModel(currentIndex).setSelected(false);
                fireHighlightChanged(getHighlightModel(currentIndex));
                currentIndex = i;
                getHighlightModel(currentIndex).setSelected(true);
                fireModelChange();
                fireHighlightChanged(m);
                break;
            }
        }
    }

    public void highlightAdded(HighlightEvent evt) {
    }

    public void highlightRemoved(HighlightEvent evt) {
    }

    public void highlightChanged(HighlightEvent evt) {
        fireHighlightChanged(evt.getHighlightModel());
        fireModelChange();
    }

    public void addHighlightListener(HighlightListener l) {
        listenerList.add(HighlightListener.class, l);
    }

    public void removeHighlightListener(HighlightListener l) {
        listenerList.remove(HighlightListener.class, l);
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireModelChange() {
        Runnable fireEvent = new Runnable() {

            public void run() {
                ChangeEvent event = new ChangeEvent(this);
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == ChangeListener.class) ((ChangeListener) listeners[i + 1]).stateChanged(event);
                }
            }
        };
        if (Runner.isEventThread()) fireEvent.run(); else Runner.runOnEventThreadLater(fireEvent);
    }

    protected void fireHighlightAdded(DiffHighlightModel m) {
        final HighlightEvent event = new HighlightEvent(m);
        Runnable fireEvent = new Runnable() {

            public void run() {
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == HighlightListener.class) ((HighlightListener) listeners[i + 1]).highlightAdded(event);
                }
            }
        };
        if (Runner.isEventThread()) fireEvent.run(); else Runner.runOnEventThreadLater(fireEvent);
    }

    protected void fireHighlightRemoved(DiffHighlightModel m) {
        final HighlightEvent event = new HighlightEvent(m);
        Runnable fireEvent = new Runnable() {

            public void run() {
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == HighlightListener.class) ((HighlightListener) listeners[i + 1]).highlightRemoved(event);
                }
            }
        };
        if (Runner.isEventThread()) fireEvent.run(); else Runner.runOnEventThreadLater(fireEvent);
    }

    protected void fireHighlightChanged(DiffHighlightModel m) {
        final HighlightEvent event = new HighlightEvent(m);
        Runnable fireEvent = new Runnable() {

            public void run() {
                Object[] listeners = listenerList.getListenerList();
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == HighlightListener.class) ((HighlightListener) listeners[i + 1]).highlightChanged(event);
                }
            }
        };
        if (Runner.isEventThread()) fireEvent.run(); else Runner.runOnEventThreadLater(fireEvent);
    }
}
