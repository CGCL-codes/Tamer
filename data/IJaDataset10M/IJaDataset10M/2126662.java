package com.nex.content.xtm;

import org.xml.sax.AttributeList;

/**
 * Title:        Nexist
 * Description:  Collaboratory testbed
 * Copyright:    Copyright (c) 2001 Jack Park
 * Company:      nex
 * @author       Jack Park
 * @version 1.0
 * @license  NexistLicense (based on Apache)
 */
public interface iXTMParserHandler {

    void handleEndElement(String name);

    void handleStartElement(String name, AttributeList attrs);

    void handlePI(String target, String data);

    void handleStartDocument();

    void handleEndDocument();

    void handleCharacters(char ch[], int start, int length);
}
