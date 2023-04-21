package org.ironrhino.common.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ironrhino.common.model.Page;
import org.ironrhino.core.cache.CheckCache;
import org.ironrhino.core.cache.FlushCache;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.search.compass.CompassSearchCriteria;
import org.ironrhino.core.search.compass.CompassSearchService;
import org.ironrhino.core.service.BaseManagerImpl;
import org.ironrhino.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;

@Singleton
@Named("pageManager")
public class PageManagerImpl extends BaseManagerImpl<Page> implements PageManager {

    @Autowired(required = false)
    private transient CompassSearchService<Page> compassSearchService;

    @Override
    @Transactional
    @FlushCache(key = "${page.path}", namespace = "page", renew = "${page}")
    public void save(Page page) {
        page.setDraft(null);
        page.setDraftDate(null);
        super.save(page);
    }

    @Override
    @Transactional
    @FlushCache(key = "${page.path}", namespace = "page")
    public void delete(Page page) {
        super.delete(page);
    }

    @Transactional(readOnly = true)
    @CheckCache(key = "${path}", namespace = "page", eternal = true)
    public Page getByPath(String path) {
        Page page = findByNaturalId(path);
        if (page != null) page.setDraft(null);
        return page;
    }

    @Transactional
    public Page saveDraft(Page page) {
        Page p = get(page.getId());
        boolean isnew = false;
        if (p == null) {
            isnew = true;
            p = page;
        }
        p.setDraftDate(new Date());
        Map<String, String> draft = new HashMap<String, String>();
        draft.put("path", page.getPath());
        draft.put("title", page.getTitle());
        draft.put("content", page.getContent());
        p.setDraft(JsonUtils.toJson(draft));
        if (isnew) {
            p.setTitle("");
            p.setContent("");
        }
        super.save(p);
        return p;
    }

    @Transactional(readOnly = true)
    public Page getDraftByPath(String path) {
        Page page = findByNaturalId(path);
        if (page == null || StringUtils.isBlank(page.getDraft())) return null;
        pullDraft(page);
        return page;
    }

    @Transactional
    public Page dropDraft(String id) {
        Page page = get(id);
        page.setDraft(null);
        page.setDraftDate(null);
        super.save(page);
        return page;
    }

    public void pullDraft(Page page) {
        try {
            Map<String, String> map = JsonUtils.fromJson(page.getDraft(), new TypeReference<Map<String, String>>() {
            });
            page.setPath(map.get("path"));
            page.setTitle(map.get("title"));
            page.setContent(map.get("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Page> findListByTag(String tag) {
        return findListByTag(new String[] { tag });
    }

    @SuppressWarnings("unchecked")
    public List<Page> findListByTag(String... tag) {
        if (tag.length == 0 || StringUtils.isBlank(tag[0])) return Collections.EMPTY_LIST;
        List<Page> list;
        if (compassSearchService != null) {
            String query = null;
            if (tag.length == 1) {
                query = "tags:" + tag[0];
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("tags:").append(tag[0]);
                for (int i = 1; i < tag.length; i++) sb.append(" AND ").append("tags:").append(tag[i]);
                query = sb.toString();
            }
            CompassSearchCriteria criteria = new CompassSearchCriteria();
            criteria.setQuery(query);
            criteria.setAliases(new String[] { "page" });
            list = compassSearchService.search(criteria);
        } else {
            DetachedCriteria dc = detachedCriteria();
            for (int i = 0; i < tag.length; i++) {
                dc.add(Restrictions.or(Restrictions.eq("tagsAsString", tag[i]), Restrictions.or(Restrictions.like("tagsAsString", tag[i] + ",", MatchMode.START), Restrictions.or(Restrictions.like("tagsAsString", "," + tag[i], MatchMode.END), Restrictions.like("tagsAsString", "," + tag[i] + ",", MatchMode.ANYWHERE)))));
            }
            list = findListByCriteria(dc);
        }
        Collections.sort(list);
        return list;
    }

    public ResultPage<Page> findResultPageByTag(ResultPage<Page> resultPage, String tag) {
        return findResultPageByTag(resultPage, new String[] { tag });
    }

    @SuppressWarnings("unchecked")
    public ResultPage<Page> findResultPageByTag(ResultPage<Page> resultPage, String... tag) {
        if (tag.length == 0 || StringUtils.isBlank(tag[0])) {
            resultPage.setResult(Collections.EMPTY_LIST);
            return resultPage;
        }
        String query = null;
        if (tag.length == 1) {
            query = "tags:" + tag[0];
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("tags:").append(tag[0]);
            for (int i = 1; i < tag.length; i++) sb.append(" AND ").append("tags:").append(tag[i]);
            query = sb.toString();
        }
        CompassSearchCriteria criteria = (CompassSearchCriteria) resultPage.getCriteria();
        if (criteria == null) {
            criteria = new CompassSearchCriteria();
            resultPage.setCriteria(criteria);
        }
        criteria.setQuery(query);
        criteria.setAliases(new String[] { "page" });
        if (criteria.getSorts().size() == 0) criteria.addSort("displayOrder", false);
        if (compassSearchService != null) {
            resultPage = compassSearchService.search(resultPage);
        } else {
            DetachedCriteria dc = detachedCriteria();
            for (int i = 0; i < tag.length; i++) {
                dc.add(Restrictions.or(Restrictions.eq("tagsAsString", tag[i]), Restrictions.or(Restrictions.like("tagsAsString", tag[i] + ",", MatchMode.START), Restrictions.or(Restrictions.like("tagsAsString", "," + tag[i], MatchMode.END), Restrictions.like("tagsAsString", "," + tag[i] + ",", MatchMode.ANYWHERE)))));
            }
            for (Map.Entry<String, Boolean> entry : criteria.getSorts().entrySet()) dc.addOrder(entry.getValue() ? Order.desc(entry.getKey()) : Order.asc(entry.getKey()));
            resultPage.setCriteria(dc);
            resultPage = findByResultPage(resultPage);
        }
        return resultPage;
    }

    public Map<String, Integer> findMatchedTags(String keyword) {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        if (compassSearchService != null) {
            CompassSearchCriteria cc = new CompassSearchCriteria();
            cc.setQuery(new StringBuilder("tags:").append("*").append(keyword).append("*").toString());
            cc.setAliases(new String[] { "page" });
            List<Page> list = compassSearchService.search(cc);
            for (Page p : list) {
                for (String tag : p.getTags()) {
                    if (!tag.contains(keyword)) continue;
                    Integer count = map.get(tag);
                    if (count != null) map.put(tag, map.get(tag) + 1); else map.put(tag, 1);
                }
            }
        } else {
            List<Page> list = findAll();
            for (Page p : list) {
                for (String tag : p.getTags()) {
                    if (!tag.contains(keyword)) continue;
                    Integer count = map.get(tag);
                    if (count != null) map.put(tag, map.get(tag) + 1); else map.put(tag, 1);
                }
            }
        }
        Map<String, Integer> sortedMap = new TreeMap<String, Integer>(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                Integer i1 = map.get(o1);
                Integer i2 = map.get(o2);
                if (i1 == null) return 1;
                if (i2 == null) return -1;
                return i2.compareTo(i1);
            }
        });
        sortedMap.putAll(map);
        return sortedMap;
    }
}
