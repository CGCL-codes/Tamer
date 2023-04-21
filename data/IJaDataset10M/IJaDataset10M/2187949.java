package com.mkk.kenji1016.service.impl;

import com.mkk.kenji1016.dao.FamilyDao;
import com.mkk.kenji1016.domain.FamilyImage;
import com.mkk.kenji1016.service.FamilyService;
import com.mkk.kenji1016.service.FileService;
import com.mkk.kenji1016.util.ApplicationUtil;
import com.mkk.kenji1016.web.dto.UploadFileDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * User: mkk
 * Date: 11-8-28
 * Time: 下午2:45
 */
public class FamilyServiceImpl implements FamilyService, InitializingBean {

    private FamilyDao familyDao;

    private FileService fileService;

    /**
     * Process upload family images.
     *
     * @param fileDtoList List of  UploadFileDto
     * @return true of false
     */
    public boolean saveFamilyImgs(List<UploadFileDto> fileDtoList) {
        if (fileDtoList.isEmpty()) {
            return false;
        }
        long userId = 1;
        List<String> imgPaths = fileService.saveUploadImages(fileDtoList);
        List<FamilyImage> imgs = ApplicationUtil.createList();
        int index = 0;
        for (String path : imgPaths) {
            UploadFileDto uploadFileDto = fileDtoList.get(index++);
            boolean isExist = StringUtils.hasLength(path);
            FamilyImage fi = createFamilyImage(userId, path, uploadFileDto, isExist);
            imgs.add(fi);
        }
        familyDao.persistFamilyImages(imgs);
        return true;
    }

    /**
     * Create a new FamilyImage .
     *
     * @param userId        userId
     * @param path          image path
     * @param uploadFileDto Current UploadFileDto
     * @param exist         exist
     * @return FamilyImage
     */
    private FamilyImage createFamilyImage(long userId, String path, UploadFileDto uploadFileDto, boolean exist) {
        FamilyImage familyImage = new FamilyImage();
        familyImage.setExist(exist);
        familyImage.setImgPath(path);
        familyImage.setUserId(userId);
        familyImage.setShare(uploadFileDto.isShare());
        familyImage.setImgName(uploadFileDto.getReallyName());
        return familyImage;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(fileService);
        Assert.notNull(familyDao);
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public void setFamilyDao(FamilyDao familyDao) {
        this.familyDao = familyDao;
    }
}
