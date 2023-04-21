package com.ham.mud.commands.skills;

import com.ham.mud.ServerConnection;
import com.ham.mud.Targetable;
import com.ham.mud.characters.MudCharacter;

/**
 * Created by hlucas on Jun 27, 2011 at 1:19:25 PM
 */
public class PeekSkill extends Skill {

    @Override
    protected void useSkill(ServerConnection connection, Targetable targetable, ServerConnection enemy) {
        if (targetable == null) {
            connection.printSolo("Peek at whose inventory?");
            return;
        }
        MudCharacter mudCharacter = (MudCharacter) targetable;
        connection.printStart("You peek at " + mudCharacter.getName() + "'s items:");
        mudCharacter.showItems(connection);
        connection.printEnd();
    }

    @Override
    protected String getCommandName() {
        return "peek";
    }

    @Override
    protected boolean isCombatSkill() {
        return false;
    }

    @Override
    public String getName() {
        return "Peek";
    }
}
