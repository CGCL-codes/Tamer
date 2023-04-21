package dataStructures.agent;

/**
 * Universal joint is a joint that has 2 axis in 2 different directions and can
 * rotate around them. The UniversalJoint class contains information about an
 * universal joint. This information includes "name" that is name of the joint,
 * "angle" that is angle between two edges around the joint in first direction.
 * Also the UniversalJoint has an "extraAngle" attribute, that is angle between
 * two edges around the joint in second direction. We saw that UniversalJoint
 * attributes includes HingeJoint attributes. Then it can be inherited from
 * HingeJoint. We have 8 hinge joints in the agent body. The complete
 * description of the joints is existed in /doc folder. Also they are listed in
 * the following: \li lae1_2 : left shoulder \li rae1_2 : right shoulder \li
 * lle2_3 : left leg \li rle2_3: right leg \li lle5_6 : left talus \li rle5_6 :
 * right talus
 * @modelguid {A95F12FD-51AB-4BD1-BF80-2ABE52CB2AF3}
 */
public class UniversalJoint extends HingeJoint {

    /**
	 * The angle between two edges around the joint in second direction.
	 * @modelguid {1ADFC410-DC7F-4FED-A47D-C7E7C9ABD41B}
	 */
    private double extraAxis;

    /**
	 * @return Returns the extraAxis.
	 * @modelguid {6DC11375-094F-4454-A61C-F6D293263629}
	 */
    public double getExtraAxis() {
        return extraAxis;
    }

    /**
	 * @param extraAxis
	 *            The extraAxis to set.
	 * @modelguid {827D419B-B6F3-4FDF-9080-DC32D6DE30DF}
	 */
    public void setExtraAxis(double extraAxis) {
        this.extraAxis = extraAxis;
    }
}
