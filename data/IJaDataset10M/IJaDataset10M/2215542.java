package kr.or.javacafe.board.bo;

import java.util.Collection;
import java.util.List;
import kr.or.javacafe.board.domain.*;

public interface BoardBO {

    public List<Article> getPreviewNoticeArticle();

    public List<Article> getPreviewRecentArticle();

    public ArticleFile getDownloadFile(int fileSeq);

    public void addArticle(Article article, List<ArticleFile> objFileList);

    public void editArticle(Article article, List<ArticleFile> objFileList);

    public void removeArticle(Article article, ArticleFile file);

    public void removeArticleFile(ArticleFile file);

    public List<Article> listArticle(Article article);

    public int getListArticleCount(Article article);

    public Bbs getBbsInfo(String bbsId);

    public Article viewArticle(int atcNo);

    public void hitArticle(int atcNo);

    public List<Comment> listComent(Comment comment);

    public void addComment(Comment comment);

    public void removeComment(Comment comment);
}
