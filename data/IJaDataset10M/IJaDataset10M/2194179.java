package uk.ac.kingston.aqurate.api;

import uk.ac.kingston.aqurate.jaxb.imsqti_v2p1.ObjectFactory;
import uk.ac.kingston.aqurate.jaxb.imsqti_v2p1.SliderInteraction;

public class AqurateSliderInteraction extends AqurateInteraction {

    private SliderInteraction sliderInteraction;

    public AqurateSliderInteraction(ObjectFactory objectFactory) {
        super(objectFactory);
        sliderInteraction = objectFactory.createSliderInteraction();
        sliderInteraction.setResponseIdentifier("RESPONSE");
        sQ = "SliderInteraction";
    }

    @Override
    public String getId() {
        return sliderInteraction.getId();
    }

    @Override
    Object getInteraction() {
        return sliderInteraction;
    }

    public double getLowerBound() {
        return sliderInteraction.getLowerBound();
    }

    @Override
    public String getQ() {
        return sQ;
    }

    @Override
    public String getResponseIdentifier() {
        return sliderInteraction.getResponseIdentifier();
    }

    @Override
    public boolean getShuffle() {
        return false;
    }

    public int getStep() {
        return sliderInteraction.getStep();
    }

    public double getUpperBound() {
        return sliderInteraction.getUpperBound();
    }

    @Override
    void init(Object sliderInteractionIn) {
        sliderInteraction = (SliderInteraction) sliderInteractionIn;
        prompt = sliderInteraction.getPrompt();
    }

    @Override
    public void setId(String value) {
        sliderInteraction.setId(value);
    }

    public void setLowerBound(double lowerBound) {
        sliderInteraction.setLowerBound(lowerBound);
    }

    @Override
    public void setPrompt(String sPrompt) {
        super.setPrompt(sPrompt);
        sliderInteraction.setPrompt(prompt);
    }

    @Override
    public void setQ(String sQ) {
        this.sQ = sQ;
    }

    @Override
    public void setResponseIdentifier(String sValue) {
        sliderInteraction.setResponseIdentifier(sValue);
    }

    @Override
    public void setShuffle(boolean value) {
    }

    public void setStep(int step) {
        sliderInteraction.setStep(step);
    }

    public void setUpperBound(double upperBound) {
        sliderInteraction.setUpperBound(upperBound);
    }
}
