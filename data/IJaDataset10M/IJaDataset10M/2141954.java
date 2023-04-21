package org.vosao.service.back;

import java.util.List;
import java.util.Map;
import org.vosao.service.AbstractService;
import org.vosao.service.ServiceResponse;
import org.vosao.service.vo.FieldVO;

public interface FieldService extends AbstractService {

    ServiceResponse updateField(Map<String, String> field);

    List<FieldVO> getByForm(final Long formId);

    FieldVO getById(final Long fieldId);

    ServiceResponse remove(final List<String> ids);

    void moveUp(final Long formId, final Long fieldId);

    void moveDown(final Long formId, final Long fieldId);
}
