package it.polito.semreview.examples;

import it.polito.semreview.classifiers.ClassificationException;
import it.polito.semreview.classifiers.KnowledgeBase;
import it.polito.semreview.classifiers.NaiveBayes;
import it.polito.semreview.utils.Sweeper;
import java.util.Hashtable;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Sweeper sw = new Sweeper();
        System.out.println(sw.run("my studying names is giuseppe!-/\"[] \" I am a phd student"));
        String a1 = "ciao io sono giuseppe vado in universita";
        String a2 = "ciao io vado in università";
        String b1 = "gioco a calcetto nella squadra del salentopoli";
        String b2 = "la mia squadra di calcetto è il salentopoli";
        String sample = "gioco a calcetto";
        KnowledgeBase kb = new KnowledgeBase();
        kb.train("interessante", b1);
        kb.train("interessante", b2);
        Hashtable<String, Double> score;
        try {
            score = NaiveBayes.estimate(sample, kb);
            System.out.println("interessante:" + score.get("interessante"));
        } catch (ClassificationException e) {
            e.printStackTrace();
        }
    }
}
