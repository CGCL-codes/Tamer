package com.aimluck.eip.modules.screens;

import java.util.List;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import com.aimluck.commons.field.ALDateTimeField;
import com.aimluck.eip.cayenne.om.portlet.EipMFacility;
import com.aimluck.eip.orm.Database;
import com.aimluck.eip.orm.query.SelectQuery;
import com.aimluck.eip.services.orgutils.ALOrgUtilsService;

/**
 *
 *
 */
public class FileIOFacilityCsvFileScreen extends ALCSVScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(FileIOScheduleCsvFileScreen.class.getName());

    /**
   * 
   * @param rundata
   * @return
   */
    @Override
    protected String getContentType(RunData rundata) {
        return "application/octet-stream";
    }

    /**
   * 
   * @param rundata
   * @return
   * @throws Exception
   */
    @Override
    protected String getCSVString(RunData rundata) throws Exception {
        String LINE_SEPARATOR = System.getProperty("line.separator");
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("施設名,メモ,施設が所属するグループ");
            sb.append(LINE_SEPARATOR);
            sb.append("\"ガスト\",\"ハンバーグ\",\"スカイラーク\"");
            sb.append(LINE_SEPARATOR);
            sb.append("\"スターバックス\",\"フラペチーノ\",\"サザビー\"");
            sb.append(LINE_SEPARATOR);
            sb.append("\"和民\",\"安い酒\",\"モンテローザ\"");
            sb.append(LINE_SEPARATOR);
            return sb.toString();
        } catch (Exception e) {
            logger.error("[ERROR]" + e);
            return null;
        }
    }

    /**
   * 
   * @param rundata
   * @return
   * @throws Exception
   */
    protected String getCSVStringDB(RunData rundata) throws Exception {
        String LINE_SEPARATOR = System.getProperty("line.separator");
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("開始日,開始時刻,終了日,終了時刻,場所,予定,内容,名前");
            sb.append(LINE_SEPARATOR);
            SelectQuery<EipMFacility> query = Database.query(EipMFacility.class);
            List<EipMFacility> list = query.fetchList();
            EipMFacility rec;
            ALDateTimeField time = new ALDateTimeField();
            return sb.toString();
        } catch (Exception e) {
            logger.error("[ERROR]" + e);
            return null;
        }
    }

    @Override
    protected String getFileName() {
        return ALOrgUtilsService.getAlias() + "_facilities.csv";
    }
}
