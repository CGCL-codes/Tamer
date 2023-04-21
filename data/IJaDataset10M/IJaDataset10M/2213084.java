package atlantik.game;

import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Player extends AtlantikIdHolder<Integer, Player> {

    String name;

    String host;

    Game game;

    int money;

    boolean isBankrupt;

    boolean hasDebt;

    boolean hasTurn;

    private boolean isMoving;

    int doubleCount;

    boolean canRoll;

    boolean canRollAgain;

    boolean canBuyEstate;

    boolean canAuction;

    boolean canUseCard;

    boolean isJailed;

    int jailCount;

    private Estate location;

    boolean isSpectator;

    List<Estate> estates = new LinkedList<Estate>();

    private Image token;

    public Player(Atlantik client) {
        super(client);
    }

    public boolean hasDebt() {
        return hasDebt;
    }

    public boolean hasTurn() {
        return hasTurn;
    }

    public boolean canRoll() {
        return canRoll;
    }

    public boolean canRollAgain() {
        return canRollAgain;
    }

    public boolean canBuyEstate() {
        return canBuyEstate;
    }

    public boolean canAuction() {
        return canAuction;
    }

    public boolean canUseCard() {
        return canUseCard;
    }

    public Iterable<Estate> estates() {
        return new Iterable<Estate>() {

            public Iterator<Estate> iterator() {
                return estates.iterator();
            }
        };
    }

    public int getDoubleCount() {
        return doubleCount;
    }

    public void setDoubleCount(int doubleCount) {
        this.doubleCount = doubleCount;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean isBankrupt) {
        this.isBankrupt = isBankrupt;
    }

    public boolean isJailed() {
        return isJailed;
    }

    public void setJailed(boolean isJailed) {
        this.isJailed = isJailed;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public void setSpectator(boolean isSpectator) {
        this.isSpectator = isSpectator;
    }

    public int getJailCount() {
        return jailCount;
    }

    public void setJailCount(int jailCount) {
        this.jailCount = jailCount;
    }

    public Estate getLocation() {
        return location;
    }

    public void setLocation(Estate location) {
        this.location = location;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getToken() {
        return token;
    }

    public void setToken(Image token) {
        this.token = token;
    }

    public void setCanAuction(boolean canAuction) {
        this.canAuction = canAuction;
    }

    public void setCanBuyEstate(boolean canBuyEstate) {
        this.canBuyEstate = canBuyEstate;
    }

    public void setCanRoll(boolean canRoll) {
        this.canRoll = canRoll;
    }

    public void setCanRollAgain(boolean canRollAgain) {
        this.canRollAgain = canRollAgain;
    }

    public void setCanUseCard(boolean canUseCard) {
        this.canUseCard = canUseCard;
    }

    public void setHasDebt(boolean hasDebt) {
        this.hasDebt = hasDebt;
    }

    public void setHasTurn(boolean hasTurn) {
        this.hasTurn = hasTurn;
    }
}
