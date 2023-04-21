package net.sf.beanlib.spi.replicator;

import java.util.Date;
import net.sf.beanlib.spi.BeanTransformerSpi;

/**
 * Date Replicator SPI.
 * 
 * @author Joe D. Velopar
 */
public interface DateReplicatorSpi {

    /**
     * Date Replicator Factory SPI.
     * 
     * @author Joe D. Velopar
     */
    public static interface Factory {

        DateReplicatorSpi newDateReplicatable(BeanTransformerSpi beanTransformer);
    }

    /** 
     * Returns a replicated date. 
     * 
     * @param <T> target class type
     * @param fromDate from date
     * @param toClass target class
     */
    public <T> T replicateDate(Date fromDate, Class<T> toClass);
}
