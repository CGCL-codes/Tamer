package judge.object.armor;

import judge.object.*;
import judge.object.alive.*;
import java.util.*;

public class Armor extends Clothes {

    protected int mArmorClass = 10;

    public Armor() {
        mClassName = "armor";
        mType = "armor";
        setName("armor");
        setShort("Some armor");
        setLong("Well, it looks like it must be some kind of armor, I reckon.");
        init();
    }

    public int getArmorClass() {
        return mArmorClass;
    }

    public void setArmorClass(int ac) {
        mArmorClass = ac;
    }

    public void wear(Living l) {
        super.wear(l);
        l.mWornArmor = this;
    }

    public void remove() {
        mWearer.mWornArmor = null;
        super.remove();
    }
}
