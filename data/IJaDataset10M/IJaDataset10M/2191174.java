package model.database;

import java.util.List;
import java.util.HashMap;
import model.base.Inventory;
import model.base.Resource;

public interface InventoryDAO {

    public List findResource(long id, String kind);

    public HashMap findResourcefromDepartment(long did);

    public Inventory getInventoryData(Inventory rc);

    public Inventory findInventory(long resourceid, long companyid);

    public List findInventories(Resource resource);

    public List getCompanyTeams(Inventory inventory);

    public List getInventoryList(Inventory inv);

    public Inventory insertInventory(Inventory inv);

    public void updateInventory(Inventory inv);

    public void deleteInventory(Inventory inv);
}
