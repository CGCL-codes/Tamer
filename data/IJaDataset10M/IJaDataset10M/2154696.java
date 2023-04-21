package org.subrecord.repo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.MVEL;
import org.mvel2.integration.PropertyHandler;
import org.mvel2.integration.PropertyHandlerFactory;
import org.mvel2.integration.VariableResolverFactory;
import org.subrecord.model.EventRecord;
import org.subrecord.model.LogRecord;
import org.subrecord.model.MetricsRecord;
import org.subrecord.model.Record;

/**
 * @author przemek
 * 
 */
public class MvelSupport {

    protected static final Log LOG = LogFactory.getLog(MvelSupport.class);

    private Map<String, Object> context;

    private String domain;

    public MvelSupport(String domain, Collection<Record> records) {
        this.domain = domain;
        context = new HashMap<String, Object>();
        context.put("records", records);
        PropertyHandler recordHandler = new PropertyHandler() {

            @Override
            public Object getProperty(String property, Object object, VariableResolverFactory arg2) {
                Object value = ((Record) object).getProperties().get(property);
                return value != null ? value : "";
            }

            @Override
            public Object setProperty(String arg0, Object arg1, VariableResolverFactory arg2, Object arg3) {
                return null;
            }
        };
        PropertyHandlerFactory.registerPropertyHandler(Record.class, recordHandler);
        PropertyHandlerFactory.registerPropertyHandler(LogRecord.class, recordHandler);
        PropertyHandlerFactory.registerPropertyHandler(EventRecord.class, recordHandler);
        PropertyHandlerFactory.registerPropertyHandler(MetricsRecord.class, recordHandler);
    }

    public Object eval(String expression) {
        String mvelExp = "($ in records if (" + expression + "));";
        LOG.debug("mvel exp=" + mvelExp);
        return MVEL.eval(mvelExp, context);
    }
}
