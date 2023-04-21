package net.sourceforge.notepod.model;

public interface DirectoryNoteModel_Observer {

    public static final int TYPE_DIRECTORY_SELECTION_CHANGED = 1;

    public static final int TYPE_NOTE_SELECTION_CHANGED = 2;

    public void directoryNoteModelHasChanged(DirectoryNoteModel model, int type);
}
