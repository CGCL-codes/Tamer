package org.vqwiki;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generic interface for actions
 *
 * Copyright 2004, Gareth Cronin
 *
 * @author $Author: wrh2 $
 *         Last Modified: $Date: 2006-04-23 09:52:28 +0200 (zo, 23 apr 2006) $
 *         $Id: WikiAction.java 644 2006-04-23 07:52:28Z wrh2 $
 */
public interface WikiAction {

    /**
     * Carry out the action resulting from the given request and then pass back the response
     * @param request request
     * @param response response
     * @throws Exception any exception that occurs in the action
     */
    void doAction(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
