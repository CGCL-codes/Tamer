package org.dasein.util.uom.storage;

import org.dasein.util.uom.Measured;
import org.dasein.util.uom.UnitOfMeasure;
import org.dasein.util.uom.UnknownUnitOfMeasure;
import javax.annotation.Nonnull;

public abstract class StorageUnit extends UnitOfMeasure {

    @Nonnull
    public static StorageUnit valueOf(@Nonnull String uom) {
        if (uom.length() < 1 || uom.equals("bit") || uom.equals("bits") || uom.equals("b")) {
            return Storage.BIT;
        } else if (uom.equals("byte") || uom.equals("bytes")) {
            return Storage.BYTE;
        } else if (uom.equals("kb") || uom.equals("kilobyte") || uom.equals("kilobytes") || uom.equals("kbyte") || uom.equals("kbytes")) {
            return Storage.KILOBYTE;
        } else if (uom.equals("mb") || uom.equals("megabyte") || uom.equals("megabytes") || uom.equals("mbyte") || uom.equals("mbytes")) {
            return Storage.MEGABYTE;
        } else if (uom.equals("gb") || uom.equals("gigabyte") || uom.equals("gigabytes") || uom.equals("gbyte") || uom.equals("gbytes")) {
            return Storage.GIGABYTE;
        } else if (uom.equals("tb") || uom.equals("terabyte") || uom.equals("terabytes") || uom.equals("tbyte") || uom.equals("tbytes")) {
            return Storage.TERABYTE;
        }
        throw new UnknownUnitOfMeasure(uom);
    }

    @Nonnull
    @Override
    public Class<StorageUnit> getRootUnitOfMeasure() {
        return StorageUnit.class;
    }

    @Nonnull
    @Override
    public UnitOfMeasure getBaseUnit() {
        return Storage.BYTE;
    }

    @Nonnull
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <B extends UnitOfMeasure, U extends B> Measured<B, U> newQuantity(@Nonnull Number quantity) {
        return new Storage(quantity, this);
    }
}
