package ru.cos.cs.agents.framework.example;

import java.util.HashSet;
import java.util.Set;
import ru.cos.cs.agents.framework.Agent;
import ru.cos.cs.agents.framework.Universe;

/**
 * @author zroslaw
 *
 */
public class SimpleUniverse implements Universe {

    private Set<Agent> newborns = new HashSet<Agent>();

    @Override
    public Set<Agent> lookupNewborns() {
        Set<Agent> result = newborns;
        newborns = new HashSet<Agent>();
        return result;
    }

    public void addNewborns(Set<Agent> newborns) {
        this.newborns.addAll(newborns);
    }

    @Override
    public void act(float dt) {
    }
}
