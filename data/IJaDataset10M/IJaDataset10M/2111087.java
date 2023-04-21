package tml.conceptmap;

import org.apache.log4j.Logger;

/**
 * @author Rodrigo
 *
 */
public class ConceptStopWordsComparator extends ConceptComparator {

    private static Logger logger = Logger.getLogger(ConceptStopWordsComparator.class);

    private boolean useStopWords;

    private String[] stopwords;

    public String[] getStopwords() {
        return stopwords;
    }

    public void setStopwords(String[] stopwords) {
        this.stopwords = stopwords;
    }

    public boolean isUseStopWords() {
        return useStopWords;
    }

    public void setUseStopWords(boolean useStopWords) {
        this.useStopWords = useStopWords;
    }

    /**
	 * @param useWordnet
	 * @param useStems
	 * @param compounds
	 */
    public ConceptStopWordsComparator(boolean useWordnet, boolean useStems, boolean compounds, boolean stopwords) {
        super(useWordnet, useStems, compounds);
        this.setUseStopWords(stopwords);
        logger.debug("Usando stop words:" + stopwords);
    }

    /**
	 * 
	 */
    public ConceptStopWordsComparator() {
    }

    @Override
    public int compare(Concept o1, Concept o2) {
        Concept oss1 = new Concept(stopwords(o1.getTerm()));
        Concept oss2 = new Concept(stopwords(o2.getTerm()));
        int resultado = super.compare(oss1, oss2);
        return resultado;
    }

    private String stopwords(String phrase) {
        String[] cortes = phrase.split(" ");
        for (int i = 0; i < cortes.length; i++) {
            for (String stopword : stopwords) {
                if (cortes[i].equals(stopword)) {
                    cortes[i] = "";
                }
            }
        }
        String sumacortes = "";
        for (int i = 0; i < cortes.length; i++) {
            if (cortes[i] != "") {
                sumacortes += cortes[i] + " ";
            }
        }
        if (sumacortes.isEmpty()) {
            sumacortes = phrase;
        }
        return sumacortes.trim();
    }
}
