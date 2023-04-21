package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaList;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * 
 */
public class AreaEventInfoTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { (byte) 0xb0, 0x1f, (byte) 0xa0, 0x16, (byte) 0xa0, 0x14, 0x30, 0x08, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x03, 0x09, 0x70, 0x71, 0x30, 0x08, (byte) 0x80, 0x01, 0x03, (byte) 0x81, 0x03, 0x04, 0x30, 0x31, (byte) 0x81, 0x01, 0x01, (byte) 0x82, 0x02, 0x7f, (byte) 0xfe };
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        AreaEventInfo areaEvtInf = new AreaEventInfoImpl();
        ((AreaEventInfoImpl) areaEvtInf).decodeAll(asn);
        AreaDefinition areaDef = areaEvtInf.getAreaDefinition();
        assertNotNull(areaDef);
        AreaList areaList = areaDef.getAreaList();
        assertNotNull(areaList);
        assertEquals(areaList.getAreas().length, 2);
        Area[] areas = areaList.getAreas();
        assertNotNull(areas[0].getAreaIdentification());
        assertTrue(Arrays.equals(new byte[] { 0x09, 0x70, 0x71 }, areas[0].getAreaIdentification()));
        OccurrenceInfo occInfo = areaEvtInf.getOccurrenceInfo();
        assertNotNull(occInfo);
        assertEquals(occInfo, OccurrenceInfo.multipleTimeEvent);
        int intTime = areaEvtInf.getIntervalTime();
        assertEquals(intTime, 32766);
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { (byte) 0xb0, 0x1f, (byte) 0xa0, 0x16, (byte) 0xa0, 0x14, 0x30, 0x08, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x03, 0x09, 0x70, 0x71, 0x30, 0x08, (byte) 0x80, 0x01, 0x03, (byte) 0x81, 0x03, 0x04, 0x30, 0x31, (byte) 0x81, 0x01, 0x01, (byte) 0x82, 0x02, 0x7f, (byte) 0xfe };
        Area area1 = new AreaImpl(AreaType.utranCellId, new byte[] { 0x09, 0x70, 0x71 });
        Area area2 = new AreaImpl(AreaType.routingAreaId, new byte[] { 0x04, 0x30, 0x31 });
        AreaList areaList = new AreaListImpl(new Area[] { area1, area2 });
        AreaDefinition areaDef = new AreaDefinitionImpl(areaList);
        AreaEventInfo areaEvtInf = new AreaEventInfoImpl(areaDef, OccurrenceInfo.multipleTimeEvent, 32766);
        AsnOutputStream asnOS = new AsnOutputStream();
        ((AreaEventInfoImpl) areaEvtInf).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, Tag.SEQUENCE);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        Area area1 = new AreaImpl(AreaType.utranCellId, new byte[] { 0x09, 0x70, 0x71 });
        Area area2 = new AreaImpl(AreaType.routingAreaId, new byte[] { 0x04, 0x30, 0x31 });
        AreaList areaList = new AreaListImpl(new Area[] { area1, area2 });
        AreaDefinition areaDef = new AreaDefinitionImpl(areaList);
        AreaEventInfo original = new AreaEventInfoImpl(areaDef, OccurrenceInfo.multipleTimeEvent, 32766);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        AreaEventInfoImpl copy = (AreaEventInfoImpl) o;
        assertEquals(copy.getAreaDefinition(), original.getAreaDefinition());
        assertEquals(copy.getOccurrenceInfo(), original.getOccurrenceInfo());
        assertEquals(copy.getIntervalTime(), original.getIntervalTime());
    }
}
