package games.midhedava.server.maps.semos;

import games.midhedava.server.entity.npc.SpeakerNPC;
import games.midhedava.server.entity.npc.SpeakerNPCFactory;

public class RomanVillager1NPC extends SpeakerNPCFactory {

    @Override
    protected void createDialog(SpeakerNPC npc) {
        npc.addGreeting("Hello there stranger. I have a little #task for you");
        npc.addGoodbye("Bye!");
        npc.setATK(30);
        npc.setHP(300);
        npc.setDEF(30);
        npc.setXP(300);
        npc.setRace("roman");
        npc.setSex("male");
    }

    ;
}
