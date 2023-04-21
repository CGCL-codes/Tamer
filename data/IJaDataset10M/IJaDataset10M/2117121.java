package com.safi.workshop.audio;

import com.safi.db.server.config.Prompt;

public interface PromptChangeListener {

    public abstract void promptModified(Prompt prompt, boolean pathChanged);

    public abstract void promptAdded(Prompt prompt);

    public abstract void promptRemoved(Prompt prompt);
}
