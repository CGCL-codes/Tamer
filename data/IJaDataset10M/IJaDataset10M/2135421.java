package com.jujunie.service.web;

import com.jujunie.service.log.Log;
import com.jujunie.service.xml.InvalidXMLElementException;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

/**
 * Display handler that outputs JSON content.
 * This display handler retreive the POJO in the request using the key
 * {@link JSONDisplayHandlerXML#POJO_KEY}
 * @author julien
 * @since 1.01.01
 */
public class JSONDisplayHandlerXML extends WebElementXML implements DisplayHandlerXML, Serializable {

    private static final long serialVersionUID = -5668584532045506416L;

    public static final String POJO_KEY = "com.jujunie.service.web.JSONDisplayHandlerXML#POJO_KEY";

    /** Code */
    private String code = "";

    /**
     * @see DisplayHandlerXML#init(org.w3c.dom.Element);
     */
    public void init(Element displayHandlerNode) throws WebConfigException, InvalidXMLElementException {
        this.code = super.getAttribute(displayHandlerNode, ATT_CODE, true);
    }

    /**
     * @see DisplayHandlerXML#display(com.jujunie.service.web.WebPage, 
     * javax.servlet.http.HttpServletRequest, 
     * javax.servlet.http.HttpServletResponse) 
     */
    public void display(WebPage page, HttpServletRequest req, HttpServletResponse resp) throws DisplayException {
        page.getDisplayInitialiser().initDisplay(new HttpRequestDisplayContext(req), req);
        resp.setContentType("application/json; charset=" + page.getEncoding());
        resp.setCharacterEncoding(page.getEncoding());
        Object pojo = req.getAttribute(POJO_KEY);
        if (pojo != null) {
            JSONObject json = new JSONObject(pojo);
            try {
                json.write(resp.getWriter());
            } catch (JSONException e) {
                String message = "Got JSON error while serializing POJO using JSON: " + e.getMessage();
                Log.getInstance(this).error(message, e);
                throw new DisplayException(message, e);
            } catch (IOException e) {
                String message = "Got IO error while serializing POJO using JSON: " + e.getMessage();
                Log.getInstance(this).error(message, e);
                throw new DisplayException(message, e);
            }
        } else {
            throw new DisplayException("Cannot find an Object in request using" + " JSONDisplayHandlerXML#POJO_KEY key");
        }
    }

    /**
     * @see com.jujunie.service.web.DisplayHandlerXML#getCode() 
     */
    public String getCode() {
        return this.code;
    }
}
