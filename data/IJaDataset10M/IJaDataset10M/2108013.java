package com.liferay.portal.service.impl;

import com.liferay.portal.NoSuchImageException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.Image;
import com.liferay.portal.service.persistence.ImageFinder;
import com.liferay.portal.service.persistence.ImageUtil;
import com.liferay.portal.service.spring.ImageService;
import java.util.Date;
import java.util.List;

/**
 * <a href="ImageServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ImageServiceImpl extends PrincipalBean implements ImageService {

    public void deleteImage(String imageId) throws PortalException, SystemException {
        ImageUtil.remove(imageId);
    }

    public Image getImage(String imageId) throws PortalException, SystemException {
        return ImageUtil.findByPrimaryKey(imageId);
    }

    public List getImageById(String imageId) throws SystemException {
        return ImageFinder.findByImageId(imageId);
    }

    public Image updateImage(String imageId, byte[] bytes) throws SystemException {
        Image image = null;
        try {
            image = ImageUtil.findByPrimaryKey(imageId);
        } catch (NoSuchImageException nsie) {
            image = ImageUtil.create(imageId);
        }
        image.setModifiedDate(new Date());
        image.setTextObj(bytes);
        ImageUtil.update(image);
        return image;
    }
}
