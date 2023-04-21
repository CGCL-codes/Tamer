package org.vardb.graphics.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = false)
public interface IGraphicsDao {

    public List<CImage> getImages();

    public CImage getImage(int id);

    public CImage getImage(String identifier);

    public int addImage(CImage image);

    public void updateImage(CImage image);

    public void deleteImage(int id);
}
