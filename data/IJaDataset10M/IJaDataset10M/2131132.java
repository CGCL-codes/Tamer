package org.libreplan.business.test.workreports.entities;

import static org.junit.Assert.fail;
import static org.libreplan.business.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_FILE;
import static org.libreplan.business.test.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_TEST_FILE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libreplan.business.common.exceptions.ValidationException;
import org.libreplan.business.test.workreports.daos.AbstractWorkReportTest;
import org.libreplan.business.workreports.daos.IWorkReportTypeDAO;
import org.libreplan.business.workreports.entities.WorkReportLabelTypeAssigment;
import org.libreplan.business.workreports.entities.WorkReportType;
import org.libreplan.business.workreports.valueobjects.DescriptionField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Diego Pino García <dpino@igalia.com>
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { BUSINESS_SPRING_CONFIG_FILE, BUSINESS_SPRING_CONFIG_TEST_FILE })
@Transactional
public class WorkReportTypeTest extends AbstractWorkReportTest {

    @Autowired
    IWorkReportTypeDAO workReportTypeDAO;

    @Test
    public void checkInvalidNameWorkReportType() throws ValidationException {
        WorkReportType workReportType = createValidWorkReportType();
        workReportType.setName("");
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        workReportType = createValidWorkReportType();
        workReportType.setName(null);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidCodeWorkReportType() throws ValidationException {
        WorkReportType workReportType = createValidWorkReportType();
        workReportType.setCode("");
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        workReportType = createValidWorkReportType();
        workReportType.setCode(null);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        workReportType.setCode("invalid_code");
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkIfCodeWorkReportTypeIsUnique() throws ValidationException {
        String code = new String("A");
        WorkReportType workReportTypeA = createValidWorkReportType();
        workReportTypeA.setCode(code);
        workReportTypeDAO.save(workReportTypeA);
        workReportTypeDAO.flush();
        WorkReportType workReportTypeACopy = createValidWorkReportType();
        workReportTypeACopy.setCode(code);
        try {
            workReportTypeDAO.save(workReportTypeACopy);
            workReportTypeDAO.flush();
            fail("It should throw an Exception");
        } catch (DataIntegrityViolationException e) {
        }
    }

    @Test
    public void checkIfNameWorkReportTypeIsUnique() throws ValidationException {
        WorkReportType workReportTypeA = createValidWorkReportType();
        workReportTypeA.setName("A");
        workReportTypeDAO.save(workReportTypeA);
        workReportTypeDAO.flush();
        WorkReportType workReportTypeACopy = createValidWorkReportType();
        workReportTypeACopy.setName("A");
        try {
            workReportTypeDAO.save(workReportTypeACopy);
            workReportTypeDAO.flush();
            fail("It should throw an Exception");
        } catch (DataIntegrityViolationException e) {
        }
    }

    @Test
    public void checkSaveDescriptionFieldsWorkReportType() {
        WorkReportType workReportType = createValidWorkReportType();
        DescriptionField descriptionFieldHead = createValidDescriptionField();
        workReportType.addDescriptionFieldToEndHead(descriptionFieldHead);
        DescriptionField descriptionFieldLine = createValidDescriptionField();
        workReportType.addDescriptionFieldToEndLine(descriptionFieldLine);
        try {
            workReportTypeDAO.save(workReportType);
        } catch (ValidationException e) {
            fail("It should throw an exception");
        }
    }

    @Test
    public void checkIfFieldNameDescriptionFieldsIsUnique() {
        WorkReportType workReportType = createValidWorkReportType();
        DescriptionField descriptionFieldHead = createValidDescriptionField();
        descriptionFieldHead.setFieldName("A");
        workReportType.addDescriptionFieldToEndHead(descriptionFieldHead);
        DescriptionField descriptionFieldLine = createValidDescriptionField();
        descriptionFieldLine.setFieldName("A");
        workReportType.addDescriptionFieldToEndLine(descriptionFieldLine);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidFieldNameDescriptionFields() {
        WorkReportType workReportType = createValidWorkReportType();
        DescriptionField descriptionFieldHead = createValidDescriptionField();
        descriptionFieldHead.setFieldName("");
        workReportType.addDescriptionFieldToEndHead(descriptionFieldHead);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        descriptionFieldHead.setFieldName(null);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        descriptionFieldHead.setFieldName("XXX");
        try {
            workReportTypeDAO.save(workReportType);
        } catch (ValidationException e) {
            fail("It should throw an exception");
        }
    }

    @Test
    public void checkInvalidLenghtDescriptionFields() {
        WorkReportType workReportType = createValidWorkReportType();
        DescriptionField descriptionFieldHead = createValidDescriptionField();
        descriptionFieldHead.setLength(null);
        workReportType.addDescriptionFieldToEndHead(descriptionFieldHead);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        descriptionFieldHead.setLength(0);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        descriptionFieldHead.setLength(1);
        try {
            workReportTypeDAO.save(workReportType);
        } catch (ValidationException e) {
            fail("It should throw an exception");
        }
    }

    @Test
    public void checkSaveWorkReportLabelTypeAssigment() {
        WorkReportType workReportType = createValidWorkReportType();
        WorkReportLabelTypeAssigment labelAssigmentHead = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndHead(labelAssigmentHead);
        WorkReportLabelTypeAssigment labelAssigmentLine = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigmentLine);
        try {
            workReportTypeDAO.save(workReportType);
        } catch (ValidationException e) {
            fail("It should throw an exception");
        }
    }

    @Test
    public void checkIfLabelTypeWorkReportLabelTypeAssigmentIsNull() {
        WorkReportType workReportType = createValidWorkReportType();
        WorkReportLabelTypeAssigment labelAssigment = createValidWorkReportLabelTypeAssigment();
        labelAssigment.setLabelType(null);
        workReportType.addLabelAssigmentToEndLine(labelAssigment);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkIfLabelWorkReportLabelTypeAssigmentIsNull() {
        WorkReportType workReportType = createValidWorkReportType();
        WorkReportLabelTypeAssigment labelAssigment = createValidWorkReportLabelTypeAssigment();
        labelAssigment.setDefaultLabel(null);
        workReportType.addLabelAssigmentToEndLine(labelAssigment);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkIfIndexLabelsAndFieldsAreConsecutive() {
        WorkReportType workReportType = createValidWorkReportType();
        WorkReportLabelTypeAssigment labelAssigment_1 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_1);
        WorkReportLabelTypeAssigment labelAssigment_2 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_2);
        WorkReportLabelTypeAssigment labelAssigment_3 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_3);
        labelAssigment_1.setPositionNumber(3);
        labelAssigment_2.setPositionNumber(0);
        labelAssigment_3.setPositionNumber(2);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkIfIndexLabelsAndFieldsInitInZero() {
        WorkReportType workReportType = createValidWorkReportType();
        WorkReportLabelTypeAssigment labelAssigment_1 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_1);
        WorkReportLabelTypeAssigment labelAssigment_2 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_2);
        WorkReportLabelTypeAssigment labelAssigment_3 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_3);
        labelAssigment_1.setPositionNumber(1);
        labelAssigment_2.setPositionNumber(2);
        labelAssigment_3.setPositionNumber(3);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkIfIndexLabelsAndFieldsAreUniques() {
        WorkReportType workReportType = createValidWorkReportType();
        WorkReportLabelTypeAssigment labelAssigment_1 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_1);
        WorkReportLabelTypeAssigment labelAssigment_2 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_2);
        WorkReportLabelTypeAssigment labelAssigment_3 = createValidWorkReportLabelTypeAssigment();
        workReportType.addLabelAssigmentToEndLine(labelAssigment_3);
        labelAssigment_1.setPositionNumber(1);
        labelAssigment_2.setPositionNumber(0);
        labelAssigment_3.setPositionNumber(1);
        try {
            workReportTypeDAO.save(workReportType);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }
}
