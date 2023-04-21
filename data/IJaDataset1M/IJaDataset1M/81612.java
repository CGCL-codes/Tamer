package org.formaria.editor;

/**
 * An interface that allows objects to be marked as modified
 * <p> Copyright (c) Formaria Ltd., 2002-2003</p>
 * <p> $Revision: 1.1 $</p>
 * <p> License: see License.txt</p>
 */
public interface Modifiable {

    public void setModified(boolean newState);

    public boolean isModified();

    public Notifiable getNotifiable();
}
