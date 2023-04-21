package opennlp.tools.parser;

import java.util.HashMap;
import java.util.Map;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.parser.chunking.Parser;
import opennlp.tools.util.SequenceValidator;

public class ParserChunkerSequenceValidator implements SequenceValidator<String> {

    private Map<String, String> continueStartMap;

    public ParserChunkerSequenceValidator(ChunkerModel model) {
        continueStartMap = new HashMap<String, String>(model.getChunkerModel().getNumOutcomes());
        for (int oi = 0, on = model.getChunkerModel().getNumOutcomes(); oi < on; oi++) {
            String outcome = model.getChunkerModel().getOutcome(oi);
            if (outcome.startsWith(Parser.CONT)) {
                continueStartMap.put(outcome, Parser.START + outcome.substring(Parser.CONT.length()));
            }
        }
    }

    public boolean validSequence(int i, String[] inputSequence, String[] tagList, String outcome) {
        if (continueStartMap.containsKey(outcome)) {
            int lti = tagList.length - 1;
            if (lti == -1) {
                return false;
            } else {
                String lastTag = tagList[lti];
                if (lastTag.equals(outcome)) {
                    return true;
                }
                if (lastTag.equals(continueStartMap.get(outcome))) {
                    return true;
                }
                if (lastTag.equals(Parser.OTHER)) {
                    return false;
                }
                return false;
            }
        }
        return true;
    }
}
