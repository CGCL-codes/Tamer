package org.iupac.goldbook.goldify.bases;

public class TextChunk {

    public String text = "";

    public String matchId = null;

    TextChunk(String text, String matchId) {
        this.text = text;
        this.matchId = matchId;
    }

    public Boolean isMatch() {
        return matchId != null;
    }
}
