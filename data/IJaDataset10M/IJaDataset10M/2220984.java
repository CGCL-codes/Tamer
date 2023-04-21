package org.formaria.swt;

/**
 * Class which extends DialogTable for the SWT Tables, and show a dialog to add
 * an item to a table.
 * 
 * @author Romain Louis
 */
public class DialogTableAddItem extends DialogTable {

    /**
   * Creates a new dialogtableadditem.
   * 
   * @param xet
   *          the table
   * @param title
   *          the title of the dialog
   */
    public DialogTableAddItem(EditTable xet, String title) {
        super(xet, title, xet.getColumnCount());
    }

    /**
   * Do the first step of the dialog addition
   */
    public void firststep() {
        String[] ss = getLabels();
        int maxWidth = 0;
        for (int i = 0; i < ss.length; i++) {
            int l = Table.charLength(ss[i]);
            if (l > maxWidth) maxWidth = l;
        }
        widthLabel = maxWidth;
        width = 2 * widthLabel + 2 * H_GAP + WIDTH_SPACING;
        height = 2 * V_GAP + 2 * (ss.length + 1) * HEIGHT_SPACING + HEIGHT_BUTTON;
    }

    /**
   * Get the text of the labels
   */
    private String[] getLabels() {
        String[] ss = new String[xls.length];
        String sentencePartOne = "Enter a new value for the attribut \" ";
        for (int i = 0; i < xes.length; i++) {
            String name = xet.getColumn(i).getText();
            ss[i] = sentencePartOne + name + " \" :";
        }
        return ss;
    }

    /**
   * Do the second step of the dialog edition
   */
    public void secondstep() {
        borders[0] = getBorder(WIDTH_BORDER_CENTER, HEIGHT_FULL, width, V_GAP);
        borders[1] = getBorder(WIDTH_BORDER_CENTER, HEIGHT_FULL + V_GAP, H_GAP, height - 2 * V_GAP);
        borders[2] = getBorder(WIDTH_BORDER_CENTER, HEIGHT_FULL + height - V_GAP, width, V_GAP);
        borders[3] = getBorder(WIDTH_BORDER_CENTER + width - H_GAP, HEIGHT_FULL + V_GAP, H_GAP, height - 2 * V_GAP);
        int hlength = width - 2 * H_GAP;
        int x = WIDTH_BORDER_CENTER + H_GAP;
        int y = HEIGHT_FULL + V_GAP;
        for (int i = 4; i < borders.length - 3; i = i + 2) {
            borders[i] = getBorder(x, y, hlength, HEIGHT_SPACING);
            borders[i + 1] = getBorder(x + widthLabel, y + HEIGHT_SPACING, WIDTH_SPACING, HEIGHT_SPACING);
            y += 2 * HEIGHT_SPACING;
        }
        borders[borders.length - 3] = getBorder(x, y, hlength, HEIGHT_SPACING);
        borders[borders.length - 2] = getBorder(x + widthLabel, y + HEIGHT_SPACING, WIDTH_SPACING, HEIGHT_BUTTON);
        borders[borders.length - 1] = getBorder(x, y + HEIGHT_SPACING + HEIGHT_BUTTON, hlength, HEIGHT_SPACING);
    }

    /**
   * Do the third step of the dialog edition
   */
    public void thirdstep() {
        String sentencePartOne = "Enter a new value for the attribut \" ";
        int x = WIDTH_BORDER_CENTER + H_GAP;
        int y = HEIGHT_FULL + V_GAP + HEIGHT_SPACING;
        for (int i = 0; i < xes.length; i++) {
            String name = xet.getColumn(i).getText();
            String sentenceLabel = sentencePartOne + name + " \" :";
            xls[i] = new Label(contentPanel);
            xls[i].setBounds(x, y, widthLabel, HEIGHT_SPACING);
            xls[i].setVisible(true);
            xls[i].setText(sentenceLabel);
            xes[i] = new Edit(contentPanel);
            xes[i].setBounds(x + widthLabel + WIDTH_SPACING, y, widthLabel, HEIGHT_SPACING);
            xes[i].setVisible(true);
            xes[i].setData("Name", name + "Edit");
            y += 2 * HEIGHT_SPACING;
        }
    }

    /**
   * Do the fourth step of the dialog edition
   */
    public void fourthstep() {
        addButtons("addItem", "Add the item");
    }

    /**
   * Add an item
   */
    public void addItem() {
        String[] result = new String[xes.length];
        for (int i = 0; i < xes.length; i++) {
            result[i] = xes[i].getText();
        }
        value = xes[0].getText();
        xet.addItem(result);
        resetAll();
        closeDlg();
    }
}
