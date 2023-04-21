package org.libreplan.business.test.qualityforms.daos;

import java.math.BigDecimal;
import java.util.UUID;
import org.libreplan.business.qualityforms.daos.IQualityFormDAO;
import org.libreplan.business.qualityforms.entities.QualityForm;
import org.libreplan.business.qualityforms.entities.QualityFormItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for {@QualityDAO}
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
public abstract class AbstractQualityFormTest {

    @Autowired
    IQualityFormDAO qualityFormDAO;

    public QualityForm createValidQualityForm() {
        QualityForm qualityForm = QualityForm.create(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        return qualityForm;
    }

    public QualityFormItem createValidQualityFormItem() {
        QualityFormItem qualityFormItem = QualityFormItem.create(UUID.randomUUID().toString(), new Integer(0), new BigDecimal(1));
        return qualityFormItem;
    }
}
