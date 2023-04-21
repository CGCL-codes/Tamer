package com.liferay.portal.kernel.util;

import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 * <a href="Database.java.html"><b><i>View Source</i></b></a>
 *
 * @author Ganesh Ram
 *
 */
public interface Database {

    public String getType();

    public void runSQLTemplate(String path) throws IOException, NamingException, SQLException;

    public void runSQLTemplate(String path, boolean failOnError) throws IOException, NamingException, SQLException;
}
