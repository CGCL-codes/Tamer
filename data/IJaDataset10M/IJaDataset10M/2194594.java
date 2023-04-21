package reconcile.featureExtractor;

import reconcile.data.Annotation;
import reconcile.data.AnnotationSet;
import reconcile.data.Document;
import reconcile.features.FeatureUtils;
import reconcile.general.Constants;
import reconcile.general.SyntaxUtils;

public class CEExtractorPromedDuncan extends CEExtractor {

    private final String[] GOLD_VOCAB = {};

    private AnnotationSet addInGoldVocab(AnnotationSet current, Document doc) {
        AnnotationSet latest = current;
        String doc_text = doc.getText();
        int lastIndex = 0, start, end;
        boolean exact = false;
        for (String gold : GOLD_VOCAB) {
            if (doc_text.contains(gold)) {
                lastIndex = 0;
                while (lastIndex != -1) {
                    lastIndex = doc_text.indexOf(gold, lastIndex);
                    if (lastIndex != -1) {
                        start = lastIndex;
                        end = lastIndex + gold.length();
                        AnnotationSet overlap = latest.getOverlapping(start, end);
                        if (overlap == null) {
                            latest.add(start, end, "added_gold_np");
                        } else {
                            exact = false;
                            for (Annotation a : overlap) {
                                if (a.getStartOffset() == start && a.getEndOffset() == end) {
                                    exact = true;
                                }
                            }
                            if (!exact) {
                                try {
                                    latest.add(start, end, "added_gold_np_overlap");
                                } catch (Exception ex) {
                                    lastIndex += gold.length();
                                    continue;
                                }
                            }
                        }
                        lastIndex += gold.length();
                    }
                }
            }
        }
        return latest;
    }

    public boolean isNoun(Annotation a, String text) {
        if (a == null) return false;
        String type = a.getType();
        if (type.startsWith("NN") || type.startsWith("PRP")) return true;
        return false;
    }

    public boolean isNP(Annotation an, String text) {
        try {
            String type = an.getType();
            if (FeatureUtils.memberArray(type, SyntaxUtils.NPType)) return true;
        } catch (NullPointerException npe) {
            return false;
        }
        return false;
    }

    public boolean addNE(Annotation a, AnnotationSet includedCEs, AnnotationSet baseCEs, Document doc) {
        return true;
    }

    public AnnotationSet run(String annSetName, Document doc) {
        AnnotationSet parseAnns = doc.getAnnotationSet(Constants.PARSE);
        AnnotationSet bnp = getBaseCEs(annSetName, doc);
        AnnotationSet result = extractCEs(bnp, parseAnns, annSetName, doc);
        fixNumbering(result);
        AnnotationSet renumbered = new AnnotationSet(annSetName);
        for (Annotation r : result) {
            add(renumbered, r, doc.getText());
        }
        return renumbered;
    }
}
