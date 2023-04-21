package com.bbn.vessel.author.instructionalSignalEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bbn.vessel.author.models.SignalType;
import com.bbn.vessel.author.models.Terminal;

/**
 * @author rtomlinson
 *
 */
public abstract class InstructionalTerm {

    static class Quad {

        SignalInput input;

        SignalOutput output;

        SignalAux1 aux1;

        SignalAux2 aux2;

        /**
         * @param input the SignalInput
         * @param output the SignalOutput
         * @param aux1 the SignalAux1
         * @param aux2 the SignalAux2
         */
        public Quad(SignalInput input, SignalOutput output, SignalAux1 aux1, SignalAux2 aux2) {
            this.input = input;
            this.output = output;
            this.aux1 = aux1;
            this.aux2 = aux2;
        }

        /**
         * @return list of InstructionalTerms not including omit
         */
        public List<InstructionalTerm> getAll() {
            List<InstructionalTerm> signals = new ArrayList<InstructionalTerm>(aux1 == null ? 2 : 4);
            signals.add(input);
            signals.add(output);
            if (aux1 != null) {
                signals.add(aux1);
            }
            if (aux2 != null) {
                signals.add(aux2);
            }
            return signals;
        }
    }

    private abstract static class SignalTerm extends InstructionalTerm {

        protected final InstructionalSignal signal;

        /**
         * @param signal
         */
        protected SignalTerm(InstructionalSignal signal) {
            this.signal = signal;
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getSignalType()
         */
        @Override
        public SignalType getSignalType() {
            return getTerminal().getSignalType();
        }
    }

    abstract static class SignalMain extends SignalTerm {

        /**
         * @param signal
         */
        protected SignalMain(InstructionalSignal signal) {
            super(signal);
        }

        @Override
        public boolean isEvent() {
            return signal.isEvent();
        }

        @Override
        public boolean isState() {
            return signal.isState();
        }
    }

    abstract static class SignalAux extends SignalTerm {

        /**
         * @param signal
         */
        protected SignalAux(InstructionalSignal signal) {
            super(signal);
        }

        @Override
        public boolean isEvent() {
            return !signal.isEvent();
        }

        @Override
        public boolean isState() {
            return !signal.isState();
        }
    }

    static class SignalInput extends SignalMain {

        private SignalInput(InstructionalSignal signal) {
            super(signal);
        }

        @Override
        public Terminal getTerminal() {
            return signal.getInputTerminal();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getName()
         */
        @Override
        public String getName() {
            return signal.getName();
        }

        @Override
        public String getAnnotatedString() {
            return signal.getAnnotatedString();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getToolTipText()
         */
        @Override
        public String getToolTipText() {
            StringBuilder sb = new StringBuilder();
            if (signal.isGlobal()) {
                sb.append("Global instructional ").append(isEvent() ? "event" : "state").append(signal.getToolTipText());
            } else {
                sb.append("");
            }
            return sb.toString();
        }
    }

    static class SignalOutput extends SignalMain {

        private SignalOutput(InstructionalSignal signal) {
            super(signal);
        }

        @Override
        public Terminal getTerminal() {
            return signal.getOutputTerminal();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getName()
         */
        @Override
        public String getName() {
            return signal.getName();
        }

        @Override
        public String getAnnotatedString() {
            return signal.getAnnotatedString();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getToolTipText()
         */
        @Override
        public String getToolTipText() {
            StringBuilder sb = new StringBuilder();
            if (signal.isGlobal()) {
                sb.append("Global instructional ").append(isEvent() ? "event" : "state").append(signal.getToolTipText());
            } else {
                sb.append("");
            }
            return sb.toString();
        }
    }

    static class SignalAux1 extends SignalAux {

        private SignalAux1(InstructionalSignal signal) {
            super(signal);
        }

        @Override
        public Terminal getTerminal() {
            return signal.getAux1Terminal();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getName()
         */
        @Override
        public String getName() {
            if (isState()) {
                return "Before " + signal.getName();
            }
            return signal.getName() + " becomes true";
        }

        @Override
        public String getAnnotatedString() {
            if (isState()) {
                return signal.getAnnotatedString("Before ", "");
            }
            return signal.getAnnotatedString("", " becomes true");
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getToolTipText()
         */
        @Override
        public String getToolTipText() {
            StringBuilder sb = new StringBuilder();
            if (signal.isGlobal()) {
                sb.append("Global instructional ").append(isEvent() ? "event" : "state").append(signal.getToolTipText());
            } else {
                sb.append("");
            }
            return sb.toString();
        }
    }

    static class SignalAux2 extends SignalAux {

        private SignalAux2(InstructionalSignal signal) {
            super(signal);
        }

        @Override
        public Terminal getTerminal() {
            return signal.getAux2Terminal();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getName()
         */
        @Override
        public String getName() {
            if (isState()) {
                return "After " + signal.getName();
            }
            return signal.getName() + " becomes false";
        }

        @Override
        public String getAnnotatedString() {
            if (isState()) {
                return signal.getAnnotatedString("After ", "");
            }
            return signal.getAnnotatedString("", " becomes false");
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getToolTipText()
         */
        @Override
        public String getToolTipText() {
            StringBuilder sb = new StringBuilder();
            if (signal.isGlobal()) {
                sb.append("Global instructional ").append(isEvent() ? "event" : "state").append(signal.getToolTipText());
            } else {
                sb.append("");
            }
            return sb.toString();
        }
    }

    static class Special extends InstructionalTerm {

        private final InstructionalTerminal special;

        public Special(InstructionalTerminal special) {
            this.special = special;
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#isEvent()
         */
        @Override
        public boolean isEvent() {
            return special.isEvent();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#isState()
         */
        @Override
        public boolean isState() {
            return special.isState();
        }

        @Override
        public Terminal getTerminal() {
            return special.getTerminal();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getName()
         */
        @Override
        public String getName() {
            return special.getPlainString();
        }

        @Override
        public String getAnnotatedString() {
            return special.getAnnotatedString();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getToolTipText()
         */
        @Override
        public String getToolTipText() {
            return special.getToolTipText();
        }

        /**
         * @see com.bbn.vessel.author.instructionalSignalEditor.InstructionalTerm#getSignalType()
         */
        @Override
        public SignalType getSignalType() {
            return getTerminal().getSignalType();
        }
    }

    private static Map<InstructionalSignal, Quad> quads = new HashMap<InstructionalSignal, Quad>();

    /**
     * @param is the InstructionalSignal
     * @return the SignalInput corresponding to the supplied instructional signal
     */
    public static SignalInput getSignalInput(InstructionalSignal is) {
        Quad quad = getQuad(is);
        return quad.input;
    }

    /**
     * @param is the InstructionalSignal
     * @return the SignalInput corresponding to the supplied instructional signal
     */
    public static SignalOutput getSignalOutput(InstructionalSignal is) {
        Quad quad = getQuad(is);
        return quad.output;
    }

    /**
     * @param is the InstructionalSignal
     * @return the SignalInput corresponding to the supplied instructional signal
     */
    public static SignalAux1 getSignalAux1(InstructionalSignal is) {
        Quad quad = getQuad(is);
        return quad.aux1;
    }

    /**
     * @param is the InstructionalSignal
     * @return the SignalInput corresponding to the supplied instructional signal
     */
    public static SignalAux2 getSignalAux2(InstructionalSignal is) {
        Quad quad = getQuad(is);
        return quad.aux2;
    }

    private static Quad getQuad(InstructionalSignal is) {
        Quad quad = quads.get(is);
        if (quad == null) {
            SignalInput input = new SignalInput(is);
            SignalOutput output = new SignalOutput(is);
            SignalAux1 aux1 = new SignalAux1(is);
            if (aux1.getTerminal() == null) {
                aux1 = null;
            }
            SignalAux2 aux2 = new SignalAux2(is);
            if (aux2.getTerminal() == null) {
                aux2 = null;
            }
            quad = new Quad(input, output, aux1, aux2);
            input.setOthers(quad.getAll());
            output.setOthers(quad.getAll());
            if (aux1 != null) {
                aux1.setOthers(quad.getAll());
            }
            if (aux2 != null) {
                aux2.setOthers(quad.getAll());
            }
            quads.put(is, quad);
        }
        return quad;
    }

    private final List<InstructionalTerm> others = new ArrayList<InstructionalTerm>(3);

    @Override
    public String toString() {
        return getName();
    }

    /**
     * @return the name of this terminal
     */
    public abstract String getName();

    /**
     * @return styled string
     */
    public abstract String getAnnotatedString();

    /**
     * @return true if this terminal is an event
     */
    public abstract boolean isEvent();

    /**
     * @return true if this terminal is a state
     */
    public abstract boolean isState();

    /**
     * @return the SignalType
     */
    public abstract SignalType getSignalType();

    /**
     * @return the tool tip text
     */
    public abstract String getToolTipText();

    /**
     * @return the corresponding Terminal
     */
    public abstract Terminal getTerminal();

    /**
     * @return the related signals
     */
    public List<InstructionalTerm> getOthers() {
        return others;
    }

    protected void setOthers(List<InstructionalTerm> others) {
        for (InstructionalTerm other : others) {
            if (other == this) continue;
            if (other == null) continue;
            this.others.add(other);
        }
    }
}
