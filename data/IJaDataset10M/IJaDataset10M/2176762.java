package org.girtools.jiri;

public final class SearchResult {

    public SearchResult() {
    }

    public void setDocId(String docId) {
        _docId = docId;
    }

    public String getDocId() {
        return _docId;
    }

    public void setScore(int score) {
        _score = score;
    }

    public int getScore() {
        return _score;
    }

    private String _docId;

    private int _score;
}
