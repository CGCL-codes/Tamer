package org.osee.indexer.automaton;

import org.osee.indexer.automaton.DFA;

public class AutomatonProcessor {

    private String[] results;

    public String[] getResults() {
        return results;
    }

    public AutomatonProcessor(DFA dfa, String content) {
        DFA[] dfas = new DFA[1];
        dfas[0] = dfa;
        content = PreWork.getNewContent(dfas, content);
        boolean[] found = new boolean[dfa.keyWordsNum];
        for (int i = 0; i < dfa.keyWordsNum; i++) {
            found[i] = false;
        }
        int now = 0, cnt = 0;
        byte[] bt = content.getBytes();
        for (int i = 0, j = 0; i < content.length(); i++) {
            if (PreWork.isInterpunction(content.charAt(i))) {
                now = 0;
                j++;
                continue;
            }
            int next = bt[j] - dfa.delta[0];
            j++;
            int nnext = bt[j] - dfa.delta[1];
            while (dfa.edge[now][next] == -1 || dfa.edge[dfa.edge[now][next]][nnext] == -1) {
                now = dfa.fail[now];
            }
            now = dfa.edge[now][next];
            if (now != 0) {
                now = dfa.edge[now][nnext];
            } else {
                now = 0;
            }
            j++;
            int index = dfa.word[now];
            if (index != -1 && !found[index]) {
                cnt++;
                found[index] = true;
            }
        }
        now = 0;
        results = new String[cnt];
        for (int i = 0; i < found.length; i++) {
            if (found[i]) {
                results[now] = dfa.words[i];
                now++;
            }
        }
    }
}
