package org.lightpersist.service;

import java.util.List;
import org.lightpersist.annotations.Dynamic;
import org.lightpersist.annotations.Query;
import org.lightpersist.bean.Author;
import org.lightpersist.dao.Dao;
import org.lightpersist.dao.Page;
import org.lightpersist.service.impl.AuthorServiceImpl;

@Dynamic(AuthorServiceImpl.class)
public interface AuthorService extends Dao<Author, String> {

    @Query
    Author getByName(String name);

    @Query
    Author[] findByNameLikeOrTextLike(String name, String text);

    @Query
    List<Author> findByTextIlike(String text, int startIndex, String orderBy);

    @Query
    Page<Author> getPageByNameLike(String name, int pageIndex, int maxResult, String orderBy);
}
