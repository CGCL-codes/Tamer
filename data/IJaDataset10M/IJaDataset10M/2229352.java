package com.kwoksys.biz.kb.dao;

import com.kwoksys.biz.base.BaseQueries;
import com.kwoksys.framework.connection.database.QueryBits;

/**
 * Format: select_, add_, update_, delete_, _Query
 */
public class KbQueries extends BaseQueries {

    public static String getOrderByColumn(String column) {
        if (column.equals("category_name")) {
            return "lower(category_name)";
        } else if (column.equals("article_name")) {
            return "lower(article_name)";
        } else {
            return column;
        }
    }

    /**
     * Returns a list of Articles.
     *
     * @return ..
     */
    public static String selectArticleListQuery(QueryBits query) {
        return "select a.article_id, a.article_name, a.article_text, a.category_id, a.category_name, a.article_syntax_type, a.view_count, " + "a.creator, a.creator_username, a.creator_display_name, a.creation_date, " + "a.modifier, a.modifier_username, a.modifier_display_name, a.modification_date " + "from kb_article_view a " + query.createWhereClause();
    }

    /**
     * Returns the number of Articles.
     *
     * @return ..
     */
    public static String selectArticlesCountQuery(QueryBits query) {
        return "select count(a.article_id) as row_count from kb_article a " + query.createWhereClause();
    }

    /**
     * Returns Article detail.
     *
     * @return ..
     */
    public static String selectArticleDetailQuery(QueryBits query) {
        return "select a.article_id, a.article_name, a.article_text, a.category_id, a.category_name, a.article_syntax_type, a.view_count, " + "a.creator, a.creator_username, a.creator_display_name, a.creation_date, " + "a.modifier, a.modifier_username, a.modifier_display_name, a.modification_date " + "from kb_article_view a " + query.createWhereClause();
    }

    /**
     * Adds Article.
     */
    public static String addArticleQuery() {
        return "{call sp_kb_article_add(?,?,?,?,?,?,?)}";
    }

    /**
     * Updates Article.
     */
    public static String updateArticleQuery() {
        return "{call sp_kb_article_update(?,?,?,?,?,?,?)}";
    }

    /**
     * Deletes Article.
     */
    public static String deleteArticleQuery() {
        return "{call sp_kb_article_delete(?,?,?,?)}";
    }

    /**
     * Updates Article view count.
     */
    public static String updateArticleViewCountQuery() {
        return "{call sp_kb_article_increment_view_count(?)}";
    }
}
