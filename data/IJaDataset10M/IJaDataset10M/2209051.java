package studygame.Model;

/**
 *
 * @author DonHoeks
 */
public class Skill {

    private SkillType type;

    private int value;

    public Skill(SkillType type, int v) {
        this.type = type;
        this.value = v;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public SkillType getType() {
        return type;
    }
}
