package net.sf.beatrix.core.event;

import net.sf.beatrix.core.module.analyzer.Analyzer;
import net.sf.beatrix.core.module.analyzer.Classification;
import net.sf.beatrix.util.event.SimpleEvent;

/**
 * Represents a simple even for {@link Classification}s.
 * 
 * @author Christian Wressnegger <chwress@users.sourceforge.net>
 */
public class ClassificationEvent extends SimpleEvent<Analyzer> {

    /** The serial version UID. */
    private static final long serialVersionUID = -2948401804966536738L;

    /** The classification. */
    private Classification classification;

    /**
   * The constructor.
   * 
   * @param source
   *          The {@link Analyzer} which came up with the {@link Classification}.
   * @param c
   *          The classification.
   */
    public ClassificationEvent(Analyzer source, Classification c) {
        super(source);
        this.classification = c;
    }

    /**
   * @return The {@link Classification}.
   */
    public Classification getClassification() {
        return classification;
    }
}
