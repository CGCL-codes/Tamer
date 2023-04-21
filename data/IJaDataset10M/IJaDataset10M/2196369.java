package be.vds.jtbdive.core.core.material;

import be.vds.jtbdive.core.core.catalogs.MaterialType;

public class DefaultSizableMaterial extends DefaultMaterial {

    private static final long serialVersionUID = 2925418328707742847L;

    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public DefaultSizableMaterial(MaterialType materialType) {
        super(materialType);
    }
}
