package game.rushhour.parking;

/**
 * A parking has car spaces of the same size. A vehicle may be larger than a car
 * space.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-03-21
 */
public class CarSpace {

    private int row;

    private int column;

    public CarSpace(int row, int column) {
        super();
        this.row = row;
        this.column = column;
    }

    public CarSpace(Integer row, Integer column) {
        this(row.intValue(), column.intValue());
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRowColumn(int row, int column) {
        setRow(row);
        setColumn(column);
    }

    public boolean equals(CarSpace space) {
        return inRow(space) & inColumn(space);
    }

    public boolean inRow(CarSpace space) {
        return getRow() == space.getRow();
    }

    public boolean inRow(int row) {
        return getRow() == row;
    }

    public boolean inColumn(CarSpace space) {
        return getColumn() == space.getColumn();
    }

    public boolean inColumn(int column) {
        return getColumn() == column;
    }

    public boolean inLine(CarSpace space) {
        return inRow(space) | inColumn(space);
    }

    public boolean lessRow(CarSpace space) {
        return getRow() < space.getRow();
    }

    public boolean lessColumn(CarSpace space) {
        return getColumn() < space.getColumn();
    }

    public boolean less(CarSpace space) {
        return lessRow(space) | lessColumn(space);
    }

    public boolean lessInLine(CarSpace space) {
        return inLine(space) & less(space);
    }

    public boolean lessOrEqualRow(CarSpace space) {
        return getRow() <= space.getRow();
    }

    public boolean lessOrEqualColumn(CarSpace space) {
        return getColumn() <= space.getColumn();
    }

    public boolean lessOrEqual(CarSpace space) {
        return lessOrEqualRow(space) | lessOrEqualColumn(space);
    }

    public boolean lessOrEqualInLine(CarSpace space) {
        return inLine(space) & lessOrEqual(space);
    }

    public CarSpace[] getHorizontalSpaces(CarSpace space) {
        CarSpace[] hLineSpaces = null;
        int row = getRow();
        int column = getColumn();
        if (inRow(space)) {
            if (lessOrEqualInLine(space)) {
                int noOfSpaces = (space.getColumn() - column) + 1;
                for (int i = 0; i < noOfSpaces; i++) {
                    hLineSpaces[i] = new CarSpace(row, column + i);
                }
            } else {
                System.out.println("Spaces are not ordered in horizontal line!");
            }
        } else {
            System.out.println("Spaces are not in horizontal line!");
        }
        return hLineSpaces;
    }

    public CarSpace[] getVerticalSpaces(CarSpace space) {
        CarSpace[] vLineSpaces = null;
        int row = getRow();
        int column = getColumn();
        if (inColumn(space)) {
            if (lessOrEqualInLine(space)) {
                int noOfSpaces = (space.getRow() - row) + 1;
                for (int i = 0; i < noOfSpaces; i++) {
                    vLineSpaces[i] = new CarSpace(row + i, column);
                }
            } else {
                System.out.println("Spaces are not ordered in vertical line!");
            }
        } else {
            System.out.println("Spaces are not in vertical line!");
        }
        return vLineSpaces;
    }

    public CarSpace[] getLineSpaces(CarSpace space) {
        CarSpace[] lineSpaces = null;
        if (inRow(space)) {
            lineSpaces = getHorizontalSpaces(space);
        } else {
            lineSpaces = getVerticalSpaces(space);
        }
        return lineSpaces;
    }

    public CarSpace getFurtherHorizontalSpace(int number) {
        return new CarSpace(getRow(), getColumn() + number);
    }

    public CarSpace getFurtherVerticalSpace(int number) {
        return new CarSpace(getRow() + number, getColumn());
    }
}
