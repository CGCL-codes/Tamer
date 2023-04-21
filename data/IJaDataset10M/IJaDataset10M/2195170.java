package Database;

import java.util.*;
import junit.framework.*;
import org.omg.DsObservationAccess.*;
import gov.lanl.COAS.*;
import gov.lanl.COAS.ojb.*;
import gov.lanl.Database.*;
import gov.lanl.Utility.*;

/**
 * ***********************************
 * Copyright Notice
 * Copyright (c) 1999, Regents of the University of California. All rights reserved.
 *
 * DISCLAIMER
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 * ************************************
 */
public class OJBDatabaseMgrTest extends TestCase {

    DatabaseMgr dbMgr;

    PersistentObjectFactory persObjFact;

    PersistentObject[] obsData_Seq;

    static Qualifier2AttributeMapper mapper;

    static String loadFile = "TestObsData.xml";

    static String databaseType = "OJB";

    static String databaseURL = "config//repository.xml";

    static String persistentProperties = "persistent.properties";

    static String personIdCode = "DNS:omg.org/DSObservationAccess/HL72.3/PID/PatientIDInternalID";

    static String startTimeCode = "DNS:omg.org/DSObservationAccess/HL72.3/OBR/ObservationDate_Time";

    static String stopTimeCode = "DNS:omg.org/DSObservationAccess/HL72.3/OBR/ObservationEndDate_Time";

    static String signingCode = "DNS:OpenEMed.org/QualifierCode/SigningInformation";

    static String parserName = "HL7";

    static PersistentObject emptyObsData_ = null;

    ConfigProperties props;

    /**
	 * Constructor declaration
	 *
	 *
	 * @param testName
	 *
	 * @see
	 */
    public OJBDatabaseMgrTest(java.lang.String testName) {
        super(testName);
        init();
    }

    /**
	 * Method declaration
	 *
	 *
	 * @param args
	 *
	 * @see
	 */
    public static void main(java.lang.String[] args) {
        if (args.length >= 2) {
            if (args[0].equals("-repository")) {
                databaseURL = args[1];
            }
        }
        junit.textui.TestRunner.run(suite());
    }

    /**
	 * Method declaration
	 *
	 *
	 * @return
	 *
	 * @see
	 */
    public static Test suite() {
        return new TestSuite(OJBDatabaseMgrTest.class);
    }

    /**
	 * Test of retrieveElement method, of class gov.lanl.Database.OJBDatabaseMgr.
     */
    public void testRetrieveElement() {
        System.out.println();
        System.out.println("testRetrieveElement");
        PersistentObject obsData_1 = obsData_Seq[0];
        PersistentObject obsData_1r;
        try {
            String obsId = obsData_1.getObjectId();
            System.out.println("retrieving object by obsId '" + obsId + "'");
            dbMgr.txn_begin();
            obsData_1r = (PersistentObject) dbMgr.retrieveElement(emptyObsData_, "obsId", obsId);
            assertEquals("Objects should been the same obsId after retrieval", obsData_1.getObjectId(), obsData_1r.getObjectId());
            dbMgr.txn_commit();
            System.out.println("done");
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of retrieveElement method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testRetrieveElementDeep() {
        System.out.println();
        System.out.println("testRetrieveElement (deep/shallow)");
        PersistentObject obsData_1 = obsData_Seq[0];
        AbstractObservationData_ obsData_1r;
        int flag = 0;
        String flagStr = "";
        try {
            for (int i = 0; i < 2; i++) {
                switch(i) {
                    case 0:
                        {
                            flag = dbMgr.SHALLOW;
                            flagStr = "SHALLOW";
                            break;
                        }
                    case 1:
                        {
                            flag = dbMgr.DEEP;
                            flagStr = "DEEP";
                            break;
                        }
                }
                String obsId = obsData_1.getObjectId();
                System.out.println("retrieving object by obsId '" + obsId + "' (" + flagStr + ")");
                dbMgr.txn_begin();
                obsData_1r = (AbstractObservationData_) dbMgr.retrieveElement(emptyObsData_, "obsId", obsId, flag);
                assertEquals("Objects should been the same obsId after retrieval", obsData_1.getObjectId(), obsData_1r.getObjectId());
                Iterator compIterator = obsData_1r.getComposites();
                if (flag == dbMgr.DEEP) {
                    assertTrue("Composite vector is empty (" + flagStr + "-retrieval)", compIterator.hasNext() == true);
                } else {
                    assertTrue("Composite vector isn't empty (" + flagStr + "-retrieval)", compIterator.hasNext() == false);
                }
                dbMgr.txn_commit();
                System.out.println("done");
                flag = dbMgr.DEEP;
            }
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of updateElement method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testUpdateElement() {
        System.out.println();
        System.out.println("testUpdateElement");
        AbstractObservationData_ obsData_3 = (AbstractObservationData_) obsData_Seq[3];
        AbstractObservationData_ obsData_3r;
        try {
            String obsId = obsData_3.getObjectId();
            String oldCode = obsData_3.code;
            String newCode = "99999";
            System.out.println("Changing code from '" + oldCode + "' to '" + newCode + "' for observation '" + obsId + "'");
            obsData_3.code = newCode;
            dbMgr.txn_begin();
            dbMgr.updateElement(obsData_3);
            dbMgr.txn_commit();
            System.out.println("retrieving object '" + obsId + "'");
            dbMgr.txn_begin();
            obsData_3r = (AbstractObservationData_) dbMgr.retrieveElement(emptyObsData_, "obsId", obsId);
            assertEquals("Object should been the new code after retrieval", obsData_3r.code, newCode);
            dbMgr.txn_commit();
            System.out.println("done");
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of deleteElement method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testDeleteElement() {
        System.out.println();
        System.out.println("testDeleteElement");
        AbstractObservationData_ obsData_2 = (AbstractObservationData_) obsData_Seq[2];
        AbstractObservationData_ obsData_2r = null;
        try {
            String obsId = obsData_2.getObjectId();
            try {
                System.out.println("deleting '" + obsId + "'");
                dbMgr.txn_begin();
                dbMgr.deleteElement(obsData_2);
                dbMgr.txn_commit();
            } catch (Throwable e) {
                fail(e.getMessage());
            }
            try {
                System.out.println("trying to retrieve '" + obsId + "'");
                dbMgr.txn_begin();
                obsData_2r = (AbstractObservationData_) dbMgr.retrieveElement(emptyObsData_, "obsId", obsId);
                dbMgr.txn_commit();
            } catch (DBException e) {
                dbMgr.txn_abort();
            }
            assertNull("Object should be null", obsData_2r);
            System.out.println("done");
        } catch (NoObjectIdException e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of insertElement method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testInsertElement() {
        System.out.println();
        System.out.println("testInsertElement");
        AbstractObservationData_ obsData_2 = (AbstractObservationData_) obsData_Seq[2];
        AbstractObservationData_ obsData_2r = null;
        org.omg.CORBA.Any[] value = new org.omg.CORBA.Any[] { org.omg.CORBA.ORB.init().create_any() };
        value[0].insert_string("INSERTION_TEST");
        ObservationDataStruct obsData = new ObservationDataStruct("DNS:OpenEMed.org/INSERTION", new ObservationDataStruct[0], new ObservationDataStruct[0], value);
        AbstractObservationData_ obsData_2i = (AbstractObservationData_) persObjFact.createPersistentObject(obsData);
        try {
            String obsId = obsData_2i.getObjectId();
            try {
                dbMgr.txn_begin();
                obsData_2r = (AbstractObservationData_) dbMgr.retrieveElement(emptyObsData_, "obsId", obsId);
                if (obsData_2r != null) dbMgr.deleteElement(obsData_2r);
                dbMgr.txn_commit();
            } catch (DBException e) {
                dbMgr.txn_abort();
            }
            try {
                System.out.println("inserting '" + obsId + "' into database");
                dbMgr.txn_begin();
                dbMgr.insertElement(obsData_2i, null);
                dbMgr.txn_commit();
                System.out.println("done! - Retrieving object...");
                dbMgr.txn_begin();
                obsData_2r = null;
                obsData_2r = (AbstractObservationData_) dbMgr.retrieveElement(emptyObsData_, "obsId", obsId);
                assertEquals("Objects should been the same after insertion", obsData_2r.getObjectId(), obsData_2i.getObjectId());
                dbMgr.txn_commit();
                System.out.println("Deleting '" + obsId + "' from database");
                dbMgr.txn_begin();
                dbMgr.deleteElement(obsData_2r);
                dbMgr.txn_commit();
            } catch (Throwable e) {
                fail(e.getMessage());
            } finally {
                System.out.println("done");
            }
        } catch (NoObjectIdException e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of accessElements method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testAccessElements() {
        System.out.println();
        System.out.println("testAccessElements");
        CodeMapper codeMapper = CodeMapper.getInstance();
        AbstractObservationData_ obsData_r;
        AbstractObservationData_ obsData_a;
        try {
            String codeId = codeMapper.getId("DNS:OpenEMed.org/TraitCode/Summary", true, false);
            Hashtable table = new Hashtable();
            table.put("code", codeId);
            table.put("personId", "DNS:OpenEMed.org////10005");
            SearchFilter filter = DBMgrFactory.createFilter();
            filter.matchSet(table, SearchFilter.AND, SearchFilter.EQUAL);
            dbMgr.txn_begin();
            Vector result = dbMgr.retrieveElements(emptyObsData_, filter, dbMgr.SHALLOW);
            assertNotNull("Result set is null", result);
            obsData_r = (AbstractObservationData_) result.firstElement();
            assertNotNull("Object is null after retrieval", obsData_r);
            Iterator compIterator = obsData_r.getComposites();
            assertTrue("The composite vector should be empty after SHALLOW retrieval", compIterator.hasNext() == false);
            result = dbMgr.accessElements(obsData_r, null, null);
            obsData_a = (AbstractObservationData_) result.firstElement();
            assertNotNull("Objects is null after retrieval", obsData_a);
            compIterator = obsData_a.getComposites();
            assertTrue("The composite vector should not be empty after accessElements", compIterator.hasNext() == false);
            dbMgr.txn_commit();
            System.out.println("done");
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of retrieveElements method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testRetrieveElementsByVector() {
        System.out.println();
        System.out.println("testRetrieveElements for 'dbMgr.retrieveElements(Object obj, String[] elements, String[] name)'");
        CodeMapper codeMapper = CodeMapper.getInstance();
        try {
            String codeId = codeMapper.getId("DNS:OpenEMed.org/TraitCode/Summary", true, true);
            String[] elements = new String[2];
            String[] values = new String[2];
            elements[0] = "code";
            elements[1] = "personId";
            values[0] = codeId;
            values[1] = "DNS:OpenEMed.org////10006";
            System.out.println("retrieving objects with filter vector ");
            dbMgr.txn_begin();
            Vector result = dbMgr.retrieveElements(emptyObsData_, elements, values);
            int size = result.size();
            assertEquals("The result vector is not oft the expected size", size, 2);
            dbMgr.txn_commit();
            System.out.println("done");
            elements = new String[2];
            values = new String[2];
            elements[0] = "code";
            elements[1] = "personId";
            values[0] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/Summary", true, false);
            values[1] = "DNS:OpenEMed.org////10005";
            System.out.println("retrieving objects with filter vector ");
            dbMgr.txn_begin();
            result = dbMgr.retrieveElements(emptyObsData_, elements, values);
            assertEquals("The result vector is not of the expected size", result.size(), 1);
            dbMgr.txn_commit();
            System.out.println("done");
        } catch (Throwable e) {
            fail(e.getMessage());
        }
    }

    /**
	 * Test of retrieveElements method, of class gov.lanl.Database.OJBDatabaseMgr.
	 */
    public void testRetrieveElementsByFilter() {
        System.out.println();
        System.out.println("testRetrieveElements for 'dbMgr.retrieveElements(Object obj, SearchFilter query, int deep)'");
        int filterTests = 12;
        SearchFilter filter = DBMgrFactory.createFilter();
        int resultElements = 0;
        int deep = dbMgr.SHALLOW;
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < filterTests; i++) {
                ojb.broker.cache.ObjectCacheFactory.getObjectCache().clear();
                resultElements = getFilter(filter, i);
                try {
                    System.out.println("retrieving objects with search filter " + filter.toString());
                    dbMgr.txn_begin();
                    Vector result = dbMgr.retrieveElements(emptyObsData_, filter, deep);
                    assertNotNull("Result set is null", result);
                    assertTrue("Result set is empty ", !result.isEmpty());
                    AbstractObservationData_ obsData = (AbstractObservationData_) result.firstElement();
                    int realSize = result.size();
                    assertEquals("The result vector is not of the expected size", realSize, resultElements);
                    Iterator compIterator = obsData.getComposites();
                    if (obsData.getParentObjId().length() == 0) {
                        if (deep == dbMgr.DEEP) {
                            assertTrue("The composite vector is empty", compIterator.hasNext() == true);
                        } else {
                            assertTrue("The composite vector should be empty after SHALLOW retrieval", compIterator.hasNext() == false);
                        }
                    }
                    dbMgr.txn_commit();
                    System.out.println("done");
                } catch (Throwable e) {
                    fail("Filter: j=" + j + " i=" + i + ", " + e.getMessage());
                }
            }
            deep = dbMgr.DEEP;
        }
    }

    /**
	 * Dispatcher method to create different kinds of searchFilter
	 *
	 */
    protected int getFilter(SearchFilter filter, int i) {
        CodeMapper codeMapper = CodeMapper.getInstance();
        int expected = 0;
        try {
            switch(i) {
                case 0:
                    {
                        String value = DBMgrFactory.getWildChar() + "5";
                        filter.compareFilter("personId", value, SearchFilter.LIKE);
                        return 8;
                    }
                case 1:
                    {
                        filter.compareFilter("personId", "DNS:OpenEMed.org////10005", SearchFilter.EQUAL);
                        return 8;
                    }
                case 2:
                    {
                        filter.compareFilter("startTime", "19950831180000", SearchFilter.GREATER_EQUAL);
                        return 47;
                    }
                case 3:
                    {
                        String[] values = new String[3];
                        values[0] = codeMapper.getId("This is not a code in the DB!", true, false);
                        values[1] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false);
                        values[2] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/Immunology", true, false);
                        filter.matchList("code", values, SearchFilter.IN);
                        return 3;
                    }
                case 4:
                    {
                        String[] values = new String[2];
                        values[0] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false);
                        values[1] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/Immunology", true, false);
                        filter.matchList("code", values, SearchFilter.NOT_IN);
                        return 147;
                    }
                case 5:
                    {
                        Vector values = new Vector();
                        values.add(codeMapper.getId("XXXXXXXXXXXXXXXXXXXXXXXXXXX", true, false));
                        values.add(codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false));
                        values.add(codeMapper.getId("DNS:OpenEMed.org/TraitCode/Immunology", true, false));
                        filter.matchList("code", values, SearchFilter.IN);
                        return 3;
                    }
                case 6:
                    {
                        Vector values = new Vector();
                        values.add(codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false));
                        values.add(codeMapper.getId("DNS:OpenEMed.org/TraitCode/Immunology", true, false));
                        filter.matchList("code", values, SearchFilter.NOT_IN);
                        return 147;
                    }
                case 7:
                    {
                        filter.matchValue("personId", "DNS:OpenEMed.org////10005", SearchFilter.IN);
                        return 8;
                    }
                case 8:
                    {
                        filter.matchValue("personId", "DNS:OpenEMed.org////10005", SearchFilter.NOT_IN);
                        return 142;
                    }
                case 9:
                    {
                        String[] values = new String[3];
                        values[0] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false);
                        values[1] = "19921111T000000";
                        values[2] = "DNS:OpenEMed.org////10006";
                        String[] elements = new String[3];
                        elements[0] = "code";
                        elements[1] = "startTime";
                        elements[2] = "personId";
                        filter.matchSet(elements, values, SearchFilter.AND);
                        return 1;
                    }
                case 10:
                    {
                        String[] values = new String[4];
                        values[0] = codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false);
                        values[1] = "19921111T000000";
                        values[2] = "DNS:OpenEMed.org////10005";
                        values[3] = codeMapper.getId("DNS:omg.org/DSObservationAccess/HL72.3/OBR/SpecimenSource", true, false);
                        String[] elements = new String[4];
                        elements[0] = "code";
                        elements[1] = "startTime";
                        elements[2] = "personId";
                        elements[3] = "code";
                        filter.matchSet(elements, values, SearchFilter.OR);
                        return 66;
                    }
                case 11:
                    {
                        Hashtable table = new Hashtable();
                        table.put("code", codeMapper.getId("DNS:OpenEMed.org/TraitCode/ImageStudy", true, false));
                        table.put("startTime", "19921111T000000");
                        table.put("personId", "DNS:OpenEMed.org////10006");
                        filter.matchSet(table, SearchFilter.AND, SearchFilter.EQUAL);
                        return 1;
                    }
                default:
                    {
                        System.out.println("No more filters available!");
                        expected = 0;
                    }
            }
        } catch (DBException e) {
            System.err.println("Can't create search filter " + e);
            return 0;
        }
        return expected;
    }

    /**
	 * Test of getNextSeq method, of class gov.lanl.Database.OJBDatabaseMgr
	 */
    public void testGetNextSeq() {
        System.out.println();
        System.out.println("testGetNextSeq");
        long seq1 = dbMgr.getNextSeq(emptyObsData_.getClass(), "obsId");
        long seq2 = dbMgr.getNextSeq(emptyObsData_.getClass(), "obsId");
        assertTrue("continous call to getNextSeq delivered same sequence number", !(String.valueOf(seq1).equals(String.valueOf(seq2))));
        System.out.println("done");
    }

    /**
	 * This method is only called once at construction time
	 */
    protected void init() {
        props = new ConfigProperties();
        props.setProperty("personId", personIdCode);
        props.setProperty("startTime", startTimeCode);
        props.setProperty("stopTime", stopTimeCode);
        props.setProperty("signingFlag", signingCode);
        props.setProperty("parser", parserName);
        mapper = Qualifier2AttributeMapper.getInstance(props);
    }

    /**
     * overriden template method
     */
    protected void setUp() {
        dbMgr = DBMgrFactory.createDbMgr(databaseURL, databaseType);
        if (dbMgr == null) {
            fail("Can't get DatabaseMgr");
        }
        if (emptyObsData_ == null) emptyObsData_ = new OjbObservationData_();
        CodeMapper codeMapper = CodeMapper.getInstance(props);
        dbMgr.setObjectFactory(persistentProperties);
        persObjFact = dbMgr.getObjectFactory();
        if (persObjFact == null) {
            fail("Can't get ObjectFactory");
        }
        ObservationDataStruct[] obsDataSeq = loadObsData(loadFile);
        obsData_Seq = createPersistentObsData(obsDataSeq);
        storeData(obsData_Seq);
        ojb.broker.cache.ObjectCacheFactory.getObjectCache().clear();
    }

    /**
     * overriden template method
     */
    protected void tearDown() {
        dbMgr.txn_abort();
        deleteData(obsData_Seq);
    }

    /**
     * Loads the test data from TestObsData.xml
     */
    protected ObservationDataStruct[] loadObsData(String loadFile) {
        try {
            Xml2DomDoc x2dd = new Xml2DomDoc(new org.xml.sax.InputSource(loadFile));
            DomDoc2ObsData dd2od = new DomDoc2ObsData(x2dd.getDomDoc());
            ObservationDataStruct[] obsDataSeq = dd2od.getObsDataSeq();
            return obsDataSeq;
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
    }

    /**
     * Convert ObservationData to persistent PersistentObject
     */
    protected PersistentObject[] createPersistentObsData(ObservationDataStruct[] obsDataSeq) {
        try {
            int elements = obsDataSeq.length;
            PersistentObject[] obsData_Seq = new PersistentObject[elements];
            for (int i = 0; i < elements; i++) {
                obsData_Seq[i] = (PersistentObject) persObjFact.createPersistentObject(obsDataSeq[i]);
            }
            return obsData_Seq;
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
    }

    /**
     * Insert data into database
     */
    protected void storeData(PersistentObject[] obsData_Seq) {
        try {
            for (int i = 0; i < obsData_Seq.length; i++) {
                dbMgr.txn_begin();
                PersistentObject persObj = obsData_Seq[i];
                dbMgr.insertElement(persObj, null);
                dbMgr.txn_commit();
            }
        } catch (DBException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Delet data from database
     */
    protected void deleteData(PersistentObject[] obsData_Seq) {
        try {
            for (int i = 0; i < obsData_Seq.length; i++) {
                dbMgr.txn_begin();
                dbMgr.deleteElement(obsData_Seq[i]);
                dbMgr.txn_commit();
            }
        } catch (DBException e) {
            fail(e.getMessage());
        }
    }
}
