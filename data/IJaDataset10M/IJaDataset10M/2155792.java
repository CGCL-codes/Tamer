package net.sf.gham.core.entity.formation;

import net.sf.gham.core.entity.player.PlayerMyTeam;
import net.sf.gham.core.entity.player.rolecalcolator.Roles.RoleConstant;

/**
 * @author fabio
 *
 */
public interface IPlayerInFormation {

    PlayerMyTeam getPlayer();

    RoleConstant getRoleConstant();
}
