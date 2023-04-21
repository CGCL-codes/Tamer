package net.sf.brightside.instantevents.service.crud;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;

public interface RetriveResults<E> {

    public List<E> retriveResults(DetachedCriteria criteria);
}
