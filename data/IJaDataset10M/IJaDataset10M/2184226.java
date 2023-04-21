package org.dicom4j.dicom;

import org.dicom4j.AbstractTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDicomPackage extends AbstractTestCase {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(TestDicomPackage.class);

    private int ELEMENT = 50;

    private int GROUP = 45;

    public void testValueRepresentation() throws Exception {
        ValueRepresentation vr = new ValueRepresentation("US", "US");
        assertEquals(ValueRepresentation.UnsignedShort, vr);
    }

    public void testConstructors() throws Exception {
        DicomTag lTag1 = new DicomTag(this.GROUP, this.ELEMENT);
        DicomTag lTag2 = new DicomTag(this.GROUP, this.ELEMENT);
        assertEquals(this.GROUP, lTag1.getGroup());
        assertEquals(this.ELEMENT, lTag1.getElement());
        assert lTag1.equals(lTag2);
    }

    public void testValueRepresentationFactory() throws Exception {
        ValueRepresentationFactory factory = new ValueRepresentationFactory();
        assertEquals(ValueRepresentation.ApplicationEntity.getName(), factory.newValueRepresentation(ValueRepresentation.AE).getName());
        assertEquals(ValueRepresentation.AgeString.getName(), factory.newValueRepresentation(ValueRepresentation.AS).getName());
        assertEquals(ValueRepresentation.AttributeTag.getName(), factory.newValueRepresentation(ValueRepresentation.AT).getName());
        assertEquals(ValueRepresentation.CodeString.getName(), factory.newValueRepresentation(ValueRepresentation.CS).getName());
        assertEquals(ValueRepresentation.Date.getName(), factory.newValueRepresentation(ValueRepresentation.DA).getName());
    }
}
