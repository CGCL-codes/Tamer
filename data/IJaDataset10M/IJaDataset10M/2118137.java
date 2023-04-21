package org.paradise.dms.web.action.buildingsmgr;

import java.io.File;
import java.util.Date;
import org.apache.log4j.Logger;
import org.paradise.dms.util.DateUtil;

public class BuildMgrActionTest {

    private static Logger log = Logger.getLogger(BuildMgrActionTest.class);

    public String makeDir() {
        File dirFile = null;
        Date d = new Date();
        String path = "C:/DMS_DB_Back_" + DateUtil.format(d, DateUtil.C_DATE_PATTON_DEFAULT) + d.getTime();
        try {
            dirFile = new File(path);
            if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
                boolean creadok = dirFile.mkdirs();
                if (creadok) {
                    log.info(" ok:创建文件夹成功！ ");
                    return path;
                } else {
                    log.info(" err:创建文件夹失败！ ");
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("DMS_error:创建保存数据库的文件夹失败,");
            log.error("失败原因" + e);
            return null;
        }
        return null;
    }

    /**
	 * Description:
	 * 
	 * @Version1.0 2009-4-1 下午10:36:34 李双江（paradise.lsj@gmail.com）创建
	 * @param args
	 */
    public static void main(String[] args) {
        @SuppressWarnings("unused") BuildMgrActionTest t = new BuildMgrActionTest();
        log.info(t.makeDir());
    }
}
