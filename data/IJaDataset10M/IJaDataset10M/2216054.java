package tml.vectorspace.operations;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tml.annotators.PennTreeAnnotator;
import tml.conceptmap.NoPennTreeAnnotationsException;
import tml.utils.StanfordUtils;
import tml.vectorspace.operations.results.TerminologicalMapResult;
import edu.stanford.nlp.trees.Tree;

/**
 * @author Jorge Villalon
 *
 */
public class TerminologicalMap extends AbstractOperation<TerminologicalMapResult> {

    public TerminologicalMap() {
        this.name = "Terminological map";
    }

    @Override
    public void start() throws Exception {
        super.start();
        Hashtable<String, List<String>> dependencies = new Hashtable<String, List<String>>();
        int total = 0;
        operationPerformed(new OperationEvent(this, this.corpus.getPassages().length, total));
        for (String sentenceId : this.corpus.getPassages()) {
            total++;
            operationPerformed(new OperationEvent(this, this.corpus.getPassages().length, total));
            Tree tree = null;
            String pennTreeString = null;
            pennTreeString = this.repository.getAnnotations(sentenceId, PennTreeAnnotator.FIELD_NAME);
            if (pennTreeString == null) {
                throw new NoPennTreeAnnotationsException();
            }
            tree = StanfordUtils.getTreeFromString(sentenceId, pennTreeString);
            List<String> current = StanfordUtils.calculateTypedDependencies(tree);
            if (current != null) {
                dependencies.put(sentenceId, current);
            }
            if (this.maxResults > 0 && total >= this.maxResults) break;
        }
        int sentenceNumber = 0;
        operationPerformed(new OperationEvent(this, dependencies.keySet().size(), sentenceNumber));
        for (String key : dependencies.keySet()) {
            sentenceNumber++;
            for (String dependecy : dependencies.get(key)) {
                Pattern pattern = Pattern.compile("^(\\w+)\\((.+)-(\\d+)'?-(.+), (.+)-(\\d+)'?-(.+)\\)$");
                Matcher matcher = pattern.matcher(dependecy);
                if (matcher.matches()) {
                    String linking = matcher.group(1);
                    String nodeAlbl = matcher.group(2);
                    String nodeBlbl = matcher.group(5);
                    String nodeAPOS = matcher.group(4);
                    if (nodeAPOS.equals("null")) nodeAPOS = "NN";
                    String nodeBPOS = matcher.group(7);
                    if (nodeBPOS.equals("null")) nodeBPOS = "NN";
                    int posNodeA = Integer.parseInt(matcher.group(3));
                    int posNodeB = Integer.parseInt(matcher.group(6));
                    TerminologicalMapResult result = new TerminologicalMapResult();
                    result.setNodeA(nodeAlbl);
                    result.setNodeB(nodeBlbl);
                    result.setLinkingWord(linking);
                    result.setSentenceNumber(sentenceNumber);
                    result.setSentenceId(key);
                    result.setNodeAPOS(nodeAPOS);
                    result.setNodeBPOS(nodeBPOS);
                    result.setNodeAPosition(posNodeA);
                    result.setNodeBPosition(posNodeB);
                    results.add(result);
                }
            }
            operationPerformed(new OperationEvent(this, dependencies.keySet().size(), sentenceNumber));
        }
        Collections.sort(results, new Comparator<TerminologicalMapResult>() {

            public int compare(TerminologicalMapResult o1, TerminologicalMapResult o2) {
                return o1.getSentenceNumber() - o2.getSentenceNumber();
            }
        });
        super.end();
    }
}
