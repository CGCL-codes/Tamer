package magictool.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import magictool.VerticalLayout;

/**
 * GridPanel is a panel which contains fields for users to enter paramters to specify
 * the coordinates, rows, and columns for a particular grid.
 */
class GridPanel extends JPanel {

    private VerticalLayout verticalLayout1 = new VerticalLayout();

    private JButton clearGridData = new JButton();

    private JPanel jPanel5 = new JPanel();

    private JLabel rowNumLabel = new JLabel();

    private JTextField rowNum = new JTextField();

    private JPanel jPanel6 = new JPanel();

    private JPanel jPanel9 = new JPanel();

    private JLabel columnNumLabel = new JLabel();

    private JTextField columnNum = new JTextField();

    private JPanel jPanel10 = new JPanel();

    private JPanel jPanel2 = new JPanel();

    private JLabel topleftYLabel = new JLabel();

    private JLabel topleftXLabel = new JLabel();

    private JPanel jPanel7 = new JPanel();

    private VerticalLayout verticalLayout2 = new VerticalLayout();

    private JPanel jPanel11 = new JPanel();

    private VerticalLayout verticalLayout3 = new VerticalLayout();

    private JLabel toprightYLabel = new JLabel();

    private JLabel toprightXLabel = new JLabel();

    private JPanel jPanel3 = new JPanel();

    private JPanel jPanel8 = new JPanel();

    private JPanel jPanel4 = new JPanel();

    private VerticalLayout verticalLayout4 = new VerticalLayout();

    private JPanel jPanel12 = new JPanel();

    private JPanel jPanel13 = new JPanel();

    private JLabel bRowPtXLabel = new JLabel();

    private JLabel bRowPtYLabel = new JLabel();

    private JButton applyBox = new JButton();

    private JComboBox applyFrom = new JComboBox();

    /**textfield for the x-coordinate of the top right corner spot of the grid*/
    protected JTextField toprightX = new JTextField();

    /**textfield for the y-coordinate of a bottom row spot on the grid*/
    protected JTextField bRowPtY = new JTextField();

    /**textfield for the x-coordinate of a bottom row spot on the grid*/
    protected JTextField bRowPtX = new JTextField();

    /**textfield for the y-coordinate of the top right corner spot of the grid*/
    protected JTextField toprightY = new JTextField();

    /**textfield for the x-coordinate of the top left corner spot of the grid*/
    protected JTextField topleftX = new JTextField();

    /**textfield for the y-coordinate of the top left corner spot of the grid*/
    protected JTextField topleftY = new JTextField();

    /**button to update grids with user specified coordinates as well as rows and columns*/
    protected JButton updateGridData = new JButton();

    /**toggle button to allow user to specifiy top left spot*/
    protected JToggleButton tlButton = new JToggleButton();

    /**toggle button to allow user to specifiy top right spot*/
    protected JToggleButton trButton = new JToggleButton();

    /**toggle button to allow user to specifiy a bottom row spot*/
    protected JToggleButton bottomRowPt = new JToggleButton();

    /**grid that the user can specify coordinates, rows, and columns*/
    protected Grid grid;

    /**x-coordinate of the top left corner spot of the grid*/
    protected int tlSpotX;

    /**y-coordinate of the top left corner spot of the grid*/
    protected int tlSpotY;

    /**x-coordinate of the top right corner spot of the grid*/
    protected int trSpotX;

    /**y-coordinate of the top right corner spot of the grid*/
    protected int trSpotY;

    /**x-coordinate of a bottom row spot on the grid*/
    protected int bRowSpotX;

    /**y-coordinate of a bottom row spot on the grid*/
    protected int bRowSpotY;

    /**number of columns of spots*/
    protected int columns;

    /**number of rows of spots*/
    protected int rows;

    /**x-coordinate of the top left corner spot of the grid*/
    protected int brSpotX;

    /**x-coordinate of the top left corner spot of the grid*/
    protected int brSpotY;

    /**x-coordinate of the top left corner spot of the grid*/
    protected int blSpotX;

    /**x-coordinate of the top left corner spot of the grid*/
    protected int blSpotY;

    /**panel which displays microarray image which is being gridded*/
    protected ImageDisplayPanel iDisplay;

    /**parameters of last updated grid*/
    protected static int[] lastUpdate = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    /**
   * Constructs a grid panel for the given grid and image display panel
   * @param g grid to set coordinates, rows and columns
   * @param id image display panel which holds the microarray image that is being gridded
   */
    public GridPanel(Grid g, ImageDisplayPanel id) {
        this.iDisplay = id;
        this.grid = g;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(verticalLayout1);
        clearGridData.setText("CLEAR");
        clearGridData.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clearGridData_actionPerformed(e);
            }
        });
        applyBox.setEnabled(false);
        applyBox.setMargin(new Insets(2, 2, 2, 2));
        applyBox.setText("Apply from Grid:");
        applyBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                applyBox_actionPerformed(e);
            }
        });
        applyFrom.addItem(new String("None"));
        applyFrom.setEnabled(false);
        rowNumLabel.setText("Rows");
        rowNum.setToolTipText("");
        rowNum.setColumns(6);
        this.setPreferredSize(new Dimension(200, 460));
        columnNumLabel.setText("Columns");
        columnNum.setColumns(6);
        topleftYLabel.setText("y");
        topleftXLabel.setText("x");
        topleftY.setColumns(6);
        topleftX.setColumns(6);
        jPanel10.setLayout(verticalLayout2);
        jPanel10.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel11.setLayout(verticalLayout3);
        toprightYLabel.setText("y");
        toprightY.setColumns(6);
        toprightXLabel.setText("x");
        toprightX.setColumns(6);
        jPanel11.setBorder(BorderFactory.createLineBorder(Color.black));
        updateGridData.setText("UPDATE");
        updateGridData.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateGridData_actionPerformed(e);
            }
        });
        tlButton.setToolTipText("Click on the center of the top-left corner of the grid.");
        tlButton.setText("Set Top Left Spot");
        trButton.setToolTipText("Click on the center of the top-right corner of the grid.");
        trButton.setActionCommand("Set Top Right");
        trButton.setText("Set Top Right Spot");
        jPanel4.setLayout(verticalLayout4);
        bottomRowPt.setMaximumSize(new Dimension(140, 23));
        bottomRowPt.setMinimumSize(new Dimension(140, 23));
        bottomRowPt.setPreferredSize(new Dimension(140, 23));
        bottomRowPt.setToolTipText("Click on the center of any spot in the bottom row of the grid.");
        bottomRowPt.setText("Set Bottom Row");
        bRowPtXLabel.setText("x");
        bRowPtYLabel.setText("y");
        bRowPtY.setColumns(6);
        bRowPtX.setColumns(6);
        jPanel4.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel3.add(trButton, null);
        this.add(jPanel6, null);
        jPanel6.add(applyBox, null);
        jPanel6.add(applyFrom, null);
        this.add(jPanel10, null);
        jPanel7.add(topleftXLabel, null);
        jPanel7.add(topleftX, null);
        jPanel7.add(topleftYLabel, null);
        jPanel7.add(topleftY, null);
        this.add(jPanel11, null);
        jPanel8.add(toprightXLabel, null);
        jPanel8.add(toprightX, null);
        jPanel8.add(toprightYLabel, null);
        jPanel8.add(toprightY, null);
        this.add(jPanel4, null);
        jPanel11.add(jPanel3, null);
        jPanel11.add(jPanel8, null);
        jPanel10.add(jPanel2, null);
        jPanel2.add(tlButton, null);
        jPanel10.add(jPanel7, null);
        this.add(jPanel5, null);
        jPanel5.add(rowNumLabel, null);
        jPanel5.add(rowNum, null);
        this.add(jPanel9, null);
        jPanel9.add(columnNumLabel, null);
        jPanel9.add(columnNum, null);
        this.add(updateGridData, null);
        this.add(clearGridData, null);
        jPanel4.add(jPanel12, null);
        jPanel12.add(bottomRowPt, null);
        jPanel4.add(jPanel13, null);
        jPanel13.add(bRowPtXLabel, null);
        jPanel13.add(bRowPtX, null);
        jPanel13.add(bRowPtYLabel, null);
        jPanel13.add(bRowPtY, null);
        updateApplyFromBox();
        if (grid.bottomLeftX != grid.bottomRightX && grid.bottomLeftY != grid.bottomRightY && grid.topLeftX != grid.topRightX && grid.topLeftY != grid.topRightY && grid.columns > 0 && grid.rows > 0) setDataFromGrid(grid);
        iDisplay.repaint();
    }

    /**
   * updates the grid associated with the grid panel based on the user specified parameters
   * in the textfields
   */
    public void updateGrids() {
        try {
            if (topleftX.getText() != null) {
                tlSpotX = Integer.parseInt(topleftX.getText().trim());
            }
            if (topleftY.getText() != null) {
                tlSpotY = Integer.parseInt(topleftY.getText().trim());
            }
            if (toprightX.getText() != null) {
                trSpotX = Integer.parseInt(toprightX.getText().trim());
            }
            if (toprightY.getText() != null) {
                trSpotY = Integer.parseInt(toprightY.getText().trim());
            }
            if (bRowPtX.getText() != null) {
                bRowSpotX = Integer.parseInt(bRowPtX.getText().trim());
            }
            if (bRowPtY.getText() != null) {
                bRowSpotY = Integer.parseInt(bRowPtY.getText().trim());
            }
            if (rowNum.getText() != null) {
                rows = Integer.parseInt(rowNum.getText().trim());
                grid.setRows(rows);
            }
            if (columnNum.getText() != null) {
                columns = Integer.parseInt(columnNum.getText().trim());
                grid.setColumns(columns);
            }
            if (rows > 0 && columns > 0) {
                double m1 = ((double) (trSpotY - tlSpotY)) / (trSpotX - tlSpotX);
                if (m1 == 0) {
                    brSpotX = trSpotX;
                    brSpotY = bRowSpotY;
                    blSpotX = tlSpotX;
                    blSpotY = bRowSpotY;
                } else {
                    double m2 = -1 / m1;
                    double normalPtX = (trSpotY - bRowSpotY - (m1 * trSpotX) + (m2 * bRowSpotX)) / (m2 - m1);
                    double normalPtY = bRowSpotY - (m2 * (bRowSpotX - normalPtX));
                    brSpotX = (int) (trSpotX + (bRowSpotX - normalPtX));
                    brSpotY = (int) (trSpotY + (bRowSpotY - normalPtY));
                    blSpotX = (int) (tlSpotX + (bRowSpotX - normalPtX));
                    blSpotY = (int) (tlSpotY + (bRowSpotY - normalPtY));
                }
                lastUpdate[0] = grid.getTopLeftX();
                lastUpdate[1] = grid.getTopLeftY();
                lastUpdate[2] = grid.getTopRightX();
                lastUpdate[3] = grid.getTopRightY();
                lastUpdate[4] = grid.getBottomLeftX();
                lastUpdate[5] = grid.getBottomLeftY();
                lastUpdate[6] = grid.getBottomRightX();
                lastUpdate[7] = grid.getBottomRightY();
                lastUpdate[8] = grid.getRows();
                lastUpdate[9] = grid.getColumns();
                grid.setTopLeftX(tlSpotX);
                grid.setTopLeftY(tlSpotY);
                grid.setTopRightX(trSpotX);
                grid.setTopRightY(trSpotY);
                grid.setBottomLeftX(blSpotX);
                grid.setBottomLeftY(blSpotY);
                grid.setBottomRightX(brSpotX);
                grid.setBottomRightY(brSpotY);
                iDisplay.repaint();
            } else JOptionPane.showMessageDialog(iDisplay, "Error Updating! Rows and Columns Must Be Greater Than Zero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(iDisplay, "Error Updating! Please Enter Integers In All Text Fields");
        }
    }

    /**
   * sets the textfields to the variable settings (useful when users enter
   * incorrect values in the textfields
   */
    public void setGridData() {
        topleftX.setText(Integer.toString(tlSpotX));
        topleftY.setText(Integer.toString(tlSpotY));
        toprightX.setText(Integer.toString(trSpotX));
        toprightY.setText(Integer.toString(trSpotY));
        bRowPtX.setText(Integer.toString(bRowSpotX));
        bRowPtY.setText(Integer.toString(bRowSpotY));
        rowNum.setText(Integer.toString(rows));
        grid.setRows(rows);
        columnNum.setText(Integer.toString(columns));
        grid.setColumns(columns);
    }

    /**
   * sets the variables and textfields to the parameters from the associated grid
   * @param g associated grid
   */
    public void setDataFromGrid(Grid g) {
        this.grid = g;
        tlSpotX = g.topLeftX;
        tlSpotY = g.topLeftY;
        trSpotX = g.topRightX;
        trSpotY = g.topRightY;
        bRowSpotX = g.bottomLeftX;
        bRowSpotY = g.bottomLeftY;
        blSpotX = g.bottomLeftX;
        blSpotY = g.bottomLeftY;
        brSpotX = g.bottomRightX;
        brSpotY = g.bottomRightY;
        rows = g.rows;
        columns = g.columns;
        setGridData();
        iDisplay.repaint();
    }

    /**
   * resets all buttons to unselected
   */
    protected void resetButtons() {
        tlButton.setSelected(false);
        trButton.setSelected(false);
        bottomRowPt.setSelected(false);
    }

    /**
   * updates the apply from combobox to show which grids a user can apply from
   */
    protected void updateApplyFromBox() {
        int orig = -1;
        try {
            orig = Integer.parseInt(applyFrom.getSelectedItem().toString());
        } catch (Exception e) {
        }
        applyFrom.removeAllItems();
        int n = 0;
        int pos = 0;
        for (int i = 0; i < iDisplay.manager.numGrids; i++) {
            if (iDisplay.manager.getGrid(i).isValid() && iDisplay.manager.getGrid(i) != grid) {
                applyFrom.addItem(new String("" + (i + 1)));
                n++;
                if (orig == i) pos = i;
            }
        }
        if (n > 0) {
            try {
                Integer.parseInt(topleftX.getText().trim());
                Integer.parseInt(topleftY.getText().trim());
                applyBox.setEnabled(true);
                applyFrom.setEnabled(true);
            } catch (Exception e) {
                try {
                    Integer.parseInt(toprightX.getText().trim());
                    Integer.parseInt(toprightY.getText().trim());
                    applyBox.setEnabled(true);
                    applyFrom.setEnabled(true);
                } catch (Exception e1) {
                    applyBox.setEnabled(false);
                    applyFrom.setEnabled(false);
                }
            }
        } else {
            applyBox.setEnabled(false);
            applyFrom.setEnabled(false);
            applyFrom.addItem(new String("--"));
        }
        applyFrom.setSelectedIndex(pos);
    }

    private Grid applyFromGrid(int gridNum) {
        return (iDisplay.manager.getGrid(gridNum - 1));
    }

    private void updateGridData_actionPerformed(ActionEvent e) {
        try {
            updateGrids();
            updateApplyFromBox();
        } catch (NumberFormatException e2) {
            JOptionPane.showMessageDialog(iDisplay, "Please Enter Integer Values For All Fields");
        }
    }

    private void clearGridData_actionPerformed(ActionEvent e) {
        topleftX.setText("");
        grid.setTopLeftX(0);
        topleftY.setText("");
        grid.setTopLeftY(0);
        toprightX.setText("");
        grid.setTopRightX(0);
        toprightY.setText("");
        grid.setTopRightY(0);
        bRowPtX.setText("");
        bRowPtY.setText("");
        grid.setBottomLeftX(0);
        grid.setBottomLeftY(0);
        grid.setBottomRightX(0);
        grid.setBottomRightY(0);
        rowNum.setText("");
        grid.setRows(0);
        columnNum.setText("");
        grid.setColumns(0);
        updateApplyFromBox();
        iDisplay.repaint();
    }

    private void applyBox_actionPerformed(ActionEvent e) {
        try {
            int gridNum = Integer.parseInt(applyFrom.getSelectedItem().toString());
            toprightX.setText(String.valueOf(Integer.parseInt(topleftX.getText().trim()) + (applyFromGrid(gridNum).getTopRightX() - applyFromGrid(gridNum).getTopLeftX())));
            toprightY.setText(String.valueOf(Integer.parseInt(topleftY.getText().trim()) + (applyFromGrid(gridNum).getTopRightY() - applyFromGrid(gridNum).getTopLeftY())));
            bRowPtX.setText(String.valueOf(applyFromGrid(gridNum).getBottomRightX() + (Integer.parseInt(topleftX.getText().trim()) - applyFromGrid(gridNum).getTopLeftX())));
            bRowPtY.setText(String.valueOf(applyFromGrid(gridNum).getBottomRightY() + (Integer.parseInt(topleftY.getText().trim()) - applyFromGrid(gridNum).getTopLeftY())));
            rowNum.setText(String.valueOf(applyFromGrid(gridNum).getRows()));
            columnNum.setText(String.valueOf(applyFromGrid(gridNum).getColumns()));
            updateGrids();
        } catch (Exception e1) {
            try {
                int gridNum = Integer.parseInt(applyFrom.getSelectedItem().toString());
                topleftX.setText(String.valueOf(Integer.parseInt(toprightX.getText().trim()) - (applyFromGrid(gridNum).getTopRightX() - applyFromGrid(gridNum).getTopLeftX())));
                topleftY.setText(String.valueOf(Integer.parseInt(toprightY.getText().trim()) - (applyFromGrid(gridNum).getTopRightY() - applyFromGrid(gridNum).getTopLeftY())));
                bRowPtX.setText(String.valueOf(applyFromGrid(gridNum).getBottomRightX() + (Integer.parseInt(toprightX.getText().trim()) - applyFromGrid(gridNum).getTopRightX())));
                bRowPtY.setText(String.valueOf(applyFromGrid(gridNum).getBottomRightY() + (Integer.parseInt(toprightY.getText().trim()) - applyFromGrid(gridNum).getTopRightY())));
                rowNum.setText(String.valueOf(applyFromGrid(gridNum).getRows()));
                columnNum.setText(String.valueOf(applyFromGrid(gridNum).getColumns()));
                updateGrids();
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(iDisplay, "Error! Could Not Apply From Grid");
            }
        }
    }
}
