package legend.entity;

import org.seasar.dao.annotation.tiger.Column;

public class Author {

    private Integer id;

    private String name;

    private byte[] pass;

    private Boolean deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPass() {
        return pass;
    }

    public void setPass(byte[] pass) {
        this.pass = pass;
    }

    @Column("delete_flag")
    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
