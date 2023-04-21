package edu.umn.cs.nlp.mt.huangchiang2005;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.umn.cs.nlp.util.Equatable;

/**
 * Represents a derivation and associated index vector.
 * <p>
 * WARNING: The compareTo method of this class is inconsistent with its equals method.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2008-01-10 14:43:06 -0500 (Thu, 10 Jan 2008) $
 * @see "Better k-best Parsing" by Liang Huang & David Chiang (IWPT, 2005)
 */
@SuppressWarnings("unchecked")
class Candidate<Parse extends Derivation> implements Comparable<Candidate<Parse>>, Equatable<Candidate<Parse>> {

    final Parse parse;

    final int[] index;

    Candidate(Parse parse, int[] index) {
        this.parse = parse;
        this.index = index;
    }

    <Node extends Vertex> Candidate(Hyperarc<Node, Parse> e) {
        final int size = e.tail.size();
        int[] indices = new int[size];
        for (int i = 0; i < size; i++) {
            indices[i] = 0;
        }
        List<Parse> childParses = new ArrayList<Parse>(e.tail.size());
        for (int index = 0; index < e.tail.size(); index++) {
            Parse child = (Parse) e.tail.get(index).getDerivation(indices[index]);
            childParses.add(child);
        }
        this.parse = e.getDerivation(childParses);
        this.index = indices;
    }

    <Node extends Vertex> Candidate(Hyperarc<Node, Parse> e, int[] indices) {
        List<Parse> childParses = new ArrayList<Parse>(e.tail.size());
        for (int index = 0; index < e.tail.size(); index++) {
            Parse child = (Parse) e.tail.get(index).getDerivation(indices[index]);
            childParses.add(child);
        }
        this.parse = e.getDerivation(childParses);
        this.index = indices;
    }

    public static int[] incrementIndex(int[] j, int b) {
        int[] j_prime = new int[j.length];
        System.arraycopy(j, 0, j_prime, 0, j.length);
        j_prime[b]++;
        return j_prime;
    }

    /**
	 * 
	 */
    public int compareTo(Candidate<Parse> o) {
        return parse.compareTo(o.parse);
    }

    public boolean equals(Candidate<Parse> o) {
        return Arrays.equals(index, o.index);
    }

    public String toString() {
        return Arrays.toString(index) + " " + parse.toString();
    }
}
