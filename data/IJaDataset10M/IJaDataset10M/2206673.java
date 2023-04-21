package org.guestshome.businessobjects.updates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import org.guestshome.commons.Consts;
import org.guestshome.commons.database.DataSourceProvider;
import org.guestshome.commons.database.DefaultFieldsCallabacks;
import org.guestshome.commons.database.EntityManagerProvider;
import org.guestshome.entities.AppSetting;
import org.guestshome.entities.Person;
import org.guestshome.entities.Role;
import org.guestshome.entities.User;
import org.sqlutils.ListCommand;
import org.sqlutils.ListResponse;
import org.sqlutils.jpa.JPAMethods;
import org.sqlutils.jpa.JPASelectStatement;
import org.sqlutils.logger.LogFilter;
import org.sqlutils.logger.Logger;

/**
 * <p>Title: GuestsHome application</p>
 * <p>Description: Business objects used to manage AppSetting entity.</p>
 * <p>Copyright: Copyright (C) 2009 Informatici senza frontiere</p>
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * @author Mauro Carniel
 * @version 1.0
 *
 */
public class AppSettingsBO {

    /**
   * Retrieve the list of application settings. 
   * @return list of AppSetting objects
   * @throws Exception in case of errors
   */
    public ListResponse<AppSetting> getAppSettings(String username, EntityManager em, ListCommand lc) throws Throwable {
        try {
            JPASelectStatement query = new JPASelectStatement("s", "select s from AppSetting s " + "where s.deleted = ?1 ");
            List<Object> params = new ArrayList<Object>();
            params.add(Consts.FLAG_N);
            return JPAMethods.executeQuery(em, query, params, lc, new HashMap<String, String>());
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
	 * Retrieve application language from APP_SETTINGS table. 
	 * @return language id
	 * @throws Exception in case of errors
	 */
    public String getApplicationLanguageId(String username) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.LANGUAGE_ID);
            if (vo == null) throw new Exception("No language id defined in APP_SETTINGS");
            return vo.getValue();
        } catch (Throwable ex) {
            try {
                Logger.error(null, ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Retrieve the timeout check for guests under leaving. 
	 * @return timeout, expressed in days
	 * @throws Exception in case of errors
	 */
    public String getLeavingTimeout(String username) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.LEAVING_TIMEOUT);
            if (vo == null) throw new Exception("No leaving timeout defined in APP_SETTINGS");
            return vo.getValue();
        } catch (Throwable ex) {
            try {
                Logger.error(null, ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
   * Set leaving timeout to APP_SETTINGS table. 
   * param leavingTimeout timeout to set 
   * @throws Exception in case of errors
   */
    public void setLeavingTimeout(String username, String leavingTimeout) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.LEAVING_TIMEOUT);
            if (vo == null) throw new Exception("No leaving timeout defined in APP_SETTINGS");
            vo.setValue(leavingTimeout);
            JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
            em.getTransaction().commit();
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Retrieve the city of facilities. 
	 * @return city code
	 * @throws Exception in case of errors
	 */
    public String getFacilitiesCity(String username) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.FACILITIES_CITY);
            if (vo == null) throw new Exception("No facility city defined in APP_SETTINGS");
            return vo.getValue();
        } catch (Throwable ex) {
            try {
                Logger.error(null, ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
   * Set facilities city to APP_SETTINGS table. 
   * param facilitiesCity facilities city to set 
   * @throws Exception in case of errors
   */
    public void setFacilitiesCity(String username, String facilitiesCity) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.FACILITIES_CITY);
            if (vo == null) throw new Exception("No facility city defined in APP_SETTINGS");
            vo.setValue(facilitiesCity);
            JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
            em.getTransaction().commit();
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Retrieve the country code of facilities.  
	 * @return country code
	 * @throws Exception in case of errors
	 */
    public String getFacilitiesCountry(String username) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.FACILITIES_COUNTRY);
            if (vo == null) throw new Exception("No facilities country defined in APP_SETTINGS");
            return vo.getValue();
        } catch (Throwable ex) {
            try {
                Logger.error(null, ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
   * Set facilities country to APP_SETTINGS table. 
   * param facilitiesCountry facilities country to set 
   * @throws Exception in case of errors
   */
    public void setFacilitiesCountry(String username, String facilitiesCountry) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.FACILITIES_COUNTRY);
            if (vo == null) throw new Exception("No facilities country defined in APP_SETTINGS");
            vo.setValue(facilitiesCountry);
            JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
            em.getTransaction().commit();
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Retrieve the nr of months to preset in available based window filter. 
	 * @return months number
	 * @throws Exception in case of errors
	 */
    public String getAvailableMonths(String username) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.AVAIL_MONTHS);
            if (vo == null) throw new Exception("No available months defined in APP_SETTINGS");
            return vo.getValue();
        } catch (Throwable ex) {
            try {
                Logger.error(null, ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
   * Set available months to APP_SETTINGS table. 
   * param availableMonths available months to set 
   * @throws Exception in case of errors
   */
    public void setAvailableMonths(String username, String availableMonths) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.FACILITIES_CITY);
            if (vo == null) throw new Exception("No available months defined in APP_SETTINGS");
            vo.setValue(availableMonths);
            JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
            em.getTransaction().commit();
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Set application language to APP_SETTINGS table and retrieve translations. 
	 * param languageId language to set 
	 * @throws Exception in case of errors
	 */
    public void setApplicationLanguageId(String username, String languageId) throws Throwable {
        EntityManager em = null;
        try {
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(username, em, AppSetting.class, Consts.LANGUAGE_ID);
            if (vo == null) throw new Exception("No application language id defined in APP_SETTINGS");
            vo.setValue(languageId);
            JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
            em.getTransaction().commit();
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Retrieve version of database schema from APP_SETTINGS table. 
	 * @return version of database schema
	 * @throws Exception in case of errors
	 */
    public int getDatabaseSchema() throws Throwable {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        EntityManager em = null;
        try {
            conn = DataSourceProvider.getInstance().getConnection();
            rset = conn.getMetaData().getTables(null, null, "APP_SETTINGS", new String[] { "TABLE" });
            boolean tableFound = false;
            if (rset.next()) tableFound = true;
            Statement stmt = rset.getStatement();
            rset.close();
            if (stmt != null) stmt.close();
            if (!tableFound) return -1;
            em = EntityManagerProvider.getInstance().getEntityManager();
            AppSetting vo = JPAMethods.find(null, em, AppSetting.class, Consts.DB_VERSION);
            if (vo == null) throw new Exception("No database version defined in APP_SETTINGS");
            return Integer.parseInt(vo.getValue());
        } catch (Throwable ex) {
            try {
                Logger.error(null, ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            return -1;
        } finally {
            try {
                if (rset != null) rset.close();
            } catch (Exception ex1) {
            }
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex1) {
            }
            try {
                if (conn != null) DataSourceProvider.getInstance().releaseConnection(conn);
            } catch (Exception ex1) {
            }
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Update database schema.
	 * @param currentVersion current version of database schema that must be upgraded 
	 * @throws Exception in case of errors
	 */
    public void updateDatabaseSchema(final int currentVersion) throws Throwable {
        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer("");
        Connection conn = null;
        BufferedReader br = null;
        EntityManager em = null;
        try {
            conn = DataSourceProvider.getInstance().getConnection();
            File[] filesToRead = new File(AppSettingsBO.class.getResource("/").getFile()).listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().startsWith("dbscript_") && pathname.getName().endsWith(".sql") && Integer.parseInt(pathname.getName().substring(9, pathname.getName().length() - 4)) > currentVersion;
                }
            });
            Arrays.sort(filesToRead, new Comparator<File>() {

                @Override
                public int compare(File f1, File f2) {
                    return f1.getName().compareTo(f2.getName());
                }
            });
            String lastVer = null;
            if (filesToRead.length > 0) lastVer = filesToRead[filesToRead.length - 1].getName().substring(9, filesToRead[filesToRead.length - 1].getName().length() - 4);
            String line = null;
            ArrayList vals = new ArrayList();
            ArrayList<StringBuffer> fks = new ArrayList<StringBuffer>();
            ArrayList<StringBuffer> indexes = new ArrayList<StringBuffer>();
            int pos = -1;
            String oldfk = null;
            String oldIndex = null;
            StringBuffer newIndex = null;
            StringBuffer newfk = null;
            boolean fkFound = false;
            boolean indexFound = false;
            long time = 0;
            for (int k = 0; k < filesToRead.length; k++) {
                Logger.debug(null, "Updating database schema with script: " + filesToRead[k].getName());
                time = System.currentTimeMillis();
                sql.delete(0, sql.length());
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filesToRead[k])));
                vals.clear();
                fks.clear();
                indexes.clear();
                while ((line = br.readLine()) != null) {
                    sql.append(' ').append(line);
                    if (line.endsWith(";")) {
                        sql = replace(sql, " DATE ", " DATETIME ");
                        sql = replace(sql, "ON DELETE NO ACTION", "");
                        sql = replace(sql, "ON UPDATE NO ACTION", "");
                        sql = replace(sql, "DEFAULT SYSDATE()", "");
                        if (sql.indexOf(":DATE") != -1) {
                            sql = replace(sql, ":DATE", "?");
                            vals.add(new java.sql.Timestamp(System.currentTimeMillis()));
                        }
                        fkFound = false;
                        while ((pos = sql.indexOf("FOREIGN KEY")) != -1) {
                            oldfk = sql.substring(pos, sql.indexOf(")", sql.indexOf(")", pos) + 1) + 1);
                            sql = replace(sql, oldfk, "");
                            newfk = new StringBuffer("ALTER TABLE ");
                            newfk.append(sql.substring(sql.indexOf(" TABLE ") + 7, sql.indexOf("(")).trim());
                            newfk.append(" ADD ");
                            newfk.append(oldfk);
                            fks.add(newfk);
                            fkFound = true;
                        }
                        if (fkFound) sql = removeCommasAtEnd(sql);
                        indexFound = false;
                        while ((pos = sql.indexOf("INDEX ")) != -1) {
                            oldIndex = sql.substring(pos, sql.indexOf(")", pos) + 1);
                            sql = replace(sql, oldIndex, "");
                            newIndex = new StringBuffer("CREATE ");
                            newIndex.append(oldIndex.substring(0, oldIndex.indexOf("(")));
                            newIndex.append(" ON ");
                            newIndex.append(sql.substring(sql.indexOf(" TABLE ") + 7, sql.indexOf("(")).trim());
                            newIndex.append(oldIndex.substring(oldIndex.indexOf("(")));
                            indexes.add(newIndex);
                            indexFound = true;
                        }
                        if (indexFound) sql = removeCommasAtEnd(sql);
                        if (sql.toString().trim().length() > 0) {
                            pstmt = conn.prepareStatement(sql.toString().substring(0, sql.length() - 1));
                            for (int i = 0; i < vals.size(); i++) {
                                pstmt.setObject(i + 1, vals.get(i));
                            }
                            pstmt.execute();
                            pstmt.close();
                        }
                        sql.delete(0, sql.length());
                        vals.clear();
                    }
                }
                br.close();
                for (int i = 0; i < fks.size(); i++) {
                    sql = (StringBuffer) fks.get(i);
                    pstmt = conn.prepareStatement(sql.toString());
                    try {
                        pstmt.execute();
                    } catch (SQLException ex4) {
                        System.out.println(ex4.toString());
                    }
                    pstmt.close();
                }
                for (int i = 0; i < indexes.size(); i++) {
                    sql = (StringBuffer) indexes.get(i);
                    pstmt = conn.prepareStatement(sql.toString());
                    try {
                        pstmt.execute();
                    } catch (SQLException ex3) {
                        System.out.println(ex3.toString());
                    }
                    pstmt.close();
                }
                Logger.debug(null, "Schema updated in " + ((System.currentTimeMillis() - time) / 1000) + " seconds");
            }
            conn.commit();
            em = EntityManagerProvider.getInstance().getEntityManager();
            if (lastVer != null) {
                AppSetting vo = JPAMethods.find(null, em, AppSetting.class, Consts.DB_VERSION);
                if (vo == null) {
                    vo = new AppSetting();
                    vo.setCode(Consts.DB_VERSION);
                    vo.setDescription("Database version");
                    vo.setIsVisible(Consts.FLAG_N);
                    vo.setValue(lastVer);
                    JPAMethods.persist(em, "UNDEFINED", DefaultFieldsCallabacks.getInstance(), vo);
                } else {
                    vo.setValue(lastVer);
                    JPAMethods.merge(em, "UNDEFINED", DefaultFieldsCallabacks.getInstance(), vo);
                }
            } else Logger.debug(null, "Database schema is already updated.");
            em.getTransaction().commit();
        } catch (Throwable ex) {
            try {
                if (br != null) br.close();
            } catch (Exception ex2) {
            }
            try {
                Logger.error(null, "Invalid SQL: " + sql + "\n" + ex.getMessage(), ex);
            } catch (Exception ex2) {
            }
            throw ex;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ex1) {
            }
            try {
                if (conn != null) DataSourceProvider.getInstance().releaseConnection(conn);
            } catch (Exception ex1) {
            }
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Remove "," symbols at the end of the script.
	 * Example: "INDEX WKF10_INSTANCE_PROPERTIES_FKIndex2(PROGRESSIVE_WKF01, PROGRESSIVE_WKF08),  , );
	 * @param sql script to analyze
	 * @return sql script, without "," symbols at the end
	 */
    private StringBuffer removeCommasAtEnd(StringBuffer sql) {
        int i = sql.length() - 3;
        while (i > 0 && (sql.charAt(i) == ' ' || sql.charAt(i) == ',')) i--;
        sql = sql.replace(i + 1, sql.length() - 2, " ");
        return sql;
    }

    /**
	 * Replace the specified pattern with the new one.
	 * @param b sql script
	 * @param oldPattern pattern to replace
	 * @param newPattern new pattern
	 * @return sql script with substitutions
	 */
    private StringBuffer replace(StringBuffer b, String oldPattern, String newPattern) {
        int i = 0;
        while ((i = b.indexOf(oldPattern, i)) != -1) {
            b.replace(i, i + oldPattern.length(), newPattern);
            i = i + oldPattern.length();
        }
        return b;
    }
}
