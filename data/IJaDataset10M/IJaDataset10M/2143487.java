package mipt.math.fuzzy.discretizer;

import mipt.math.Number;
import mipt.math.fuzzy.ExactNumber;
import mipt.math.fuzzy.FuzzyNumber;
import mipt.math.fuzzy.IntervalNumber;

/**
 * @author korotkov
 * Transform interval of IntervalNumber to array of numbers in this interval
 */
public class IntervalNumberDiscretizer implements FuzzyNumberDiscretizer {

    private int pointsCount = 2;

    public Number[] transform(FuzzyNumber number) {
        return transform((IntervalNumber) number);
    }

    public Number[] transform(IntervalNumber number) {
        ExactNumber[] numbers = new ExactNumber[pointsCount];
        double left = number.getLeftValue();
        double right = number.getRightValue();
        double step = (right - left) / (pointsCount - 1);
        for (int i = 1; i < pointsCount - 1; i++) {
            numbers[i] = new ExactNumber(left + step * i);
        }
        numbers[0] = new ExactNumber(left);
        numbers[pointsCount - 1] = new ExactNumber(right);
        return numbers;
    }

    public Number[] transform(IntervalNumber number, int pointsCount) {
        setPointsCount(pointsCount);
        return transform(number);
    }

    public final void setPointsCount(int count) {
        if (count < 2) throw new IllegalArgumentException("count of points must be greater or equal 2");
        this.pointsCount = count;
    }

    public final int getPointsCount() {
        return pointsCount;
    }

    public Number getElement(IntervalNumber number, int pointsCount, int index) {
        setPointsCount(pointsCount);
        return getElement(number, index);
    }

    public Number getElement(FuzzyNumber number, int index) {
        return getElement((IntervalNumber) number, index);
    }

    public Number getElement(IntervalNumber number, int index) {
        if (index > pointsCount - 1 || index < 0) throw new IndexOutOfBoundsException();
        double left = number.getLeftValue();
        double right = number.getRightValue();
        if (left == right) {
            if (index > 0) return null;
            return new ExactNumber(left);
        }
        return new ExactNumber(left + (right - left) / (pointsCount - 1) * index);
    }
}
