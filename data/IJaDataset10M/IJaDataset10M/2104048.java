package com.tenline.pinecone.platform.web.payment.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import com.tenline.pinecone.platform.web.payment.BatchPayInterface;
import com.tenline.pinecone.platform.web.payment.model.PayInfo;

/**
 * create alibaba batch file to transfer 
 * @author WongYQ
 * 
 */
public class AlipayBatchPay implements BatchPayInterface {

    /**
	 * logger
	 */
    protected static Logger logger = Logger.getLogger(AlipayBatchPay.class.toString());

    /**
	 *  alipaybatch info
	 */
    private ArrayList<PayInfo> payInfoList;

    /**
	 * 
	 */
    public AlipayBatchPay(ArrayList<PayInfo> payInfoList) {
        this.payInfoList = payInfoList;
    }

    /**
	 * create alipay batch file txt
	 * @return
	 */
    public String createBatchFile() {
        String result = "";
        try {
            String cfgFilePath = System.getProperty("catalina.home");
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
            String day = sd.format(today);
            String path = cfgFilePath + "/webapps/paycenter/downfiles/moneytree/" + day + "/";
            String fileName = path + "AlipayBatch.txt";
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            File txt = new File(fileName);
            if (!txt.exists()) {
                txt.createNewFile();
                System.out.println("create new file ok");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (PayInfo payino : payInfoList) {
                writer.write(payino.getUserAccountID() + "\t\t" + payino.getPayNumber());
                writer.newLine();
            }
            writer.flush();
            writer.close();
            result = fileName;
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }
}
