package com.hy.erp.inventory.service.interfaces;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import com.hy.enterprise.framework.service.business.IBusinessService;
import com.hy.erp.inventory.pojo.Inventory;
import com.hy.erp.inventory.pojo.interfaces.IInventory;
import com.hy.erp.inventory.service.InventoryService;
import com.hy.framework.lang.annotation.ImplementsBy;

/**
 * 
 * <ul>
 * <li>开发作者：李冰</li>
 * <li>设计日期：2010-11-9；时间：下午04:22:19</li>
 * <li>类型名称：IInventoryService</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
@Local
@ImplementsBy(InventoryService.class)
public interface IInventoryService extends IBusinessService<IInventory> {

    public boolean addInventory(IInventory inventory);

    public String[] addInventory(Object[] inventory);

    public String[] modifyInventory(Object[] inventory);

    public boolean modifyInventory(IInventory inventory);

    public Integer removeInventory(String[] inventoryIds);

    public List<Inventory> findByNamedQuery(String queryName, Map<String, Object> parameters);

    public Inventory findUniqueResultByNamedQuery(String queryName, Map<String, Object> parameters);

    public List<IInventory> getAllInventory();

    public IInventory findMaterielCount(String queryName, String[] Ids);
}
