package freelands.actor.npc;

import freelands.actor.Player;
import freelands.protocol.message.toclient.RawMessage;

/**
 * this npc option is to show a proposition and  follow it to a new Window
 */
public class BasicNpcOption extends NpcOption {

    private int nextWindowId;

    public BasicNpcOption(String text, int responseId, int nextWindowId) {
        super(text, responseId);
        this.nextWindowId = nextWindowId;
    }

    @Override
    boolean checkConditions(Player p) {
        return true;
    }

    @Override
    void action(Player p) {
        NpcWindow next = p.npcResponseStatus.getNpc().getNpcWindow(nextWindowId);
        if (next == null) {
        } else {
            NpcCharacter npc = p.npcResponseStatus.getNpc();
            p.npcResponseStatus.setWindow(next);
            p.sendPacket(RawMessage.sendNpcInfoMessage(npc.getContent().name, npc.icon));
            p.sendPacket(RawMessage.npcTextMessage(next.whiteText, next.speech));
            p.sendPacket(RawMessage.npcOptionsListMessage(next, npc));
        }
    }
}
