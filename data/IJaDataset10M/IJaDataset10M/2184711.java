package it.polimi.miaria.epo.core;

import it.gonzo.assessor.WordNetAssessor;
import it.gonzo.similarity.utils.SimilarityConstants;
import it.polimi.miaria.epo.entities.TerminiBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import rita.wordnet.RiWordnet;

public class Test {

    static List<String> allwords = new ArrayList<String>();

    static List<Coppia> wordsWeigth = new ArrayList<Coppia>();

    /**
	 * @param args
	 */
    public ArrayList<TerminiBean> main(String s) {
        RiWordnet wordnet = new RiWordnet();
        String parola = s;
        String[] pos;
        int i;
        allwords = new ArrayList<String>();
        if (wordnet.exists(parola)) {
            pos = wordnet.getPos(parola);
            for (i = 0; i < pos.length; i++) {
                allwords.addAll(Arrays.asList(wordnet.getAllSynsets(parola, pos[i])));
                allwords.addAll(Arrays.asList(wordnet.getAllSynonyms(parola, pos[i])));
                allwords.addAll(Arrays.asList(wordnet.getAllHypernyms(parola, pos[i])));
                allwords.addAll(Arrays.asList(wordnet.getAllHyponyms(parola, pos[i])));
                allwords.addAll(Arrays.asList(wordnet.getAllSynsets(parola, pos[i])));
                if (pos[i] == "a") {
                    allwords.addAll(Arrays.asList(wordnet.getAllSimilar(parola, pos[i])));
                    allwords.addAll(Arrays.asList(wordnet.getAllAlsoSees(parola, pos[i])));
                    allwords.addAll(Arrays.asList(wordnet.getAllNominalizations(parola, pos[i])));
                }
                if (pos[i] == "n") {
                    allwords.addAll(Arrays.asList(wordnet.getAllMeronyms(parola, pos[i])));
                    allwords.addAll(Arrays.asList(wordnet.getAllAlsoSees(parola, pos[i])));
                    allwords.addAll(Arrays.asList(wordnet.getAllHolonyms(parola, pos[i])));
                    allwords.addAll(Arrays.asList(wordnet.getAllNominalizations(parola, pos[i])));
                }
                if (pos[i] == "r") {
                    allwords.addAll(Arrays.asList(wordnet.getAllDerivedTerms(parola, pos[i])));
                }
                if (pos[i] == "v") {
                    allwords.addAll(Arrays.asList(wordnet.getAllNominalizations(parola, pos[i])));
                }
            }
            return pesi(allwords, parola);
        }
        return null;
    }

    public static void stampa(List<String> s, String title) {
        int i = 0;
        System.out.println("\n------" + title + "------");
        if (s.size() > 0) {
            for (i = 0; i < s.size(); i++) {
                System.out.println(s.get(i));
            }
        } else {
            System.out.println("Nessun termine trovato.");
        }
    }

    public ArrayList<TerminiBean> pesi(List<String> s, String p) {
        int i;
        WordNetAssessor wn = new WordNetAssessor();
        for (i = 0; i < s.size(); i++) {
            double d = wn.getWordNetNounSimilarityByIC(p, s.get(i), SimilarityConstants.FAITH_MEASURE, SimilarityConstants.EXTENDED_IC);
            if (d >= 0.7) wordsWeigth.add(new Coppia(s.get(i), d));
        }
        Collections.sort(wordsWeigth, Collections.reverseOrder());
        Set<Coppia> c = new LinkedHashSet<Coppia>(wordsWeigth);
        ArrayList<TerminiBean> temp = new ArrayList<TerminiBean>();
        TerminiBean t2 = new TerminiBean();
        t2.setTermine(p);
        t2.setPeso(1);
        temp.add(t2);
        for (Coppia coppia : c) {
            TerminiBean t1 = new TerminiBean();
            t1.setTermine(coppia.getText());
            t1.setPeso(coppia.getPes());
            temp.add(t1);
        }
        return temp;
    }
}
