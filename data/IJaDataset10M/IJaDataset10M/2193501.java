package com.khotyn.heresy.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.fileupload.FileItem;
import org.springframework.transaction.annotation.Transactional;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.MetadataException;
import com.khotyn.heresy.bean.Album;
import com.khotyn.heresy.bean.PictureAlbum;
import com.khotyn.heresy.bean.PictureDetail;
import com.khotyn.heresy.dao.AlbumDAO;
import com.khotyn.heresy.dao.PictureAlbumDAO;
import com.khotyn.heresy.dao.PictureDAO;
import com.khotyn.heresy.dao.UserDAO;
import com.khotyn.heresy.util.ImageUtil;

public class UploadService {

    private PictureDAO pictureDAO;

    private AlbumDAO albumDAO;

    private PictureAlbumDAO pictureAlbumDAO;

    private UserDAO userDAO;

    private static final String saveLoc = "/home/ibis/jboss-4.2.3.GA/server/default/deploy/Dragonfly.war/pic";

    private static final int uploadPoint = 10;

    private static final int zoomSize = 150;

    /**
	 * 处理上传的图片
	 * 
	 * @param fileItems 上传的图片列表
	 * @param userID 上传者ID
	 * @param albumID 上传的目标相册
	 * @param requestURL 浏览地址
	 * @throws Exception
	 * @throws IOException
	 */
    public void doService(List<FileItem> items, Integer userID, Integer albumID) throws IOException, Exception {
        for (int i = 1; i < items.size() - 1; i++) {
            FileItem fileItem = (FileItem) items.get(i);
            if (fileItem.getName() != null) {
                String picPK = this.generatePicPK(userID, albumID);
                File saveFile = new File(saveLoc, this.generateNewPicName(picPK, fileItem.getName()));
                fileItem.write(saveFile);
                if (com.khotyn.heresy.util.ImageUtil.isImage(saveFile)) {
                    PictureDetail picDetail = this.generatePicDetail(saveFile, userID, albumID, fileItem.getName(), picPK);
                    PictureAlbum pictureAlbum = this.generatePictureAlbum(picDetail.getPictureID(), albumID);
                    File thumbFile = new File(saveLoc, picPK + "thumb.jpg");
                    ImageIO.write(ImageUtil.scaleJ2D(saveFile, zoomSize), "jpg", thumbFile);
                    doDBOperate(picDetail, pictureAlbum);
                } else {
                    saveFile.delete();
                    throw new Exception("非法的文件" + fileItem.getName());
                }
            } else {
                continue;
            }
        }
    }

    /**
	 * 生成PictureDetail Bean
	 * 
	 * @param file 上传的文件
	 * @param userID 上传者ID
	 * @param albumID 目标相册ID
	 * @param originalName 原始文件名
	 * @param picPK 图片主键
	 * @return PictureDetail Bean
	 */
    private PictureDetail generatePicDetail(File file, Integer userID, Integer albumID, String originalName, String picPK) {
        PictureDetail picDetail = new PictureDetail();
        try {
            BufferedImage image = ImageIO.read(file);
            picDetail.setPictureID(picPK);
            picDetail.getPicOwner().setUserID(userID);
            picDetail.setName(originalName);
            picDetail.setOriginalName(originalName);
            picDetail.setPictureSize(file.length());
            picDetail.setSizeX(image.getWidth());
            picDetail.setSizeY(image.getHeight());
            picDetail.setUploadTime(new Date(System.currentTimeMillis()));
            if (com.khotyn.heresy.util.ImageUtil.getEXIFInfo(file) != null) picDetail.setExifInfo(com.khotyn.heresy.util.ImageUtil.getEXIFInfo(file));
            picDetail.setLocation(this.generateNewPicName(picPK, originalName));
            picDetail.setThumbLocation(picPK + "thumb.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        }
        return picDetail;
    }

    /**
	 * 生成图片相册Bean
	 * 
	 * @param pictureID 图片ID
	 * @param albumID 相册ID
	 * @return 图片相册ID
	 */
    private PictureAlbum generatePictureAlbum(String pictureID, Integer albumID) {
        PictureAlbum pictureAlbum = new PictureAlbum();
        pictureAlbum.getPictureBrief().setPictureID(pictureID);
        pictureAlbum.setAlbumID(albumID);
        return pictureAlbum;
    }

    /**
	 * 生成图片的新文件名
	 * 
	 * @param picPK 图片主键
	 * @param originalName 图片的原始名字
	 * @return 图片的新文件名
	 */
    private String generateNewPicName(String picPK, String originalName) {
        return picPK + originalName.substring(originalName.lastIndexOf('.'));
    }

    /**
	 * 生成图片主键
	 * 
	 * @param userID 用户ID
	 * @param albumID 相册ID
	 * @return 图片主键
	 */
    private String generatePicPK(Integer userID, Integer albumID) {
        return userID.toString() + albumID.toString() + System.currentTimeMillis();
    }

    /**
	 * 插入图片的数据库操作，包括：将PictureDetail Bean插入图片表，
	 * 修改相册表的PICTURE_COUNT字段，将相册图片的对应关系插入相册图片表 TODO:事务功能尚未实现
	 * 
	 * @param picDetail 要插入到图片表的PictureDetail Bean
	 * @param pictureAlbum 图片相册Bean
	 * @param requestURL 浏览地址
	 */
    @Transactional
    public void doDBOperate(PictureDetail picDetail, PictureAlbum pictureAlbum) {
        int orgPictureCount = albumDAO.getPictureCount(pictureAlbum.getAlbumID());
        int pictureCount = orgPictureCount + 1;
        Album album = new Album();
        album.setAlbumID(pictureAlbum.getAlbumID());
        album.setPictureCount(pictureCount);
        pictureDAO.insertPicture(picDetail);
        albumDAO.updatePictureCount(album);
        pictureAlbumDAO.insertPictureAlbum(pictureAlbum);
        if (orgPictureCount == 0) {
            albumDAO.updateEnvelopPicId(pictureAlbum.getAlbumID(), picDetail.getPictureID());
        }
        Integer points = userDAO.selectUserPoint(picDetail.getPicOwner().getUserID());
        points += uploadPoint;
        userDAO.updateScore(picDetail.getPicOwner().getUserID(), points);
    }

    public void setPictureDAO(PictureDAO pictureDAO) {
        this.pictureDAO = pictureDAO;
    }

    public void setAlbumDAO(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    public void setPictureAlbumDAO(PictureAlbumDAO pictureAlbumDAO) {
        this.pictureAlbumDAO = pictureAlbumDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
