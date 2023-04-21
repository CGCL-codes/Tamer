package org.or5e.core.dsource;

import java.util.List;
import org.or5e.core.dsource.dao.CommentDAO;

public interface CommentManager {

    public List<CommentDAO> getAllCommentsForVideo(Long videoID);

    public Boolean publishComment(CommentDAO comment);
}
