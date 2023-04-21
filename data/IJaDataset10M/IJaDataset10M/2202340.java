package org.identifylife.key.editor.gwt.shared.model;

/**
 * @author dbarnier
 *
 */
@SuppressWarnings("serial")
public class Taxon extends Model {

    private String name;

    private String lsid;

    private boolean isLeaf = false;

    private TaxonRef mappedTo;

    public Taxon() {
        super(ItemType.TAXON);
    }

    public Taxon(String uuid) {
        super(uuid, ItemType.TAXON);
    }

    public Taxon(String uuid, String name) {
        super(uuid, ItemType.TAXON);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLsid() {
        return lsid;
    }

    public void setLsid(String lsid) {
        this.lsid = lsid;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public TaxonRef getMappedTo() {
        return mappedTo;
    }

    public void setMappedTo(TaxonRef mappedTo) {
        this.mappedTo = mappedTo;
    }

    @Override
    public String toString() {
        return "Taxon[uuid=" + uuid + ",name=" + name + ",lsid=" + lsid + ",isLeaf=" + isLeaf + ",mappedTo=" + mappedTo + "]";
    }
}
