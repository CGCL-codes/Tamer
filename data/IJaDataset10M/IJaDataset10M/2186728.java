package com.hibernate.dao.news;

import java.util.List;
import java.util.Map;
import com.hibernate.pojo.ArticlesLeibie;
import com.struts.form.ArticlesLeibieForm;

public interface ArticlesLeibieServiceDao {

    public boolean saveNewsLeibie(ArticlesLeibie articlesleibie);

    public boolean updateNewsLeibie(ArticlesLeibieForm articlesleibieform);

    public boolean deleteNewsLeibie(Long id);

    public List findNewsLeibie(String hql);
}
