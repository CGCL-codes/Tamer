package fileimport.impl;

import javax.servlet.http.HttpServletRequest;
import fileimport.IImportRequest;

public class ImportRequestImpl implements IImportRequest {

    private HttpServletRequest request;

    public ImportRequestImpl(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getHttpServletRequest() {
        return this.request;
    }
}
