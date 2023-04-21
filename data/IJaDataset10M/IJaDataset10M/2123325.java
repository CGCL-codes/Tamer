package com.anasoft.os.sample.dmf.service;

import java.io.Serializable;

public interface Command<T extends Result> extends Serializable {

    T execute() throws RemoteServiceException;
}
