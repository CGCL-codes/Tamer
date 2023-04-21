package org.musicbrainz.wsxml.element;

/**
 * Represents a search result.
 * 
 * @author Patrick Ruhkopf
 */
public abstract class SearchResult {

    /**
	 * The score indicates how good this result matches the search
	 * parameters. The higher the value, the better the match.
	 * The score is a number between 0 and 100.
	 */
    private Integer score;

    /**
	 * @return the score
	 */
    public Integer getScore() {
        return score;
    }

    /**
	 * @param score the score to set
	 */
    public void setScore(Integer score) {
        this.score = score;
    }
}
