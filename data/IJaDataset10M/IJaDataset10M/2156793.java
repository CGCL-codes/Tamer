package monopoly.results;

/**
 *
 * @author blecherl
 */
public class PlayerDetailsResult extends MonopolyResult {

    private String[] names;

    private boolean[] isHumans;

    private boolean[] isActive;

    private int[] money;

    public PlayerDetailsResult(String[] names, boolean[] isHumans, boolean[] isActive, int[] money) {
        this.names = names;
        this.isHumans = isHumans;
        this.isActive = isActive;
        this.money = money;
    }

    public PlayerDetailsResult(String errorMessage) {
        super(errorMessage);
    }

    public String[] getNames() {
        return names;
    }

    public boolean[] getIsHumans() {
        return isHumans;
    }

    public boolean[] getIsActive() {
        return isActive;
    }

    public int[] getMoney() {
        return money;
    }

    public static PlayerDetailsResult error(String message) {
        return new PlayerDetailsResult(message);
    }
}
