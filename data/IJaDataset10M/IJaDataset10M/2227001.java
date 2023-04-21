package com.idna.batchid.service.record;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.*;
import com.idna.batchid.model.RecordState;
import com.idna.batchid.model.RecordStatus;
import com.idna.batchid.model.RecordRequest;
import com.idna.batchid.service.record.RecordCleanserAction;
import com.idna.batchid.service.record.RecordCleanserMinimumRequirementsAction;

public class RecordCleanserMinimumRequirementsActionTest {

    private RecordCleanserMinimumRequirementsAction _action = null;

    private RecordCleanserAction getAction() {
        return _action;
    }

    @Before
    public void setUp() throws Exception {
        _action = new RecordCleanserMinimumRequirementsAction();
    }

    @After
    public void tearDown() throws Exception {
        _action = null;
    }

    @Test
    public void testGoodData() {
        List<String> columns = Arrays.asList("forename", "surname", "postcode", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "Jack");
        data.put("surname", "Jackson");
        data.put("postcode", "VTTVTT");
        data.put("premise", "221B");
        data.put("countrycode", "GBR");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record failed to meet minimum field requirements!", (record.getStatus().getState() == RecordState.GOOD));
    }

    @Test
    public void testBadData() {
        List<String> columns = Arrays.asList("forename", "surname", "dateofbirth", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "Jack");
        data.put("surname", "Jackson");
        data.put("dateofbirth", "01/01/80");
        data.put("premise", "221B");
        data.put("countrycode", "GBR");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record did NOT fail to meet minimum field requirements as it should have done!", (record.getStatus().getState() == RecordState.BAD_DATA));
    }

    @Test
    public void testEmptyForename() {
        List<String> columns = Arrays.asList("forename", "surname", "postcode", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "");
        data.put("surname", "Jackson");
        data.put("postcode", "VTTVTT");
        data.put("premise", "221B");
        data.put("countrycode", "GBR");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record did NOT fail to meet minimum field requirements as it should have done!", (record.getStatus().getState() == RecordState.BAD_DATA));
    }

    @Test
    public void testEmptySurname() {
        List<String> columns = Arrays.asList("forename", "surname", "postcode", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "Jack");
        data.put("surname", "");
        data.put("postcode", "VTTVTT");
        data.put("premise", "221B");
        data.put("countrycode", "GBR");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record did NOT fail to meet minimum field requirements as it should have done!", (record.getStatus().getState() == RecordState.BAD_DATA));
    }

    @Test
    public void testEmptyPostcode() {
        List<String> columns = Arrays.asList("forename", "surname", "postcode", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "Jack");
        data.put("surname", "Jackson");
        data.put("postcode", "");
        data.put("premise", "221B");
        data.put("countrycode", "GBR");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record did NOT fail to meet minimum field requirements as it should have done!", (record.getStatus().getState() == RecordState.BAD_DATA));
    }

    @Test
    public void testEmptyPremise() {
        List<String> columns = Arrays.asList("forename", "surname", "postcode", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "Jack");
        data.put("surname", "Jackson");
        data.put("postcode", "VTTVTT");
        data.put("premise", "");
        data.put("countrycode", "GBR");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record did NOT fail to meet minimum field requirements as it should have done!", (record.getStatus().getState() == RecordState.BAD_DATA));
    }

    @Test
    public void testEmptyCountryCode() {
        List<String> columns = Arrays.asList("forename", "surname", "postcode", "premise", "countrycode");
        Map<String, String> data = new HashMap<String, String>();
        RecordRequest record = new RecordRequest();
        data.put("forename", "Jack");
        data.put("surname", "Jackson");
        data.put("postcode", "VTTVTT");
        data.put("premise", "221B");
        data.put("countrycode", "");
        record.setHeaderFields(columns);
        record.setProductFields(data);
        this.getAction().cleanseRecord(record);
        assertTrue("The record did NOT fail to meet minimum field requirements as it should have done!", (record.getStatus().getState() == RecordState.BAD_DATA));
    }
}
