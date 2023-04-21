package com.ericsson.xsmp.service.cdr;

import java.io.File;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spark.util.FileUtil;
import org.spark.util.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import com.ericsson.xsmp.service.cdr.Record.Field;
import com.ericsson.xsmp.util.DateUtil;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

public class FileAppender implements Appender, CacheListener, InitializingBean {

    private static Log LOG = LogFactory.getLog(FileAppender.class);

    private AtomicInteger counter = new AtomicInteger();

    private Cache cache;

    private String cdrIdTime = null;

    private int cdrIdSeq = 0;

    String encoding = "UTF-8";

    String storgePath = "";

    String fileNamePrefix = "";

    String fieldSeparator = "|";

    String rowSeparator = "\n";

    Parser parser;

    boolean headerControl = false;

    boolean footerControl = false;

    int fieldStyle = 0;

    public void afterPropertiesSet() throws Exception {
        cache.init();
    }

    public void deliver(Object content, int count) {
        try {
            String nowTime = DateUtil.getCurDateTime();
            int seqNo = 0;
            String fileName = storgePath + "/" + fileNamePrefix + nowTime + "-0000";
            File file = new File(fileName);
            while (!file.createNewFile()) {
                seqNo++;
                fileName = storgePath + "/" + fileNamePrefix + nowTime + "-" + StringUtil.recruitString("" + seqNo, 4, '0');
                file = new File(fileName);
            }
            StringBuffer buffer = new StringBuffer();
            if (headerControl) buffer.append(fieldSeparator).append(rowSeparator);
            buffer.append(content.toString());
            if (footerControl) buffer.append(count).append(rowSeparator);
            FileUtil.appendFileText(file.getPath(), buffer.toString(), "UTF-8");
            counter.addAndGet(count);
        } catch (Throwable err) {
            LOG.error(null, err);
        }
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public void setFileNamePrefix(String fileNamePrefix) {
        this.fileNamePrefix = fileNamePrefix;
    }

    public String getRowSeparator() {
        return rowSeparator;
    }

    public void setRowSeparator(String rowSeparator) {
        this.rowSeparator = rowSeparator;
    }

    public String getStorgePath() {
        return storgePath;
    }

    public void setStorgePath(String storgePath) {
        this.storgePath = storgePath;
        File file = new File(storgePath);
        if (!file.exists()) file.mkdirs();
    }

    public synchronized String generateCdrId() {
        if (cdrIdTime == null) {
            cdrIdTime = DateUtil.getCurDateTime();
            cdrIdSeq = 0;
        } else {
            String temp = DateUtil.getCurDateTime();
            if (temp.equals(cdrIdTime)) cdrIdSeq++; else {
                cdrIdTime = temp;
                cdrIdSeq = 0;
            }
        }
        return cdrIdTime + StringUtil.recruitString("" + cdrIdSeq, 4, '0');
    }

    public void append(Object event) {
        if (event == null) return;
        StringBuffer tempBuffer = new StringBuffer();
        Object cdr = null;
        if (event instanceof Record) cdr = event; else {
            if (parser == null || !parser.accept(event)) return;
            String id = generateCdrId();
            cdr = parser.parse(id, event);
        }
        if (cdr == null) return;
        if (cdr instanceof Record) {
            Record record = (Record) cdr;
            if (record != null && record.getFields() != null) {
                Iterator iter = record.getFields().iterator();
                while (iter.hasNext()) {
                    Field field = (Field) iter.next();
                    switch(fieldStyle) {
                        case 1:
                            tempBuffer.append(field.getName()).append("=").append(field.getValue() == null ? "" : field.getValue()).append(fieldSeparator);
                            break;
                        default:
                            tempBuffer.append(field.getValue() == null ? "" : field.getValue()).append(fieldSeparator);
                            break;
                    }
                }
            }
            tempBuffer.append(rowSeparator);
        } else tempBuffer.append(cdr.toString());
        cache.append(tempBuffer.toString());
    }

    public String getFieldSeparator() {
        return fieldSeparator;
    }

    public void setFieldSeparator(String fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }

    public int getTotal() {
        return counter.get();
    }

    public int getFieldStyle() {
        return fieldStyle;
    }

    public void setFieldStyle(int fieldStyle) {
        this.fieldStyle = fieldStyle;
    }

    public boolean isFooterControl() {
        return footerControl;
    }

    public void setFooterControl(boolean footerControl) {
        this.footerControl = footerControl;
    }

    public boolean isHeaderControl() {
        return headerControl;
    }

    public void setHeaderControl(boolean headerControl) {
        this.headerControl = headerControl;
    }
}
