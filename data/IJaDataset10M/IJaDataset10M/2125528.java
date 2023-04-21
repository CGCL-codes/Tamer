package ua.lendon.blog.dao;

import java.util.List;
import ua.lendon.blog.domain.Article;
import ua.lendon.blog.domain.Tag;

public interface ArticleDao {

    Article addArticle(Article artilce);

    void updateArticle(Article article);

    List<Article> list();

    List<Article> listByTag(int tagId);

    Article getArticle(int id);

    void updateTags(int articleId, int[] tagIds);

    void deleteArticle(int id);

    void addTag(int articleId, Tag tag);

    void addTag(int articleId, int tagId);
}
