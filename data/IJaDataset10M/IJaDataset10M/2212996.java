package org.nbnResolving.database.dao;

import java.util.List;
import org.nbnResolving.database.model.TableURL;

/**
 * EPICUR: Tabelle URL (DAO Interface)
 * 
 * @author t_kleinm, German National Library 27.06.2011
 */
public interface UrlDaoIf {

    /**
	 * Urls (enthalten sind auch fehlerhafte)
	 * 
	 * @param urnId
	 * @return List<TableURL>
	 * @author kleinm, German National Library
	 */
    public List<TableURL> getUrlsByUrnId(long urnId);

    /**
	 * Fehlerhafte Urls
	 * 
	 * @param urnId
	 * @return List<TableURL>
	 * @author kleinm, German National Library
	 */
    public List<TableURL> getFaultyUrlsByUrnId(long urnId);

    /**
	 * @param urnId
	 * @return Anzahl Urls als Integer
	 * @author t_kleinm, German National Library
	 */
    public int getNumberOfUrlsByUrnId(long urnId);

    /**
	 * @param urnId
	 * @return Anzahl fehlerhafter Urls als Integer
	 * @author t_kleinm, German National Library
	 */
    public int getNumberOfFaultyUrlsByUrnId(long urnId);

    /**
	 * @param urlStr
	 * @author t_kleinm, German National Library
	 */
    public void deleteUrl(String urlStr);

    /**
	 * Urls
	 * 
	 * @param urlStr
	 *            URL-Teilstring
	 * @return List<TableURL>
	 * @author kleinm, German National Library
	 */
    public List<TableURL> getUrlsByUrl(String urlStr);

    /**
	 * @param urlStr
	 * @return Url
	 * @author t_kleinm, German National Library
	 */
    public TableURL getUrlByUrl(String urlStr);

    /**
	 * @param url
	 * @param urlStr
	 *            URL-String als Key der URL-Tabelle
	 * @author kleinm, German National Library
	 */
    public void updateUrl(TableURL url, String urlStr);

    /**
	 * @param url
	 * @author kleinm, German National Library
	 */
    public void insertUrl(TableURL url);
}
