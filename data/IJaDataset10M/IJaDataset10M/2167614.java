package br.ufrj.dcc.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Vector;
import br.ufrj.dcc.AbstractLearningProblem;

/**
 * Tokenize the input file and do assertions to the learning problem.
 * Currently only supports .txt type.
 * See the input.txt example in resources for a tip of input format.
 * @author Pedro Rougemont
 *
 */
public class InputSetter {

    private AbstractLearningProblem learningProblem;

    public InputSetter(AbstractLearningProblem abstractLearningProblem) {
        learningProblem = abstractLearningProblem;
    }

    public void parseAndSetEntries(File file) throws Exception {
        String fileType = "";
        String in = "";
        FileReader fileReader;
        BufferedReader bufferedReader;
        int i = 1;
        fileType = file.getName();
        if (fileType.endsWith("txt")) {
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            } catch (Exception e) {
                throw new Error("File reader error!");
            }
            for (in = bufferedReader.readLine(); in != null; in = bufferedReader.readLine(), i++) {
                in.trim();
                if (in.startsWith("atrib(")) {
                    in = trimOuterBracers(in);
                    String[] attributeNames = in.split(",");
                    List<String> attributes = new Vector<String>();
                    for (int j = 0; j < attributeNames.length; j++) {
                        attributes.add(attributeNames[j]);
                    }
                    learningProblem.declareAttributes(attributes);
                } else if (in.startsWith("goal_atrib(")) {
                    in = trimOuterParenteses(in);
                    learningProblem.declareGoalAttribute(in);
                } else if (in.startsWith("dom(")) {
                    in = trimOuterParenteses(in);
                    String attribute = in.substring(0, in.indexOf(","));
                    in = trimOuterBracers(in.substring(in.indexOf(",") + 1, in.length()));
                    String[] domainValues = in.split(",");
                    List<String> attributeDomain = new Vector<String>();
                    for (int j = 0; j < domainValues.length; j++) {
                        attributeDomain.add(domainValues[j]);
                    }
                    learningProblem.declareAttributeDomain(attribute, attributeDomain);
                } else if (in.startsWith("goal_dom(")) {
                    in = trimOuterBracers(in);
                    String[] domainValues = in.split(",");
                    List<String> attributeDomain = new Vector<String>();
                    for (int j = 0; j < domainValues.length; j++) {
                        attributeDomain.add(domainValues[j]);
                    }
                    learningProblem.declareGoalAttributeDomain(attributeDomain);
                } else if (in.startsWith("size(")) {
                    in = trimOuterParenteses(in);
                    learningProblem.setNumberOfTrainingEntries(Integer.parseInt(in));
                } else if (in.startsWith("column(")) {
                    in = trimOuterParenteses(in);
                    String attribute = in.substring(0, in.indexOf(","));
                    in = trimOuterBracers(in.substring(in.indexOf(",") + 1, in.length()));
                    String[] exampleValues = in.split(",");
                    List<String> attributeColumn = new Vector<String>();
                    for (int j = 0; j < exampleValues.length; j++) {
                        attributeColumn.add(exampleValues[j]);
                    }
                    learningProblem.declareExamples(attribute, attributeColumn);
                } else if (in.startsWith("goal_column(")) {
                    in = trimOuterBracers(in);
                    String[] exampleValues = in.split(",");
                    List<String> attributeColumn = new Vector<String>();
                    for (int j = 0; j < exampleValues.length; j++) {
                        attributeColumn.add(exampleValues[j]);
                    }
                    learningProblem.declareGoals(attributeColumn);
                }
            }
        }
    }

    private String trimOuterBracers(String in) {
        in.trim();
        in = (String) in.subSequence(in.indexOf("[") + 1, in.lastIndexOf("]"));
        in.trim();
        return in;
    }

    private String trimOuterParenteses(String in) {
        in.trim();
        in = (String) in.subSequence(in.indexOf("(") + 1, in.lastIndexOf(")"));
        in.trim();
        return in;
    }
}
