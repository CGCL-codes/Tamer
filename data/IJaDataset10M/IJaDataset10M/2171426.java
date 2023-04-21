package com.bbn.vessel.author.instructionalSignalEditor;

import java.util.List;
import com.bbn.vessel.author.models.SignalType;

/**
 * @author rtomlinson
 *
 */
public class FnBetween extends FnBase {

    private final Pattern pattern1 = new Pattern("") {

        @Override
        public SignalType getOutputSignalType() {
            return SignalType.CONDITION;
        }

        @Override
        public SignalType getInput1SignalType() {
            return SignalType.TRIGGER;
        }

        @Override
        public SignalType getInput2SignalType() {
            return SignalType.TRIGGER;
        }

        @Override
        public boolean isInput1Multiselect() {
            return false;
        }

        @Override
        public boolean isInput2Multiselect() {
            return false;
        }

        @Override
        public String asEnglish(InstructionalTerm output, List<InstructionalTerm> inputs1, List<InstructionalTerm> inputs2) {
            return String.format("State \"%s\" is true between event \"%s\" and event \"%s\"", (output == null ? "<undefined>" : output.getName()), (inputs1.isEmpty() ? "<undefined>" : inputs1.get(0).getName()), (inputs2.isEmpty() ? "<undefined>" : inputs2.get(0).getName()));
        }

        @Override
        protected String getNodeType() {
            return "IsIf";
        }

        @Override
        protected String getOutName() {
            return "";
        }

        @Override
        protected String getIn1Name() {
            return "true";
        }

        @Override
        protected String getIn2Name() {
            return "false";
        }
    };

    /**
     * Constructor
     */
    public FnBetween() {
        super("Between");
        setPatterns(pattern1);
    }
}
