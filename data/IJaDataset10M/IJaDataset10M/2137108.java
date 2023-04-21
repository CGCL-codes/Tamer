package pms.whq.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author filter
 */
public class MonsterContainer {

    private String diceRoll;

    private List<MonsterEntry> monsters;

    public MonsterContainer(String diceRoll, List<MonsterEntry> monsters) {
        this.diceRoll = diceRoll;
        this.monsters = monsters;
    }

    public MonsterContainer(String diceRoll, MonsterEntry monster) {
        List<MonsterEntry> monsterList = new ArrayList<MonsterEntry>();
        monsterList.add(monster);
        this.monsters = monsterList;
        this.diceRoll = diceRoll;
    }

    public List<MonsterEntry> getMonsters() {
        return this.monsters;
    }

    public String getDiceRoll() {
        return this.diceRoll;
    }
}
