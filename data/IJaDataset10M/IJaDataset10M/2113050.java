package com.nyandu.weboffice.mail.business;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: alvaro
 * Date: 11/01/2005
 * Time: 04:35:21 PM
 */
public class ByteArrayDataSource implements DataSource {

    private String contentType;

    private byte data[];

    public ByteArrayDataSource(String contentType, String text) {
        this.contentType = contentType;
        this.data = text.getBytes();
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    public String getName() {
        return "dummy";
    }

    public OutputStream getOutputStream() throws IOException {
        return null;
    }
}
