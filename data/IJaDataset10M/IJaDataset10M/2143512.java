package glowaxes.plots;

import glowaxes.data.Series;
import glowaxes.data.Value;
import glowaxes.glyphs.IGlyph;
import glowaxes.glyphs.TextLabel;
import glowaxes.labeling.Instance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The Class AGeneralPlotter.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 * @version $Id: AGeneralPlotter.java 459 2012-02-27 22:00:34Z nejoom $
 */
public abstract class AGeneralPlotter implements IPlotter {

    /** The logger. */
    private static Logger logger = Logger.getLogger(AGeneralPlotter.class.getName());

    List<Object> myXArrayList;

    List<Object> myYArrayList;

    public double getAxisCategoricalOffset(Object object, int series) {
        return getInitialAxisCategoricalOffset();
    }

    public int getAxisCategoricalUnits() {
        int size = 0;
        int numSeries = getData().getGroup().getSeriesList().size();
        for (int i = 0; i < numSeries; i++) {
            int tmpSize = getData().getGroup().getSeries(i).getSize();
            if (tmpSize > size) size = tmpSize;
        }
        return size - 2;
    }

    public double getAxisNumericalRange(double min, double max) {
        return 0;
    }

    public double getInitialAxisCategoricalOffset() {
        return 0;
    }

    public double getXAxisCategoryIndex(Object object, int series) {
        return myXArrayList.indexOf(object);
    }

    public double getYAxisCategoryIndex(Object object, int series) {
        return myYArrayList.indexOf(object);
    }

    @SuppressWarnings("unchecked")
    public void processLabels() {
        Instance instance = getData().getParentArea().getInstance();
        for (int j = 0; j < getData().getGroup().getSize(); j++) {
            Series aSeries = getData().getGroup().getSeries(j);
            for (int i = 0; i < aSeries.getSize(); i++) {
                Value myValue = aSeries.getValue(i);
                String label = null;
                if (myValue.getLabel() != null) {
                    label = myValue.getLabel().getTextTrim();
                }
                if (label == null) {
                    continue;
                }
                label = replaceLabel(label, myValue);
                double x;
                double y;
                if (getData().getParentArea().getType().toLowerCase().indexOf("stackedbar") != -1) {
                    Value myNewValue = new Value();
                    myNewValue.setX((myValue.getA1() + myValue.getA2()) / 2);
                    x = getData().getParentArea().getXAxis().getSVGOffset(myNewValue);
                    y = getData().getParentArea().getYAxis().getSVGOffset(myValue);
                } else if (getData().getParentArea().getType().toLowerCase().indexOf("stackedcolumn") != -1 || getData().getParentArea().getType().toLowerCase().indexOf("stackedarea") != -1 || getData().getParentArea().getType().toLowerCase().indexOf("stackedsplinearea") != -1) {
                    Value myNewValue = new Value();
                    myNewValue.setY((myValue.getA1() + myValue.getA2()) / 2);
                    y = getData().getParentArea().getYAxis().getSVGOffset(myNewValue);
                    x = getData().getParentArea().getXAxis().getSVGOffset(myValue);
                } else if (getData().getParentArea().getType().toLowerCase().indexOf("column") != -1) {
                    Value myNewValue = new Value();
                    myNewValue.setY(myValue.getY() * 2 / 3);
                    y = getData().getParentArea().getYAxis().getSVGOffset(myNewValue);
                    x = getData().getParentArea().getXAxis().getSVGOffset(myValue);
                } else if (getData().getParentArea().getType().toLowerCase().indexOf("pie") != -1) {
                    if (myValue.getB1() == -1) continue;
                    x = myValue.getB1();
                    y = myValue.getB2();
                } else {
                    x = getData().getParentArea().getXAxis().getSVGOffset(myValue);
                    y = getData().getParentArea().getYAxis().getSVGOffset(myValue);
                }
                if (getData().getLabels().getType().equals("all")) {
                    logger.info("Generating all labels");
                    instance.addGlyph(new TextLabel(label, myValue.getLabelStyle(), myValue.getLabelBackgroundStyle(), null, x, y));
                } else if (getData().getLabels().getType().equals("outliners")) {
                    boolean outliner = false;
                    if (myValue.getX() == myValue.getParentSeries().getXMax()) {
                        outliner = true;
                        logger.info("Generating outliner getXMax");
                    } else if (myValue.getX() == myValue.getParentSeries().getXMin()) {
                        outliner = true;
                        logger.info("Generating outliner getXMin");
                    } else if (myValue.getY() == myValue.getParentSeries().getYMax()) {
                        outliner = true;
                        logger.info("Generating outliner getYMax");
                    } else if (myValue.getY() == myValue.getParentSeries().getYMin()) {
                        outliner = true;
                        logger.info("Generating outliner getYMin");
                    } else if (myValue.getA1() == myValue.getParentSeries().getA1Max() && myValue.getParentSeries().getA1Max() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getA1Max");
                    } else if (myValue.getA1() == myValue.getParentSeries().getA1Min() && myValue.getParentSeries().getA1Min() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getA1Min");
                    } else if (myValue.getA2() == myValue.getParentSeries().getA2Max() && myValue.getParentSeries().getA2Max() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getA2Max");
                    } else if (myValue.getA2() == myValue.getParentSeries().getA2Min() && myValue.getParentSeries().getA2Min() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getA2Min");
                    } else if (myValue.getB1() == myValue.getParentSeries().getB1Max() && myValue.getParentSeries().getB1Max() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getB1Max");
                    } else if (myValue.getB1() == myValue.getParentSeries().getB1Min() && myValue.getParentSeries().getB1Min() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getB1Min");
                    } else if (myValue.getB2() == myValue.getParentSeries().getB2Max() && myValue.getParentSeries().getB2Max() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getB2Max");
                    } else if (myValue.getB2() == myValue.getParentSeries().getB2Min() && myValue.getParentSeries().getB2Min() != 0) {
                        outliner = true;
                        logger.info("Generating outliner getB2Min");
                    }
                    if (outliner) {
                        instance.addGlyph(new TextLabel(label, myValue.getLabelStyle(), myValue.getLabelBackgroundStyle(), null, x, y));
                    }
                }
            }
        }
        ArrayList<IGlyph> glyphs = getGlyphs();
        if (glyphs != null) for (int i = 0; i < glyphs.size(); i++) {
            instance.addGlyph(glyphs.get(i), 10);
        }
    }

    public String replaceLabel(String label, Value myValue) {
        return getData().getDataFormatter().replaceLabel(label, myValue, getData());
    }

    public void setAxisCategories() {
        LinkedHashSet<Object> myXSet = new LinkedHashSet<Object>();
        LinkedHashSet<Object> myYSet = new LinkedHashSet<Object>();
        myXArrayList = new ArrayList<Object>();
        myYArrayList = new ArrayList<Object>();
        int numSeries = getData().getGroup().getSeriesList().size();
        for (int i = 0; i < numSeries; i++) {
            ArrayList<Value> currentList = getData().getGroup().getSeries(i).getArrayList();
            for (int j = 0; j < currentList.size(); j++) {
                myYSet.add(currentList.get(j).getYObject());
                myXSet.add(currentList.get(j).getXObject());
            }
        }
        for (Iterator<Object> iter = myXSet.iterator(); iter.hasNext(); ) {
            myXArrayList.add(iter.next());
        }
        for (Iterator<Object> iter = myYSet.iterator(); iter.hasNext(); ) {
            myYArrayList.add(iter.next());
        }
    }
}
