package org.riverock.webmill.portal.dao;

import java.util.Map;
import java.util.List;
import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id: InternalXsltDao.java,v 1.9 2006/06/05 19:18:57 serg_main Exp $
 */
public interface InternalXsltDao {

    public Map<String, XsltTransformer> getTransformerForCurrentXsltMap(Long siteId);

    public StringBuilder getXsltData(Long xsltId);

    /** key is language of site */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId);

    public Xslt getCurrentXslt(Long siteLanguageId);

    public Xslt getXslt(Long xsltId);

    public Xslt getXslt(String xsltName, Long siteLanguageId);

    public Long createXslt(Xslt xslt);

    public List<Xslt> getXsltList(Long siteLanguageId);

    public void deleteXsltForSite(DatabaseAdapter adapter, Long siteId);

    public void deleteXsltForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);

    public void updateXslt(Xslt xslt);

    public void deleteXslt(Long xsltId);
}
