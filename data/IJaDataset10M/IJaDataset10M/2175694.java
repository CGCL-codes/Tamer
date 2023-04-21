package com.tutorial.demo.service.impl;

import java.util.Collection;
import java.util.List;
import com.tutorial.demo.model.BookModel;
import com.tutorial.demo.service.BookManager;

public class BookManagerImpl implements BookManager {

    @Override
    public BookModel get(Long id) {
        return null;
    }

    @Override
    public List<BookModel> getAll() {
        return null;
    }

    @Override
    public List<BookModel> findByExample(BookModel example) {
        return null;
    }

    @Override
    public BookModel save(BookModel model) {
        return null;
    }

    @Override
    public List<BookModel> saveAll(Collection<BookModel> models) {
        return null;
    }

    @Override
    public void remove(BookModel model) {
    }

    @Override
    public void removeAll(Collection<BookModel> models) {
    }

    @Override
    public void removeByPk(Long id) {
    }

    @Override
    public void removeAllByPk(Collection<Long> ids) {
    }
}
