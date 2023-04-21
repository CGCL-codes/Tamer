package org.gocha.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author gocha
 */
public class TableCellTest {

    @Test
    public void tableCell() {
        String text = "Lorem ipsum dolor sit amet," + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur" + "consectetur adipiscing elit. \n" + "Nulla urna nibh, cursus in gravida nec, condimentum non est. \n" + "Suspendisse convallis, est quis pharetra pharetra, mi metus " + "lacinia enim, tempor ornare erat turpis et metus. \n" + "Fusce tellus elit, fringilla ut fermentum quis, fermentum vel turpis.";
        TableCell tc = new TableCell(text, 30, Align.Begin, Align.Begin, true, 1);
        int idx = 0;
        int empty = 0;
        for (String t : tc.getTextLines()) {
            idx++;
            if (idx < 10) System.out.print(" ");
            System.out.println("" + idx + ". " + t);
            assertTrue(t.length() == 30);
            if (t.trim().length() == 0) empty++;
        }
        assertTrue(empty == 3);
    }
}
