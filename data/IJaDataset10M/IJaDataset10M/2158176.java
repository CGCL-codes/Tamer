package org.deft.artifactviewer.table;

import java.util.HashSet;
import java.util.Set;
import org.deft.artifactviewer.ArtifactViewerInput;
import org.deft.artifactviewer.ArtifactViewerPart;
import org.deft.repository.datamodel.Artifact;
import org.deft.representation.table.data.Table;
import org.deft.representation.table.data.TableCell;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class TableViewerPart extends EditorPart implements ArtifactViewerPart {

    public static final String ID = "org.deft.artifactviewer.table";

    public Artifact getArtifact() {
        ArtifactViewerInput input = (ArtifactViewerInput) getEditorInput();
        Artifact artifact = input.getArtifact();
        return artifact;
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName();
    }

    private void setPartName() {
        Artifact artifact = getArtifact();
        super.setPartName(artifact.getName());
    }

    private Table getTable() {
        ArtifactViewerInput input = ((ArtifactViewerInput) getEditorInput());
        Table table = (Table) input.getContent();
        return table;
    }

    @Override
    public void createPartControl(Composite parent) {
        Grid grid = new Grid(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        Table table = getTable();
        createGridColumns(table, grid);
        setColumnHeadersIfApplicable(table, grid);
        enableRowHeadersIfApplicable(table, grid);
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        Set<TableCell> alreadyProcessedCells = new HashSet<TableCell>();
        for (int row = 1; row <= rowCount; row++) {
            GridItem item = new GridItem(grid, SWT.NONE);
            for (int col = 1; col <= columnCount; col++) {
                TableCell cell = table.getTableCell(row, col);
                if (!alreadyProcessedCells.contains(cell)) {
                    String text = cell.getText();
                    int colSpan = cell.getColSpan() - 1;
                    int rowSpan = cell.getRowSpan() - 1;
                    int idx = col - 1;
                    item.setText(idx, text);
                    item.setColumnSpan(idx, colSpan);
                    item.setRowSpan(idx, rowSpan);
                    alreadyProcessedCells.add(cell);
                }
            }
            setRowHeaderIfApplicable(table, item, row);
        }
        packTable(grid, columnCount);
    }

    private void createGridColumns(Table table, Grid grid) {
        for (int col = 1; col <= table.getColumnCount(); col++) {
            new GridColumn(grid, SWT.NONE);
        }
    }

    private void setColumnHeadersIfApplicable(Table table, Grid grid) {
        if (table.hasColumnHeaders()) {
            grid.setHeaderVisible(true);
            for (int col = 1; col <= table.getColumnCount(); col++) {
                TableCell cell = table.getColumnHeader(col);
                String text = cell.getText();
                GridColumn column = grid.getColumn(col - 1);
                column.setText(text);
            }
        }
    }

    private void enableRowHeadersIfApplicable(Table table, Grid grid) {
        if (table.hasRowHeaders()) {
            grid.setRowHeaderVisible(true);
        }
    }

    private void setRowHeaderIfApplicable(Table table, GridItem item, int row) {
        if (table.hasRowHeaders()) {
            String text = table.getRowHeader(row).getText();
            item.setHeaderText(text);
        }
    }

    private void packTable(Grid grid, int columnCount) {
        for (int col = 0; col < columnCount; col++) {
            GridColumn column = grid.getColumn(col);
            column.pack();
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void setFocus() {
    }
}
