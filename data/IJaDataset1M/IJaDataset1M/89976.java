package org.blogsomy.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.code.lightsomy.annotations.RequestEntryFilter;

@RequestEntryFilter
public class EntryFilter {

    @SuppressWarnings("unused")
    private HttpServletRequest request;

    @SuppressWarnings("unused")
    private HttpServletResponse response;

    public void filter() throws IOException {
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
