package org.vardb.explorer.dao;

import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface IExplorerDao {

    public void add(CExplorerData data);

    public void update(CExplorerData data);

    public List<CExplorerData> getDataByUser(String username);

    public CExplorerData getData(String id);

    public void deleteData(String id);

    public CLabel getLabel(String identifier);

    public CLabel getLabel(int id);

    public int addLabel(CLabel label);

    public void updateLabel(CLabel label);

    public void deleteLabel(int id);
}
