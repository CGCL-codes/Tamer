package com.tiger.aowim.info.generalinfo.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.framework.page.Page;
import com.framework.util.ResourceUtils;
import com.tiger.aowim.basetool.composition.ContentManage;
import com.tiger.aowim.file.service.FileAttInfoControlService;
import com.tiger.aowim.info.InfoControlDao;
import com.tiger.aowim.info.InfoControlService;
import com.tiger.aowim.info.InfoViewDao;
import com.tiger.aowim.info.InfoViewService;
import com.tiger.aowim.info.exception.InfoException;
import com.tiger.aowim.info.exception.InfoExceptionFactory;
import com.tiger.aowim.info.generalinfo.model.GInfoEntity;
import com.tiger.aowim.util.FileHelpUtils;

public class GeneralInfoServiceImpl implements InfoViewService<GInfoEntity>, InfoControlService<GInfoEntity, Long> {

    @Autowired
    @Qualifier("generalInfoDao")
    private Object generalInfoDao;

    @Autowired
    @Qualifier("contentSave")
    private ContentManage contentManage;

    @Autowired
    @Qualifier("fileControlService")
    private FileAttInfoControlService fileAttControl;

    private static final String defaultFilePath = ResourceUtils.getPropertyString("variable", "owim_attachment_path");

    @Override
    public GInfoEntity infoBringAttrOne(GInfoEntity gie) throws InfoException {
        GInfoEntity ge;
        List<GInfoEntity> list = this.getGeneralInfoViewDaoInstance().infoBringAttr(gie);
        if (list != null && list.size() > 0) {
            ge = list.get(0);
            ge.setC_name(ge.getC_contents());
            try {
                ge.setC_contents(contentManage.getInfoContent(defaultFilePath + ge.getC_contents()));
                ge.setC_p_name(ge.getFolderEntity().getC_name());
            } catch (Exception e) {
                e.printStackTrace();
                throw InfoExceptionFactory.create(10003);
            }
        } else ge = new GInfoEntity();
        return ge;
    }

    @Override
    public void createInfo(GInfoEntity ge) throws InfoException {
        String tempPath = FileHelpUtils.getRandom(100);
        String contentfileName = FileHelpUtils.getfileName(".txt");
        ge.setC_path(ge.getC_path() + "/" + tempPath);
        ge.setC_id_path(ge.getC_id_path() + "/" + tempPath);
        ge.setC_name(tempPath);
        try {
            contentManage.contentOperating(defaultFilePath + ge.getC_id_path(), contentfileName, ge.getC_contents());
        } catch (Exception e) {
            e.printStackTrace();
            throw InfoExceptionFactory.create(10000);
        }
        ge.setC_contents(ge.getC_id_path() + "/" + contentfileName);
        this.getGeneralInfoControlDaoInstance().newInfo(ge);
        if (ge.getAttachments() != null && ge.getAttachments().length > 0) fileAttControl.saveinfoFile(ge.getAttachments());
    }

    @Override
    public void updateInfo(GInfoEntity ge) throws InfoException {
        try {
            contentManage.contentOperating(defaultFilePath + ge.getC_id_path(), ge.getC_name().split("/")[ge.getC_name().split("/").length - 1], ge.getC_contents());
        } catch (Exception e) {
            e.printStackTrace();
            throw InfoExceptionFactory.create(10000);
        }
        this.getGeneralInfoControlDaoInstance().updateInfo(ge);
        fileAttControl.delAttFile(ge.getC_id());
        if (ge.getAttachments() != null && ge.getAttachments().length > 0) fileAttControl.saveinfoFile(ge.getAttachments());
    }

    @Override
    public Page<GInfoEntity> folderInfos(int pageSize, int pageNumber, GInfoEntity gie) {
        gie.setC_type("generalInfo");
        return this.getGeneralInfoViewDaoInstance().queryPageInfos(pageSize, pageNumber, gie);
    }

    @Override
    public void updateChangeFName(Map<String, String> changePar) {
        this.getGeneralInfoControlDaoInstance().updateChangeFName(changePar);
    }

    @Override
    public void deleteInfo(Long id) {
        this.getGeneralInfoControlDaoInstance().deleteInfo(id);
        this.fileAttControl.delInfowithFile(id);
    }

    @SuppressWarnings("unchecked")
    private InfoViewDao<GInfoEntity> getGeneralInfoViewDaoInstance() {
        return (InfoViewDao<GInfoEntity>) generalInfoDao;
    }

    @SuppressWarnings("unchecked")
    private InfoControlDao<GInfoEntity> getGeneralInfoControlDaoInstance() {
        return (InfoControlDao<GInfoEntity>) generalInfoDao;
    }
}
