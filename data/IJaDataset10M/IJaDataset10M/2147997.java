package people.needs;

import game.Buildings;
import people.Human;

public class HumanNeedThirst extends HumanNeed implements IHumanNeeds {

    public static final String LABEL = "Thirst";

    public HumanNeedThirst(int need) {
        super(LABEL, need, Buildings.getInstance().getDrink());
    }

    public void fulfilNeed(Human human) {
    }

    public void dissatisfyNeed(Human human) {
        human.setHumanActionGoHome();
    }
}
