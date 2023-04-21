package com.loribel.commons.abstraction;

import com.loribel.commons.exception.GB_LoadException;

/**
 * Abstraction of a String content.
 * 
 * @author Gregory Borelli
 */
public interface GB_StringContent {

    String getContent() throws GB_LoadException;

    void resetContent();
}
