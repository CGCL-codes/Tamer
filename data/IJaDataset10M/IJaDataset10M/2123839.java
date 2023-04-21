package org.geonetwork.services.ebrim.csw202.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import org.geonetwork.domain.csw202.discovery.ElementSet;
import org.geonetwork.domain.csw202.discovery.ElementSetName;
import org.geonetwork.domain.csw202.discovery.GetRecordById;
import org.geonetwork.domain.csw202.exception.Exception;
import org.geonetwork.services.ebrim.csw202.CSW202Constants;
import org.geonetwork.services.ebrim.csw202.CSW202ServiceException;
import org.junit.Test;

public class GetRecordByIdValidationTest {

    @Test
    public void testGetRecordByIdValidation() {
        GetRecordById request = new GetRecordById();
        try {
            new GetRecordByIdValidation(request);
            fail();
        } catch (CSW202ServiceException e) {
        }
        List<String> ids = new ArrayList<String>();
        request.setId(ids);
        request.setOutputFormat(CSW202Constants.outputFormatapplicationXml);
        request.setOutputSchema(CSW202Constants.outputSchema);
        request.setElementSetName(new ElementSetName());
        request.getElementSetName().setElementSet(ElementSet.brief);
        try {
            new GetRecordByIdValidation(request);
            fail();
        } catch (CSW202ServiceException e) {
            assertEquals(e.getFaultMessage().getExceptionList().get(0).getExceptionCode(), Exception.WRS_INVALIDREQUEST);
        }
        ids.add("How unique can I be?");
        try {
            new GetRecordByIdValidation(request);
        } catch (CSW202ServiceException e) {
            fail();
        } catch (NullPointerException e) {
        }
        request.setOutputSchema(" invalid output schema definition ");
        try {
            new GetRecordByIdValidation(request);
            fail();
        } catch (CSW202ServiceException e) {
            assertEquals(e.getFaultMessage().getExceptionList().get(0).getExceptionCode(), Exception.WRS_NOTSUPPORTED);
        }
    }
}
