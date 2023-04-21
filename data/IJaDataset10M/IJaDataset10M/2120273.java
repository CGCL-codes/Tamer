package kernel.pojo.tests;

import java.sql.SQLException;
import kernel.pojo.*;
import net.ko.cache.KCache;
import net.ko.kobject.KListObject;
import net.ko.kobject.KObject;
import net.ko.kobject.KSession;
import net.ko.ksql.KDBMysql;
import net.ko.utils.KScriptTimer;

public class Test_sakila_KCity {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        KObject.useCache = true;
        KCache.loadAllCache();
        KSession ks = new KSession();
        try {
            ks.connect(new KDBMysql("localhost", "root", "", "sakila"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        KScriptTimer.start();
        KScriptTimer.start("KCity");
        KListObject<KCity> citys = (KListObject<KCity>) ks.kloadFromDb(KCity.class);
        KScriptTimer.stop("KCity");
        System.out.println(citys);
        KScriptTimer.stop();
        KObject.cacheShutdown();
        try {
            ks.getDb().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
