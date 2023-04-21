package nothanks;

public interface PlayerPot {

    public List<IntArray> getPlayerCards();

    public List<String> getPlayerNames();

    public int getNumChips();

    public int getNumCards();

    public int getUpCard();

    public boolean cardsLeft();

    public void printPlayerCards();
}
