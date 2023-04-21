package com.cartagena.financo.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.caelum.vraptor.ioc.Component;
import com.cartagena.financo.model.Category;
import com.cartagena.financo.repository.CategoryRepository;

@Component
public class CategoryDao extends BaseDao<Category> implements CategoryRepository {

    CategoryDao(Session session) {
        super(session);
    }

    @Override
    public Category loadByName(String name) {
        Criteria criteria = this.session.createCriteria(Category.class);
        criteria.add(Restrictions.eq("name", name));
        return (Category) criteria.uniqueResult();
    }
}
