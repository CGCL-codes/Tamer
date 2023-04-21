package pcgen.cdom.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import pcgen.base.formula.Formula;
import pcgen.cdom.base.ChoiceSet;
import pcgen.core.PlayerCharacter;
import pcgen.util.StringPClassUtil;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.chooser.ChooserInterface;

/**
 * This is a transitional class from PCGen 5.15+ to the final CDOM core. It is
 * provided as convenience to hold a set of choices and the number of choices
 * allowed, prior to final implementation of the new choice system
 * 
 * @param <T>
 */
public class TransitionChoice<T> {

    private final ChoiceSet<? extends T> choices;

    private final Formula choiceCount;

    private String title;

    private boolean required = true;

    private ChoiceActor<T> choiceActor;

    public TransitionChoice(ChoiceSet<? extends T> cs, Formula count) {
        choices = cs;
        choiceCount = count;
    }

    public ChoiceSet<? extends T> getChoices() {
        return choices;
    }

    public Formula getCount() {
        return choiceCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TransitionChoice) {
            TransitionChoice<?> other = (TransitionChoice<?>) obj;
            return choiceCount.equals(other.choiceCount) && choices.equals(other.choices);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return choiceCount.hashCode() * 29 + choices.hashCode();
    }

    public Collection<? extends T> driveChoice(PlayerCharacter pc) {
        ChooserInterface c = ChooserFactory.getChooserInstance();
        int intValue = choiceCount.resolve(pc, "").intValue();
        c.setPoolFlag(required);
        if (intValue == Integer.MAX_VALUE) {
            c.setPickAll(true);
        } else {
            c.setTotalChoicesAvail(intValue);
        }
        if (title == null) {
            title = "Choose a " + StringPClassUtil.getStringFor(choices.getChoiceClass());
        }
        c.setTitle(title);
        Set<? extends T> set = choices.getSet(pc);
        if (c.pickAll()) {
            return set;
        } else {
            c.setAvailableList(new ArrayList<T>(set));
            c.setVisible(true);
            return c.getSelectedList();
        }
    }

    public void setTitle(String string) {
        title = string;
    }

    public void setRequired(boolean b) {
        required = b;
    }

    public void setChoiceActor(ChoiceActor<T> ca) {
        choiceActor = ca;
    }

    public void act(Collection<? extends T> driveChoice, PlayerCharacter apc) {
        if (choiceActor == null) {
            throw new IllegalStateException("Cannot act without a defined ChoiceActor");
        }
        for (T choice : driveChoice) {
            choiceActor.applyChoice(choice, apc);
            apc.addAssociation(this, choice);
        }
    }

    public T castChoice(Object o) {
        return (T) o;
    }
}
