package com.hk.svr.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.CmpFile;
import com.hk.bean.CmpOrgArticle;
import com.hk.bean.CmpOrgArticleContent;
import com.hk.bean.CmpOrgFile;
import com.hk.frame.util.image.ImageException;
import com.hk.frame.util.image.NotPermitImageFormatException;
import com.hk.frame.util.image.OutOfSizeException;
import com.hk.svr.CmpOrgArticleService;
import com.hk.svr.CmpOrgFileService;

public class CmpOrgArticleProcessor {

    @Autowired
    private CmpOrgArticleService cmpOrgArticleService;

    @Autowired
    private CmpOrgFileProcessor cmpOrgFileProcessor;

    @Autowired
    private CmpOrgFileService cmpOrgFileService;

    /**
	 * 大于200k的文件将不能被上传，图片上传出错时，文章会保存
	 * 
	 * @param cmpArticle
	 * @param cmpArticleContent
	 * @param files
	 *            2010-5-10
	 * @throws IOException
	 */
    public CmpOrgArticleUploadFileResult createCmpOrgArticle(CmpOrgArticle cmpOrgArticle, CmpOrgArticleContent cmpOrgArticleContent, File[] files, int topIdx) throws Exception {
        this.cmpOrgArticleService.createCmpOrgArticle(cmpOrgArticle, cmpOrgArticleContent);
        CmpOrgArticleUploadFileResult cmpOrgArticleUploadFileResult = null;
        if (files != null) {
            cmpOrgArticleUploadFileResult = this.saveFile(cmpOrgArticle.getCompanyId(), files, cmpOrgArticle, topIdx);
        }
        if (cmpOrgArticleUploadFileResult != null && cmpOrgArticleUploadFileResult.isFileUploadError()) {
            throw new Exception("imgupdateerror");
        }
        return cmpOrgArticleUploadFileResult;
    }

    /**
	 * 大于200k的文件将不能被上传
	 * 
	 * @param cmpArticle
	 * @param cmpArticleContent
	 * @param files
	 *            2010-5-10
	 */
    public CmpOrgArticleUploadFileResult updateCmpOrgArticle(CmpOrgArticle cmpOrgArticle, CmpOrgArticleContent cmpOrgArticleContent, File[] files, int topIdx) throws Exception {
        CmpOrgArticleUploadFileResult cmpOrgArticleUploadFileResult = null;
        if (files != null) {
            cmpOrgArticleUploadFileResult = this.saveFile(cmpOrgArticle.getCompanyId(), files, cmpOrgArticle, topIdx);
        }
        this.cmpOrgArticleService.updateCmpOrgArticle(cmpOrgArticle, cmpOrgArticleContent);
        if (cmpOrgArticleUploadFileResult != null && cmpOrgArticleUploadFileResult.isFileUploadError()) {
            throw new Exception("imgupdateerror");
        }
        return cmpOrgArticleUploadFileResult;
    }

    private CmpOrgArticleUploadFileResult saveFile(long companyId, File[] files, CmpOrgArticle cmpOrgArticle, int topIdx) {
        CmpOrgArticleUploadFileResult cmpOrgArticleUploadFileResult = new CmpOrgArticleUploadFileResult();
        int size = files.length;
        if (size > 5) {
            size = 5;
        }
        List<CmpOrgFile> list = new ArrayList<CmpOrgFile>();
        for (int i = 0; i < files.length; i++) {
            try {
                CmpOrgFile cmpOrgFile = new CmpOrgFile();
                cmpOrgFile.setCompanyId(companyId);
                if (topIdx == i) {
                    cmpOrgFile.setTopflg(CmpFile.TOPFLG_Y);
                } else {
                    cmpOrgFile.setTopflg(CmpFile.TOPFLG_N);
                }
                cmpOrgFile.setArticleOid(cmpOrgArticle.getOid());
                if (cmpOrgArticle.getPath() == null) {
                    cmpOrgArticle.setPath(cmpOrgFile.getPath());
                }
                this.cmpOrgFileProcessor.createCmpOrgFile(cmpOrgFile, files[i]);
                list.add(cmpOrgFile);
            } catch (ImageException e) {
                cmpOrgArticleUploadFileResult.setFileUploadError(true);
            } catch (NotPermitImageFormatException e) {
                cmpOrgArticleUploadFileResult.addFmtErrorNum(1);
            } catch (OutOfSizeException e) {
                cmpOrgArticleUploadFileResult.addOutOfSizeErrorNum(1);
            }
        }
        cmpOrgArticleUploadFileResult.setCmpOrgFileList(list);
        return cmpOrgArticleUploadFileResult;
    }

    public void deleteCmpArticleFile(long companyId, long cmpArticleOid, long cmpFileOid) {
        CmpOrgArticle cmpOrgArticle = this.cmpOrgArticleService.getCmpOrgArticle(companyId, cmpArticleOid);
        CmpOrgFile cmpOrgFile = this.cmpOrgFileService.getCmpOrgFile(companyId, cmpFileOid);
        if (cmpOrgFile == null) {
            return;
        }
        this.cmpOrgFileProcessor.deleteCmpOrgFile(companyId, cmpFileOid);
        if (cmpOrgArticle != null) {
            if (cmpOrgArticle.getPath() != null && cmpOrgArticle.getPath().equals(cmpOrgFile.getPath())) {
                cmpOrgArticle.setPath(null);
                this.cmpOrgArticleService.updateCmpOrgArticle(cmpOrgArticle, null);
            }
        }
    }

    public void deleteCmpArticle(long companyId, long oid) {
        List<CmpOrgFile> list = this.cmpOrgFileService.getCmpOrgFileListByCompanyIdAndArticleOid(companyId, oid);
        for (CmpOrgFile o : list) {
            this.cmpOrgFileProcessor.deleteCmpOrgFile(companyId, o.getOid());
        }
        this.cmpOrgArticleService.deleteCmpOrgArticle(companyId, oid);
    }

    public void setMainFile(long companyId, long cmpArticleOid, long cmpFileOid) {
        CmpOrgArticle cmpOrgArticle = this.cmpOrgArticleService.getCmpOrgArticle(companyId, cmpArticleOid);
        CmpOrgFile cmpOrgFile = this.cmpOrgFileService.getCmpOrgFile(companyId, cmpFileOid);
        if (cmpOrgFile == null || cmpOrgArticle == null) {
            return;
        }
        cmpOrgArticle.setPath(cmpOrgFile.getPath());
        this.cmpOrgArticleService.updateCmpOrgArticle(cmpOrgArticle, null);
    }
}
