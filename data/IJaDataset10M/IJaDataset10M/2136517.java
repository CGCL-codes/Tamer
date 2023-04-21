package com.acciente.induction.view;

import javax.servlet.http.HttpServletResponse;

/**
 * This view interface should be implemented to send an image to the browser. This interface
 * provided maximum control over how the image data is handed over to Induction.
 *
 * @created Mar 9, 2008

 * @author Adinath Raveendra Raj
 */
public interface ImageStream {

    /**
    * This method when called by Induction, should write the image contents
    * to the servlet response passed to the method
    *
    * @param oResponse the servlet response to which the image data should be sent
    */
    void writeImage(HttpServletResponse oResponse);

    /**
    * This method should return the mime type of the text content
    *
    * @return a string representation of a mime type, e.g. text/html
    */
    String getMimeType();
}
