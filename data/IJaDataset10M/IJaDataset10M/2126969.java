package com.vagavaga.sandbox.springjpatest;

public interface IMessageService {

    public abstract void setDefaultMessage(String defaultMessage);

    public abstract String getDefaultMessage();

    public abstract long createMessage(String message) throws AppMessageException;

    public abstract String retreiveMessage(long id);

    public abstract void removeMessage(long id);
}
