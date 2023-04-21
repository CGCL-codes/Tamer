package org.xulbooster.eclipse.xb.ui.snippets.ui.page;

/**
 * Class used as a trivial case of a Task 
 * Serves as the business object for the TableViewer Example.
 * 
 * A Task has the following properties: completed, description,
 * owner and percentComplete 
 * 
 * @author Laurent
 *
 * 
 */
public class Line {

    private String[] cells;

    /**
	 * Create a task with an initial description
	 * 
	 * @param string
	 */
    public Line(String string, int nbrOfCell) {
        super();
        cells = new String[nbrOfCell];
        cells[0] = string;
        for (int x = 1; x < nbrOfCell; x++) {
            cells[x] = new String("1");
        }
    }

    public String getCellValue(int y) {
        return new String(cells[y]);
    }

    public void setCellValue(int y, String s) {
        cells[y] = new String(s);
    }
}
