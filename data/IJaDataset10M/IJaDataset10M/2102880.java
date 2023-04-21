package org.bresearch.websec.parse.model;

public class NLPWord {

    private String term = "";

    private String nlpShortDesc = "";

    private String nlpLongDesc = "";

    private final String termGroup;

    private final NLPCommonTags tags;

    public NLPWord(final NLPCommonTags tags, final String termGroup) {
        this.termGroup = termGroup;
        this.tags = tags;
    }

    public String toString() {
        return "{NLPWord: term=" + this.term + " , nlp=" + this.nlpShortDesc + "}";
    }

    public String toReport() {
        return "{NLPWord: term=" + this.term + " , nlp=" + this.nlpShortDesc + " nlplong=" + this.nlpLongDesc + "}";
    }

    public void parse() {
        final String[] group = this.termGroup.split("/");
        if (group.length == 2) {
            this.term = group[0];
            this.nlpShortDesc = group[1];
            final NLPWordTag wordTag = tags.getTag(this.nlpShortDesc);
            if (wordTag != null) {
                this.nlpLongDesc = wordTag.toString();
            }
        }
    }

    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @return the nlpShortDesc
     */
    public String getNlpShortDesc() {
        return nlpShortDesc;
    }

    /**
     * @return the nlpLongDesc
     */
    public String getNlpLongDesc() {
        return nlpLongDesc;
    }
}
