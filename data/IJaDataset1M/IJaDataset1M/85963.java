package org.libreplan.business.test.advance.daos;

import static org.junit.Assert.assertTrue;
import static org.libreplan.business.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_FILE;
import static org.libreplan.business.test.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_TEST_FILE;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libreplan.business.advance.daos.IAdvanceAssignmentDAO;
import org.libreplan.business.advance.daos.IAdvanceTypeDAO;
import org.libreplan.business.advance.entities.AdvanceAssignment;
import org.libreplan.business.advance.entities.AdvanceType;
import org.libreplan.business.advance.entities.DirectAdvanceAssignment;
import org.libreplan.business.common.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { BUSINESS_SPRING_CONFIG_FILE, BUSINESS_SPRING_CONFIG_TEST_FILE })
@Transactional
public class AdvanceAssignmentDAOTest {

    @Autowired
    private IAdvanceAssignmentDAO advanceAssignmentDAO;

    @Autowired
    private IAdvanceTypeDAO advanceTypeDAO;

    private AdvanceType givenAdvanceType() {
        BigDecimal value = new BigDecimal(100);
        BigDecimal precision = BigDecimal.ONE;
        AdvanceType advanceType = AdvanceType.create("advance-type", value, true, precision, true, false);
        advanceTypeDAO.save(advanceType);
        return advanceType;
    }

    @Test
    public void saveValidAdvanceAssignment() {
        AdvanceAssignment advance = DirectAdvanceAssignment.create(false, BigDecimal.TEN);
        advance.setAdvanceType(givenAdvanceType());
        advanceAssignmentDAO.save(advance);
        assertTrue(advance.getId() != null);
    }

    @Test(expected = ValidationException.class)
    public void saveAdvanceAssignmentWithZeroAsMaxValue() {
        AdvanceAssignment advance = DirectAdvanceAssignment.create(false, BigDecimal.ZERO);
        advance.setAdvanceType(givenAdvanceType());
        advanceAssignmentDAO.save(advance);
        assertTrue(advance.getId() != null);
    }

    @Test(expected = ValidationException.class)
    public void saveAdvanceAssignmentWithNegativeNumberAsMaxValue() {
        AdvanceAssignment advance = DirectAdvanceAssignment.create(false, BigDecimal.valueOf(-10));
        advance.setAdvanceType(givenAdvanceType());
        advanceAssignmentDAO.save(advance);
        assertTrue(advance.getId() != null);
    }
}
