package com.kescom.matrix.core.comm;

public interface IEmailMessage extends ICommMessage {

    String getSubject();

    void setSubject(String subject);
}
