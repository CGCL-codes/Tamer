package service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import model.Image;

@Transactional
public interface ImageService {

    public Integer insertRoomImage(Image image);

    public Integer insertRoomTypeImage(Image image);

    public Integer insertStructureImage(Image image);

    public List<Image> findImagesByIdRoom(Integer id_room);

    public List<Image> findImagesByIdRoomType(Integer id_roomType);

    public List<Image> findImagesByIdStructure(Integer id_structure);

    public Image findStructureImageByName(Integer id_structure, String name);

    public Image findRoomImageByName(Integer id_structure, String name);

    public Image findRoomTypeImageByName(Integer id_structure, String name);

    public Integer deleteRoomImage(Integer id);

    public Integer deleteAllImagesFromRoom(Integer id);

    public Integer deleteRoomTypeImage(Integer id);

    public Integer deleteAllImagesFromRoomType(Integer id);

    public Integer deleteStructureImage(Integer id);
}
