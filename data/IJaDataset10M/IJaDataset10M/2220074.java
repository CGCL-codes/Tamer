package org.gamegineer.game.internal.core.system;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gamegineer.game.core.system.IGameSystem;
import org.gamegineer.game.core.system.IRole;
import org.gamegineer.game.core.system.IStage;

/**
 * A collection of useful methods for working with game systems.
 */
public final class GameSystemUtils {

    /**
     * Initializes a new instance of the {@code GameSystemUtils} class.
     */
    private GameSystemUtils() {
        super();
    }

    /**
     * Asserts a game system is legal.
     * 
     * @param gameSystem
     *        The game system; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code gameSystem} is illegal.
     * @throws java.lang.NullPointerException
     *         If {@code gameSystem} is {@code null}.
     */
    public static void assertGameSystemLegal(final IGameSystem gameSystem) {
        assertArgumentNotNull(gameSystem, "gameSystem");
        final List<IRole> roles = gameSystem.getRoles();
        assertArgumentLegal(!roles.isEmpty(), "gameSystem", Messages.GameSystemUtils_assertGameSystemLegal_roles_empty);
        assertArgumentLegal(isRoleIdentifierUnionUnique(roles, null), "gameSystem", Messages.GameSystemUtils_assertGameSystemLegal_roles_notUnique);
        final List<IStage> stages = gameSystem.getStages();
        assertArgumentLegal(!stages.isEmpty(), "gameSystem", Messages.GameSystemUtils_assertGameSystemLegal_stages_empty);
        assertArgumentLegal(isStageIdentifierUnionUnique(null, stages, null), "gameSystem", Messages.GameSystemUtils_assertGameSystemLegal_stages_notUnique);
    }

    /**
     * Asserts a role is legal.
     * 
     * @param role
     *        The role; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code role} is illegal.
     * @throws java.lang.NullPointerException
     *         If {@code role} is {@code null}.
     */
    public static void assertRoleLegal(final IRole role) {
        assertArgumentNotNull(role, "role");
    }

    /**
     * Asserts a stage is legal.
     * 
     * @param stage
     *        The stage; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code stage} is illegal.
     * @throws java.lang.NullPointerException
     *         If {@code stage} is {@code null}.
     */
    public static void assertStageLegal(final IStage stage) {
        assertArgumentNotNull(stage, "stage");
        assertArgumentLegal(stage.getCardinality() >= 0, "stage", Messages.GameSystemUtils_assertStageLegal_cardinality_negative);
        assertArgumentLegal(isStageIdentifierUnionUnique(stage.getId(), stage.getStages(), null), "stage", Messages.GameSystemUtils_assertStageLegal_stages_notUnique);
    }

    /**
     * Determines if the union of the role identifiers of the roles in the
     * specified list and the role identifier of the specified role is unique.
     * 
     * @param roles
     *        A list of roles; must not be {@code null}.
     * @param role
     *        A role; may be {@code null}.
     * 
     * @return {@code true} if the specified union of role identifiers is
     *         unique; otherwise {@code false}.
     */
    private static boolean isRoleIdentifierUnionUnique(final List<IRole> roles, final IRole role) {
        assert roles != null;
        final Set<String> idSet = new HashSet<String>();
        for (final IRole aRole : roles) {
            if (!isRoleIdentifierUnionUnique(idSet, aRole)) {
                return false;
            }
        }
        if (role != null) {
            if (!isRoleIdentifierUnionUnique(idSet, role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the union of the specified role identifier set and the role
     * identifier of the specified role is unique.
     * 
     * @param idSet
     *        A set of role identifiers; must not be {@code null}. The role
     *        identifier of the specified role will be added to this set before
     *        returning.
     * @param role
     *        A role; must not be {@code null}.
     * 
     * @return {@code true} if the specified union of role identifiers is
     *         unique; otherwise {@code false}.
     */
    private static boolean isRoleIdentifierUnionUnique(final Set<String> idSet, final IRole role) {
        assert idSet != null;
        assert role != null;
        if (idSet.contains(role.getId())) {
            return false;
        }
        idSet.add(role.getId());
        return true;
    }

    /**
     * Determines if the union of the specified stage identifier, the stage
     * identifiers of the stages in the specified list and all their
     * descendants, and the stage identifiers of the specified stage and all its
     * descendants is unique.
     * 
     * @param id
     *        A stage identifier; may be {@code null}.
     * @param stages
     *        A list of stages; must not be {@code null}.
     * @param stage
     *        A stage; may be {@code null}.
     * 
     * @return {@code true} if the specified union of stage identifiers is
     *         unique; otherwise {@code false}.
     */
    private static boolean isStageIdentifierUnionUnique(final String id, final List<IStage> stages, final IStage stage) {
        assert stages != null;
        final Set<String> idSet = new HashSet<String>();
        if (id != null) {
            idSet.add(id);
        }
        for (final IStage aStage : stages) {
            if (!isStageIdentifierUnionUnique(idSet, aStage)) {
                return false;
            }
        }
        if (stage != null) {
            if (!isStageIdentifierUnionUnique(idSet, stage)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the union of the specified stage identifier set and the
     * stage identifiers of the specified stage and all its descendants are
     * unique.
     * 
     * @param idSet
     *        A set of stage identifiers; must not be {@code null}. The
     *        identifier of the specified stage and those of its descendants
     *        will be added to this set before returning.
     * @param stage
     *        A stage; must not be {@code null}.
     * 
     * @return {@code true} if the specified union of stage identifiers are
     *         unique; otherwise {@code false}.
     */
    private static boolean isStageIdentifierUnionUnique(final Set<String> idSet, final IStage stage) {
        assert idSet != null;
        assert stage != null;
        if (idSet.contains(stage.getId())) {
            return false;
        }
        idSet.add(stage.getId());
        for (final IStage aStage : stage.getStages()) {
            if (!isStageIdentifierUnionUnique(idSet, aStage)) {
                return false;
            }
        }
        return true;
    }
}
