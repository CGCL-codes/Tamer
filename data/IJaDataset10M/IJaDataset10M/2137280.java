package yapgen.base.knowledge.event;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import yapgen.base.EntityContext;
import yapgen.base.ObjectSubstance;
import yapgen.base.Word;
import yapgen.base.WordValueDoubleTypeDelta;
import yapgen.base.knowledge.KnowledgeWords;
import yapgen.base.knowledge.fact.Fact;
import yapgen.base.knowledge.character.Character;
import yapgen.base.util.AttributeMap;
import yapgen.base.util.XmlUtils;

/**
 *
 * @author riccardo
 */
public class Event extends ObjectSubstance implements Serializable {

    public Event(Word factWord, Word subjectWord) {
        this(factWord, subjectWord, null);
    }

    public Event(Word factWord, Word subjectWord, Word complementWord) {
        this.setFactWord(factWord);
        this.setSubjectWord(subjectWord);
        this.setComplementWord(complementWord);
    }

    public final void setDeltaMap(Map<Word, List<WordValueDoubleTypeDelta>> value) {
        this.attributes.put(KnowledgeWords.EventTransformationDeltaMapWord, value);
    }

    public Map<Word, List<WordValueDoubleTypeDelta>> getDeltaMap() {
        return (Map<Word, List<WordValueDoubleTypeDelta>>) this.attributes.get(KnowledgeWords.EventTransformationDeltaMapWord);
    }

    public final void setLikeliness(double value) {
        this.attributes.put(KnowledgeWords.EventLikelinessWord, value);
    }

    public Double getLikeliness() {
        return (Double) this.attributes.get(KnowledgeWords.EventLikelinessWord);
    }

    public final void setComplementWord(Word complementWord) {
        this.attributes.put(KnowledgeWords.EventComplementWord, complementWord);
    }

    public final void setFactWord(Word factWord) {
        this.attributes.put(KnowledgeWords.EventFactWord, factWord);
    }

    public final void setSubjectWord(Word subjectWord) {
        this.attributes.put(KnowledgeWords.EventSubjectWord, subjectWord);
    }

    public Word getComplementWord() {
        return (Word) this.attributes.get(KnowledgeWords.EventComplementWord);
    }

    public Word getFactWord() {
        return (Word) this.attributes.get(KnowledgeWords.EventFactWord);
    }

    public void applyFact(EntityContext ec, boolean withInfluence) {
        Fact fact = (Fact) ec.get(this.getFactWord());
        int factComplementNumber = fact.getComplementNumber();
        if (factComplementNumber == 0) {
            fact.apply((Character) ec.get(this.getSubjectWord()));
        } else if (factComplementNumber == 1) {
            fact.apply((Character) ec.get(this.getSubjectWord()), (Character) ec.get(this.getComplementWord()), withInfluence);
        }
    }

    public Word getSubjectWord() {
        return (Word) this.attributes.get(KnowledgeWords.EventSubjectWord);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (this.getFactWord() != other.getFactWord() && (this.getFactWord() == null || !this.getFactWord().equals(other.getFactWord()))) {
            return false;
        }
        if (this.getSubjectWord() != other.getSubjectWord() && (this.getSubjectWord() == null || !this.getSubjectWord().equals(other.getSubjectWord()))) {
            return false;
        }
        if (this.getComplementWord() != other.getComplementWord() && (this.getComplementWord() == null || !this.getComplementWord().equals(other.getComplementWord()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getSubjectWord()).append(" ").append(this.getFactWord());
        if (this.getComplementWord() != null) {
            builder.append(" ").append(this.getComplementWord());
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.getFactWord() != null ? this.getFactWord().hashCode() : 0);
        hash = 11 * hash + (this.getSubjectWord() != null ? this.getSubjectWord().hashCode() : 0);
        hash = 11 * hash + (this.getComplementWord() != null ? this.getComplementWord().hashCode() : 0);
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Event copy = (Event) super.clone();
        copy.attributes = new TreeMap<Word, Object>();
        copy.attributes.put(KnowledgeWords.EventFactWord, this.get(KnowledgeWords.EventFactWord));
        copy.attributes.put(KnowledgeWords.EventSubjectWord, this.get(KnowledgeWords.EventSubjectWord));
        copy.attributes.put(KnowledgeWords.EventComplementWord, this.get(KnowledgeWords.EventComplementWord));
        return copy;
    }

    @Override
    public String toXml() {
        StringBuilder contentBuilder = new StringBuilder();
        AttributeMap contentAttributes = new AttributeMap();
        contentBuilder.append(XmlUtils.tagString("subject", contentAttributes, this.getSubjectWord().getText()));
        if (this.getComplementWord() != null) contentBuilder.append(XmlUtils.tagString("complement", contentAttributes, this.getComplementWord().getText()));
        return contentBuilder.toString();
    }
}
