package g1105.metaps.action;

import g1105.metaps.dao.AlbumDAO;
import g1105.metaps.dao.Album_PictureDAO;
import g1105.metaps.entity.Album;
import g1105.metaps.entity.Album_picture;
import g1105.metaps.key.Album_picturePK;
import g1105.ps.constant.Constant;
import g1105.ps.dao.PhotoDAO;
import g1105.ps.entity.Photo;
import g1105.ps.service.UserService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

public class UploadPictureAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;

    private UserService userService;

    private PhotoDAO photoDAO;

    private Album_PictureDAO albumPictureDAO;

    private AlbumDAO albumDAO;

    private List<File> upload;

    private List<String> uploadContentType;

    private List<String> uploadFileName;

    private String uploadPath;

    private String albumName;

    private String result;

    public void setSession(Map<String, Object> arg0) {
        this.session = arg0;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPhotoDAO(PhotoDAO photoDAO) {
        this.photoDAO = photoDAO;
    }

    public void setAlbumPictureDAO(Album_PictureDAO albumPictureDAO) {
        this.albumPictureDAO = albumPictureDAO;
    }

    public void setUpload(List<File> upload) {
        this.upload = upload;
    }

    public void setUploadContentType(List<String> uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public void setUploadFileName(List<String> uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public AlbumDAO getAlbumDAO() {
        return albumDAO;
    }

    public void setAlbumDAO(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getResult() {
        return result;
    }

    public String execute() {
        try {
            String userEmail = session.get("SessionName").toString();
            Integer userId = userService.getUserId(userEmail);
            Album album = new Album();
            album.setName(albumName);
            album.setUserId(userId);
            List<Album> albums = albumDAO.getAlbum(album);
            for (int i = 0; i < uploadFileName.size(); i++) {
                Random random = new Random();
                String filename = uploadFileName.get(i);
                String picId = userId.toString() + "@" + random.nextLong() + filename.substring(filename.lastIndexOf("."));
                filename = uploadPath + picId;
                FileOutputStream fos = new FileOutputStream(filename);
                InputStream is = new FileInputStream(upload.get(i));
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
                Date date = new Date();
                Photo photo = new Photo();
                photo.setId(picId);
                photo.setPath(Constant.flickRefer + picId);
                photo.setOwnerId(userId);
                photo.setUploadDate(date);
                photo.setSource("user");
                photoDAO.addPhoto(photo);
                Album_picture albumPicture = new Album_picture();
                Album_picturePK albumPicturePK = new Album_picturePK();
                albumPicturePK.setAlbumId(albums.get(0).getId());
                albumPicturePK.setPictureId(picId);
                albumPicture.setAlbumPicturePK(albumPicturePK);
                albumPictureDAO.addPictureToAlbum(albumPicture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "result";
    }
}
