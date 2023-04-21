package csa.jportal.gameModes.player.achievments;

import csa.jportal.card.CardList;
import csa.jportal.carddeck.CardDeck;
import csa.jportal.carddeck.CardDeckData;
import csa.jportal.cardheap.CardHeap;
import csa.jportal.cardset.CardSet;
import csa.jportal.config.Configuration;
import csa.jportal.gameModes.player.Player;
import java.util.Collection;
import java.util.Vector;

/**
 *
 * @author malban
 */
public class Achievement {

    private static Vector<Achievement> achievments = new Vector<Achievement>();

    private static Vector<String> classes = new Vector<String>();

    String _class;

    String name;

    String describtion;

    int score;

    int number;

    public static int scoremax = 0;

    static {
        classes.addElement("Playing");
        achievments.addElement(new Achievement("Give it a try!", "Play a game of JPortal.", 5, 1, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getGamesPlayed() > 0;
            }
        });
        achievments.addElement(new Achievement("Beat JPortal!", "Win a game of JPortal.", 5, 2, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getWins() > 0;
            }
        });
        achievments.addElement(new Achievement("JPortal player", "Win 10 games of JPortal.", 5, 3, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getWins() >= 10;
            }
        });
        achievments.addElement(new Achievement("JPortal professional", "Win 100 games of JPortal.", 5, 4, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getWins() >= 100;
            }
        });
        achievments.addElement(new Achievement("JPortal maniac", "Win 500 games of JPortal.", 5, 5, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getWins() >= 500;
            }
        });
        classes.addElement("Sets");
        achievments.addElement(new Achievement("Portal is second to none!", "Get a card of \"Portal the Second Age\".", 5, 6, "Sets") {

            public boolean testAchievment(Player player) {
                Vector<String> sets = player.getData().getKnownSets();
                for (int i = 0; i < sets.size(); i++) {
                    String string = sets.elementAt(i);
                    if (string.equalsIgnoreCase("Portal Second Age")) {
                        return true;
                    }
                }
                return false;
            }
        });
        achievments.addElement(new Achievement("Starting a Startup!", "Get a card of \"Startup 1999\".", 5, 7, "Sets") {

            public boolean testAchievment(Player player) {
                Vector<String> sets = player.getData().getKnownSets();
                for (int i = 0; i < sets.size(); i++) {
                    String string = sets.elementAt(i);
                    if (string.equalsIgnoreCase("Starter 1999")) {
                        return true;
                    }
                }
                return false;
            }
        });
        achievments.addElement(new Achievement("And suddenly it was three!", "Get a card of \"Portal Three Kingdoms\".", 5, 8, "Sets") {

            public boolean testAchievment(Player player) {
                Vector<String> sets = player.getData().getKnownSets();
                for (int i = 0; i < sets.size(); i++) {
                    String string = sets.elementAt(i);
                    if (string.equalsIgnoreCase("Portal Three Kingdoms")) {
                        return true;
                    }
                }
                return false;
            }
        });
        achievments.addElement(new Achievement("Getting closer to magic!", "Get a card of \"Startup 2000\".", 5, 41, "Sets") {

            public boolean testAchievment(Player player) {
                Vector<String> sets = player.getData().getKnownSets();
                for (int i = 0; i < sets.size(); i++) {
                    String string = sets.elementAt(i);
                    if (string.equalsIgnoreCase("Starter 2000")) {
                        return true;
                    }
                }
                return false;
            }
        });
        classes.addElement("Booster");
        achievments.addElement(new Achievement("There is second booster!", "Get an additional booster.", 5, 9, "Booster") {

            public boolean testAchievment(Player player) {
                return player.getData().getBoostersGot() > 1;
            }
        });
        achievments.addElement(new Achievement("10 little boosters!", "Get 10 booster.", 5, 10, "Booster") {

            public boolean testAchievment(Player player) {
                return player.getData().getBoostersGot() >= 10;
            }
        });
        achievments.addElement(new Achievement("Booster busting!", "Get 100 booster.", 5, 11, "Booster") {

            public boolean testAchievment(Player player) {
                return player.getData().getBoostersGot() >= 100;
            }
        });
        classes.addElement("Money");
        achievments.addElement(new Achievement("10 bucks!", "Have 10 dollars.", 5, 12, "Money") {

            public boolean testAchievment(Player player) {
                return player.getData().getMoney() >= 10;
            }
        });
        achievments.addElement(new Achievement("100 bucks!", "Have 100 dollars.", 5, 42, "Money") {

            public boolean testAchievment(Player player) {
                return player.getData().getMoney() >= 100;
            }
        });
        achievments.addElement(new Achievement("1000 bucks!", "Have 1000 dollars.", 5, 13, "Money") {

            public boolean testAchievment(Player player) {
                return player.getData().getMoney() >= 1000;
            }
        });
        achievments.addElement(new Achievement("10 bucks gone!", "Spend 10 dollars.", 5, 14, "Money") {

            public boolean testAchievment(Player player) {
                return player.getData().getMoneySpend() >= 10;
            }
        });
        achievments.addElement(new Achievement("100 bucks gone!", "Spend 100 dollars.", 5, 15, "Money") {

            public boolean testAchievment(Player player) {
                return player.getData().getMoneySpend() >= 100;
            }
        });
        achievments.addElement(new Achievement("1000 bucks gone!", "Spend 1000 dollars.", 5, 16, "Money") {

            public boolean testAchievment(Player player) {
                return player.getData().getMoneySpend() >= 1000;
            }
        });
        classes.addElement("Cards");
        achievments.addElement(new Achievement("Got some variety!", "Have 100 cards.", 5, 17, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().size() >= 100;
            }
        });
        achievments.addElement(new Achievement("Such a variety!", "Have 1000 cards.", 5, 18, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().size() >= 1000;
            }
        });
        achievments.addElement(new Achievement("One variety is not enough!", "Have 2000 cards.", 5, 43, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().size() >= 2000;
            }
        });
        achievments.addElement(new Achievement("Huge pile of cards!", "Have 5000 cards.", 5, 44, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().size() >= 5000;
            }
        });
        achievments.addElement(new Achievement("Rare!", "Have 10 rare cards.", 5, 19, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().getSubListByRarity("R").size() >= 10;
            }
        });
        achievments.addElement(new Achievement("Hardly rare!", "Have 100 rare cards.", 5, 20, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().getSubListByRarity("R").size() >= 100;
            }
        });
        achievments.addElement(new Achievement("Plenty rare!", "Have 1000 rare cards.", 5, 21, "Cards") {

            public boolean testAchievment(Player player) {
                return new CardHeap(player.getHeapName(), false).getCardList().getSubListByRarity("R").size() >= 1000;
            }
        });
        classes.addElement("Deck");
        achievments.addElement(new Achievement("A deck!", "Build a deck on your own.", 5, 22, "Deck") {

            public boolean testAchievment(Player player) {
                CardDeck mCardDeck = new CardDeck(player.getName());
                Collection<CardDeckData> colC = mCardDeck.getPool().getMapForKlasse(player.getName()).values();
                return colC.size() > 0;
            }
        });
        achievments.addElement(new Achievement("10 decks!", "Build 10 decks on your own.", 5, 23, "Deck") {

            public boolean testAchievment(Player player) {
                CardDeck mCardDeck = new CardDeck(player.getName());
                Collection<CardDeckData> colC = mCardDeck.getPool().getMapForKlasse(player.getName()).values();
                return colC.size() >= 10;
            }
        });
        classes.addElement("Quests");
        achievments.addElement(new Achievement("One quest!", "Complete one quest.", 5, 24, "Quests") {

            public boolean testAchievment(Player player) {
                return player.getData().getQuestsCompleted().size() > 0;
            }
        });
        achievments.addElement(new Achievement("One and null quests!", "Complete 10 quests.", 5, 25, "Quests") {

            public boolean testAchievment(Player player) {
                return player.getData().getQuestsCompleted().size() > 9;
            }
        });
        achievments.addElement(new Achievement("Achiever", "Complete 20 quests.", 5, 45, "Quests") {

            public boolean testAchievment(Player player) {
                return player.getData().getQuestsCompleted().size() > 19;
            }
        });
        achievments.addElement(new Achievement("Over-Achiever", "Complete 50 quests.", 5, 46, "Quests") {

            public boolean testAchievment(Player player) {
                return player.getData().getQuestsCompleted().size() > 49;
            }
        });
        achievments.addElement(new Achievement("Portal is all!", "Own all cards of the original Portal.", 10, 26, "Sets") {

            public boolean testAchievment(Player player) {
                CardList heapList = new CardHeap(Configuration.getConfiguration().getAktivePlayer().getHeapName(), false).getCardList();
                heapList.onlyWithSet("Portal");
                heapList = CardList.makeUniqueById(heapList);
                return heapList.size() == 200;
            }
        });
        achievments.addElement(new Achievement("All of two!", "Own all cards of Portal Second Age.", 10, 47, "Sets") {

            public boolean testAchievment(Player player) {
                CardList heapList = new CardHeap(Configuration.getConfiguration().getAktivePlayer().getHeapName(), false).getCardList();
                heapList.onlyWithSet("Portal Second Age");
                heapList = CardList.makeUniqueById(heapList);
                return heapList.size() == 155;
            }
        });
        achievments.addElement(new Achievement("All three!", "Own all cards of Portal Three Kingdoms.", 10, 48, "Sets") {

            public boolean testAchievment(Player player) {
                CardList heapList = new CardHeap(Configuration.getConfiguration().getAktivePlayer().getHeapName(), false).getCardList();
                heapList.onlyWithSet("Portal Three Kingdoms");
                heapList = CardList.makeUniqueById(heapList);
                return heapList.size() == 170;
            }
        });
        achievments.addElement(new Achievement("Owner of '99!", "Own all cards of Starter 1999.", 10, 49, "Sets") {

            public boolean testAchievment(Player player) {
                CardList heapList = new CardHeap(Configuration.getConfiguration().getAktivePlayer().getHeapName(), false).getCardList();
                heapList.onlyWithSet("Starter 1999");
                heapList = CardList.makeUniqueById(heapList);
                return heapList.size() == 158;
            }
        });
        achievments.addElement(new Achievement("2000!", "Own all cards of Starter 2000.", 10, 50, "Sets") {

            public boolean testAchievment(Player player) {
                CardList heapList = new CardHeap(Configuration.getConfiguration().getAktivePlayer().getHeapName(), false).getCardList();
                heapList.onlyWithSet("Starter 2000");
                heapList = CardList.makeUniqueById(heapList);
                return heapList.size() == 52;
            }
        });
        achievments.addElement(new Achievement("Healthy", "Get 30 HP in a game.", 5, 27, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxHPInGame() >= 30;
            }
        });
        achievments.addElement(new Achievement("Very healthy", "Get 40 HP in a game.", 5, 28, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxHPInGame() >= 40;
            }
        });
        achievments.addElement(new Achievement("Undying", "Get 50 HP in a game.", 5, 29, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxHPInGame() >= 50;
            }
        });
        achievments.addElement(new Achievement("Take a beating!", "Survive 10 HP loss.", 5, 30, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxDamageSurvived() >= 10;
            }
        });
        achievments.addElement(new Achievement("To the brink of death!", "Survive 15 HP loss.", 5, 31, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxDamageSurvived() >= 15;
            }
        });
        achievments.addElement(new Achievement("Stroke with a feather!", "Deal 5 creature damage.", 5, 32, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxDamageDealtToOtherPlayer() >= 5;
            }
        });
        achievments.addElement(new Achievement("Stroke with a dagger!", "Deal 10 creature damage.", 5, 33, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxDamageDealtToOtherPlayer() >= 10;
            }
        });
        achievments.addElement(new Achievement("Stroke with an sword!", "Deal 15 creature damage.", 5, 34, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxDamageDealtToOtherPlayer() >= 15;
            }
        });
        achievments.addElement(new Achievement("Beaten to death!", "Deal 20 creature damage.", 5, 35, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getMaxDamageDealtToOtherPlayer() >= 20;
            }
        });
        achievments.addElement(new Achievement("2 is more than 1!", "Have two creatures.", 5, 36, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getCreatureMax() >= 2;
            }
        });
        achievments.addElement(new Achievement("Four times in a row!", "Have four creatures.", 5, 37, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getCreatureMax() >= 4;
            }
        });
        achievments.addElement(new Achievement("Little zoo!", "Have six creatures.", 5, 38, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getCreatureMax() >= 6;
            }
        });
        achievments.addElement(new Achievement("Big zoo!", "Have eight creatures.", 5, 39, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getCreatureMax() >= 8;
            }
        });
        achievments.addElement(new Achievement("Build an arch!", "Have ten creatures.", 5, 40, "Playing") {

            public boolean testAchievment(Player player) {
                return player.getData().getCreatureMax() >= 10;
            }
        });
        for (int i = 0; i < achievments.size(); i++) {
            Achievement achievement = achievments.elementAt(i);
            scoremax += achievement.score;
        }
    }

    public static Vector<Achievement> getAllAchievements() {
        return achievments;
    }

    public static Vector<String> getClasses() {
        return classes;
    }

    public static Vector<Achievement> getAchievementsForClass(String c) {
        if (c.trim().length() == 0) return achievments;
        Vector<Achievement> classified = new Vector<Achievement>();
        for (int i = 0; i < achievments.size(); i++) {
            Achievement achievement = achievments.elementAt(i);
            if (achievement._class.equals(c)) {
                classified.addElement(achievement);
            }
        }
        return classified;
    }

    public static Vector<Achievement> getAchievementsForNotClass(String c) {
        if (c.trim().length() == 0) return achievments;
        Vector<Achievement> classified = new Vector<Achievement>();
        for (int i = 0; i < achievments.size(); i++) {
            Achievement achievement = achievments.elementAt(i);
            if (!achievement._class.equals(c)) {
                classified.addElement(achievement);
            }
        }
        return classified;
    }

    private Achievement(String n, String d, int s, int nu, String c) {
        name = n;
        describtion = d;
        score = s;
        number = nu;
        _class = c;
    }

    public boolean testAchievment(Player player) {
        return false;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getDescribtion() {
        return describtion;
    }
}
