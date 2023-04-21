package org.lilian.models;

import java.util.*;
import static java.util.Collections.*;
import org.lilian.*;
import org.lilian.corpora.*;

public class FrequencyModelDemo {

    /**
	 * A demo showing the use of the Frequency model.
	 */
    public static void main(String[] args) {
        SequenceCorpus<String> corpus = Common.alice();
        BasicFrequencyModel<String> model = new BasicFrequencyModel<String>(corpus);
        List<String> tokens = new ArrayList<String>(model.tokens());
        Collections.sort(tokens, reverseOrder(new ProbabilityModel.Comparator<String>(model)));
        System.out.println(tokens.subList(0, 10));
        System.out.println();
        BasicFrequencyModel<Character> charModel = new BasicFrequencyModel<Character>();
        charModel.add('a');
        charModel.add('a');
        charModel.add('b');
        charModel.add('c', 4);
        charModel.add('d', 0);
        charModel.print(System.out);
    }
}
