package com.tensegrity.palorules.util;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.palo.api.Cube;
import org.palo.api.Element;
import org.palo.api.Rule;
import com.tensegrity.palobrowser.cube.PaloTable;
import com.tensegrity.palobrowser.cubeview.FrameFormatData;
import com.tensegrity.palobrowser.editors.cubeeditor.CellFormat;
import com.tensegrity.palobrowser.editors.cubeeditor.CubeEditor;
import com.tensegrity.palobrowser.editors.cubeeditor.DimensionClause;
import com.tensegrity.palobrowser.editors.cubeeditor.DimensionSelectRegion;
import com.tensegrity.palorules.EditorResources;
import com.tensegrity.palorules.ICellSpecifier;
import com.tensegrity.palorules.IRuleEditor;
import com.tensegrity.palorules.source.IContentItem;
import com.tensegrity.palorules.source.IContentItemContainer;

/**
 * This is the global entry point for all palo table framed cell related actions.
 * @author Andreas Ebbert
 * @version $Id: FramedCellsUtil.java,v 1.20 2008/05/28 16:25:20 AndreasEbbert Exp $
 */
public class FramedCellsUtil {

    private static final boolean DFC = true;

    public static final CellFormat CF_TARGET;

    public static final CellFormat CF_SOURCE;

    public static final int LINE_WIDTH = 3;

    public static final int LINE_STYLE = SWT.LINE_DASH;

    static {
        CF_TARGET = new CellFormat();
        final RGB rgbTarget = EditorResources.COLOR_BORDER_TARGET.getRGB();
        CF_TARGET.topFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbTarget, CellFormat.HORIZONTAL_TOP);
        CF_TARGET.leftFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbTarget, CellFormat.VERTICAL_LEFT);
        CF_TARGET.bottomFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbTarget, CellFormat.HORIZONTAL_BOTTOM);
        CF_TARGET.rightFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbTarget, CellFormat.VERTICAL_RIGHT);
        CF_SOURCE = new CellFormat();
        final RGB rgbSource = EditorResources.COLOR_BORDER_SOURCE.getRGB();
        CF_SOURCE.topFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbSource, CellFormat.HORIZONTAL_TOP);
        CF_SOURCE.leftFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbSource, CellFormat.VERTICAL_LEFT);
        CF_SOURCE.bottomFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbSource, CellFormat.HORIZONTAL_BOTTOM);
        CF_SOURCE.rightFrame = new FrameFormatData(LINE_WIDTH, LINE_STYLE, rgbSource, CellFormat.VERTICAL_RIGHT);
    }

    private FramedCellsUtil() {
    }

    private static Element[][] lastSetSource = null;

    public static final void markSourceCells(IRuleEditor editor, Element[][] cells, Rule rule, CubeEditor cubeEditor) {
        if (!DFC) return;
        if (cells == null) return;
        final PaloTable table = editor.getActiveTable();
        if (table == null) return;
        if (rule != null && !fittingCubeEditor(editor, rule, cubeEditor)) return;
        if (rule == null && !editor.isDirty()) return;
        DimensionSelectRegion dsReg = cubeEditor.getSelectDimensionRegion();
        DimensionClause[] dc = dsReg.getDimensionClauses();
        if (dc.length > 0) {
            for (int i = 0; i < dc.length; i++) {
                Element el = dc[i].getSelectedElement();
                boolean m = false;
                outer: for (int j = 0; j < cells.length; j++) {
                    for (int k = 0; k < cells[j].length; k++) {
                        Element els = cells[j][k];
                        if (els != null && els.equals(el)) {
                            m = true;
                            break outer;
                        }
                    }
                }
                if (!m) {
                    lastSetSource = null;
                    table.clearFramedCells(CF_SOURCE);
                    return;
                }
            }
        }
        table.setFramedCells(cells, CF_SOURCE);
        lastSetSource = cells;
        table.getContent().refresh();
    }

    public static final void markSourceCells(IRuleEditor editor, Element[][] el) {
        if (!DFC) return;
        final PaloTable table = editor.getActiveTable();
        if (table == null) return;
        table.setFramedCells(el, CF_SOURCE);
    }

    public static final void addSourceCell(PaloTable table, Element[] el) {
        if (!DFC) return;
        if (table == null) return;
        table.addFramedCell(el, CF_SOURCE);
    }

    private static Element[][] lastSetTarget = null;

    public static final void markTargetCells(IRuleEditor editor, Element[] cell, Rule rule, CubeEditor cubeEditor) {
        if (!DFC) return;
        if (cell == null) return;
        final PaloTable table = editor.getActiveTable();
        if (table == null) return;
        if (rule != null && !fittingCubeEditor(editor, rule, cubeEditor)) return;
        if (rule == null && !editor.isDirty()) return;
        DimensionSelectRegion dsReg = cubeEditor.getSelectDimensionRegion();
        DimensionClause[] dc = dsReg.getDimensionClauses();
        if (dc.length > 0) {
            for (int i = 0; i < dc.length; i++) {
                Element el = dc[i].getSelectedElement();
                boolean m = false;
                for (int j = 0; j < cell.length; j++) {
                    Element els = cell[j];
                    if (els != null && els.equals(el)) {
                        m = true;
                        break;
                    }
                }
                if (!m) {
                    lastSetTarget = null;
                    table.clearFramedCells(CF_TARGET);
                    return;
                }
            }
        }
        ArrayList<Element> eList = new ArrayList<Element>();
        for (int i = 0; i < cell.length; i++) {
            if (cell[i] == null) continue;
            eList.add(cell[i]);
        }
        Element[][] cellsToSet = new Element[][] { eList.toArray(new Element[eList.size()]) };
        table.setFramedCells(cellsToSet, CF_TARGET);
        lastSetTarget = cellsToSet;
        table.getContent().refresh();
    }

    public static final void itemRemoved(IRuleEditor editor, IContentItem item) {
        if (item == null || item.getControl() == null || item.getControl().isDisposed()) return;
        final PaloTable table = editor.getActiveTable();
        if (table == null) return;
        traverseAndDeselect(table, item);
        item.getControl().getDisplay().asyncExec(new Runnable() {

            public void run() {
                if (table == null || table.isDisposed()) return;
                table.refresh();
            }
        });
    }

    private static void traverseAndDeselect(PaloTable table, IContentItem item) {
        if (item instanceof ICellSpecifier) {
            Element[] e = ((ICellSpecifier) item).getElements();
            if (e != null && table != null) {
                table.removeFramedCell(e, CF_SOURCE);
            }
        }
        if (item instanceof IContentItemContainer) {
            IContentItem[] ch = ((IContentItemContainer) item).getContentItems();
            for (int i = 0; i < ch.length; i++) {
                traverseAndDeselect(table, ch[i]);
            }
        }
    }

    public static final boolean fittingCubeEditor(IRuleEditor editor, Rule rule, CubeEditor cubeEditor) {
        Cube act = editor.getDataProvider().getActiveCube();
        if (act == null || rule == null || cubeEditor == null) return false;
        if (cubeEditor.getInput() == null || cubeEditor.getInput().getCube() == null) {
            return false;
        }
        final Cube inputCube = RuleUtil.getRegularCube(cubeEditor.getInput().getCube());
        final Cube ruleCube = RuleUtil.getRegularCube(rule.getCube());
        if (inputCube.equals(ruleCube)) {
            return true;
        }
        return false;
    }

    private static boolean sameElements(Element[][] e0, Element[][] e1) {
        if (e0 == null || e1 == null) return false;
        if (e0.length != e1.length) return false;
        for (int i = 0; i < e0.length; i++) {
            if (e0[i].length != e1[i].length) return false;
        }
        for (int i = 0; i < e0.length; i++) {
            for (int j = 0; j < e0[i].length; j++) {
                if (e0[i][j] != null && !e0[i][j].equals(e1[i][j])) return false;
            }
        }
        return true;
    }
}
