package org.spantus.core.beans;

import org.spantus.core.IValues;

public interface IValueHolder<T extends IValues> {

    public T getValues();

    public void setValues(T values);

    public Double getSampleRate();

    public void setSampleRate(Double sampleRate);
}
