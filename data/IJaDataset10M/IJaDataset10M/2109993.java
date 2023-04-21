package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.List;

public class ListAggregation implements IAggregation {

    int m_sum = 0;

    @Override
    public void collect(Object value) {
        if (value instanceof List && ((List) value).size() > 0 && value != null) {
            m_sum++;
        }
    }

    @Override
    public void finish() {
    }

    @Override
    public Object getAggregate() {
        List<String> list = new ArrayList<String>();
        if (m_sum > 0) {
            String value = String.valueOf(m_sum);
            list.add(value);
        }
        return list;
    }
}
