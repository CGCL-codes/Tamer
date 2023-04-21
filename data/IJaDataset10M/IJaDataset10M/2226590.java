package org.jmeld.ui;

import org.jmeld.ui.search.*;

public interface JMeldContentPanelIF {

    public String getId();

    public void setId(String id);

    public boolean isSaveEnabled();

    public void doSave();

    public boolean checkSave();

    public boolean isUndoEnabled();

    public void doUndo();

    public boolean isRedoEnabled();

    public void doRedo();

    public void doLeft();

    public void doRight();

    public void doUp();

    public void doDown();

    public void doZoom(boolean direction);

    public void doGoToSelected();

    public void doGoToFirst();

    public void doGoToLast();

    public void doGoToLine(int line);

    public void doStopSearch();

    public SearchHits doSearch();

    public void doNextSearch();

    public void doPreviousSearch();

    public void doRefresh();

    public void doMergeMode(boolean mergeMode);

    public boolean checkExit();

    public String getSelectedText();
}
