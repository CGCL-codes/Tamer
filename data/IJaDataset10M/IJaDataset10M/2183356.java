package buttress.gui;

import java.awt.Component;
import java.awt.Container;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class FormLayout extends SpringLayout {

    private Container panel;

    /**
         *
         *
         * @param panel Container
         */
    public FormLayout(Container panel) {
        super();
        this.panel = panel;
    }

    /**
         *
         *
         * @param row int
         * @param col int
         * @param cols int
         * @return Constraints
         */
    private SpringLayout.Constraints getConstraintsForCell(int row, int col, int cols) {
        Component c = panel.getComponent(row * cols + col);
        return getConstraints(c);
    }

    /**
         *
         *
         * @param rows int
         * @param cols int
         * @param initialX int
         * @param initialY int
         * @param xPad int
         * @param yPad int
         */
    public void makeCompactGrid(int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width, getConstraintsForCell(r, c, cols).getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height, getConstraintsForCell(r, c, cols).getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }
        SpringLayout.Constraints pCons = getConstraints(panel);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
