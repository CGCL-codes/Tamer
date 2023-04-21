package org.softmed.rest.admin.mediaType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import main.RESTEditor;
import org.softmed.rest.admin.handler.HandlerChooser;
import org.softmed.rest.admin.httpMethods.MethodActionPanel;
import org.softmed.rest.admin.interfaces.CreateListener;
import org.softmed.rest.admin.interfaces.EventListener;
import org.softmed.rest.admin.interfaces.FileHandler;
import org.softmed.rest.admin.interfaces.Recompiler;
import org.softmed.rest.admin.interfaces.SelectionPathChanged;
import org.softmed.rest.admin.interfaces.Validator;
import org.softmed.rest.admin.module.ValidationException;
import org.softmed.rest.admin.interfaces.RemoveListener;
import org.softmed.swing.IconManager;

public class MediaTypeEditor extends JPanel implements RemoveListener, FileHandler, SelectionPathChanged, CreateListener {

    MediaTypeSimpleEditor mediaTypeSimpleEditor = new MediaTypeSimpleEditor();

    org.softmed.rest.admin.httpMethods.MethodActionPanel actionPanel = new org.softmed.rest.admin.httpMethods.MethodActionPanel();

    private RemoveListener removeListener;

    FileHandler fileHandler;

    public MediaTypeEditor() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(540, 50));
        setMaximumSize(new Dimension(540, 50));
        setMinimumSize(new Dimension(540, 50));
        actionPanel.setFileHandler(this);
        actionPanel.setCreateListener(this);
        add(mediaTypeSimpleEditor);
        add(actionPanel);
        mediaTypeSimpleEditor.setRemoveListener(this);
    }

    public MediaTypeChooser getMediaTypeChooser() {
        return mediaTypeSimpleEditor.getMediaTypeChooser();
    }

    public Validator getValidator() {
        return mediaTypeSimpleEditor.getValidator();
    }

    public void setValidator(Validator validator) {
        mediaTypeSimpleEditor.setValidator(validator);
    }

    public Recompiler getRecompiler() {
        return mediaTypeSimpleEditor.getRecompiler();
    }

    public void setRecompiler(Recompiler recompiler) {
        mediaTypeSimpleEditor.setRecompiler(recompiler);
    }

    public HandlerChooser getHandlerChooser() {
        return mediaTypeSimpleEditor.getHandlerChooser();
    }

    public void validateData() throws ValidationException {
        mediaTypeSimpleEditor.validateData();
    }

    public RemoveListener getRemoveListener() {
        return removeListener;
    }

    public void setRemoveListener(RemoveListener removeListener) {
        this.removeListener = removeListener;
    }

    @Override
    public void remove(Object source) {
        removeListener.remove(this);
    }

    public void selectionPathChanged(boolean directory, String name, String parent) {
        actionPanel.selectionPathChanged(directory, name, parent);
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public String retrieveFilePath() {
        String path = fileHandler.retrieveFilePath();
        if (path.endsWith(".class") || path.endsWith(".java")) {
            int index = path.lastIndexOf('.');
            path = path.substring(1, index);
            path = path.replace('/', '.');
        }
        mediaTypeSimpleEditor.handlerChooser.getHandlerPath().setText(path);
        return null;
    }

    @Override
    public void created() {
        String path = RESTEditor.RESTEditor.createFileWithPopup();
        mediaTypeSimpleEditor.handlerChooser.getHandlerPath().setText(path);
    }
}
