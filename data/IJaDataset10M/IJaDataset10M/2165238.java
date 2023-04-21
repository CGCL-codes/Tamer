package com.phloc.webbasics.app.menu;

import javax.annotation.Nonnull;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.idfactory.GlobalIDFactory;

/**
 * Default implementation of the {@link IMenuSeparator} interface.
 * 
 * @author philip
 */
public final class MenuSeparator extends AbstractMenuObject implements IMenuSeparator {

    public MenuSeparator() {
        this(GlobalIDFactory.getNewStringID());
    }

    public MenuSeparator(@Nonnull @Nonempty final String sID) {
        super(sID);
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
