package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import java.util.List;

/**
 * Class representing path answers of the left and right side of the functional 
 * dependency.
 * @author sviro
 */
public class SideAnswers {

    private List<PathAnswer> leftside = null;

    private List<PathAnswer> rightside = null;

    /**
   * Set path answers of the left side of the functional dependency.
   * @param leftside Path answers for the left side.
   */
    public void setLeftside(final List<PathAnswer> leftside) {
        this.leftside = leftside;
    }

    /**
   * Set path answers of the rights side of the functional dependency.
   * @param rightside Path answers for the right side.
   */
    public void setRightside(final List<PathAnswer> rightside) {
        this.rightside = rightside;
    }

    /**
   * Get path answers for the left side.
   * @return Path answers for the left side. 
   */
    public List<PathAnswer> getLeftside() {
        return leftside;
    }

    /**
   * Get path answers for the right side.
   * @return Path answers for the right side. 
   */
    public List<PathAnswer> getRightside() {
        return rightside;
    }

    /**
   * Get path answers of some side. If left flag is true, it get answers for the 
   * left side, otherwise for the right side
   * @param left flag indicating if the left answer will be returned.
   * @return Path answers of particular side.
   */
    public List<PathAnswer> getSide(final boolean left) {
        if (left) {
            return getLeftside();
        } else {
            return getRightside();
        }
    }

    /**
   * Check if have answers for the left side.
   * @return true if have answers for the left side.
   */
    public boolean hasLeft() {
        return leftside != null;
    }

    /**
   * Check if have answers for the right side.
   * @return true if have answers for the right side.
   */
    public boolean hasRight() {
        return rightside != null;
    }

    /**
   * Set answers of particular side. If isLeft is true, left side is set, otherwise
   * right side is set.
   * @param fDSidePathAnswers answers to be set.
   * @param isLeft flag indicating if the left side will be set.
   */
    public void setSide(final List<PathAnswer> fDSidePathAnswers, final boolean isLeft) {
        if (isLeft) {
            setLeftside(fDSidePathAnswers);
        } else {
            setRightside(fDSidePathAnswers);
        }
    }
}
