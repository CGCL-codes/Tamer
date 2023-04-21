package cn.jsprun.dao;

import java.util.List;
import cn.jsprun.domain.Faqs;

public interface FaqsDao {

    public List<Faqs> findFaqsByfpid(short fpid, short id);

    public List<Faqs> findFaqsParents();

    public List<Faqs> findFaqsChilds();

    public List<Faqs> findFaqsByHql(String hql);

    public Faqs findFaqlById(short id);
}
