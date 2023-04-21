package de.tobiasmaasland.voctrain.client.tag;

import java.util.Collection;
import java.util.Vector;
import de.tobiasmaasland.voctrain.business.data.Vocabulary;
import fi.mmm.yhteinen.swing.core.YModel;

public class TagModel extends YModel {

    private Collection<Vocabulary> vocabularies;

    public TagModel() {
        vocabularies = new Vector<Vocabulary>();
        vocabularies.add(new Vocabulary("Spanisch", "Deutsch"));
        vocabularies.add(new Vocabulary("Englisch", "Deutsch"));
    }

    public Collection<Vocabulary> getVocabularies() {
        return vocabularies;
    }

    public void setVocabularies(Collection<Vocabulary> inVocabularies) {
        this.vocabularies = inVocabularies;
    }
}
