package net.sf.mpango.game.web.adapter;

import net.sf.mpango.common.adapter.BaseAdapter;
import net.sf.mpango.common.directory.adapter.UserAdapter;
import net.sf.mpango.game.core.entity.Player;
import net.sf.mpango.game.web.dto.PlayerDTO;

/**
 * 
 * @author etux
 *
 */
public class PlayerAdapter extends BaseAdapter<Player, PlayerDTO> {

    private PlayerAdapter() {
        super();
    }

    public static PlayerAdapter instance() {
        return new PlayerAdapter();
    }

    @Override
    public Player fromDTO(PlayerDTO dto) {
        if (dto == null) {
            return null;
        }
        Player player = new Player();
        player.setIdentifier(dto.getId());
        player.setState(dto.getState());
        player.setPosition(PositionAdapter.instance().fromDTO(dto.getPosition()));
        player.setUnits(UnitAdapter.instance().fromDTOList(dto.getUnits()));
        player.setUser(UserAdapter.getInstance().fromDTO(dto.getUser()));
        return player;
    }

    @Override
    public PlayerDTO toDTO(Player player) {
        if (player == null) {
            return null;
        }
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getIdentifier());
        dto.setState(player.getState());
        dto.setPosition(PositionAdapter.instance().toDTO(player.getPosition()));
        dto.setUnits(UnitAdapter.instance().toDTOList(player.getUnits()));
        dto.setUser(UserAdapter.getInstance().toDTO(player.getUser()));
        return dto;
    }
}
