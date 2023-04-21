package chapter2.dsl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exo05 {

    public static void main(String[] args) {
        TurtleParser parser = new TurtleParser();
        StringBuilder code = new StringBuilder();
        code.append("P 2").append("\n");
        code.append("D").append("\n");
        code.append("W 2").append("\n");
        code.append("N 1").append("\n");
        code.append("E 2").append("\n");
        code.append("S 1").append("\n");
        code.append("U").append("\n");
        parser.parse(code.toString());
    }

    public static class TurtleParser {

        private static final Map<String, Integer> grammar = new HashMap<String, Integer>();

        private static final Map<String, String> interpreter = new HashMap<String, String>();

        static {
            grammar.put("P", 1);
            grammar.put("D", 0);
            grammar.put("W", 1);
            grammar.put("N", 1);
            grammar.put("E", 1);
            grammar.put("S", 1);
            grammar.put("U", 0);
            interpreter.put("P", "Set pen with to $1");
            interpreter.put("D", "Start drawing");
            interpreter.put("W", "Move to west by $1 position");
            interpreter.put("N", "Move to north by $1 position");
            interpreter.put("E", "Move to east by $1 position");
            interpreter.put("S", "Move to south by $1 position");
            interpreter.put("U", "Stop drawing");
        }

        public Boolean parse(String code) {
            List<String> lines = Arrays.asList(code.split("\n"));
            for (String line : lines) {
                System.out.println(line);
                List<String> terms = Arrays.asList(line.split(" "));
                if (terms.isEmpty()) {
                    System.err.println("Parsing error, Line " + lines.indexOf(line) + " : Empty Line");
                    return false;
                }
                String instruction = (terms.get(0)).toUpperCase();
                if (!grammar.containsKey(instruction)) {
                    System.err.println("Parsing error, Line " + lines.indexOf(line) + " : Unrecongnized instruction " + instruction);
                    return false;
                } else if (grammar.get(instruction) != (terms.size() - 1)) {
                    System.err.println("Parsing error, Line " + lines.indexOf(line) + " : Expected " + grammar.get(instruction) + " param. for the instruction " + instruction + " but found " + (terms.size() - 1));
                    return false;
                }
                String interpretation = interpreter.get(instruction);
                for (String term : terms) {
                    interpretation = interpretation.replace("$" + terms.indexOf(term), term);
                }
                System.out.println(interpretation);
            }
            return true;
        }
    }
}
