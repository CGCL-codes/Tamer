package konozama.function;

import java.sql.Timestamp;
import java.util.Random;
import konozama.dao.HistoryDao;
import konozama.dao.ItemDao;
import konozama.entity.History;
import org.seasar.framework.container.SingletonS2Container;

public interface HistoryManagerInterface {

    public int[] getRelation(int id);

    public int setHistory(int[] itemIds);

    public int generateHistory(int num);
}
