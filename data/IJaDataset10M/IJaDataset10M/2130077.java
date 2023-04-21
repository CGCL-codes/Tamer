package com.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {

    public void execute(HttpServletRequest request, HttpServletResponse response, String action) throws Exception;
}
