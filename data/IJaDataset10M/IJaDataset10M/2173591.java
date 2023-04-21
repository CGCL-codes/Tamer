package cn.edu.nju.tsip.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.edu.nju.tsip.entity.Picture;
import cn.edu.nju.tsip.service.IPictureService;

@Service("pictureService")
@Transactional
public class PictureServiceImpl<T extends Picture> extends ServiceImpl<T> implements IPictureService<T> {

    @Override
    public void create(T baseBean) {
        dao.persist(baseBean);
    }

    @Override
    public void delete(T baseBean) {
    }

    @SuppressWarnings("unchecked")
    public T getCover(int albumId) {
        return (T) dao.createQuery("from Picture as picture where pictures_id = " + albumId + " order by picture.createDate desc ").uniqueResult();
    }
}
