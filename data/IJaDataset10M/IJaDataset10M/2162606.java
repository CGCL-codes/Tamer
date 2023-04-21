package com.passionatelife.dao.image;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import com.passionatelife.common.dao.BasicHibernateDAO;
import com.passionatelife.familytree.model.ImageVO;

public interface ImageDAO extends BasicHibernateDAO<ImageVO, Long> {

    List<ImageVO> findByImagePath(String imagePath);

    Query queryModifiedSince(Date modifiedSince);

    void lock(ImageVO imageVO);

    List<ImageVO> findByImageName(String imageName);

    List<ImageVO> findByCategoryId(long categoryId);

    List<ImageVO> findByOwnerId(long ownerId);
}
