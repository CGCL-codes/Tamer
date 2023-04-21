package com.sks.service.uploadfile.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sks.bean.uploadfile.UploadFile;
import com.sks.core.SystemConstants;
import com.sks.dao.house.AreaDao;
import com.sks.dao.uploadfile.UploadFileDao;
import com.sks.service.base.BaseService;
import com.sks.service.uploadfile.UploadFileService;

@Service
@Transactional
public class UploadFileServiceBean extends BaseService<UploadFile> implements UploadFileService {

    @Resource(name = "uploadFileDao")
    public void setUploadFileDao(UploadFileDao uploadFileDao) {
        this.setDao(uploadFileDao);
    }

    public List<String> getFilepath(Integer[] ids) {
        if (ids != null && ids.length > 0) {
            StringBuffer jpql = new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                jpql.append('?').append((i + 1)).append(',');
            }
            jpql.deleteCharAt(jpql.length() - 1);
            return getDao().getListByJpql("select o.path from UploadFile o where o.fileId in(" + jpql.toString() + ")", ids);
        }
        return null;
    }

    public void delete(String rootPath, Serializable... entityids) {
        for (Serializable id : entityids) {
            UploadFile uf = find(id);
            if (null == uf) {
                continue;
            }
            String filePath = rootPath + uf.getPath() + "/" + uf.getName() + "." + uf.getExtension();
            File f = new File(filePath);
            if (f != null && f.exists() && f.canRead() && f.canWrite()) {
                f.delete();
            }
        }
        super.delete(entityids);
    }

    @Override
    public List<UploadFile> getPictures(Integer objId, String objType) {
        String jpql = " o.objId = ? and o.objType = ?";
        return this.getScrollData(-1, -1, jpql, new Object[] { objId, objType }).getResultlist();
    }

    @Override
    public List<UploadFile> getHousePictures(Integer houseId) {
        return this.getPictures(houseId, SystemConstants.OBJ_TYPE_HOUSE);
    }

    @Override
    public List<UploadFile> getBuildingPictures(Integer buildingId) {
        return this.getPictures(buildingId, SystemConstants.OBJ_TYPE_BUILDING);
    }

    @Override
    public List<UploadFile> findBuildingPictures() {
        UploadFileDao uploadFileDao = (UploadFileDao) this.getDao();
        StringBuffer jpql = new StringBuffer(" 1 = 1 ");
        return uploadFileDao.getScrollData(-1, -1, jpql.toString(), null).getResultlist();
    }
}
