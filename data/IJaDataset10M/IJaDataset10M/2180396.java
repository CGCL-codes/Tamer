package games.midhedava.server.maps.semos;

import games.midhedava.server.entity.npc.SpeakerNPC;
import games.midhedava.server.entity.npc.SpeakerNPCFactory;

public class HelperNPC extends SpeakerNPCFactory {

    @Override
    protected void createDialog(SpeakerNPC npc) {
        npc.addGreeting("A survivor from the war! You look wounded.I will help you if you do a #task for me.");
        npc.addVillageInf("It is a nearby village that you will reach if you go west. The village is placed faraway from the roman threat");
        npc.addThanks("You're welcome");
        npc.addGoodbye();
        npc.setATK(20);
        npc.setHP(200);
        npc.setDEF(30);
        npc.setXP(200);
        npc.setRace("dacian");
        npc.setSex("male");
    }

    ;
}
