package com.amazon.carbonado.stored;

import com.amazon.carbonado.Storable;
import com.amazon.carbonado.PrimaryKey;

/**
 * StorableTestBasic
 *
 * @author Don Schneider
 */
@PrimaryKey("id")
public interface StorableTestMinimal extends Storable {

    int getId();

    void setId(int id);
}
