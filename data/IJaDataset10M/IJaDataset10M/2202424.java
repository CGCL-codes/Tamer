package com.hk.web.company.action;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.CmpProduct;
import com.hk.bean.CmpProductPhoto;
import com.hk.bean.CompanyPhoto;
import com.hk.bean.Photo;
import com.hk.bean.User;
import com.hk.frame.util.DataUtil;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpProductService;
import com.hk.svr.CompanyPhotoService;
import com.hk.svr.PhotoService;
import com.hk.svr.pub.Err;
import com.hk.web.pub.action.BaseAction;

@Component("/op/uploadproductphoto")
public class UploadProductPhotoAction extends BaseAction {

    @Autowired
    private CompanyPhotoService companyPhotoService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CmpProductService cmpProductService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLongAndSetAttr("companyId");
        List<CompanyPhoto> list = this.companyPhotoService.getPhotoListByCompanyId(companyId, 0, 30);
        if (list.size() == 30) {
            resp.alertJSAndGoBack(req.getText("view.cmp.photo.toomany"));
            return null;
        }
        return this.getWeb3Jsp("/e/photo/op/upload2.jsp");
    }

    /**
	 * 最多只能上传30张
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String upload(HkRequest req, HkResponse resp) {
        long companyId = req.getLong("companyId");
        long productId = req.getLong("productId");
        CmpProduct cmpProduct = this.cmpProductService.getCmpProduct(productId);
        if (cmpProduct == null) {
            return null;
        }
        File[] files = req.getFiles();
        User loginUser = this.getLoginUser(req);
        int successnum = 0;
        int errornum = 0;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i] == null) {
                    continue;
                }
                List<CmpProductPhoto> list = this.cmpProductService.getCmpProductPhotoListByProductId(productId, 0, 30);
                if (list.size() == 30) {
                    return this.initError(req, Err.CMPPRODUCTPHOTO_OUT_OF_LIMIT, -1, null, "upload", "onuploaderror", successnum);
                }
                Photo photo = new Photo();
                photo.setUserId(loginUser.getUserId());
                try {
                    this.photoService.createPhoto(photo, files[i], 2);
                    CmpProductPhoto cmpProductPhoto = new CmpProductPhoto();
                    cmpProductPhoto.setCompanyId(companyId);
                    cmpProductPhoto.setUserId(loginUser.getUserId());
                    cmpProductPhoto.setPhotoId(photo.getPhotoId());
                    cmpProductPhoto.setPath(photo.getPath());
                    cmpProductPhoto.setProductId(productId);
                    this.cmpProductService.createCmpProductPhoto(cmpProductPhoto);
                    if (DataUtil.isEmpty(cmpProduct.getHeadPath())) {
                        this.cmpProductService.updateCmpProductHeadPath(productId, photo.getPath());
                    }
                    successnum++;
                } catch (Exception e) {
                    errornum++;
                    e.printStackTrace();
                }
            }
        }
        return this.initSuccess(req, "upload", "onuploadok", errornum);
    }
}
