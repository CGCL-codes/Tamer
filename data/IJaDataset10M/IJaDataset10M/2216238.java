package org.softmed.rest.admin.resource;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.softmed.rest.admin.DetailLevelListener;
import org.softmed.rest.admin.DetailChooser.DetailLevel;
import org.softmed.rest.admin.interfaces.EventListener;
import org.softmed.rest.admin.interfaces.FileHandler;
import org.softmed.rest.admin.interfaces.RemoveListener;
import org.softmed.rest.admin.interfaces.SelectionPathChanged;
import org.softmed.rest.admin.interfaces.Validator;
import org.softmed.rest.admin.module.ValidationException;
import restlet.description.convert.RESTResourceDescription;

public class ResourceEditor extends JPanel implements RemoveListener, FileHandler, SelectionPathChanged, DetailLevelListener {

    RESTResourceDescription resourceDescription;

    ResourceHeader resourceHeader = new ResourceHeader();

    ResourceHandlerChooser resourceHandlerChooser = new ResourceHandlerChooser();

    ResourceMethodsManager restMethodsManager = new ResourceMethodsManager();

    ResourceActionPanel actionPanel = new ResourceActionPanel();

    EventListener listener;

    Validator validator;

    FileHandler fileHandler;

    public ResourceEditor() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMinimumSize(new Dimension(100, 25));
        resourceHeader.setRemoveListener(this);
        actionPanel.setFileHandler(this);
        actionPanel.setDetailListener(this);
        add(resourceHeader);
        add(actionPanel);
        add(resourceHandlerChooser);
        add(restMethodsManager);
        detailLevelChanged(DetailLevel.HANDLER);
        actionPanel.setDetailLevel(DetailLevel.HANDLER);
    }

    public void setResourceDescription(RESTResourceDescription resource) {
        this.resourceDescription = resource;
        resourceHeader.setResourceDescription(resource);
        restMethodsManager.setResourceDescription(resourceDescription);
        resourceHandlerChooser.setResourceDescription(resourceDescription);
        if (resource.hasMediaTypeHandlers()) {
        } else {
        }
    }

    public RESTResourceDescription getResourceDescription() {
        return resourceDescription;
    }

    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void remove(Object source) {
        listener.listen(this);
    }

    public void validateData() throws ValidationException {
        resourceHeader.validateData();
        restMethodsManager.validateData();
    }

    public void selectionPathChanged(boolean directory, String name, String parent) {
        restMethodsManager.selectionPathChanged(directory, name, parent);
        resourceHandlerChooser.selectionPathChanged(directory, name, parent);
        if (name != null) actionPanel.retrieve.setEnabled(true);
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        restMethodsManager.setFileHandler(fileHandler);
        resourceHandlerChooser.setFileHandler(fileHandler);
    }

    @Override
    public String retrieveFilePath() {
        String path = fileHandler.retrieveFilePath();
        int index = path.lastIndexOf('.');
        if (index > 0 && index - path.length() < 3) path = path.substring(0, index);
        resourceHeader.resource.setText(path);
        return null;
    }

    @Override
    public void detailLevelChanged(DetailLevel detailLevel) {
        switch(detailLevel) {
            case RESOURCE:
                resourceHandlerChooser.setVisible(false);
                restMethodsManager.setVisible(false);
                break;
            case HANDLER:
                resourceHandlerChooser.setVisible(true);
                restMethodsManager.setVisible(false);
                break;
            case METHODS:
                resourceHandlerChooser.setVisible(true);
                restMethodsManager.setVisible(true);
                restMethodsManager.detailLevelChanged(detailLevel);
                break;
            case MEDIA_TYPE:
                resourceHandlerChooser.setVisible(true);
                restMethodsManager.setVisible(true);
                restMethodsManager.detailLevelChanged(detailLevel);
                break;
        }
        revalidate();
        repaint();
    }

    public void setDetailLevel(DetailLevel detailLevel) {
        detailLevelChanged(detailLevel);
        actionPanel.setDetailLevel(detailLevel);
    }
}
