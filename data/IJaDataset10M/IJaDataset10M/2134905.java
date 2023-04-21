package net.sf.brightside.beautyshop.service;

import net.sf.brightside.beautyshop.exceptions.BusinessException;

public interface Command<T> {

    public T execute() throws BusinessException;
}
