package frostcode.icetasks.data;

import frostcode.icetasks.i18n.I18n;

public interface Entity<T extends Entity<T>> {

    public String getEntityTitle(final I18n i18n, final boolean plural);

    public State<T> getState();

    public void setState(State<T> template);

    public interface State<T> {
    }
}
