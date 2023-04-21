package de.molimo.client;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 @author Marcus Schiesser
 */
public interface IResourceFactory {

    Object createLabel(Object resource, String style);

    Object createLabel(Object resource);

    Object createLabel(String style);

    Object createButton(Object resource, String style);

    Object createButton(String style);

    Object createButton(Object resource);

    Object createButton();

    Object createTemplateLayout(URL url);

    Object createTemplateLayout(String filename);

    Object createMultipleValueField(List possibleValues, Object defaultValue, boolean multiline);

    void changeResource(Object object, Object resource);

    void changeResource(Object object, String refResource);

    Object createToggleButton(Object resource);

    Object createToggleButton(Object resource, String style);

    Object createIcon(String refResource);

    void setResourceBundle(ResourceBundle resources);

    void displayMessage(String message);

    void displayMessage(Throwable throwable);

    Object createContent(Object value, Object action);

    boolean isPicture(Object oldChildren);

    String transformToString(Throwable throwable);
}
