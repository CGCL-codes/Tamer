package naru.aweb.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.log4j.Logger;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import naru.aweb.config.FilterCategory;
import naru.aweb.config.FilterEntry;
import naru.aweb.util.JdoUtil;

public class Maintenance {

    private static Logger logger = Logger.getLogger(Maintenance.class);

    private static int BATCH_SIZE = 128;

    public static void addCategorys(String soruce, File listFile) throws IOException {
        FileInputStream fis = new FileInputStream(listFile);
        TarInputStream tin = new TarInputStream(new GZIPInputStream(fis));
        TarEntry tarEnt = tin.getNextEntry();
        while (tarEnt != null) {
            addCategory(soruce, tarEnt, tin);
            tarEnt = tin.getNextEntry();
        }
        tin.close();
    }

    private static void addCategory(String source, TarEntry tarEnt, InputStream is) throws IOException {
        if (tarEnt.getSize() == 0) {
            return;
        }
        String categoryName = tarEnt.getName();
        logger.info("addCategory categoryName:" + categoryName + ":size:" + tarEnt.getSize());
        boolean isUrl;
        if (categoryName.endsWith("/urls")) {
            isUrl = true;
        } else if (categoryName.endsWith("/domains")) {
            isUrl = false;
        } else {
            return;
        }
        PersistenceManager pm = JdoUtil.getPersistenceManager();
        pm.currentTransaction().begin();
        FilterCategory category = null;
        try {
            category = FilterCategory.getByKey(source, categoryName, isUrl);
            if (category == null) {
                category = new FilterCategory(source, categoryName, isUrl);
                category.save();
            }
            category.setLastUpdate(new Date());
            category = pm.detachCopy(category);
            pm.currentTransaction().commit();
        } finally {
            if (pm.currentTransaction().isActive()) {
                pm.currentTransaction().rollback();
            }
        }
        addEntrys(category, is);
    }

    private static void addEntrys(FilterCategory category, InputStream listIs) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(listIs, "iso8859_1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PersistenceManager pm = JdoUtil.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            int counter = 0;
            while (true) {
                if ((counter % BATCH_SIZE) == 0) {
                    logger.info("addEntrys transaction:" + counter);
                    if (tx.isActive()) {
                        tx.commit();
                    }
                    tx.begin();
                }
                String filter = reader.readLine();
                if (filter == null) {
                    break;
                }
                counter++;
                FilterEntry entry = FilterEntry.getByKey(category, filter);
                if (entry == null) {
                    entry = new FilterEntry(category, filter);
                    entry.save();
                }
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
