package org.gamegineer.table.internal.ui;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import javax.swing.Icon;
import net.jcip.annotations.Immutable;
import org.gamegineer.table.core.CardSurfaceDesignId;
import org.gamegineer.table.ui.ICardSurfaceDesignUI;

/**
 * Implementation of {@link org.gamegineer.table.ui.ICardSurfaceDesignUI}.
 */
@Immutable
public final class CardSurfaceDesignUI implements ICardSurfaceDesignUI {

    /** The card surface design icon. */
    private final Icon icon_;

    /** The card surface design identifier. */
    private final CardSurfaceDesignId id_;

    /** The card surface design name. */
    private final String name_;

    /**
     * Initializes a new instance of the {@code CardSurfaceDesignUI} class.
     * 
     * @param id
     *        The card surface design identifier; must not be {@code null}.
     * @param name
     *        The card surface design name; must not be {@code null}.
     * @param icon
     *        The card surface design icon; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code id}, {@code name}, or {@code icon} is {@code null}.
     */
    public CardSurfaceDesignUI(final CardSurfaceDesignId id, final String name, final Icon icon) {
        assertArgumentNotNull(id, "id");
        assertArgumentNotNull(name, "name");
        assertArgumentNotNull(icon, "icon");
        id_ = id;
        name_ = name;
        icon_ = icon;
    }

    @Override
    public Icon getIcon() {
        return icon_;
    }

    @Override
    public CardSurfaceDesignId getId() {
        return id_;
    }

    @Override
    public String getName() {
        return name_;
    }
}
