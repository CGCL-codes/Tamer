package com.narirelays.ems.sandbox;

import java.util.List;
import org.apache.commons.beanutils.LazyDynaBean;
import com.narirelays.ems.applogic.MaintainDetector4Sophic;
import com.narirelays.ems.persistence.orm.MaintainLog;
import com.narirelays.ems.resources.StorageService;
import com.narirelays.ems.services.MaintainMeasurePoints4SophicService;

public class TestMainLog {

    public static void main(String[] argv) {
        MaintainDetector4Sophic maintainDetector4Sophic = (MaintainDetector4Sophic) StorageService.ctx.getBean("maintainDetector4Sophic");
        List<LazyDynaBean> records = maintainDetector4Sophic.queryUpdatedSophicRecords();
        List<MaintainLog> logs = maintainDetector4Sophic.queryMaintainLogs();
        List<String> classes = maintainDetector4Sophic.queryUpdatedClass(records, logs);
        System.out.println(classes);
    }
}
