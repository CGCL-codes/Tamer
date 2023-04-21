package org.gamegineer.game.internal.ui.system;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import net.jcip.annotations.Immutable;
import org.gamegineer.game.ui.system.IRoleUi;

/**
 * Null implementation of {@link org.gamegineer.game.ui.system.IRoleUi}.
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@Immutable
public final class NullRoleUi implements IRoleUi {

    /** The role identifier. */
    private final String m_id;

    /**
     * Initializes a new instance of the {@code NullRoleUi} class.
     * 
     * @param id
     *        The role identifier; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code id} is {@code null}.
     */
    public NullRoleUi(final String id) {
        assertArgumentNotNull(id, "id");
        m_id = id;
    }

    public String getId() {
        return m_id;
    }

    public String getName() {
        return Messages.NullRoleUi_name(m_id);
    }
}
