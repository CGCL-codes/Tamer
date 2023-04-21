package org.intellij.plugins.nativeNeighbourhood.dummyEditor;

import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import javax.swing.*;
import java.beans.PropertyChangeListener;
import org.jetbrains.annotations.NotNull;

/**
 * Dummy Editor
 *
 * Contains two workarounds in {@link #selectNotify}
 *
 * @author frb
 */
public class NativeFileEditor implements FileEditor {

    private JLabel label = new JLabel();

    private Project project;

    private VirtualFile virtualFile;

    private VirtualFile lastActiveVirtualFile;

    public VirtualFile getVirtualFile() {
        return virtualFile;
    }

    public NativeFileEditor(Project pProject, VirtualFile pFile, VirtualFile pLastFile) {
        project = pProject;
        virtualFile = pFile;
        lastActiveVirtualFile = pLastFile;
    }

    /**
     * @return component which represents editor in the UI.
     *         The method should never return <code>null</code>.
     */
    @NotNull
    public JComponent getComponent() {
        return label;
    }

    /**
     * Returns component to be focused when editor is opened. Method should never return null.
     */
    public JComponent getPreferredFocusedComponent() {
        return label;
    }

    /**
     * @return editor's name, a string that identifies editor among
     *         other editors. For example, UI form might have two editor: "GUI Designer"
     *         and "Text". So "GUI Designer" can be a name of one editor and "Text"
     *         can be a name of other editor. The method should never return <code>null</code>.
     */
    @NotNull
    public String getName() {
        return "Native Neighbourhood";
    }

    /**
     * @return editor's internal state. Method should never return <code>null</code>.
     */
    @NotNull
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return new NativeFileEditorState();
    }

    /**
     * Applies given state to the editor.
     *
     * @param state cannot be null
     */
    public void setState(@NotNull FileEditorState state) {
    }

    /**
     * @return whether the editor's content is modified in comparision with its virtualFile.
     */
    public boolean isModified() {
        return false;
    }

    /**
     * @return whether the editor is valid or not. For some reasons
     *         editor can become invalid. For example, text editor becomes invalid when its virtualFile is deleted.
     */
    public boolean isValid() {
        return true;
    }

    /**
     * This method is invoked each time when the editor is selected.
     * This can happen in two cases: editor is selected because the selected virtualFile
     * has been changed or editor for the selected virtualFile has been changed.
     */
    public void selectNotify() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                FileEditorManager tManager = FileEditorManager.getInstance(project);
                if (tManager.isFileOpen(virtualFile)) {
                    tManager.closeFile(virtualFile);
                }
                if (lastActiveVirtualFile != null) {
                    tManager.openFile(lastActiveVirtualFile, true);
                }
            }
        });
    }

    /**
     * This method is invoked each time when the editor is deselected.
     */
    public void deselectNotify() {
    }

    /**
     * Removes specified listener
     *
     * @param listener to be added
     */
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    /**
     * Adds specified listener
     *
     * @param listener to be removed
     */
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    /**
     * @return highlighter object to perform background analysis and highlighting activities.
     *         Return <code>null</code> if no background highlighting activity necessary for this virtualFile editor.
     */
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    /**
     * The method is optional. Currently is used only by find usages subsystem
     *
     * @return the location of user focus. Typically it's a caret or any other form of selection start.
     */
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    public StructureViewBuilder getStructureViewBuilder() {
        return null;
    }

    public <T> T getUserData(Key<T> key) {
        return null;
    }

    public <T> void putUserData(Key<T> key, T value) {
    }

    public void dispose() {
    }
}
