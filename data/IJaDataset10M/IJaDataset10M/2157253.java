package net.sf.parser4j.kernelgenerator.service.dfa;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.sf.parser4j.kernelgenerator.entity.state.StateByTerminalRange;
import net.sf.parser4j.kernelgenerator.entity.state.dfa.DfaState;
import net.sf.parser4j.parser.entity.data.NonTerminalMap;
import net.sf.parser4j.parser.entity.data.TerminalCharRange;

/**
 * 
 * @author luc peuvrier
 * 
 */
public final class DfaToStringUtil {

    private static final DfaToStringUtil INSTANCE = new DfaToStringUtil();

    public static DfaToStringUtil getInstance() {
        return INSTANCE;
    }

    private DfaToStringUtil() {
        super();
    }

    public String dfaToString(final DfaState initialState, final NonTerminalMap nonTerminalMap) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Deque<DfaState> stateQue = new LinkedList<DfaState>();
        final Map<Integer, String> stateNameMap = new TreeMap<Integer, String>();
        final Set<Integer> visitedSet = new TreeSet<Integer>();
        stateQue.addLast(initialState);
        stateNameMap.put(initialState.getStateIdentifier(), "state 0");
        int stateCount = 1;
        DfaState dfaState;
        while ((dfaState = stateQue.pollFirst()) != null) {
            visitedSet.add(dfaState.getStateIdentifier());
            stringBuilder.append(stateNameMap.get(dfaState.getStateIdentifier()));
            stringBuilder.append('\n');
            final Set<Integer> matchedNonTerminalSet = dfaState.getMatchedNonTerminalSet();
            for (int nonTerminalIdentifier : matchedNonTerminalSet) {
                stringBuilder.append("match ");
                stringBuilder.append(nonTerminalMap.nonTerminal(nonTerminalIdentifier));
                stringBuilder.append('\n');
            }
            final Iterator<StateByTerminalRange> iterator = dfaState.gotoByTerminalIterator();
            while (iterator.hasNext()) {
                final StateByTerminalRange stateByTerminalRange = (StateByTerminalRange) iterator.next();
                final DfaState gotoState = (DfaState) stateByTerminalRange.getState();
                final int stateIdentifier = gotoState.getStateIdentifier();
                if (!visitedSet.contains(stateIdentifier)) {
                    stateQue.addLast(gotoState);
                    stateNameMap.put(gotoState.getStateIdentifier(), "state " + (stateCount++));
                }
                final TerminalCharRange terminalCharRange = stateByTerminalRange.getTerminalCharRange();
                stringBuilder.append("by ");
                stringBuilder.append(terminalCharRange.toString());
                stringBuilder.append(" goto ");
                stringBuilder.append(stateNameMap.get(gotoState.getStateIdentifier()));
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }
}
