package org.dcm4chee.xero.template;

import java.util.HashMap;
import java.util.Map;
import org.dcm4chee.xero.metadata.MetaDataBean;
import org.dcm4chee.xero.metadata.StaticMetaData;
import org.dcm4chee.xero.metadata.access.MapWithDefaults;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.testng.annotations.Test;

/** Tests that the event model filter gets created as expected, with the correct values from the metadata */
public class EventModelFilterTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testEventModelFilter() {
        MetaDataBean root = StaticMetaData.getMetaData("util-test.metadata");
        MetaDataBean mdb = root.get("model");
        Filter filter = (Filter) mdb.getValue();
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> model = (Map<String, Object>) filter.filter(null, params);
        ;
        assert model != null;
        assert model instanceof MapWithDefaults;
        assert model.get("view").equals("simpleMacro");
    }
}
