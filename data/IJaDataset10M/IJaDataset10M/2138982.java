package com.screenrunner.data;

class SearchResult implements Comparable<SearchResult> {

    private String _key;

    private int _score;

    public SearchResult(String key, int score) {
        _key = key;
        _score = score;
    }

    public String getKey() {
        return _key;
    }

    public void incrementScore(int value) {
        _score += value;
    }

    @Override
    public int compareTo(SearchResult o) {
        return _score - o._score;
    }
}
