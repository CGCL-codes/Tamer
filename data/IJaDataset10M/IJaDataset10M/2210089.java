package demo.service.impl;

import java.util.List;
import demo.dao.impl.CommentDaoImpl;
import demo.entity.Comment;
import demo.service.CommentService;

public class CommentServiceImpl extends GenericServiceImpl<Comment, Long, CommentDaoImpl> implements CommentService {

    public List<Object[]> queryCommentByWeiboId(Long weiboid) {
        return generalDao.queryCommentByWeiboId(weiboid);
    }
}
