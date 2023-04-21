package corner.orm.tapestry.service.blob;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.request.IUploadFile;
import org.springframework.util.FileCopyUtils;
import corner.model.IBlobModel;
import corner.service.EntityService;
import corner.util.BeanUtils;

/**
 * 针对blob字段的处理的委派类.
 * 
 * @author	<a href="http://wiki.java.net/bin/view/People/JunTsai">Jun Tsai</a>
 * @version	$Revision:3677 $
 * @since	2006-1-20
 */
public class BlobPageDelegate<T extends IBlobModel> implements IBlobPageDelegate<T> {

    private static final Log log = LogFactory.getLog(BlobPageDelegate.class);

    private IUploadFile uploadFile;

    private String keyValue;

    private EntityService service;

    private Class<T> clazz;

    /**
	 * 构造一个委派类对象.
	 * @param clazz 类.
	 * @param uploadFile 上传文件对象.
	 * @param keyValue 主键的值.
	 * @param service 实体服务.
	 * @see EntityService
	 */
    public BlobPageDelegate(Class<T> clazz, IUploadFile uploadFile, String keyValue, EntityService service) {
        this.clazz = clazz;
        this.keyValue = keyValue;
        this.uploadFile = uploadFile;
        this.service = service;
    }

    /**
	 * @see corner.orm.tapestry.service.blob.IBlobPageDelegate#save(corner.orm.tapestry.service.blob.IBlobBeforSaveCallBack)
	 */
    public void save(IBlobBeforSaveCallBack<T> callback) {
        if (uploadFile == null) {
            if (keyValue != null) service.deleteEntityById(clazz, keyValue);
            return;
        }
        T blob = null;
        if (keyValue != null) {
            blob = service.getEntity(clazz, keyValue);
        }
        if (blob == null) {
            blob = BeanUtils.instantiateClass(clazz);
        }
        try {
            blob.setBlobData(FileCopyUtils.copyToByteArray(uploadFile.getStream()));
        } catch (IOException e) {
            log.warn("fail to set blob data [" + e.getMessage() + "]");
            return;
        }
        blob.setContentType(this.uploadFile.getContentType());
        blob.setBlobName(this.uploadFile.getFileName());
        if (callback != null) {
            callback.doBeforeSaveBlob(blob);
        }
        this.service.saveOrUpdateEntity(blob);
    }

    /**
	 * @see corner.orm.tapestry.service.blob.IBlobPageDelegate#save()
	 */
    public void save() {
        this.save(null);
    }
}
