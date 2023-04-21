package org.streets.eis.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.streets.database.annotations.NestedFetch;

@Entity
@Table(name = "EIS_CODE_ITEM")
public class CodeItem {

    @Id
    public String id;

    @Column
    public String groupId;

    @Column
    public String code;

    @Column
    public String name;

    @Column
    public String alias;

    @Column
    public Integer showIndex;

    @Column
    public String tag;

    @Column(name = "item_level")
    public Integer level;

    @Column(name = "parent_code")
    public String parent;

    @NestedFetch(value = "select * from eis_code_item where parent_code = :code", type = CodeItem.class)
    private List<CodeItem> childs;

    public CodeItem() {
    }

    public List<CodeItem> getChilds() {
        return childs;
    }

    public void setChilds(List<CodeItem> childs) {
        this.childs = childs;
    }
}
