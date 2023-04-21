package view;

import data.Data;
import data.elements.Arc;
import data.elements.Element;
import data.elements.Place;
import data.elements.Transition;

/**
 * Finds elements of drawing area by coordinates.
 * 
 * @author <a href="mailto:ydzyuban@gmail.com">Dzyuban Yuriy</a>
 * 
 */
public class ElementFinder {

    public static Element findElement(final int pixX, final int pixY, final Data data) {
        int transitionWidth = FrameSettings.transitionWidth();
        int elementHeight = FrameSettings.elementHeight();
        int arcFindingDistance = FrameSettings.arcFindingDistance();
        Element res;
        for (int i = 0; i < data.getElements().size(); i++) {
            if (data.getElements().get(i).getType() == "P") {
                Place current = (Place) data.getElements().get(i);
                int y = current.getY();
                int x = current.getX();
                if (Math.sqrt((x - pixX) * (x - pixX) + (y - pixY) * (y - pixY)) < elementHeight / 2) {
                    res = data.getElements().get(i);
                    return res;
                }
            } else if (data.getElements().get(i).getType() == "T") {
                Transition current = (Transition) data.getElements().get(i);
                int y = current.getY();
                int x = current.getX();
                if ((Math.abs(x - pixX) < (transitionWidth / 2)) && (Math.abs(y - pixY) < (elementHeight / 2))) {
                    res = data.getElements().get(i);
                    return res;
                }
            }
        }
        for (int i = 0; i < data.getElements().size(); i++) {
            Element el = data.getElements().get(i);
            for (int j = 0; j < el.getOutputArcs().size(); j++) {
                Arc arc = el.getOutputArcs().get(j);
                for (int k = 0; k < arc.getXsequence().size() - 1; k++) {
                    int x0 = arc.getXsequence().get(k);
                    int x1 = arc.getXsequence().get(k + 1);
                    int y0 = arc.getYsequence().get(k);
                    int y1 = arc.getYsequence().get(k + 1);
                    int maxx = (x1 > x0) ? (x1) : (x0);
                    int minx = (x1 < x0) ? (x1) : (x0);
                    int maxy = (y1 > y0) ? (y1) : (y0);
                    int miny = (y1 < y0) ? (y1) : (y0);
                    if ((pixX < maxx + arcFindingDistance) && (pixX > minx - arcFindingDistance) && (pixY < maxy + arcFindingDistance) && (pixY > miny - arcFindingDistance)) {
                        int d = (int) (Math.abs((y0 - y1) * pixX + (x1 - x0) * pixY + (x0 * y1 - x1 * y0)) / Math.sqrt(Math.pow((double) x1 - x0, 2) + Math.pow((double) y1 - y0, 2)));
                        if (d < arcFindingDistance) {
                            return arc;
                        }
                    }
                }
            }
        }
        return null;
    }
}
