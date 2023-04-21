package com.c2b2.ipoint.messaging;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
  * This class retrieves Messages Resources from the configuration file
  * for the generation of standard messages
  * <p>
  * $Date: 2005/12/26 21:13:05 $
  * 
  * $Id: MessageResources.java,v 1.1 2005/12/26 21:13:05 steve Exp $<br/>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  */
public class MessageResources {

    /**
   * Default Constructor
   */
    public MessageResources() {
        prb = (PropertyResourceBundle) ResourceBundle.getBundle("com/c2b2/ipoint/messaging/StandardMessages");
    }

    public String getMessage(String key) {
        return prb.getString(key);
    }

    private PropertyResourceBundle prb;
}
