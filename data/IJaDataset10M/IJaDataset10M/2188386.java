package org.yuanheng.cookcc.lexer;

import java.util.LinkedList;

/**
 * @author Heng Yuan
 * @version $Id: NFAFactory.java 747 2012-03-20 06:04:10Z superduperhengyuan@gmail.com $
 */
class NFAFactory {

    private final CCL m_ccl;

    private final ECS m_ecs;

    private final LinkedList<NFA> m_spareNFAs = new LinkedList<NFA>();

    private int m_nfaCounter = 0;

    NFAFactory(CCL ccl) {
        m_ccl = ccl;
        m_ecs = new ECS(ccl.MAX_SYMBOL);
    }

    int incNFACounter() {
        return m_nfaCounter++;
    }

    public CCL getCCL() {
        return m_ccl;
    }

    public int getTotalNFACount() {
        return m_nfaCounter - m_spareNFAs.size();
    }

    public NFA createNFA() {
        NFA nfa;
        if (m_spareNFAs.isEmpty()) nfa = new NFA(this); else nfa = m_spareNFAs.removeFirst();
        return nfa;
    }

    public NFA createNFA(NFA sample) {
        NFA nfa = createNFA();
        nfa.copyStates(sample);
        return nfa;
    }

    public NFA createNFA(int ch, boolean[] ccl) {
        NFA nfa = createNFA();
        nfa.thisChar = ch;
        nfa.charSet = ccl;
        if (ch >= 0) m_ecs.add(ch); else if (ch == NFA.ISCCL) m_ecs.add(ccl);
        nfa.next = createNFA();
        return nfa;
    }

    public ECS getECS() {
        return m_ecs;
    }

    public void deleteNFA(NFA nfa) {
        nfa.init();
        m_spareNFAs.add(nfa);
    }

    @Override
    public String toString() {
        return "total NFAs: " + getTotalNFACount() + ", maximum NFAs: " + m_nfaCounter;
    }
}
