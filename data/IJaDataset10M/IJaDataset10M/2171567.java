package orcajo.azada.core.model;

import org.eclipse.core.runtime.IAdaptable;

public interface Mode extends IAdaptable {

    public enum Drill {

        DRILL_MEMBER, DRILL_POSITION, DRILL_REPLACE
    }

    public Drill getDrill();

    public boolean isDrillThrough();
}
