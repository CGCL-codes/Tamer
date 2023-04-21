package test.gen.tables;

import net.jalisq.types.*;
import test.gen.schema.*;

/**
DO NOT MODIFY!
THIS WILL BE OVERWRITTEN!
**/
public class monitoring {

    public static final PhysicalColumn date = new PhysicalColumn("date", Integer.class, systemdb_prod.monitoring);

    public static final PhysicalColumn time = new PhysicalColumn("time", Integer.class, systemdb_prod.monitoring);

    public static final PhysicalColumn done = new PhysicalColumn("done", Integer.class, systemdb_prod.monitoring);

    public static final PhysicalColumn updated = new PhysicalColumn("updated", Integer.class, systemdb_prod.monitoring);

    public static final PhysicalColumn errored = new PhysicalColumn("errored", Integer.class, systemdb_prod.monitoring);
}
