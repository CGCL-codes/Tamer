package net.sf.brightside.instantevents.service.queries;

import java.util.List;
import net.sf.brightside.instantevents.core.exception.BusinessException;
import net.sf.brightside.instantevents.service.crud.RetriveResults;
import org.hibernate.criterion.DetachedCriteria;

public class GetAllByTypeCommandImpl<Type> implements GetAllByTypeCommand<Type> {

    private Class<Type> type;

    private RetriveResults<Type> retriveResults;

    public Class<Type> getType() {
        return type;
    }

    public void setType(Class<Type> clazz) {
        this.type = clazz;
    }

    public RetriveResults<Type> getRetriveResults() {
        return retriveResults;
    }

    public void setRetriveResults(RetriveResults<Type> retriveResults) {
        this.retriveResults = retriveResults;
    }

    public List<Type> execute() throws BusinessException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(type);
        return retriveResults.retriveResults(detachedCriteria);
    }
}
