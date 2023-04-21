package org.fantasy.cpp.core.pojo;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.fantasy.cpp.core.annotation.Field;

/**
 * 查询条件类型
 * 
 * @author 王文成
 * @version 1.0
 * @since 2011-7-5
 */
@Table("ITEM_TYPE")
public class ItemType {

    @Id
    @Column("item_type_id")
    @Field(desc = "条件类型id", nullable = false)
    private Long itemTypeId;

    @Column("item_type_name")
    @Field(desc = "条件类型名称", nullable = false, maxlenth = 256)
    private String itemTypeName;

    @Column("state")
    @Field(desc = "状态", nullable = false, maxlenth = 3)
    private String state;

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
