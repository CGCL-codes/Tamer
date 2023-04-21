package org.archive.settings.file;

import org.archive.settings.path.PathChange;

public interface PathChangeListener {

    public void change(PathChange pc);
}
