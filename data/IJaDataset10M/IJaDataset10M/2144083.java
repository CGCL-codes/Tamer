package gameLogic;

public class Options {

    private final int NumOfDecks;

    private final int playerCount;

    private final boolean fortune;

    private final boolean takenew;

    private final boolean isFirstAI;

    private String[] names;

    public Options(int decks, int players, boolean fortunecard, boolean newhandcard) {
        this.NumOfDecks = decks;
        if (players == 1) {
            this.playerCount = 2;
        } else {
            this.playerCount = players;
        }
        this.fortune = fortunecard;
        this.takenew = newhandcard;
        this.isFirstAI = players == 1;
        initNames();
    }

    public Options() {
        this(1, 2, false, false);
    }

    public int getNumOfDecks() {
        return this.NumOfDecks;
    }

    public int getNumOfPlayers() {
        return this.playerCount;
    }

    public boolean getFortuneCard() {
        return this.fortune;
    }

    public boolean getNewHand() {
        return this.takenew;
    }

    public boolean getAI() {
        return isFirstAI;
    }

    public void giveNames(String[] names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }

    private void initNames() {
        names = new String[this.playerCount];
        for (int i = 0; i < names.length; i++) names[i] = "default";
    }
}
