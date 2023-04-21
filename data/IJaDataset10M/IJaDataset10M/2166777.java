package de.walware.eclipsecommons.ui.dialogs.groups;

import org.eclipse.swt.widgets.Composite;

public interface OptionsGroup {

    public abstract void createGroup(Composite parent, int hSpan);

    public abstract void initFields();
}
