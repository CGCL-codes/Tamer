package com.nyandu.weboffice.mail.database;

import com.nyandu.weboffice.mail.business.MailMessage;
import com.nyandu.weboffice.common.database.DAOException;
import com.nyandu.weboffice.common.database.IBaseDAO;

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
 * User: ern
 * Date: Mar 15, 2005
 * Time: 4:00:06 PM
 */
public interface IMailMessageDAO extends IBaseDAO {

    MailMessage[] selectMailMessagesByAccount(int accountId) throws DAOException;

    MailMessage[] selectMailMessagesByAccountAndFolder(int accountId, int folderId) throws DAOException;

    MailMessage[] selectMailMessagesByUser(int userId, int folderId) throws DAOException;

    MailMessage selectMailMessages(int id) throws DAOException;

    MailMessage insertMailMessage(MailMessage mail) throws DAOException;

    /**
	 * Change current the folder of the mail to folderId
	 * @param mailId
	 * @param folderId The new folder where the mail will be contained.
	 * @throws DAOException
	 */
    void updateMailMessage(int mailId, int folderId) throws DAOException;

    void updateMailMessageFlag(int mailId, String flag, boolean value) throws DAOException;

    void deleteMailMessage(int mailId) throws DAOException;

    long selectSizeByUser(int userId) throws DAOException;
}
