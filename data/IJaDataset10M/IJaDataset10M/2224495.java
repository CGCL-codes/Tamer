package net.sf.jeckit.impl.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.sf.jeckit.common.Sentence;
import net.sf.jeckit.common.StringDistance;
import net.sf.jeckit.common.Suggestion;
import net.sf.jeckit.common.Token;
import net.sf.jeckit.interfaces.Suggester;

public class SuggesterList implements Suggester {

    private DBReader db;

    private int n;

    private List<String> words;

    public SuggesterList(DBReader db, int n) {
        super();
        this.db = db;
        this.n = n;
        words = db.selectTopNWords(n);
    }

    public static double getLevenshteinDistance(String s1, String s2, double insertion_cost, double deletion_cost, double substitution_cost) {
        double[][] matrix = new double[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j < matrix[0].length; j++) {
            matrix[0][j] = j;
        }
        double tmp;
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[i].length; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) tmp = 0; else tmp = substitution_cost;
                double min1 = Math.min(matrix[i - 1][j] + insertion_cost, matrix[i][j - 1] + deletion_cost);
                matrix[i][j] = Math.min(min1, matrix[i - 1][j - 1] + tmp);
            }
        }
        double dist = matrix[s1.length()][s2.length()];
        return dist;
    }

    public List<String> getTopN(String str, int n) {
        List<StringWithDist> list = new ArrayList<StringWithDist>(words.size());
        List<String> result = new ArrayList<String>(n);
        for (String cand : words) {
            list.add(new StringWithDist(getLevenshteinDistance(str, cand, 0.123, 0.132, 0.112), cand));
        }
        Collections.sort(list, new StringWithDistComparator());
        Iterator<StringWithDist> iterator = list.iterator();
        for (int i = 0; i < n; i++) {
            if (iterator.hasNext()) {
                StringWithDist strwd = iterator.next();
                result.add(strwd.str);
            }
        }
        return result;
    }

    @Override
    public void createSuggestions(Sentence sentence) {
        int suspicious = -1;
        List<Token> nonEmptyTokens = Utilities.getNotEmptyTokens(sentence);
        for (int i = 0; i < nonEmptyTokens.size(); i++) {
            if (nonEmptyTokens.get(i).getErrorProbability() > 0.2) {
                suspicious = i;
            }
        }
        Token tok = nonEmptyTokens.get(suspicious);
        String tokenStr = tok.getContent();
        HashSet<String> allCandidates = new HashSet<String>();
        allCandidates.addAll(getTopN(tokenStr, 20));
        for (String s : allCandidates) {
            tok.addSuggestion(new Suggestion(s, 0));
        }
    }
}

class StringWithDist {

    public String str;

    public double dist;

    public StringWithDist(double dist, String str) {
        super();
        this.dist = dist;
        this.str = str;
    }
}

class StringWithDistComparator implements Comparator<StringWithDist> {

    @Override
    public int compare(StringWithDist arg0, StringWithDist arg1) {
        return (int) Math.signum(arg0.dist - arg1.dist);
    }
}
