package net.narusas.si.auction.pdf.attested;

import java.util.Comparator;

public class TextPositionComparator implements Comparator {

    /**
	 * {@inheritDoc}
	 */
    public int compare(Object o1, Object o2) {
        int retval = 0;
        TextPosition pos1 = (TextPosition) o1;
        TextPosition pos2 = (TextPosition) o2;
        float x1 = 0;
        float x2 = 0;
        float pos1YBottom = 0;
        float pos2YBottom = 0;
        x1 = pos1.getX();
        x2 = pos2.getX();
        pos1YBottom = pos1.getY();
        pos2YBottom = pos2.getY();
        if (pos1.getPage() != pos2.getPage()) {
            return pos1.getPage() > pos2.getPage() ? 1 : pos1.getPage() > pos2.getPage() ? -1 : 0;
        }
        float pos1YTop = pos1YBottom - pos1.getHeight();
        float pos2YTop = pos2YBottom - pos2.getHeight();
        float yDifference = Math.abs(pos1YBottom - pos2YBottom);
        if (yDifference < .1 || (pos2YBottom >= pos1YTop && pos2YBottom <= pos1YBottom) || (pos1YBottom >= pos2YTop && pos1YBottom <= pos2YBottom)) {
            if (x1 < x2) {
                retval = -1;
            } else if (x1 > x2) {
                retval = 1;
            } else {
                retval = 0;
            }
        } else if (pos1YBottom < pos2YBottom) {
            retval = -1;
        } else {
            return 1;
        }
        return retval;
    }
}
