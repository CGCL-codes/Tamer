package cease.butter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import cease.urs.Session;
import cease.vo.UploadedPhoto;

/**
 * @author dhf
 */
public class PhotoExecutor extends BaseExecutor {

    private static PhotoExecutor executor = new PhotoExecutor();

    private static String[] IMG_SUFFIXES = { "jpg", "jpeg", "gif", "bmp", "png" };

    private static boolean isImg(String file) {
        if (null == file) {
            return false;
        }
        String ext = file.substring(file.lastIndexOf(".") + 1);
        for (String suffix : IMG_SUFFIXES) {
            if (suffix.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    public static PhotoExecutor getInstance() {
        return executor;
    }

    public UploadedPhoto upload(Session session, String filePath, String watermark, Boolean isAvatar, Boolean useAsAvatar, Integer x, Integer y, Integer w, Integer h) throws ApiException {
        if (null == filePath) {
            throw new IllegalArgumentException("[filePath] could not be null");
        }
        File f = new File(filePath);
        if (!f.exists()) {
            throw new IllegalArgumentException("file [" + filePath + "] not found");
        }
        if (!isImg(filePath)) {
            throw new IllegalArgumentException("file [" + filePath + "] not an image");
        }
        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("input", new FileBody(f));
        try {
            if (null != watermark) {
                reqEntity.addPart("watermark", new StringBody(watermark));
            }
            if (null != isAvatar) {
                reqEntity.addPart("isAvatar", new StringBody(isAvatar + ""));
            }
            if (null != useAsAvatar) {
                reqEntity.addPart("useAsAvatar", new StringBody(useAsAvatar + ""));
            }
            if (null != x) {
                reqEntity.addPart("x", new StringBody(x + ""));
            }
            if (null != y) {
                reqEntity.addPart("y", new StringBody(y + ""));
            }
            if (null != w) {
                reqEntity.addPart("w", new StringBody(w + ""));
            }
            if (null != h) {
                reqEntity.addPart("h", new StringBody(h + ""));
            }
        } catch (UnsupportedEncodingException e) {
        }
        List<HttpEntity> entities = new LinkedList<HttpEntity>();
        entities.add(reqEntity);
        JSON json = post(Constants.URL_PHOTO_UPLOAD, null, entities, null, session);
        return toUploadedPhoto(json);
    }
}
