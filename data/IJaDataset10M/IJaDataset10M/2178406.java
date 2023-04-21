package net.sourceforge.nattable.data.pricing.valuegenerator;

import java.text.DecimalFormat;
import java.util.Random;
import net.sourceforge.nattable.data.valuegenerator.UniqueLongValueGenerator;

public class IsinValueGenerator extends UniqueLongValueGenerator {

    private DecimalFormat format = new DecimalFormat("0000000000");

    private String[] prefixes = new String[] { "DE", "FR", "IT", "XS" };

    @Override
    public Object newValue(Random random) {
        return prefixes[random.nextInt(prefixes.length)] + format.format(super.newValue(random));
    }
}
