package cn.jsprun.dao;

import java.util.List;
import cn.jsprun.domain.Forums;

public interface ForumsDao {

    public boolean addForum(Forums forum);

    public boolean removeForum(Forums forum);

    public boolean updateForum(Forums forum);

    public boolean updatePost(Short sourcefid, Short targetfid);

    public boolean updateThread(Short sourcefid, Short targetfid);

    public Forums findById(Short fid);

    public List<Forums> getForumsList(List<Short> fidList);

    public List<Forums> findByUp(Short fup);

    public List<Forums> findByType(String type);

    public List<Forums> findByProperty(String propertyName, Object value);

    public List<Forums> findAll();

    public List<Forums> findExceptFupEqualZero();

    public List<Forums> findFourmsByHql(String hql, int start, int maxrow);

    public int findFourmsCount();

    public Integer findForumsCountWithoutGroup();

    public Forums getHotForumsInfo();

    public void updateForumsByHQL(String hql);
}
