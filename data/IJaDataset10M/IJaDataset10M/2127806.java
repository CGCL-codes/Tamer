package net.sf.signs.fsm;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.signs.HashSetArray;
import net.sf.signs.SigType;
import net.sf.signs.SignsException;
import net.sf.signs.Value;

public class DumpKISS implements IDumpFSM {

    public enum KissSignal {

        Zero, One, DontCare
    }

    private HashSetArray<String> outputs;

    private HashSetArray<String> inputs;

    private HashMap<String, Integer> inputPositions;

    private HashMap<String, Integer> outputPositions;

    private void dumpState(FSM fsm_, State state, PrintStream out_) throws SignsException {
        if (state == null) {
            state = fsm_.getState("others");
        }
        if (state == null) {
            throw new SignsException("Unspecified state " + state + ". Try adding it or an others branch into your case statement.");
        }
        state.dump(fsm_, state, inputs, outputs, out_);
    }

    public void dump(ArrayList<FSM> fsms_, PrintStream out_) throws SignsException, IOException {
        int n = fsms_.size();
        for (int i = 0; i < n; i++) {
            FSM fsm = fsms_.get(i);
            outputs = fsm.getOutputs();
            inputs = fsm.getInputs();
            inputPositions = new HashMap<String, Integer>();
            outputPositions = new HashMap<String, Integer>();
            out_.println(".model " + fsm.getName());
            out_.print(".inputs");
            for (int j = 0; j < inputs.size(); j++) {
                out_.print(" " + inputs.get(j));
                inputPositions.put(inputs.get(j), j);
            }
            out_.println();
            out_.print(".outputs");
            for (int j = 0; j < outputs.size(); j++) {
                out_.print(" " + outputs.get(j));
                outputPositions.put(outputs.get(j), j);
            }
            out_.println();
            out_.println(".clock " + fsm.getClock());
            out_.println(".start_kiss");
            out_.println(".i " + inputs.size());
            out_.println(".o " + outputs.size());
            State resetState = fsm.getResetState();
            if (resetState != null) {
                out_.println(".r " + resetState);
            }
            SigType t = fsm.getStateType();
            if (t.getCategory() == SigType.CAT_ENUM) {
                int m = t.getNumEnumLiterals();
                for (int j = 0; j < m; j++) {
                    String el = t.getEnumLiteral(j).id;
                    State state = fsm.createState(el);
                    dumpState(fsm, state, out_);
                }
            } else {
                long max = (1 << t.getWidth());
                for (long j = 0; j < max; j++) {
                    String el = Value.convert(j, t.getWidth());
                    State state = fsm.createState(el);
                    dumpState(fsm, state, out_);
                }
            }
            out_.println(".end_kiss");
            out_.println(".end");
        }
    }

    public static KissSignal convert(char v_) throws SignsException {
        switch(v_) {
            case '0':
                return KissSignal.Zero;
            case '1':
                return KissSignal.One;
            case '-':
                return KissSignal.DontCare;
            default:
                throw new SignsException("Sorry, unsupported condition found: KISS only supports {0, 1, -}.");
        }
    }

    public static char convert(KissSignal v_) throws SignsException {
        switch(v_) {
            case DontCare:
                return '-';
            case One:
                return '1';
            case Zero:
                return '0';
        }
        throw new SignsException("KissSignal: " + v_ + " does not compute.");
    }
}
