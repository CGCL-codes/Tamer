package collab.fm.server.bean.transfer;

public class BinRelationType2 extends DataItem2 {

    private String typeName;

    private Long superId;

    private Long model;

    private boolean hier;

    private boolean dir;

    private Long sourceId;

    private Long targetId;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getSuperId() {
        return superId;
    }

    public void setSuperId(Long superId) {
        this.superId = superId;
    }

    public Long getModel() {
        return model;
    }

    public void setModel(Long model) {
        this.model = model;
    }

    public boolean isHier() {
        return hier;
    }

    public void setHier(boolean hier) {
        this.hier = hier;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
