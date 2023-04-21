package org.geometerplus.zlibrary.text.view;

public abstract class ZLTextPosition implements Comparable<ZLTextPosition> {

    public abstract int getParagraphIndex();

    public abstract int getElementIndex();

    public abstract int getCharIndex();

    public boolean samePositionAs(ZLTextPosition position) {
        return (getParagraphIndex() == position.getParagraphIndex()) && (getElementIndex() == position.getElementIndex()) && (getCharIndex() == position.getCharIndex());
    }

    public int compareTo(ZLTextPosition position) {
        final int pDiff = getParagraphIndex() - position.getParagraphIndex();
        if (pDiff != 0) {
            return pDiff;
        }
        final int eDiff = getElementIndex() - position.getElementIndex();
        if (eDiff != 0) {
            return eDiff;
        }
        return getCharIndex() - position.getCharIndex();
    }

    @Override
    public int hashCode() {
        return (getParagraphIndex() << 16) + (getElementIndex() << 8) + getCharIndex();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ZLTextPosition)) {
            return false;
        }
        final ZLTextPosition position = (ZLTextPosition) object;
        return getParagraphIndex() == position.getParagraphIndex() && getElementIndex() == position.getElementIndex() && getCharIndex() == position.getCharIndex();
    }
}
