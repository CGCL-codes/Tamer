package com.viithiisys.example.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.eclipse.core.databinding.observable.list.WritableList;

public interface ExampleModel {

    public WritableList getExamples();

    public List<ExampleType> getTypes() throws Exception;

    public List<Status> getStatuses() throws Exception;

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void setId(String string);

    public void addExample(Example example);
}
