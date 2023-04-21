package konozama.dao;

import java.sql.Timestamp;
import konozama.entity.Item;
import org.seasar.dao.annotation.tiger.Arguments;
import org.seasar.dao.annotation.tiger.S2Dao;
import org.seasar.dao.annotation.tiger.Sql;

@S2Dao(bean = Item.class)
public interface ItemDao {

    @Sql("show columns from item;")
    public String[] getColumnNames();

    public Item[] selectAll();

    @Arguments({ "offset", "num" })
    @Sql("select * from item limit /*offset*/, /*num*/")
    public Item[] selectLimit(int offset, int num);

    @Sql("select name from item;")
    public String[] getName();

    @Arguments("id")
    public Item selectById(Integer id);

    @Arguments("limit")
    @Sql("select id from item order by rand() limit 0, /*limit*/;")
    public int[] selectRandomId(int limit);

    @Sql("select count( id ) from item;")
    public int getCount();

    @Sql("select max( id ) from item;")
    public int getMaxId();

    public int insert(Item item);

    public int update(Item item);

    @Arguments({ "id", "name", "price", "updateDate", "updaterName", "deleteFlag" })
    @Sql("update item set name=/*name*/, price=/*price*/, update_date=/*updateDate*/, updater_name=/*updaterName*/, delete_flag=/*deleteFlag*/ where id=/*id*/;")
    public int updateItem(int id, String name, int price, Timestamp updateDate, String updaterName, boolean deleteFlag);

    public int delete(Item item);

    @Arguments("id")
    @Sql("delete from item where id=/*id*/0;")
    public int deleteById(int id);
}
