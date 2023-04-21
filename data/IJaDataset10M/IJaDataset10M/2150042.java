package jeco.lib.problems.fsm.problemFSMs;

import java.util.Random;

/**
 * Class implementing the functionality of a 1101 circuit detector
 *
 * @author J. Manuel Colmenar
 */
public class Seq1101Detector implements OracleProblemFunction {

    public String outputValue(String inputValue) {
        String output = "000";
        for (int i = 4; i <= inputValue.length(); i++) {
            String window = inputValue.substring(i - 4, i);
            if (window.equals("1101")) output += "1"; else output += "0";
        }
        return output;
    }

    public String[] randomInputOutput(int inputLength) {
        Random rnd = new Random();
        String[] data = new String[2];
        data[0] = "";
        for (int i = 0; i < inputLength; i++) data[0] += String.valueOf(rnd.nextInt(2));
        data[1] = outputValue(data[0]);
        return data;
    }
}
