package net.sourceforge.ondex.xten.functions.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Temporary solution that maintains compatibility with previous version.
 * (Needs to be refactored into a proper wrapper that does not access any of the AttributePrototype code)
 *
 * @author lysenkoa
 *
 */
public class APDelimitedAdaptor extends AttributePrototype<String[]> {

    private List<Integer[]> vectorToPrototype = new LinkedList<Integer[]>();

    protected Integer intialiser = null;

    public APDelimitedAdaptor(AttributePrototype<?> ap) {
        this.required = ap.required;
        this.currentValues = ap.currentValues;
        this.exactDictionary = ap.exactDictionary;
        this.regexDictionary = ap.regexDictionary;
        this.regexExtractor = ap.regexExtractor;
        this.userInput = ap.userInput;
        this.split = ap.split;
        if (userInput.length == 0) return;
        if (userInput[0] instanceof String) initialise((String) userInput[0], userInput); else if (userInput[0] instanceof Integer) {
            intialiser = (Integer) userInput[0];
        }
    }

    @Override
    protected void initialise(String attType, Object... args) {
        super.initialise(attType, args);
        int min = Math.min(prot.length, args.length);
        for (int i = 1; i < min; i++) {
            if (args[i].getClass().equals(Integer.class)) {
                vectorToPrototype.add(new Integer[] { (Integer) args[i], i });
            }
        }
    }

    @Override
    public void parse(String[] line) {
        if (intialiser != null && intialiser < line.length) {
            String attType = line[intialiser];
            if (attType == null) return;
            userInput[0] = attType;
            initialise(attType, userInput);
        }
        if (prot == null) return;
        String[] currentValue = Arrays.copyOf(prot, prot.length);
        for (Integer[] pos : vectorToPrototype) {
            if (pos[0] >= line.length) currentValue[pos[1]] = null; else currentValue[pos[1]] = translate(pos[1], line[pos[0]]);
        }
        if (split != null) {
            for (Integer[] pos : vectorToPrototype) {
                String[] substrings = (line[pos[0]]).split(split);
                for (String s : substrings) {
                    String[] copyOfCurrentValue = Arrays.copyOf(currentValue, currentValue.length);
                    copyOfCurrentValue[pos[1]] = s;
                    currentValues.add(copyOfCurrentValue);
                }
            }
        } else {
            currentValues.add(currentValue);
        }
    }
}
