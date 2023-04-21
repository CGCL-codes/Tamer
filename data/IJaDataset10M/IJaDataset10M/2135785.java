package org.dasein.util.uom.storage;

import javax.annotation.Nonnull;
import java.text.ChoiceFormat;
import java.text.MessageFormat;

public class Kilobyte extends StorageUnit {

    public Kilobyte() {
    }

    @Override
    public double getBaseUnitConversion() {
        return 1024.0;
    }

    @Nonnull
    @Override
    public String format(@Nonnull Number quantity) {
        MessageFormat fmt = new MessageFormat("{0}");
        fmt.setFormatByArgumentIndex(0, new ChoiceFormat(new double[] { 0, 1, 2 }, new String[] { "0 kilobytes", "1 kilobyte", "{0,number} kilobytes" }));
        return fmt.format(new Object[] { quantity });
    }
}
