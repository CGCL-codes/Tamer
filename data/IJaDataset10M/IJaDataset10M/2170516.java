package net.jetrix.filter;

import java.util.*;
import java.util.logging.*;
import net.jetrix.*;
import net.jetrix.messages.channel.*;
import net.jetrix.messages.channel.specials.*;

/**
 * Defines a generic filter to be used and extended by filter developers.
 * GenericFilter makes writing filters easier by dispatching messages to an
 * appropriate method according to the type of this message (onPline(),
 * onStartGame(), etc...).
 *
 * @author Emmanuel Bourg
 * @version $Revision: 799 $, $Date: 2009-02-18 11:28:08 -0500 (Wed, 18 Feb 2009) $
 */
public abstract class GenericFilter extends MessageFilter {

    protected Logger log = Logger.getLogger("net.jetrix");

    public final void process(Message m, List<Message> out) {
        onMessage(m);
        if (m instanceof SpecialMessage) {
            onMessage((SpecialMessage) m, out);
        } else if (m instanceof FieldMessage) {
            onMessage((FieldMessage) m, out);
        } else if (m instanceof CommandMessage) {
            onMessage((CommandMessage) m, out);
        } else if (m instanceof PlineMessage) {
            onMessage((PlineMessage) m, out);
        } else if (m instanceof LevelMessage) {
            onMessage((LevelMessage) m, out);
        } else if (m instanceof PlayerLostMessage) {
            onMessage((PlayerLostMessage) m, out);
        } else if (m instanceof PlineActMessage) {
            onMessage((PlineActMessage) m, out);
        } else if (m instanceof TeamMessage) {
            onMessage((TeamMessage) m, out);
        } else if (m instanceof JoinMessage) {
            onMessage((JoinMessage) m, out);
        } else if (m instanceof LeaveMessage) {
            onMessage((LeaveMessage) m, out);
        } else if (m instanceof PlayerNumMessage) {
            onMessage((PlayerNumMessage) m, out);
        } else if (m instanceof StartGameMessage) {
            onMessage((StartGameMessage) m, out);
        } else if (m instanceof StopGameMessage) {
            onMessage((StopGameMessage) m, out);
        } else if (m instanceof NewGameMessage) {
            onMessage((NewGameMessage) m, out);
        } else if (m instanceof EndGameMessage) {
            onMessage((EndGameMessage) m, out);
        } else if (m instanceof PauseMessage) {
            onMessage((PauseMessage) m, out);
        } else if (m instanceof ResumeMessage) {
            onMessage((ResumeMessage) m, out);
        } else if (m instanceof GmsgMessage) {
            onMessage((GmsgMessage) m, out);
        } else if (m instanceof PlayerWonMessage) {
            onMessage((PlayerWonMessage) m, out);
        } else {
            onMessage(m, out);
        }
    }

    /**
     * Message pre-processing. This method is called at the beginning of the
     * <tt>process(Message m, List out)</tt> method and allow custom
     * processing for all filtered messages.
     */
    public void onMessage(Message m) {
    }

    public void onMessage(Message m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(PlineMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(PlineActMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(TeamMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(JoinMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(LeaveMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(PlayerNumMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(StartGameMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(StopGameMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(NewGameMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(EndGameMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(PauseMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(ResumeMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(GmsgMessage m, List<Message> out) {
        out.add(m);
    }

    private void onMessage(SpecialMessage m, List<Message> out) {
        onSpecial(m, out);
        if (m instanceof LinesAddedMessage) {
            onMessage((LinesAddedMessage) m, out);
        } else if (m instanceof AddLineMessage) {
            onMessage((AddLineMessage) m, out);
        } else if (m instanceof ClearLineMessage) {
            onMessage((ClearLineMessage) m, out);
        } else if (m instanceof ClearSpecialsMessage) {
            onMessage((ClearSpecialsMessage) m, out);
        } else if (m instanceof RandomClearMessage) {
            onMessage((RandomClearMessage) m, out);
        } else if (m instanceof BlockQuakeMessage) {
            onMessage((BlockQuakeMessage) m, out);
        } else if (m instanceof BlockBombMessage) {
            onMessage((BlockBombMessage) m, out);
        } else if (m instanceof GravityMessage) {
            onMessage((GravityMessage) m, out);
        } else if (m instanceof NukeFieldMessage) {
            onMessage((NukeFieldMessage) m, out);
        } else if (m instanceof SwitchFieldsMessage) {
            onMessage((SwitchFieldsMessage) m, out);
        }
    }

    /**
     * Special block message pre-processing. This method is called for all
     * specials filtered and allow custom processing for all specials
     * (lines added, blockbomb switchs, etc...).
     */
    public void onSpecial(SpecialMessage m, List<Message> out) {
    }

    public void onMessage(LevelMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(FieldMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(PlayerLostMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(PlayerWonMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(CommandMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(LinesAddedMessage m, List<Message> out) {
        if (m instanceof OneLineAddedMessage) {
            onMessage((OneLineAddedMessage) m, out);
        } else if (m instanceof TwoLinesAddedMessage) {
            onMessage((TwoLinesAddedMessage) m, out);
        } else if (m instanceof FourLinesAddedMessage) {
            onMessage((FourLinesAddedMessage) m, out);
        }
    }

    public void onMessage(OneLineAddedMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(TwoLinesAddedMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(FourLinesAddedMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(AddLineMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(ClearLineMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(NukeFieldMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(RandomClearMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(SwitchFieldsMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(ClearSpecialsMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(GravityMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(BlockQuakeMessage m, List<Message> out) {
        out.add(m);
    }

    public void onMessage(BlockBombMessage m, List<Message> out) {
        out.add(m);
    }
}
