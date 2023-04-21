package ie.ul.brendancleary.forager.views;

import java.io.Serializable;
import java.util.Vector;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IJavaElement;

/**
 * Records a value associated with a document. 
 * 
 * @author Brendan.Cleary
 *
 */
public class AssignmentResult implements Serializable {

    private IJavaElement javaElement;

    private IMarker marker;

    private int start;

    private int end;

    private int frequency = 0;

    private int type;

    private boolean display;

    private Vector<AssignmentResult> childern = new Vector<AssignmentResult>();

    private AssignmentResult parent = null;

    private double lmScore = 0.0f;

    private double dlmScore = 0.0;

    private double vsmScore = 0.0;

    private double lsiScore = 0.0;

    private double kldScore = 0.0;

    private double qekldScore = 0.0;

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setMarker(IMarker marker) {
        this.marker = marker;
    }

    public IMarker getMarker() {
        return marker;
    }

    public void setDLMScore(double dlmScore) {
        this.dlmScore = dlmScore;
    }

    public double getDLMScore() {
        return dlmScore;
    }

    public void setLMScore(double lmScore) {
        this.lmScore = lmScore;
    }

    public double getLMScore() {
        return lmScore;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setJavaElement(IJavaElement javaElement) {
        this.javaElement = javaElement;
    }

    public IJavaElement getJavaElement() {
        return javaElement;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setChildern(Vector<AssignmentResult> childern) {
        this.childern = childern;
    }

    public Vector<AssignmentResult> getChildern() {
        return childern;
    }

    public void setParent(AssignmentResult parent) {
        this.parent = parent;
    }

    public AssignmentResult getParent() {
        return parent;
    }

    public void setVsmScore(double vsmScore) {
        this.vsmScore = vsmScore;
    }

    public double getVsmScore() {
        return vsmScore;
    }

    public void setLsiScore(double lsiScore) {
        this.lsiScore = lsiScore;
    }

    public double getLsiScore() {
        return lsiScore;
    }

    public void setKldScore(double kldScore) {
        this.kldScore = kldScore;
    }

    public double getKldScore() {
        return kldScore;
    }

    public void setQekldScore(double qekldScore) {
        this.qekldScore = qekldScore;
    }

    public double getQekldScore() {
        return qekldScore;
    }
}
