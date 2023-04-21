package org.dinnermate.menu;

import net.java.ao.Entity;
import net.java.ao.Implementation;
import net.java.ao.OneToMany;
import net.java.ao.Preload;
import net.java.ao.schema.TableName;

@TableName("ItemCategory")
@Implementation(ItemCategoryImpl.class)
@Preload
public interface ItemCategory extends Entity {

    public String getName();

    public void setName(String name);

    @OneToMany
    public MenuItem[] getMenuItems();
}
