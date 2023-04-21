package org.nagios.dao;

import java.util.List;
import org.nagios.core.Comment;

public interface CommentDao {

    public Comment getCommentById(Integer id);

    public Comment getCommentByInternalId(Integer id);

    public List getComments();

    public List getHostComments();

    public List getServiceComments();
}
