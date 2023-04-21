package org.jcvi.align;

public class DefaultScoringMatrixElement implements ScoringMatrixElement {

    private final Path dir;

    private final int score;

    /**
     * @param dir
     * @param score
     */
    public DefaultScoringMatrixElement(Path dir, int score) {
        this.dir = dir;
        this.score = score;
    }

    @Override
    public Path getDirection() {
        return dir;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dir == null) ? 0 : dir.hashCode());
        result = prime * result + score;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof DefaultScoringMatrixElement)) return false;
        DefaultScoringMatrixElement other = (DefaultScoringMatrixElement) obj;
        if (dir == null) {
            if (other.dir != null) return false;
        } else if (!dir.equals(other.dir)) return false;
        if (score != other.score) return false;
        return true;
    }

    @Override
    public String toString() {
        return "DefaultScoringMatrixElement [dir=" + dir + ", score=" + score + "]";
    }
}
