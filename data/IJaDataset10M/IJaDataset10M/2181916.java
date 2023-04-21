package gov.ca.modeling.timeseries.map.server.data.persistence;

import gov.ca.modeling.timeseries.map.shared.data.TimeSeriesData;
import gov.ca.modeling.timeseries.map.shared.data.TimeSeriesReferenceData;
import java.util.List;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class TestTimeSeriesReferenceDataDAOImpl extends BaseDatastoreTestCase {

    public TimeSeriesData saveTimeSeries(TimeSeriesDataDAO dao, double[] values, int checkNumberAfterAdd) {
        TimeSeriesData ts1 = new TimeSeriesData();
        ts1.setInterval("15MIN");
        ts1.setStartTime("01JAN1980 0100");
        ts1.setValues(values);
        return dao.createObject(ts1);
    }

    protected void checkNumber(int number) {
        Objectify ofy = ObjectifyService.begin();
        assertEquals(number, ofy.query(TimeSeriesData.class).countAll());
    }

    public void testCreate() {
        TimeSeriesDataDAO dao = new TimeSeriesDataDAOImpl();
        double[] values = new double[] { 1, 3, 5, 7, 9, 11 };
        TimeSeriesData tsd1 = saveTimeSeries(dao, values, 1);
        checkNumber(1);
        double[] values2 = new double[] { 1, 3, 5, 7, 9, 11 };
        TimeSeriesData tsd2 = saveTimeSeries(dao, values2, 2);
        checkNumber(2);
        TimeSeriesReferenceData tsref1 = new TimeSeriesReferenceData();
        tsref1.setSource("source1");
        tsref1.setInterval("1DAY");
        tsref1.setLocation("location1");
        tsref1.setGroup("group1");
        tsref1.setName("name1");
        tsref1.setTimeSeriesId(new Key<TimeSeriesData>(TimeSeriesData.class, tsd1.getId()));
        tsref1.setTimeWindow("01JAN1990 0000 - 05FEB1991 2400");
        tsref1.setType("type1");
        tsref1.setUnits("units1");
        TimeSeriesReferenceDataDAO refDao = new TimeSeriesReferenceDataDAOImpl();
        TimeSeriesReferenceData tsref11 = refDao.createObject(tsref1);
        assertNotNull(tsref11);
        assertFalse(new Long(0).equals(tsref11.getId()));
        List<TimeSeriesReferenceData> list = refDao.findBySourceAndLocationAndType("source1", "location1", "type1");
        assertNotNull(list);
        assertEquals(1, list.size());
        TimeSeriesReferenceData tsref3 = list.get(0);
        assertNotNull(tsref3);
        assertEquals(tsref1.getId(), tsref3.getId());
        Key<TimeSeriesData> tsdKey3 = tsref3.getTimeSeriesId();
        assertEquals(tsd1.getId(), tsdKey3.getId());
        TimeSeriesData tsd3 = dao.findObjectById(TimeSeriesData.class, tsdKey3.getId());
        assertEquals(tsd1.getInterval(), tsd3.getInterval());
    }
}
