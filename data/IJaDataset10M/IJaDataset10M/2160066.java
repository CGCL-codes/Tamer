package org.springframework.richclient.core;

import org.springframework.richclient.progress.ProgressMonitor;

public interface Saveable extends Dirtyable {

    void save();

    void save(ProgressMonitor saveProgressTracker);

    void saveAs();

    boolean isSaveAsSupported();

    boolean isSaveOnCloseRecommended();
}
