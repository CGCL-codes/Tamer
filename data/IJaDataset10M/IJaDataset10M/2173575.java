package wn;

import java.util.ArrayList;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.relationship.Relationship;

public class SemanticDistance {

    public static void main(String[] args) throws JWNLException {
        WordNetHelper.initialize("resources/file_properties.xml");
        IndexWord start = WordNetHelper.getWord(POS.ADJECTIVE, "nice");
        IndexWord end = WordNetHelper.getWord(POS.ADJECTIVE, "good");
        if (start != null && end != null) {
            findRelationshipsDemo(start, end, PointerType.SIMILAR_TO);
        } else {
            System.out.println("No relationship");
        }
    }

    public static void findRelationshipsDemo(IndexWord start, IndexWord end, PointerType type) throws JWNLException {
        System.out.println("\n\nTrying to find a relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\".");
        System.out.println("Looking for relationship of type " + type.getLabel() + ".");
        Relationship rel = WordNetHelper.getRelationship(start, end, type);
        if (rel != null) {
            System.out.println("The depth of this relationship is: " + rel.getDepth());
            System.out.println("Here is how the words are related: ");
            ArrayList a = WordNetHelper.getRelationshipSenses(rel);
            System.out.println("Start: " + start.getLemma());
            for (int i = 0; i < a.size(); i++) {
                Synset s = (Synset) a.get(i);
                Word[] words = s.getWords();
                System.out.print(i + ": ");
                for (int j = 0; j < words.length; j++) {
                    System.out.print(words[j].getLemma());
                    if (j != words.length - 1) System.out.print(", ");
                }
                System.out.println();
            }
        } else {
            System.out.println("I could not find a relationship between these words!");
        }
    }
}
