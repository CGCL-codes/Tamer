package pl.org.minions.stigma.server.ai;

import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.world.World;

/**
 * Class implementing AI that does nothing. Ever.
 */
public class IdleAiScript extends AiScript {

    /** {@inheritDoc} */
    @Override
    public Command react(Actor currentActor, World world, Command command) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public AiScript create() {
        return new IdleAiScript();
    }
}
