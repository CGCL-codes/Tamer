package org.opensaas.jaudit;

import java.beans.PropertyDescriptor;
import org.opensaas.jaudit.test.ObjectFactory;

/**
 * Tests {@link ConsumptionAuditEventVO}.
 */
public class ConsumptionAuditEventVOTest extends AuditEventVOTest<ConsumptionAuditEventVO> {

    static final ObjectFactory<ConsumptionAuditEventVO> FACTORY = newFactory(ConsumptionAuditEventVO.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectFactory<ConsumptionAuditEventVO> getObjectFactory() {
        return FACTORY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[] getTestValues(final PropertyDescriptor pd) {
        if (pd.getName().equals("scale")) {
            return new Object[] { 0, Integer.MAX_VALUE, null };
        }
        return super.getTestValues(pd);
    }
}
