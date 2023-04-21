package main.log.manager;

import java.util.Date;
import org.lc.dao.hibernate.BaseHibernateDao;
import domain.log.Log;

public class LogManager extends BaseHibernateDao<Log> {

    public void saveLog(String user, String msg) {
        Log log = new Log();
        log.setOperator(user);
        log.setMsg(msg);
        log.setOperateDate(new Date());
        save(log);
    }
}
