package net.sf.ideoreport.engines.chartengine.data.xy;

import java.util.ArrayList;
import java.util.List;
import net.sf.ideoreport.engines.chartengine.data.DataItem;

/**
 * TODO [jroyer] Needs a description for this class.
 *
 * @author jroyer (Last modification by $Author$ on $Date$)
 * @version $Revision$
 */
public class AnnotationDataItems {

    private String label;

    private String category;

    private Number x;

    private Number y;

    public AnnotationDataItems(String pLabel, String pCategory, Number pValue) {
        this.label = pLabel;
        this.category = pCategory;
        this.y = pValue;
    }

    public AnnotationDataItems(String pLabel, Number pX, Number pY) {
        this.label = pLabel;
        this.x = pX;
        this.y = pY;
    }

    public static List getTestXYAnnotationDataItems(int pNbItems, boolean allowNegative) {
        List vRetour = new ArrayList();
        double vStartX = 2005 - pNbItems;
        for (int i = 0; i < pNbItems; i++) {
            int factor = DataItem.getRandomFactor(allowNegative);
            vRetour.add(new AnnotationDataItems("Annotation " + i, new Double(vStartX + i), new Double(factor * Math.random() * 10000)));
        }
        return vRetour;
    }

    public static List getTestCategoryAnnotationDataItems(int pNbItems, boolean allowNegative) {
        List vRetour = new ArrayList();
        int startYear = 2005 - (pNbItems / 2);
        for (int i = 0; i < pNbItems; i++) {
            int factor = DataItem.getRandomFactor(allowNegative);
            String year = String.valueOf(startYear + i);
            vRetour.add(new AnnotationDataItems("Annotation for " + year, year, new Double(factor * Math.random() * 10000)));
        }
        return vRetour;
    }

    /**
     * Getter for the category.
     * @return Returns the category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Setter for the category.
     * @param category The category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter for the label.
     * @return Returns the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Setter for the label.
     * @param label The label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for the x.
     * @return Returns the x
     */
    public Number getX() {
        return this.x;
    }

    /**
     * Setter for the x.
     * @param x The x to set
     */
    public void setX(Number x) {
        this.x = x;
    }

    /**
     * Getter for the y.
     * @return Returns the y
     */
    public Number getY() {
        return this.y;
    }

    /**
     * Setter for the y.
     * @param y The y to set
     */
    public void setY(Number y) {
        this.y = y;
    }
}
