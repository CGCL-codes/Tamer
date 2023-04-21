package org.td4j.core.binding.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.td4j.core.internal.binding.model.ListDataContainerConnector;
import org.td4j.core.model.Observable;
import ch.miranet.commons.TK;

public class ListDataContainer<T> extends Observable {

    private final Class<?> contentType;

    private final boolean canRead;

    private final boolean canWrite;

    private final String propertyName;

    private List<T> content;

    public ListDataContainer(Class<?> contentType, String propertyName) {
        this(contentType, propertyName, true, true);
    }

    public ListDataContainer(Class<?> contentType, String propertyName, boolean canRead, boolean canWrite) {
        this.contentType = TK.Objects.assertNotNull(contentType, "contentType");
        this.propertyName = TK.Strings.assertNotEmpty(propertyName, "propertyName");
        this.canRead = canRead;
        this.canWrite = canWrite;
    }

    public Class<?> getContentType() {
        return contentType;
    }

    public boolean canRead() {
        return canRead;
    }

    public boolean canWrite() {
        return canWrite;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public List<T> getContent() {
        if (content != null) {
            return content;
        } else {
            return Collections.emptyList();
        }
    }

    public void setContent(List<T> newContent) {
        if (this.content != null && TK.Objects.equal(this.content, newContent)) return;
        if (newContent != null) {
            this.content = newContent;
        } else {
            this.content = new ArrayList<T>();
        }
        changeSupport.fireStateChange();
    }

    public void clearContent() {
        this.content = new ArrayList<T>();
        changeSupport.fireStateChange();
    }

    public void addContent(T newContent) {
        TK.Objects.assertNotNull(newContent, "newContent");
        if (this.content == null) this.content = new ArrayList<T>();
        content.add(newContent);
        changeSupport.fireStateChange();
    }

    public void removeContent(T contentToRemove) {
        TK.Objects.assertNotNull(contentToRemove, "contentToRemove");
        if (this.content == null) return;
        content.remove(contentToRemove);
        changeSupport.fireStateChange();
    }

    public ListDataProxy createProxy() {
        final ListDataContainerConnector con = new ListDataContainerConnector(getContentType());
        final ListDataProxy proxy = new ListDataProxy(con, getPropertyName());
        proxy.setContext(this);
        return proxy;
    }

    @Override
    public String toString() {
        return propertyName;
    }
}
