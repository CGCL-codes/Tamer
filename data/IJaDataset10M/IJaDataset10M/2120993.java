package org.palo.api.impl;

import org.palo.api.Condition;
import org.palo.api.Cube;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ExportContext;
import com.tensegrity.palojava.ExportContextInfo;

/**
 * {@<describe>}
 * <p>
 * <code>ExportContext</code> interface implementation
 * </p>
 * {@</describe>}
 *
 * @author Axel Kiselev
 * @author ArndHouben
 * @version $Id: ExportContextImpl.java,v 1.5 2008/04/15 08:20:57 PhilippBouillon Exp $
 */
class ExportContextImpl implements ExportContext {

    private final Cube cube;

    private final ExportContextInfo contextInfo;

    ExportContextImpl(Cube cube) {
        this(cube, null);
    }

    ExportContextImpl(Cube cube, Element[][] area) {
        this.cube = cube;
        contextInfo = new ExportContextInfo();
        init(cube, null);
    }

    ExportContextInfo getInfo() {
        return contextInfo;
    }

    public void reset() {
        init(cube, null);
    }

    public Condition createCondition(String condition, double value) {
        Condition cond = ConditionImpl.getCondition(condition);
        cond.setValue(value);
        return cond;
    }

    public Condition createCondition(String condition, String value) {
        Condition cond = ConditionImpl.getCondition(condition);
        cond.setValue(value);
        return cond;
    }

    public String getConditionRepresentation() {
        return contextInfo.getConditionRepresentation();
    }

    public void setCombinedCondition(Condition firstCondition, Condition secondCondition, String operator) {
        if (isValid(operator)) {
            StringBuffer condition = new StringBuffer();
            condition.append(firstCondition.toString());
            condition.append(operator);
            condition.append(secondCondition.toString());
            contextInfo.setConditionRepresentation(condition.toString());
        }
    }

    public void setCondition(Condition condition) {
        contextInfo.setConditionRepresentation(condition.toString());
    }

    public boolean isBaseCellsOnly() {
        return contextInfo.isBaseCellsOnly();
    }

    public void setBaseCellsOnly(boolean baseCellsOnly) {
        contextInfo.setBaseCellsOnly(baseCellsOnly);
    }

    public boolean ignoreEmptyCells() {
        return contextInfo.ignoreEmptyCells();
    }

    public void setIgnoreEmptyCells(boolean ignoreEmptyCells) {
        contextInfo.setIgnoreEmptyCells(ignoreEmptyCells);
    }

    public int getBlocksize() {
        return contextInfo.getBlocksize();
    }

    public void setBlocksize(int blocksize) {
        contextInfo.setBlocksize(blocksize);
    }

    public Element[][] getCellsArea() {
        String[][] area = contextInfo.getCellsArea();
        Element[][] cells = new Element[area.length][];
        for (int i = 0; i < area.length; i++) {
            cells[i] = new Element[area[i].length];
            for (int j = 0; j < area[i].length; j++) {
                Dimension dim = cube.getDimensionAt(j);
                cells[i][j] = dim.getDefaultHierarchy().getElementByName(area[i][j]);
            }
        }
        return cells;
    }

    public void setCellsArea(Element[][] area) {
        if (area == null) setAreaToDefault(); else setArea(area);
    }

    public double getProgress() {
        return contextInfo.getProgress();
    }

    public void setProgress(double progress) {
        contextInfo.setProgress(progress);
    }

    public Element[] getExportAfter() {
        String[] ids = contextInfo.getExportAfter();
        if (ids == null) return null;
        Element[] path = new Element[ids.length];
        for (int i = 0; i < ids.length; i++) {
            Dimension dim = cube.getDimensionAt(i);
            path[i] = dim.getDefaultHierarchy().getElementById(ids[i]);
        }
        return path;
    }

    public void setExportAfter(Element[] path) {
        if (path == null) {
            contextInfo.setExportAfter(null);
            return;
        }
        String[] ids = new String[path.length];
        for (int i = 0; i < path.length; i++) ids[i] = path[i].getId();
        contextInfo.setExportAfter(ids);
    }

    private final boolean isValid(String operator) {
        return operator.equals(ExportContext.OR) || operator.equals(ExportContext.XOR) || operator.equals(ExportContext.AND);
    }

    private final void init(Cube cube, Element[][] area) {
        contextInfo.setProgress(0);
        contextInfo.setConditionRepresentation(null);
        contextInfo.setBlocksize(1000);
        contextInfo.setBaseCellsOnly(true);
        contextInfo.setIgnoreEmptyCells(true);
        contextInfo.setExportAfter(null);
        if (area == null) {
            setAreaToDefault();
        } else {
            setArea(area);
        }
    }

    private final void setAreaToDefault() {
        Dimension[] dims = cube.getDimensions();
        Element[][] area = new Element[dims.length][];
        for (int i = 0; i < area.length; ++i) {
            Dimension dim = cube.getDimensionAt(i);
            area[i] = dim.getDefaultHierarchy().getElements();
        }
        setArea(area);
    }

    private final void setArea(Element[][] area) {
        String[][] ids = new String[area.length][];
        for (int i = 0; i < area.length; i++) {
            ids[i] = new String[area[i].length];
            for (int j = 0; j < area[i].length; j++) {
                ids[i][j] = area[i][j].getId();
            }
        }
        contextInfo.setCellsArea(ids);
    }
}
