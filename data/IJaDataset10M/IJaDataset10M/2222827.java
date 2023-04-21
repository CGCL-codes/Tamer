package jp.seraph.same.model;

import java.util.Set;
import jp.seraph.jsade.model.AngleVelocityCalculator;
import jp.seraph.jsade.model.JointIdentifier;
import jp.seraph.jsmf.motion.MotionElement;

public interface MotionElementModel extends ViewModel {

    public void setBasePose(String aPose);

    public String getBasePose();

    public String getName();

    public void setName(String aName);

    public String getConditionElementName();

    public void setConditionElementName(String aConditionElementName);

    public int getTiming();

    public void setTiming(int aTiming);

    public void selectJoint(JointIdentifier aId) throws IllegalArgumentException;

    public void deselectJoint(JointIdentifier aId) throws IllegalArgumentException;

    public Set<JointIdentifier> getSelectedJoints();

    public void setCalculator(AngleVelocityCalculator aCalculator);

    public AngleVelocityCalculator getCalculator();

    public MotionElement toElement();
}
