package com.sks.service.uploadfile;

import java.io.Serializable;
import java.util.List;
import com.sks.bean.uploadfile.UploadFile;
import com.sks.dao.uploadfile.UploadFileDao;
import com.sks.service.base.IBaseService;

public interface UploadFileService extends IBaseService<UploadFile> {

    public void setUploadFileDao(UploadFileDao uploadFileDao);

    /**
	 * 获取文件路径
	 * @param ids
	 * @return
	 */
    public List<String> getFilepath(Integer[] ids);

    public void delete(String rootPath, Serializable... entityids);

    public List<UploadFile> getPictures(Integer objId, String objType);

    public List<UploadFile> getHousePictures(Integer houseId);

    public List<UploadFile> getBuildingPictures(Integer buildingId);

    public List<UploadFile> findBuildingPictures();
}
