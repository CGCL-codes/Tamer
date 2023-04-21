package com.bening.smsapp.digester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.bening.smsapp.bean.*;
import com.bening.smsapp.bootstrap.BootConfigParameters;
import com.bening.smsapp.dao.SmsAppDao;
import com.bening.smsapp.factory.SmsAppFactory;
import com.bening.smsapp.util.Asn1Digester;
import com.bening.smsapp.util.SmsAppTools;

public class DigesterEngine {

    static final Logger logger = Logger.getLogger(DigesterEngine.class);

    private static SmsAppDao smsDao = SmsAppFactory.getDaoObject();

    public static void process(BootConfigParameters params) {
        try {
            PathSource p = new PathSource();
            p.setLocalPathDir(params.getTempDir());
            p.setFileNamePattern(".TMP");
            List files = SmsAppTools.getLocalFileList(p);
            for (int i = 0; i < files.size(); i++) {
                FileSource fs = (FileSource) files.get(i);
                StringBuffer filePath = new StringBuffer();
                filePath.append(params.getTempDir());
                filePath.append(File.separator);
                filePath.append(fs.getFileName());
                FileInputStream fis = new FileInputStream(new File(filePath.toString()));
                System.out.println(System.currentTimeMillis());
                Asn1Digester.parseXml(fis);
                System.out.println(System.currentTimeMillis());
                List list = Asn1Digester.getTemp();
                List<Asn1Bean> result = new ArrayList<Asn1Bean>();
                for (int j = 0; j < list.size(); j++) {
                    RawBean r = (RawBean) list.get(j);
                    Asn1Bean bean = r.buildAsn1Bean();
                    result.add(bean);
                }
                smsDao.insertParsingResultAsn1(result, fs.getFileName());
                int idxDot = filePath.toString().indexOf(".TMP");
                String newName = filePath.toString().substring(0, idxDot);
                SmsAppTools.moveFile(filePath.toString(), newName);
            }
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
}
