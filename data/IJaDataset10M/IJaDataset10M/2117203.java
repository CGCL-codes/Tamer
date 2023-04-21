package com.bcurtu.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import com.bcurtu.db.AccountDao;
import com.bcurtu.service.TempAliasService;

public class DeleteJob extends TimerTask {

    private Logger log = Logger.getLogger(this.getClass());

    private TempAliasService service;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public TempAliasService getService() {
        return service;
    }

    public void setService(TempAliasService service) {
        this.service = service;
    }

    @Override
    public void run() {
        Date date = new Date();
        log.debug("Running DeleteJob" + sdf.format(date));
        int i = service.deleteExpiredAccounts(date);
        log.debug("Deleted " + i + " Accounts");
    }
}
