package org.systemsbiology.apps.transitiongenerator;

import java.io.IOException;
import java.util.List;
import org.systemsbiology.lib.commonobj.*;

public class TransitionGeneratorTest {

    public static final String INPUT_TEST_FILE = "/users/skwok/Desktop/inputTransition.csv";

    public static final String OUTPUT_TEST_FILE = "/users/skwok/Desktop/outputTransition.csv";

    public static void main(String args[]) {
        ITransitionParser parser = TransitionParserFactory.getInstance(TransitionParserFactory.FULL_FORMAT);
        IDecoyAlgorithm algor = DecoyAlgorithmFactory.getInstance(DecoyAlgorithmFactory.SIMPLE_ALGORITHM);
        DecoyAlgorithmBean bean = DecoyAlgorithmBean.getDefaultInstance();
        DecoyTransitionGenerator gen = new DecoyTransitionGenerator(INPUT_TEST_FILE, algor, parser);
        try {
            String name = parser.getTransitionParserName();
            parser.parse(INPUT_TEST_FILE);
            List<Transition> t = parser.getTransitions();
            System.out.println("print each transition");
            for (Transition tt : t) System.out.println(tt);
            System.out.println("\nthe total transition: " + t.size());
            System.out.println("print each decoy transition");
            List<Transition> decoy_t = gen.makeDecoyTransition();
            for (Transition tt : decoy_t) System.out.println(tt);
            System.out.println("\nthe total transition: " + decoy_t.size());
            System.out.println("write decoy transition to a file");
            ExportType.FULL.write(decoy_t, OUTPUT_TEST_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
