package com.kanjiportal.portal.dao;

import com.kanjiportal.portal.model.Knowledge;
import com.kanjiportal.portal.model.Tag;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Nickho
 * Date: Jan 31, 2009
 * Time: 10:57:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KnowledgeDao {

    public Knowledge findByKanjiAndTagForUser(String kanji, String tag, String username);

    public List<Knowledge> findByTagForUser(Tag tag, String username, int page, int pageSize);

    public Knowledge update(Knowledge knowledge);

    public void create(Knowledge knowledge);

    public void delete(Knowledge knowledge);
}
