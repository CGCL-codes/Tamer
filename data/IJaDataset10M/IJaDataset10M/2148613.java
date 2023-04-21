package com.infojarda.gbasedwiki.web.server.managers;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.datastore.Text;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.infojarda.gbasedwiki.web.client.dtos.WikiPageDTO;
import com.infojarda.gbasedwiki.web.client.services.WikiService;
import com.infojarda.gbasedwiki.web.server.PMF;
import com.infojarda.gbasedwiki.web.server.entities.WikiPage;

public class WikiPageManager extends RemoteServiceServlet implements WikiService {

    @Override
    public Long createWikiPage(WikiPageDTO wikiPageDTO) {
        WikiPage result = null;
        String pageTitle = wikiPageDTO.getPageTitle();
        String author = wikiPageDTO.getAuthor();
        Long parentId = wikiPageDTO.getParentId();
        WikiPage page = new WikiPage(pageTitle, author, parentId);
        page.setContent(new Text(wikiPageDTO.getContent()));
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            result = pm.makePersistent(page);
        } finally {
            pm.close();
        }
        if (null != result) {
            return result.getId();
        } else return new Long(-1);
    }

    @Override
    public ArrayList<WikiPageDTO> readWikiPages() {
        ArrayList<WikiPageDTO> wikiPages = new ArrayList<WikiPageDTO>(0);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(WikiPage.class);
            try {
                List<WikiPage> results = (List<WikiPage>) query.execute();
                GWT.log(results.toString(), null);
                if (results.iterator().hasNext()) {
                    for (WikiPage e : results) {
                        WikiPageDTO wikiDTO = new WikiPageDTO();
                        wikiDTO.setAuthor(e.getAuthor());
                        wikiDTO.setId(e.getId());
                        wikiDTO.setPageTitle(e.getPageTitle());
                        wikiPages.add(wikiDTO);
                    }
                }
            } catch (Exception e) {
                GWT.log("ajjaj", e);
            } finally {
                query.closeAll();
            }
        } finally {
            pm.close();
        }
        return wikiPages;
    }

    @Override
    public String deleteWikiPage(Long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.deletePersistent(pm.getObjectById(WikiPage.class, id));
        } finally {
            pm.close();
        }
        return "Hello, ";
    }

    @Override
    public WikiPageDTO getWikiPage(Long id) {
        WikiPageDTO wikiPageToReturn = new WikiPageDTO();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(WikiPage.class);
            query.setFilter("id == idParam");
            query.declareParameters("Long idParam");
            try {
                List<WikiPage> wpList = (List<WikiPage>) query.execute(id);
                WikiPage wp = wpList.get(0);
                wikiPageToReturn.setAuthor(wp.getAuthor());
                wikiPageToReturn.setCreatedOn(wp.getCreatedOn());
                wikiPageToReturn.setId(wp.getId());
                wikiPageToReturn.setLastModifiedBy(wp.getLastModifiedBy());
                wikiPageToReturn.setLastModifiedOn(wp.getLastModifiedOn());
                wikiPageToReturn.setPageTitle(wp.getPageTitle());
                wikiPageToReturn.setParentId(wp.getParentId());
                wikiPageToReturn.setContent(wp.getContent().getValue());
            } finally {
                query.closeAll();
            }
        } finally {
            pm.close();
        }
        return wikiPageToReturn;
    }

    @Override
    public String updateWikiPage(WikiPageDTO wikiPageDTO) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            WikiPage wp = pm.getObjectById(WikiPage.class, wikiPageDTO.getId());
            wp.setContent(new Text(wikiPageDTO.getContent()));
        } finally {
            pm.close();
        }
        return null;
    }

    @Override
    public ArrayList<WikiPageDTO> getRootWikiPages() {
        ArrayList<WikiPageDTO> wikiPages = new ArrayList<WikiPageDTO>(0);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(WikiPage.class);
            query.setFilter("parentId == idParam");
            query.declareParameters("Long idParam");
            try {
                List<WikiPage> results = (List<WikiPage>) query.execute(new Long(0));
                GWT.log(results.toString(), null);
                if (results.iterator().hasNext()) {
                    for (WikiPage e : results) {
                        WikiPageDTO wikiDTO = new WikiPageDTO();
                        wikiDTO.setAuthor(e.getAuthor());
                        wikiDTO.setId(e.getId());
                        wikiDTO.setPageTitle(e.getPageTitle());
                        wikiPages.add(wikiDTO);
                    }
                }
            } catch (Exception e) {
                GWT.log("ajjaj", e);
            } finally {
                query.closeAll();
            }
        } finally {
            pm.close();
        }
        return wikiPages;
    }
}
