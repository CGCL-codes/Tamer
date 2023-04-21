package hu.ihash.common.service;

import hu.ihash.common.collections.FixedOrderedList;
import hu.ihash.common.collections.OrderableValue;
import hu.ihash.common.util.ByteUtils;
import hu.ihash.database.dao.ImageDao;
import hu.ihash.database.entities.Image;
import hu.ihash.hashing.methods.versions.YCrCb_Hash;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A service for finding similar images.
 * 
 * @author Gergely Kiss
 * 
 */
public class SimilarImageService implements SimilarService<Image> {

    private static final int PAGE_SIZE = 100;

    @Autowired
    private ImageDao imageDao;

    @Override
    public Collection<Image> findEqual(Image source) {
        return imageDao.byHash(source.getImageHash());
    }

    @Override
    public Collection<Similarity<Image>> findSimilar(Image source, int n) {
        YCrCb_Hash shash = new YCrCb_Hash();
        YCrCb_Hash thash = new YCrCb_Hash();
        int cnt = imageDao.count();
        FixedOrderedList<Image> similars = new FixedOrderedList<Image>(n);
        shash.fromByteArray(ByteUtils.hexToBytes(source.getImageHash()));
        double smax = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < cnt; i += PAGE_SIZE) {
            Collection<Image> images = imageDao.page(i, PAGE_SIZE);
            for (Image image : images) {
                thash.fromByteArray(ByteUtils.hexToBytes(image.getImageHash()));
                if (shash.equals(thash)) continue;
                double s = shash.compareSimilarity(thash);
                similars.add(image, s);
                smax = Math.max(s, smax);
            }
        }
        List<Similarity<Image>> r = new ArrayList<Similarity<Image>>(similars.size());
        for (OrderableValue<Image> ov : similars.getDescendingList()) {
            r.add(new Similarity<Image>(source, ov.elem, ov.value));
        }
        return r;
    }
}
