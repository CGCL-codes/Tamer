package org.processmining.framework.models.hlprocess.distribution;

import java.io.IOException;
import java.io.Writer;

/**
 * Represents a student distribution.
 * 
 * @see HLStudentDistributionGui
 * 
 * @author arozinat
 * @author rmans
 */
public class HLStudentDistribution extends HLDistribution {

    private int myDegreesFreedom;

    /**
	 * Creates a chi square distribution based on a degrees of freedom value
	 * 
	 * @param degreesFreedom
	 *            double the degrees of freedom value. Not that this value
	 *            always has to be equal or greater to 1.
	 */
    public HLStudentDistribution(int degreesFreedom) {
        myDegreesFreedom = degreesFreedom;
    }

    /**
	 * Retrieves the degrees of freedom for this distribution
	 * 
	 * @return double the degrees of freedom
	 */
    public int getDegreesFreedom() {
        return myDegreesFreedom;
    }

    /**
	 * Sest the degrees of freedom for this distribution
	 * 
	 * @param value
	 *            the degrees of freedom
	 */
    public void setDegreesFreedom(int value) {
        myDegreesFreedom = value;
    }

    public DistributionEnum getDistributionType() {
        return HLDistribution.DistributionEnum.STUDENT_DISTRIBUTION;
    }

    public String toString() {
        return "Student";
    }

    public boolean equals(Object obj) {
        return (obj instanceof HLStudentDistribution) && (this.getDegreesFreedom() == ((HLStudentDistribution) obj).getDegreesFreedom());
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getDegreesFreedom();
        return result;
    }

    public void writeDistributionToDot(String boxId, String nodeId, String addText, Writer bw) throws IOException {
        String label = "";
        label = label + addText + "\\n";
        label = label + "Student Distribution\\n";
        label = label + "degrees freedom=" + myDegreesFreedom + "\\n";
        bw.write(boxId + " [shape=\"ellipse\", label=\"" + label + "\"];\n");
        if (!nodeId.equals("")) {
            bw.write(nodeId + " -> " + boxId + " [dir=none, style=dotted];\n");
        }
    }

    public void setTimeMultiplicationValue(double value) {
    }

    public boolean checkValuesOfTimeParameters(String info) {
        return false;
    }
}
