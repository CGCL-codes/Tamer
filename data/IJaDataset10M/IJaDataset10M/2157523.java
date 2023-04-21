package org.sourceforge.kga.rules;

import org.sourceforge.kga.Family;
import org.sourceforge.kga.Hint;
import org.sourceforge.kga.Square;
import org.sourceforge.kga.translation.Translation;

/**
 * This rule gives a hint when garden is planned with incorrect crop rotation. For instance
 * legumes after cabbage.
 * @author Christian Nilsson
 *
 */
public class BadCropRotationRule extends Rule {

    private Family family;

    public BadCropRotationRule(Family family, int effect) {
        super(effect);
        this.family = family;
    }

    public BadCropRotationRule(Family family) {
        this(family, Rule.UNKNOWN);
    }

    public Hint getHint(Square square) {
        Translation t = Translation.getPreferred();
        String text = t.translate("badcroprotation") + " " + t.translate(getHost()) + " " + t.translate("after") + " " + t.translate(family);
        for (Square s : square.getPreviousSquares(Rule.ONE_YEAR_BACK)) if (s.containsFamily(family)) return new Hint(this, s, square, text, BAD);
        return null;
    }

    public Family getFamily() {
        return family;
    }

    @Override
    public String getImageName() {
        return "bad.png";
    }

    @Override
    public String toString() {
        return super.toString() + " family: " + Translation.getPreferred().translate(family);
    }
}
