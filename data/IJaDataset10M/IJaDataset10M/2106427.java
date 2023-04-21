package net.sf.brightside.qualifications.service;

import net.sf.brightside.qualifications.core.command.Command;
import net.sf.brightside.qualifications.metamodel.Player;

public interface DeletePlayerCommand extends Command<Player> {

    DeletePlayerCommand setPlayerToDelete(Player player);

    Player getPlayer();
}
