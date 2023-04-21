package apollo.gui.event;

import java.util.EventObject;
import apollo.datamodel.*;

/**
 * A controller managed event class which signals when a change is made to
 * a set of annotations and what type of change occurred. Objects interested
 * in listening for these event should implement the FeatureChangeListener
 * interface and register with the controller.
 */
public class AnnotationChangeEvent extends FeatureChangeEvent {

    public static final int COMMENT = 10;

    public static final int EVIDENCE = 11;

    public static final int TRANSLATION = 12;

    public static final int EXON = 13;

    public static final int TRANSCRIPT = 14;

    public static final int GENE = 15;

    protected SeqFeatureI[] features = new SeqFeatureI[2];

    /**
   *
   */
    public AnnotationChangeEvent(Object source, SeqFeatureI feature1, SeqFeatureI feature2, SeqFeatureI changeTop, int type, int subtype) {
        super(source, changeTop, type, subtype);
        this.features[0] = feature1;
        this.features[1] = feature2;
    }

    public SeqFeatureI getFeature() {
        return features[0];
    }

    public SeqFeatureI getAnnotation() {
        return changeTop;
    }

    public SeqFeatureI[] getFeatures() {
        return features;
    }

    public SeqFeatureI getSingleFeature() {
        return features[0];
    }

    public String getSubTypeAsString() {
        switch(changeSubType) {
            case COMMENT:
                return new String("COMMENT");
            case EVIDENCE:
                return new String("EVIDENCE");
            case EXON:
                return new String("EXON");
            case TRANSLATION:
                return new String("TRANSLATION");
            case TRANSCRIPT:
                return new String("TRANSCRIPT");
            case GENE:
                return new String("GENE");
            default:
                return new String("!!!UNKNOWN!!!");
        }
    }
}
