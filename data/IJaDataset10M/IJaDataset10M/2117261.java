package playground.johannes.interaction;

import java.util.Collection;

/**
 * @author illenberger
 *
 */
public interface InteractionSelector {

    public Collection<Visitor> select(Visitor v, Collection<Visitor> choiceSet);
}
