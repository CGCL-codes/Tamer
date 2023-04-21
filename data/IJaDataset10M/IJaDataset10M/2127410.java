package games.midhedava.server.maps.quests;

import games.midhedava.server.MidhedavaRPWorld;
import games.midhedava.server.entity.item.Item;
import games.midhedava.server.entity.item.StackableItem;
import games.midhedava.server.entity.npc.ConversationPhrases;
import games.midhedava.server.entity.npc.ConversationStates;
import games.midhedava.server.entity.npc.SpeakerNPC;
import games.midhedava.server.entity.player.Player;
import java.util.ArrayList;
import java.util.List;

/** 
 * QUEST: Roman spy report
 * PARTICIPANTS: 
 * - Gate keeper
 * 
 * 
 * STEPS: 
 * - Talk with gate keeper to activate the quest.
 * 
 * - Return the spyreport to gate keeper
 *
 * REWARD: 
 * - 230 XP
 * - 100 gold coins
 *
 * REPETITIONS:
 * - None.
 */
public class TheSpyreport2 extends AbstractQuest {

    private static final String QUEST_SLOT = "spy report";

    @Override
    public void init(String name) {
        super.init(name, QUEST_SLOT);
    }

    @Override
    public List<String> getHistory(Player player) {
        List<String> res = new ArrayList<String>();
        if (!player.hasQuest(QUEST_SLOT)) {
            return res;
        }
        res.add("FIRST_CHAT");
        String questState = player.getQuest(QUEST_SLOT);
        if (questState.equals("rejected")) {
            res.add("QUEST_REJECTED");
        }
        if (player.isQuestInState(QUEST_SLOT, "start", null, "done")) {
            res.add("QUEST_ACCEPTED");
        }
        if ((questState.equals("spy report") && player.isEquipped("spy report")) || questState.equals("done")) {
            res.add("FOUND_ITEM");
        }
        if (questState.equals("spy report") && !player.isEquipped("spy report")) {
            res.add("LOST_ITEM");
        }
        if (questState.equals("done")) {
            res.add("DONE");
        }
        return res;
    }

    private void step_1() {
        SpeakerNPC npc = npcs.get("Guardian");
        npc.add(ConversationStates.ATTENDING, ConversationPhrases.QUEST_MESSAGES, null, ConversationStates.ATTENDING, null, new SpeakerNPC.ChatAction() {

            @Override
            public void fire(Player player, String text, SpeakerNPC engine) {
                if (player.isQuestCompleted(QUEST_SLOT)) {
                    engine.say("I have nothing for you now.");
                } else {
                    engine.say("There is a band of dacian spies north from here and I need their #spyreport");
                }
            }
        });
        npc.add(ConversationStates.ATTENDING, "spyreport", new SpeakerNPC.ChatCondition() {

            @Override
            public boolean fire(Player player, String text, SpeakerNPC npc) {
                return player.isQuestCompleted(QUEST_SLOT);
            }
        }, ConversationStates.ATTENDING, "I already got the spy report. Thank you!", null);
        npc.add(ConversationStates.ATTENDING, "spyreport", new SpeakerNPC.ChatCondition() {

            @Override
            public boolean fire(Player player, String text, SpeakerNPC npc) {
                return !player.hasQuest(QUEST_SLOT);
            }
        }, ConversationStates.QUEST_OFFERED, "I had reports indicating that their #boss has it.", null);
        npc.add(ConversationStates.QUEST_OFFERED, ConversationPhrases.YES_MESSAGES, null, ConversationStates.ATTENDING, "Thank you! Come back fast with it!", new SpeakerNPC.ChatAction() {

            @Override
            public void fire(Player player, String text, SpeakerNPC engine) {
                player.setQuest(QUEST_SLOT, "start");
            }
        });
        npc.add(ConversationStates.QUEST_OFFERED, "no", null, ConversationStates.ATTENDING, "Oh... I guess I will have to get somebody else to do it, then.", null);
        npc.add(ConversationStates.QUEST_OFFERED, "boss", null, ConversationStates.QUEST_OFFERED, "They should have a commander or some sort of leader. Will you help us?", null);
        npc.add(ConversationStates.ATTENDING, "spyreport", new SpeakerNPC.ChatCondition() {

            @Override
            public boolean fire(Player player, String text, SpeakerNPC npc) {
                return player.hasQuest(QUEST_SLOT) && player.getQuest(QUEST_SLOT).equals("start");
            }
        }, ConversationStates.ATTENDING, "You have to bring me the spy report. It must not get in the hands of the dacian army.", null);
        npc.add(ConversationStates.ATTENDING, "where", null, ConversationStates.ATTENDING, "south.", null);
    }

    private void step_2() {
    }

    private void step_3() {
        SpeakerNPC npc = npcs.get("Guardian");
        npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES, new SpeakerNPC.ChatCondition() {

            @Override
            public boolean fire(Player player, String text, SpeakerNPC npc) {
                return player.hasQuest("spy report") && player.getQuest("spy report").equals("start") && player.isEquipped("spy report");
            }
        }, ConversationStates.ATTENDING, null, new SpeakerNPC.ChatAction() {

            @Override
            public void fire(Player player, String text, SpeakerNPC engine) {
                if (player.drop("spy report")) {
                    engine.say("I was sure you will make it! Here is your reward.");
                    StackableItem money = (StackableItem) MidhedavaRPWorld.get().getRuleManager().getEntityManager().getItem("money");
                    money.setQuantity(100);
                    player.equip(money);
                    player.addXP(230);
                    player.addKarma(4.0);
                    player.notifyWorldAboutChanges();
                    player.setQuest(QUEST_SLOT, "done");
                } else {
                    engine.say("You haven't got the spy report! Hurry before it reaches their base");
                    player.removeQuest(QUEST_SLOT);
                }
            }
        });
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        step_1();
        step_2();
        step_3();
    }
}
