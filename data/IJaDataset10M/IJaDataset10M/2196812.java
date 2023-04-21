package se.kristofer.karlsson.util.bunchpackager.backend.bunch;

public class SubImage {

    private String parent;

    private int xOffset;

    private int yOffset;

    public SubImage(String parent, int offset, int offset2) {
        super();
        this.parent = parent;
        xOffset = offset;
        yOffset = offset2;
    }

    public String getParent() {
        return parent;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((parent == null) ? 0 : parent.hashCode());
        result = PRIME * result + xOffset;
        result = PRIME * result + yOffset;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SubImage other = (SubImage) obj;
        if (parent == null) {
            if (other.parent != null) return false;
        } else if (!parent.equals(other.parent)) return false;
        if (xOffset != other.xOffset) return false;
        if (yOffset != other.yOffset) return false;
        return true;
    }

    public String toString() {
        return parent + "(" + xOffset + "," + yOffset + ")";
    }
}
