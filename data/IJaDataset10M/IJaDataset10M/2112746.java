package net.sf.wicketdemo.service;

import java.io.Serializable;
import java.util.UUID;
import net.sf.wicketdemo.domain.Book;
import net.sf.wicketdemo.tech.service.BaseService;

/**
 * @author Dieter D'haeyere
 */
class BookServiceImpl extends BaseService<Book, UUID> implements BookService, Serializable {

    private static final long serialVersionUID = 1L;
}
