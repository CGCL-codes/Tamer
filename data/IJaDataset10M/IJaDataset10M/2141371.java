package de.rockon.fuzzy.controller.model;

import java.text.DecimalFormat;
import de.rockon.fuzzy.controller.util.factories.IconFactory;
import de.rockon.fuzzy.exceptions.DuplicateXValueException;
import de.rockon.fuzzy.exceptions.ValueOutOfDomainException;

/**
 * A class representing a Fuzzy Point
 * 
 * A fuzzy point is contained in a  
 * @see FuzzySet 
 */
public class FuzzyPoint extends FuzzyBasicElement<FuzzySet, FuzzyPoint> {

    /** Mode of a permanent point */
    public static final int SET_POINT = 1;

    /** Mode of a temporar point which will be deleted again */
    public static final int HELP_POINT = 2;

    /** Default mode */
    private int type = FuzzyPoint.SET_POINT;

    /** X-coordinate */
    private double x;

    /** Y-coordinate */
    private double y;

    /**
	 * Constructor
	 * 
	 * @param x
	 *            The x value
	 * @param y
	 *            The y value
	 */
    public FuzzyPoint(double x, double y) {
        this.x = x;
        this.y = y;
        setName("Point");
        setContentType(FuzzyPoint.class);
        setIcon(IconFactory.ICON_POINT);
    }

    @Override
    public void add() {
        System.out.println("FuzzyPoint.add() - Sollte nie eintreten");
    }

    /**
	 * Sets the x value of the point
	 * 
	 * @param value
	 *            The new x value
	 * @throws ValueOutOfDomainException
	 * @throws DuplicateXValueException
	 */
    public void setX(double value) throws ValueOutOfDomainException, DuplicateXValueException {
        FuzzyVariable var = (FuzzyVariable) getPredecessor(FuzzyVariable.class);
        if (value >= var.getDomain()[0] && value <= var.getDomain()[1]) {
            for (final FuzzyPoint p : getParent()) {
                if (p.getX() == value) {
                    throw new DuplicateXValueException();
                }
            }
            x = value;
            parent.sortContent();
            fireChangeEvent(this, value);
        } else {
            throw new ValueOutOfDomainException();
        }
    }

    /**
	 * Sets the y value of the point
	 * 
	 * @param value
	 *            y value
	 * @throws ValueOutOfDomainException
	 */
    public void setY(double value) throws ValueOutOfDomainException {
        if (value >= 0 && value <= 1) {
            y = value;
            fireChangeEvent(this, value);
        } else {
            throw new ValueOutOfDomainException();
        }
    }

    /**
	 * Returns the x value of the point
	 * 
	 * @return x value
	 */
    public double getX() {
        return x;
    }

    /**
	 * Returns the y value of the point
	 * 
	 * @return y value
	 */
    public double getY() {
        return y;
    }

    @Override
    public int compareTo(FuzzyBasicElement<?, ?> o) {
        if (o instanceof FuzzyPoint) {
            if (getX() > ((FuzzyPoint) o).getX()) {
                return 1;
            } else if (getX() < ((FuzzyPoint) o).getX()) {
                return -1;
            } else {
                return hashCode() - o.hashCode();
            }
        }
        return 0;
    }

    /**
	 * Called when the value of a point got changed
	 * 
	 * @deprecated not in use anymore
	 */
    @Deprecated
    @Override
    public void editValue(Object newValue) {
        if (newValue == null) {
            return;
        }
        String tmp = (String) newValue;
        tmp = tmp.replaceAll("\\(|\\)", "");
        tmp = tmp.replaceAll("\\[|\\]", "");
        tmp = tmp.replaceAll("\\{|\\}", "");
        tmp = tmp.replaceAll("[a-zA-Z]", "");
        tmp = tmp.replaceAll("\\s", "");
        String[] partNumbers = tmp.split(",");
        try {
            setX(Double.parseDouble(partNumbers[0]));
            setY(Double.parseDouble(partNumbers[1]));
            fireChangeEvent(this, newValue);
            getParent().sortContent();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ValueOutOfDomainException e) {
            e.printStackTrace();
        } catch (DuplicateXValueException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        FuzzyPoint p = (FuzzyPoint) obj;
        if (getX() != p.getX()) {
            return false;
        }
        if (getY() != p.getY()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
	 * Returns the mode of the element
	 * 
	 * @return mode 
	 */
    public int getType() {
        return type;
    }

    /**
	 * Sets the type of the point
	 * 
	 * @param type
	 * 			The type
	 */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getX() + "," + getY();
    }

    /**
	 * Dump the content of the point as string representation 
	 * 
	 * @return content information
	 */
    public String dump() {
        final DecimalFormat df = new DecimalFormat("#0.00");
        final String x = df.format(getX()).replace(",", ".");
        final String y = df.format(getY()).replace(",", ".");
        return "(" + x + " , " + y + ")";
    }
}
