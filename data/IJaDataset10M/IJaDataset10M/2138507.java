package org.ct42.r42.animation;

import java.util.Collections;
import java.util.List;
import org.ct42.r42.structure.Container;

/**
 * 
 * 
 * @author cthiele
 */
public class KeyPointTransformModifier extends TransformModifier implements Modifier {

    private List<DoubleValueKeyPoint> moveXKeyPoints;

    private List<DoubleValueKeyPoint> moveYKeyPoints;

    private List<DoubleValueKeyPoint> moveZKeyPoints;

    private List<DoubleValueKeyPoint> rotXKeyPoints;

    private List<DoubleValueKeyPoint> rotYKeyPoints;

    private List<DoubleValueKeyPoint> rotZKeyPoints;

    /**
	 * Default ctor for castor. 
	 *
	 */
    public KeyPointTransformModifier() {
    }

    /**
	 * @see org.ct42.r42.animation.Modifier#modify(double)
	 */
    public void modify(double timepoint, Container target) {
        this.moveX = calculateAnimatedValue(timepoint, target.getMoveX(), this.moveXKeyPoints);
        this.moveY = calculateAnimatedValue(timepoint, target.getMoveY(), this.moveYKeyPoints);
        this.moveZ = calculateAnimatedValue(timepoint, target.getMoveZ(), this.moveZKeyPoints);
        this.rotX = calculateAnimatedValue(timepoint, target.getRotX(), this.rotXKeyPoints);
        this.rotY = calculateAnimatedValue(timepoint, target.getRotY(), this.rotYKeyPoints);
        this.rotZ = calculateAnimatedValue(timepoint, target.getRotZ(), this.rotZKeyPoints);
    }

    private static double calculateAnimatedValue(double timepoint, double defaultvalue, List<DoubleValueKeyPoint> keypoints) {
        if (keypoints == null || keypoints.isEmpty()) {
            return defaultvalue;
        } else {
            double beforeValue = defaultvalue;
            double beforeTime = 0.0;
            double afterValue = keypoints.get(keypoints.size() - 1).getValue();
            double afterTime = timepoint;
            if (keypoints.get(0).getTimepoint() > timepoint) {
                afterValue = keypoints.get(0).getValue();
                afterTime = keypoints.get(0).getTimepoint();
            } else {
                for (int i = 1; i < keypoints.size(); i++) {
                    if (keypoints.get(i).getTimepoint() > timepoint) {
                        afterValue = keypoints.get(i).getValue();
                        afterTime = keypoints.get(i).getTimepoint();
                        beforeValue = keypoints.get(i - 1).getValue();
                        beforeTime = keypoints.get(i - 1).getTimepoint();
                        break;
                    }
                }
            }
            double alpha = (timepoint - beforeTime) / (afterTime - beforeTime);
            double distance = afterValue - beforeValue;
            return beforeValue + alpha * distance;
        }
    }

    /**
	 * @return Returns the moveXKeyPoints.
	 */
    public List<DoubleValueKeyPoint> getMoveXKeyPoints() {
        return moveXKeyPoints;
    }

    /**
	 * @param moveXKeyPoints The moveXKeyPoints to set.
	 */
    public void setMoveXKeyPoints(List<DoubleValueKeyPoint> moveXKeyPoints) {
        Collections.sort(moveXKeyPoints);
        this.moveXKeyPoints = moveXKeyPoints;
    }

    /**
	 * @return Returns the moveYKeyPoints.
	 */
    public List<DoubleValueKeyPoint> getMoveYKeyPoints() {
        return moveYKeyPoints;
    }

    /**
	 * @param moveYKeyPoints The moveYKeyPoints to set.
	 */
    public void setMoveYKeyPoints(List<DoubleValueKeyPoint> moveYKeyPoints) {
        Collections.sort(moveYKeyPoints);
        this.moveYKeyPoints = moveYKeyPoints;
    }

    /**
	 * @return Returns the moveZKeyPoints.
	 */
    public List<DoubleValueKeyPoint> getMoveZKeyPoints() {
        return moveZKeyPoints;
    }

    /**
	 * @param moveZKeyPoints The moveZKeyPoints to set.
	 */
    public void setMoveZKeyPoints(List<DoubleValueKeyPoint> moveZKeyPoints) {
        Collections.sort(moveZKeyPoints);
        this.moveZKeyPoints = moveZKeyPoints;
    }

    /**
	 * @return Returns the rotXKeyPoints.
	 */
    public List<DoubleValueKeyPoint> getRotXKeyPoints() {
        return rotXKeyPoints;
    }

    /**
	 * @param rotXKeyPoints The rotXKeyPoints to set.
	 */
    public void setRotXKeyPoints(List<DoubleValueKeyPoint> rotXKeyPoints) {
        Collections.sort(rotXKeyPoints);
        this.rotXKeyPoints = rotXKeyPoints;
    }

    /**
	 * @return Returns the rotYKeyPoints.
	 */
    public List<DoubleValueKeyPoint> getRotYKeyPoints() {
        return rotYKeyPoints;
    }

    /**
	 * @param rotYKeyPoints The rotYKeyPoints to set.
	 */
    public void setRotYKeyPoints(List<DoubleValueKeyPoint> rotYKeyPoints) {
        Collections.sort(rotYKeyPoints);
        this.rotYKeyPoints = rotYKeyPoints;
    }

    /**
	 * @return Returns the rotZKeyPoints.
	 */
    public List<DoubleValueKeyPoint> getRotZKeyPoints() {
        return rotZKeyPoints;
    }

    /**
	 * @param rotZKeyPoints The rotZKeyPoints to set.
	 */
    public void setRotZKeyPoints(List<DoubleValueKeyPoint> rotZKeyPoints) {
        Collections.sort(rotZKeyPoints);
        this.rotZKeyPoints = rotZKeyPoints;
    }
}
