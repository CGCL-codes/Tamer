package xhack.menu;

import xhack.object.Map;
import java.awt.Color;
import xhack.ui.Colours;

public abstract class HackMenu {

    public static final int MAX_ROWS = 50;

    public static final int MAX_COLS = 10;

    public static final Color ACTIVE_COLOUR = Colours.colours[Colours.WHITE];

    public static final Color INACTIVE_COLOUR = Colours.colours[Colours.GRAY];

    public static final Color SELECTED_COLOUR = Colours.colours[Colours.RED];

    public static final Color HIGHLIGHT_COLOUR = Colours.colours[Colours.BLUE];

    protected int rows;

    protected int cols;

    protected MenuItem[][] items = new MenuItem[rows][cols];

    protected int rowSelection;

    protected int colSelection;

    public HackMenu() {
    }

    public HackMenu(int rows, int cols) {
        init(rows, cols);
    }

    public void init(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        items = new MenuItem[rows][cols];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) {
            items[i][j] = new MenuItem(" ");
            items[i][j].isSelectable(false);
        }
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    /**
   * Get the text for a menu item
   * @param i int row
   * @param j int column
   * @return String text of menu item at row i, column j
   */
    public String getItemText(int i, int j) {
        if (items[i][j] == null) return " ";
        return items[i][j].getText();
    }

    public boolean isItemSelected(int i, int j) {
        if (items[i][j] == null) return false;
        return items[i][j].isSelected();
    }

    public boolean isItemSelected(int i, int j, boolean b) {
        if (items[i][j] == null) return false;
        return items[i][j].isSelected(b);
    }

    public boolean singleSelect(int i, int j) {
        if (isItemSelected((i + rows) % rows, (j + cols) % cols, true)) {
            isItemSelected(rowSelection, colSelection, false);
            rowSelection = (i + rows) % rows;
            colSelection = (j + cols) % cols;
            items[rowSelection][colSelection].isSelected(true);
            return true;
        } else return false;
    }

    public void moveSelection(int dir) {
        switch(dir) {
            case (Map.NORTH):
                for (int i = 1; i < rows && !singleSelect(rowSelection - i, colSelection); i++) ;
                break;
            case (Map.SOUTH):
                for (int i = 1; i < rows && !singleSelect(rowSelection + i, colSelection); i++) ;
                break;
            case (Map.EAST):
                for (int i = 1; i < cols && !singleSelect(rowSelection, colSelection - i); i++) ;
                break;
            case (Map.WEST):
                for (int i = 1; i < cols && !singleSelect(rowSelection, colSelection + i); i++) ;
        }
    }

    public int rowSelection() {
        return rowSelection;
    }

    public int colSelection() {
        return colSelection();
    }

    public Color getItemColour(int i, int j) {
        if (isItemSelected(i, j)) return ACTIVE_COLOUR;
        return items[i][j].getColour();
    }

    public void enter() {
    }
}
