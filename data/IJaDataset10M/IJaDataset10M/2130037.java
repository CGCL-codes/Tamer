package com.google.code.insect.workflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.code.insect.workflow.comm.TransitionType;

public class DefaultWorkFlowAlgorithm implements WorkFlowAlgorithm {

    public final synchronized void enabledTraversing(WorkFlow wf) {
        List<Transition> transitions = wf.getTransitions();
        for (int i = 0; i < transitions.size(); i++) {
            Transition transition = transitions.get(i);
            synchronized (transition) {
                List<Place> inputs = transition.getInputs();
                int inputTokenCount = inputs.size();
                if (inputTokenCount == 0) {
                    transition.setEnable(false);
                    continue;
                }
                Set<Token> maxSet = new HashSet<Token>();
                for (int j = 0; j < inputs.size(); j++) {
                    Place place = inputs.get(j);
                    if (place.getTokens().size() > maxSet.size()) maxSet = new HashSet<Token>(place.getTokens());
                }
                if (maxSet.size() > 0 && transition.getType().equals(TransitionType.OR_JOIN)) {
                    transition.setEnable(true);
                    continue;
                }
                Map<Long, AtomicInteger> map = new HashMap<Long, AtomicInteger>();
                for (Token token : maxSet) {
                    map.put(token.getId(), new AtomicInteger(0));
                }
                boolean enable = false;
                Iterator<Place> it = transition.getInputs().iterator();
                while (it.hasNext()) {
                    Place element = it.next();
                    List<Token> set = element.getTokens();
                    for (int j = 0; j < set.size(); j++) {
                        Token token = set.get(j);
                        if (map.containsKey(token.getId())) {
                            map.get(token.getId()).incrementAndGet();
                        }
                    }
                }
                Collection<AtomicInteger> values = map.values();
                Iterator<AtomicInteger> countIt = values.iterator();
                while (countIt.hasNext()) {
                    int num = countIt.next().get();
                    if (num == inputTokenCount) {
                        enable = true;
                        break;
                    }
                }
                transition.setEnable(enable);
            }
        }
    }
}
