package org.libreplan.web.common.converters;

import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.workreports.daos.IWorkReportTypeDAO;
import org.libreplan.business.workreports.entities.WorkReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class WorkReportTypeConverter implements IConverter<WorkReportType> {

    @Autowired
    private IWorkReportTypeDAO workReportTypeDAO;

    @Override
    @Transactional(readOnly = true)
    public WorkReportType asObject(String stringRepresentation) {
        long id = Long.parseLong(stringRepresentation);
        try {
            WorkReportType workReportType = workReportTypeDAO.find(id);
            return workReportType;
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String asString(WorkReportType entity) {
        return entity.getId().toString();
    }

    @Override
    public String asStringUngeneric(Object entity) {
        return asString((WorkReportType) entity);
    }

    @Override
    public Class<WorkReportType> getType() {
        return WorkReportType.class;
    }
}
