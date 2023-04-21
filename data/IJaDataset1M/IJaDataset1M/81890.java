package relex.anaphora;

import java.util.ArrayList;
import relex.feature.FeatureNode;
import relex.feature.FeatureNodeCallback;
import relex.feature.FeatureForeach;
import relex.ParsedSentence;
import relex.PhraseTree;

/**
 * Return a list of all the pronouns in a parse.
 *
 * Copyright (C) 2008 Linas Vepstas <linas@linas.org>
 */
public class FindPronouns {

    private static class GetPronouns implements FeatureNodeCallback {

        public ArrayList<PhraseTree> pronouns;

        GetPronouns() {
            pronouns = new ArrayList<PhraseTree>();
        }

        public Boolean FNCallback(FeatureNode fn) {
            FeatureNode pr = fn.get("PRONOUN-FLAG");
            if (pr != null) {
                pr = fn.get("nameSource");
                pronouns.add(new PhraseTree(pr));
            }
            return false;
        }
    }

    public static ArrayList<PhraseTree> findPronouns(ParsedSentence parse) {
        GetPronouns gp = new GetPronouns();
        FeatureForeach.foreach(parse.getLeft(), gp);
        return gp.pronouns;
    }
}
