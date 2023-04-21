package ch.kwa.ee.model.transkription;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Foil elements are elements that can not contain other elements and that can
 * be spanned by spanning elements. Characters and Milestones are FoilElements.
 * 
 * @author Matthias Sprünglin
 * 
 */
public class FoilElement extends TranskriptionModelElement {

    List<SpanningElement> spanningElements = new ArrayList<SpanningElement>();

    private boolean isMilestone;

    Reference reference = null;

    public List<? extends SpanningElement> getSpanningElements() {
        return spanningElements;
    }

    public void addSpanningElement(SpanningElement spanning) {
        spanningElements.add(spanning);
    }

    public void removeSpanningElement(SpanningElement spanning) {
        spanningElements.remove(spanning);
    }

    public void setSpanningElements(List<SpanningElement> spanningElement) {
        this.spanningElements = spanningElement;
    }

    @Override
    public FoilElement getFirstFoil() {
        return this;
    }

    @Override
    public FoilElement getLastFoil() {
        return this;
    }

    @Override
    public boolean containsFoil(TranskriptionModelElement foil) {
        return this == foil;
    }

    @Override
    public boolean isAnchestorOf(TranskriptionModelElement end) {
        return false;
    }

    @Override
    public boolean isMilestone() {
        return isMilestone;
    }

    public void setMilestone(boolean isMilestone) {
        this.isMilestone = isMilestone;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }
}
