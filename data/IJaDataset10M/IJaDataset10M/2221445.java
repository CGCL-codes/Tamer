package tuxiazi.bean;

import tuxiazi.dao.dbpartitionhelper.TuxiaziDbPartitionHelper;
import halo.dao.annotation.Id;
import halo.dao.annotation.Table;

/**
 * 图片id
 * 
 * @author akwei
 */
@Table(name = "photoid", partitionClass = TuxiaziDbPartitionHelper.class)
public class Photoid {

    /**
	 * 图片id
	 */
    @Id
    private long photoid;

    public long getPhotoid() {
        return photoid;
    }

    public void setPhotoid(long photoid) {
        this.photoid = photoid;
    }
}
