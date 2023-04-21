package org.megadix.jfcm.samples;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;
import org.megadix.jfcm.CognitiveMap;
import org.megadix.jfcm.Concept;
import org.megadix.jfcm.utils.FcmIO;
import org.megadix.jfcm.utils.FcmRunner;
import org.megadix.jfcm.utils.SimpleFcmRunner;

/**
 * Modified example inspired by:
 * http://www.ochoadeaspuru.com/fuzcogmap/wildlifepark.php
 * by Guillermo Ochoa de Aspuru
 * 
 * Code by Dimitri De Franciscis - www.megadix.it
 *
 */
public class WildlifePark {

    FcmRunner runner;

    CognitiveMap map;

    public WildlifePark() throws ParseException {
        map = FcmIO.loadXml(getClass().getResourceAsStream("WildlifePark.fcm.xml")).get(0);
        runner = new SimpleFcmRunner(map, 0.1, 1000);
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.1
     */
    public void test_scenario_1() throws Exception {
        resetMap();
        map.getConcepts().get("Rain").setOutput(0.1);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.1);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 1", runner.converge());
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.1 and 0.5
     */
    public void test_scenario_2() throws Exception {
        resetMap();
        map.getConcepts().get("Rain").setOutput(0.1);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.5);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 2", runner.converge());
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.1 and 0.9
     */
    public void test_scenario_3() throws Exception {
        resetMap();
        map.getConcepts().get("Rain").setOutput(0.1);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.9);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 3", runner.converge());
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.5 and 0.1
     */
    public void test_scenario_4() throws Exception {
        resetMap();
        map.getConcepts().get("Rain").setOutput(0.5);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.1);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 4", runner.converge());
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.5 and 0.5
     */
    public void test_scenario_5() throws Exception {
        resetMap();
        map.getConcepts().get("Rain").setOutput(0.5);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.5);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 5", runner.converge());
    }

    /**
     * Keep "Rain" and "Rangers" with fixed outputs of 0.5 and 0.9
     */
    public void test_scenario_6() throws Exception {
        resetMap();
        map.getConcepts().get("Rain").setOutput(0.5);
        map.getConcepts().get("Rain").setFixedOutput(true);
        map.getConcepts().get("Rangers").setOutput(0.9);
        map.getConcepts().get("Rangers").setFixedOutput(true);
        map.getConcepts().get("Grassland").setOutput(0.0);
        map.getConcepts().get("Poachers").setOutput(0.0);
        map.getConcepts().get("Erbivores").setOutput(0.0);
        map.getConcepts().get("Predators").setOutput(0.0);
        showResults("Scenario 6", runner.converge());
    }

    public void run() {
        try {
            System.out.print("Scenario\tConverged");
            for (Concept c : map.getConcepts().values()) {
                System.out.print("\t" + c.getName());
            }
            System.out.println();
            test_scenario_1();
            test_scenario_2();
            test_scenario_3();
            test_scenario_4();
            test_scenario_5();
            test_scenario_6();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void resetMap() {
        Iterator<Concept> iter = map.getConceptsIterator();
        while (iter.hasNext()) {
            Concept concept = iter.next();
            concept.setOutput(0.0);
            concept.setPrevOutput(null);
            concept.setFixedOutput(false);
        }
    }

    void showResults(String scenario, boolean converged) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(8);
        System.out.print(scenario + "\t" + converged);
        for (Concept c : map.getConcepts().values()) {
            System.out.print("\t");
            System.out.print(c.getOutput() != null ? nf.format(c.getOutput()) : "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        WildlifePark example;
        try {
            example = new WildlifePark();
            example.run();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
