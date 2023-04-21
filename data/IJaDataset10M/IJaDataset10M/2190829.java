package org.libreplan.business.test.qualityforms.entities;

import static org.junit.Assert.fail;
import static org.libreplan.business.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_FILE;
import static org.libreplan.business.test.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_TEST_FILE;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libreplan.business.common.exceptions.ValidationException;
import org.libreplan.business.qualityforms.daos.IQualityFormDAO;
import org.libreplan.business.qualityforms.entities.QualityForm;
import org.libreplan.business.qualityforms.entities.QualityFormItem;
import org.libreplan.business.qualityforms.entities.QualityFormType;
import org.libreplan.business.test.qualityforms.daos.AbstractQualityFormTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { BUSINESS_SPRING_CONFIG_FILE, BUSINESS_SPRING_CONFIG_TEST_FILE })
@Transactional
public class QualityFormTest extends AbstractQualityFormTest {

    @Autowired
    IQualityFormDAO qualityFormDAO;

    @Test
    public void checkInvalidNameQualityForm() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        qualityForm.setName("");
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        qualityForm.setName(null);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidQualityFormType() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        try {
            qualityForm.setQualityFormType(null);
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void checkInvalidRepeatedQualityFormItemPosition() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem1 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem1);
        QualityFormItem qualityFormItem2 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem2);
        qualityFormItem1.setPosition(0);
        qualityFormItem2.setPosition(0);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidNotConsecutivesQualityFormItemPosition() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem1 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem1);
        QualityFormItem qualityFormItem2 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem2);
        qualityFormItem1.setPosition(0);
        qualityFormItem2.setPosition(2);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidOutOfRangeQualityFormItemPosition() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem1 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem1);
        QualityFormItem qualityFormItem2 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem2);
        qualityFormItem1.setPosition(1);
        qualityFormItem2.setPosition(2);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidPercentageQualityFormItemPosition() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem1 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem1);
        QualityFormItem qualityFormItem2 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem2);
        qualityFormItem1.setPosition(0);
        qualityFormItem1.setPercentage(new BigDecimal(1));
        qualityFormItem2.setPosition(1);
        qualityFormItem2.setPercentage(new BigDecimal(2));
        try {
            qualityFormDAO.save(qualityForm);
        } catch (ValidationException e) {
            fail("It shouldn't throw an exception");
        }
        qualityFormItem1.setPercentage(new BigDecimal(2));
        qualityFormItem2.setPercentage(new BigDecimal(1));
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidQualityFormItemPositionByItems() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        qualityForm.setQualityFormType(QualityFormType.BY_ITEMS);
        QualityFormItem qualityFormItem1 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem1);
        QualityFormItem qualityFormItem2 = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem2);
        try {
            qualityFormDAO.save(qualityForm);
        } catch (ValidationException e) {
            fail("It shouldn't throw an exception");
        }
        qualityFormItem1.setPosition(2);
        qualityFormItem2.setPosition(1);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It shouldn't throw an exception");
        } catch (ValidationException e) {
        }
        qualityFormItem1.setPosition(0);
        qualityFormItem2.setPosition(1);
        qualityFormItem1.setPercentage(new BigDecimal(100));
        qualityFormItem2.setPercentage(new BigDecimal(1));
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        qualityFormItem1.setPosition(0);
        qualityFormItem2.setPosition(1);
        qualityFormItem1.setPercentage(new BigDecimal(10));
        qualityFormItem2.setPercentage(new BigDecimal(1));
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkInvalidQualityFormItemName() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem);
        try {
            qualityFormDAO.save(qualityForm);
        } catch (ValidationException e) {
            fail("It should not throw an exception");
        }
        qualityFormItem.setName(null);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
        qualityFormItem.setName("");
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkNotNullQualityFormItemPosition() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem = createValidQualityFormItem();
        qualityForm.addQualityFormItemOnTop(qualityFormItem);
        qualityFormItem.setPosition(null);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkNotNullQualityFormItemPercentage() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem = createValidQualityFormItem();
        qualityFormItem.setPercentage(null);
        qualityForm.addQualityFormItemOnTop(qualityFormItem);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }

    @Test
    public void checkIncorrectQualityFormItemPercentage() throws ValidationException {
        QualityForm qualityForm = createValidQualityForm();
        QualityFormItem qualityFormItem = createValidQualityFormItem();
        qualityFormItem.setPercentage(new BigDecimal(100.1));
        qualityForm.addQualityFormItemOnTop(qualityFormItem);
        try {
            qualityFormDAO.save(qualityForm);
            fail("It should throw an exception");
        } catch (ValidationException e) {
        }
    }
}
