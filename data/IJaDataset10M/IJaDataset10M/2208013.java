package writer2latex.latex.util;

/** This class contains a trie of string -> LaTeX code replacements 
*/
public class ReplacementTrie extends ReplacementTrieNode {

    public ReplacementTrie() {
        super('*', 0);
    }

    public ReplacementTrieNode get(String sInput) {
        return get(sInput, 0, sInput.length());
    }

    public ReplacementTrieNode get(String sInput, int nStart, int nEnd) {
        if (sInput.length() == 0) {
            return null;
        } else {
            return super.get(sInput, nStart, nEnd);
        }
    }

    public void put(String sInput, String sLaTeXCode, int nFontencs) {
        if (sInput.length() == 0) {
            return;
        } else {
            super.put(sInput, sLaTeXCode, nFontencs);
        }
    }

    public String[] getInputStrings() {
        return null;
    }
}
