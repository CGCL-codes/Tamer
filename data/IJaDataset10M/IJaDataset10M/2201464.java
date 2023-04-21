package org.dinnermate.gui.trackers;

public class NetIncomeTracker {

    String[][] array;

    public NetIncomeTracker(String[][] staffArray, String[][] stockArray, String[][] menuArray) {
        array = new String[1][4];
        for (int i = 0; i < array[0].length; i++) {
            array[0][i] = "" + 0;
        }
        populateArray(staffArray, stockArray, menuArray);
    }

    private void populateArray(String[][] staffArray, String[][] stockArray, String[][] menuArray) {
        for (int i = 0; i < menuArray.length; i++) {
            array[0][0] = "" + (Float.parseFloat(array[0][0]) + Float.parseFloat(menuArray[i][3]));
        }
        for (int i = 0; i < staffArray.length; i++) {
            if (staffArray[i][3] == null) {
                break;
            }
            array[0][1] = "" + (Float.parseFloat(array[0][1]) + Float.parseFloat(staffArray[i][3]));
        }
        for (int i = 0; i < stockArray.length; i++) {
            array[0][2] = "" + (Float.parseFloat(array[0][2]) + Float.parseFloat(stockArray[i][3]));
        }
        calcNetIncome();
    }

    private void calcNetIncome() {
        array[0][3] = (Float.parseFloat(array[0][0]) - Float.parseFloat(array[0][1]) - Float.parseFloat(array[0][2])) + "";
    }

    public String[][] getNetIncomeArray() {
        return array;
    }
}
