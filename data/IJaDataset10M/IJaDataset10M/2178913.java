package org.dasein.cloud.flexiscale.compute;

import java.io.Serializable;

/**
 * @author Qunying Huang @ enstratus
 * <p>
 * storage(Volume) product offers by flexiscale
 * </p>
 *
 */
@SuppressWarnings("serial")
public class FlexiscaleDiskProduct implements Serializable {

    private String description;

    private int diskSizeInGb;

    private String name;

    private long productId;

    public FlexiscaleDiskProduct() {
    }

    public boolean equals(Object ob) {
        if (ob == null) {
            return false;
        }
        if (ob == this) {
            return true;
        }
        if (!getClass().getName().equals(ob.getClass().getName())) {
            return false;
        }
        return getProductId() == (((FlexiscaleDiskProduct) ob).getProductId());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiskSizeInGb() {
        return diskSizeInGb;
    }

    public void setDiskSizeInGb(int diskSizeInGb) {
        this.diskSizeInGb = diskSizeInGb;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
