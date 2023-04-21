package br.com.arsmachina.esculentus.controller.impl;

import br.com.arsmachina.controller.impl.SpringControllerImpl;
import br.com.arsmachina.esculentus.entity.BookmarkOwner;
import java.lang.Integer;
import br.com.arsmachina.esculentus.dao.BookmarkOwnerDAO;
import br.com.arsmachina.esculentus.controller.BookmarkOwnerController;

/**
 * Default {@link BookmarkOwnerController} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class BookmarkOwnerControllerImpl extends SpringControllerImpl<BookmarkOwner, Integer> implements BookmarkOwnerController {

    /**
	 * Single constructor of this class.
	 * 
	 * @param sessionFactory a {@link BookmarkOwnerDAO}. It cannot be null.
	 */
    public BookmarkOwnerControllerImpl(BookmarkOwnerDAO dao) {
        super(dao);
    }
}
