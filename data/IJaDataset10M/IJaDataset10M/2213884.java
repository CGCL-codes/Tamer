package com.germinus.xpression.service.core;

import javax.servlet.http.HttpServletRequest;

public interface FileDirectoryServiceFactory {

    public FileDirectoryService buildFileDirectoryService(HttpServletRequest request);
}
