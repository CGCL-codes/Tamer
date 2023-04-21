package org.wcb.model.service;

import java.io.File;

/**
 * <small>
 * Copyright (c)  2006  wbogaardt.
 * Permission is granted to copy, distribute and/or modify this document
 * under the terms of the GNU Free Documentation License, Version 1.2
 * or any later version published by the Free Software Foundation;
 * with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
 * Texts.  A copy of the license is included in the section entitled "GNU
 * Free Documentation License".
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Mar 24, 2006 1:19:38 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Mar 24, 2006
 *          Time: 1:19:38 PM
 */
public interface IFTPClientService {

    public void setHostAddress(String hostAddress);

    public String getHostAddress();

    public int getHostPort();

    public void setHostPort(int hostPort);

    public String getUserName();

    public void setUserName(String userName);

    public String getPassword();

    public void setPassword(String password);

    public void getFile(String remoteFileName, File localFile);

    public void putFile(File localFile, String remoteFileName, boolean binaryTransfer);
}
