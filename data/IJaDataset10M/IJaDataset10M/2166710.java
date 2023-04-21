package nl.utwente.ewi.hmi.deira.tgm;

public class EmotionalBoundPair {

    private final String emotionName;

    private float lowerBound;

    private float upperBound;

    public EmotionalBoundPair(String emotionName, float lowerBound, float upperBound) {
        super();
        this.emotionName = emotionName;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public EmotionalBoundPair(String boundsstring) {
        emotionName = boundsstring.substring(0, boundsstring.indexOf('='));
        lowerBound = Float.parseFloat(boundsstring.substring(boundsstring.indexOf('=') + 1, boundsstring.indexOf('-')));
        upperBound = Float.parseFloat(boundsstring.substring(boundsstring.indexOf('-') + 1));
    }

    public String getEmotionName() {
        return emotionName;
    }

    public float getLowerBound() {
        return lowerBound;
    }

    public float getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(float newBound) {
        lowerBound = newBound;
    }

    public void setUpperBound(float newBound) {
        upperBound = newBound;
    }

    public float getCenter() {
        float center = lowerBound + (upperBound - lowerBound) / 2;
        return center;
    }

    public boolean equals(Object other) {
        boolean match = true;
        if (other instanceof EmotionalBoundPair) {
            EmotionalBoundPair otherPair = (EmotionalBoundPair) other;
            if (!emotionName.equals(otherPair.getEmotionName())) {
                return false;
            }
            if (lowerBound != otherPair.getLowerBound() || upperBound != otherPair.getUpperBound()) {
                return false;
            }
        } else return false;
        return match;
    }
}
