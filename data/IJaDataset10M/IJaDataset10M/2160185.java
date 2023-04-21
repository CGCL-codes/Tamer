package com.google.code.booktogether.service;

import java.util.List;
import com.google.code.booktogether.web.domain.Book;

public interface RecoBookService {

    public boolean insertRecoBook(Integer bookIdNum);

    public List<Book> getListRecoBook();
}
