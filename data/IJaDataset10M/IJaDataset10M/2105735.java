package saga.model.map;

import saga.model.*;
import saga.model.features.Teacher;
import saga.model.commands.*;
import saga.control.*;
import javax.swing.JOptionPane;

/**
 * Generic town house tile.
 * @version $Id: HouseTile.java,v 1.1 2004/04/18 07:01:00 marion Exp $
 */
public class HouseTile extends saga.model.map.BaseTile implements java.io.Serializable {

    String owner;

    Teacher teacher;

    String rumor;

    private static final long serialVersionUID = -2037264447420862124L;

    /** Deserialization constructor. */
    private HouseTile() {
        super(Map.GENERIC, "shop.closed");
    }

    public static HouseTile create() {
        HouseTile result = new HouseTile();
        result.owner = NameFactory.getName((int) (Math.random() * 2));
        result.teacher = null;
        if (Control.random(0, 10) > 5) {
            result.teacher = new Teacher(result.owner, Control.random(1, 4));
        }
        int type = Control.random(0, RumorFactory.NUM_TYPE);
        result.rumor = RumorFactory.getRandomRumor(type, 1);
        return result;
    }

    public int execute(Command c) {
        if (c instanceof DoKnock) {
            if (teacher == null) {
                Control.message("You knock on the door and " + owner + " whispers:");
                Control.message(">" + rumor + "<");
                return Command.SUCCESS;
            } else {
                Hero hero = HeroLocation.getHeroLocation().getHero();
                int result = 0;
                if (!teacher.willTeach(hero)) {
                    Control.infoIcon(teacher.getInfo(), owner, teacher.getIcon());
                    result = Control.confirmIcon("Try to become a student of " + owner + "?", owner, teacher.getIcon());
                    if (result == JOptionPane.YES_OPTION) {
                        teacher.addPupil(hero);
                    }
                } else {
                    result = Control.confirmIcon("Train " + teacher.getSkill() + " with " + teacher.getName() + "?", owner, teacher.getIcon());
                    if (result == JOptionPane.YES_OPTION) {
                        teacher.teach(hero);
                    }
                }
                return Command.SUCCESS;
            }
        } else return c.doIt();
    }

    public int moveCost() {
        return 5;
    }
}
