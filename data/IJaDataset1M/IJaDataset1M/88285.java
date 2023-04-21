package ca.uhn.hl7v2.util;

import java.util.Iterator;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;
import junit.framework.TestCase;

/**
 * Unit tests for ReadOnlyMessageIterator.  
 * 
 * @author <a href="mailto:bryan.tripp@uhn.on.ca">Bryan Tripp</a>
 * @version $Revision: 1.1 $ updated on $Date: 2007-02-19 02:24:30 $ by $Author: jamesagnew $
 */
public class ReadOnlyMessageIteratorTest extends TestCase {

    /**
     * Constructor for ReadOnlyMessageIteratorTest.
     * @param arg0
     */
    public ReadOnlyMessageIteratorTest(String arg0) {
        super(arg0);
    }

    public void testFiltered() throws EncodingNotSupportedException, HL7Exception {
        PipeParser parser = new PipeParser();
        Message msg = parser.parse(getMessageOne());
        Iterator<Structure> it = ReadOnlyMessageIterator.createPopulatedSegmentIterator(msg);
        assertEquals("MSH", ((Segment) it.next()).getName());
        assertEquals("PID", ((Segment) it.next()).getName());
        assertEquals("PV1", ((Segment) it.next()).getName());
        assertEquals("OBR", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
        assertEquals("NTE", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
        assertEquals("OBX", ((Segment) it.next()).getName());
    }

    private String getMessageOne() {
        return "MSH|^~\\&|LABGL1||DMCRES||19951002180700||ORU^R01|LABGL1199510021807427|P|2.5\r" + "PID|||T12345||TEST^PATIENT^P||19601002|M||||||||||123456\r" + "PV1|||NER|||||||GSU||||||||E||||||||||||||||||||||||||19951002174900|19951006\r" + "OBR|1||09527539021001920|1001920^BLOOD GASES, ARTERIAL^^^ABG|||19951002180200|||||||19951002180300||||1793559||0952753902||19951002180700||350|F||^^^^^RT\r" + "OBX||NM|1001910^PATIENT TEMPERATURE, ARTERIAL^^^TEMP CORR|0001|37.0||||||F|||19951002180700||42\r" + "OBX||TX|1001912^FIO2, ARTERIAL^^^FIO2 art|0001|*|%||A|||F|||19951002180700||42\r" + "OBX||NM|1001960^PO2, ARTERIAL^^^PO2 art|0001|65|mmHg|75-100|L|||F|||19951002180700||42\r" + "OBX||NM|1001420^PCO2, ARTERIAL^^^PCO2 art|0001|42|mmHg|35-45||||F|||19951002180700||42\r" + "OBX||NM|1001449^PH, BLOOD ARTERIAL^^^PH art|0001|7.20||7.35-7.45|LL|||F|||19951002180700||42\r" + "NTE|||ALERT PH: CALLED TO DD BY BP @ 1809.\r" + "OBX||NM|1001935^HEMOGLOBIN, ARTERIAL^^^HGB art|0001|13.4|g/dL|14.0-17.0|L|||F|||19951002180700||42\r" + "OBX||NM|1001060^BICARBONATE, ARTERIAL^^^HCO3 art|0001|17|mmol/L|20-28|L|||F|||19951002180700||42\r" + "OBX||NM|1001930^BASE EXCESS,ARTERIAL^^^BASE XSart|0001|-10|mmol/L|-3-3|L|||F|||19951002180700||42\r" + "OBX||NM|1001980^%O2 SATURATION, ARTERIAL^^^%O2HGB art|0001|88.3|%|96.0-100.0|L|||F|||19951002180700||42\r" + "OBX||NM|1001970^%CO SATURATION, ARTERIAL^^^%COHGB art|0001|0.8|%|1.5-9.0|L|||F|||19951002180700||42\r" + "OBX||NM|1001990^%METHEMOGLOBIN, ARTERIAL^^^%METHGBart|0001|.2|%|.4-1.5|L|||F|||19951002180700||42\r" + "OBX||NM|1001950^VOLUME % O2, ARTERIAL^^^VOL%O2 art|0001|16.4|%|15.0-24.0||||F|||19951002180700||42\r" + "OBX||NM|1001940^CO2 TOTAL, ARTERIAL^^^TCO2 art|0001|18|mmol/L|21-30|L|||F|||19951002180700||42\r";
    }
}
