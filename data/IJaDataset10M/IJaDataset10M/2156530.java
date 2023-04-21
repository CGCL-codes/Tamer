package com.ericsson.xsmp.service.alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spark.util.FileUtil;
import org.spark.util.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import com.ericsson.xsmp.common.PulseListener;
import com.ericsson.xsmp.common.mq.MessageQueue;
import com.ericsson.xsmp.common.template.TemplateResolver;

public class AlertServiceImpl implements AlertService, PulseListener, InitializingBean {

    private static Log LOG = LogFactory.getLog(AlertServiceImpl.class);

    private static final String DEFAULT_ALERT_TYPE = "default";

    String[] importFileStorges;

    String backupStorge = "/tmp/backup";

    MessageQueue alertQueue;

    long period = 30000;

    TemplateResolver templateResolver;

    String separator = "|";

    Map<String, Long> importTaskMap = new HashMap<String, Long>();

    Map<String, Map<Integer, String>> alertTypeDefs;

    Map<String, String> alertTemplates;

    public void afterPropertiesSet() throws Exception {
        if (alertTypeDefs == null) alertTypeDefs = new HashMap<String, Map<Integer, String>>();
        if (!alertTypeDefs.containsKey(DEFAULT_ALERT_TYPE)) {
            Map<Integer, String> defaultAlertTypeDef = new HashMap<Integer, String>();
            defaultAlertTypeDef.put(0, "receiver");
            defaultAlertTypeDef.put(1, "mode");
            defaultAlertTypeDef.put(2, "message");
            alertTypeDefs.put(DEFAULT_ALERT_TYPE, defaultAlertTypeDef);
        }
        if (alertTemplates == null) alertTemplates = new HashMap<String, String>();
        if (!alertTemplates.containsKey(DEFAULT_ALERT_TYPE)) alertTemplates.put(DEFAULT_ALERT_TYPE, "${message}");
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public TemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    public Map<String, String> getAlertTemplates() {
        return alertTemplates;
    }

    public void setAlertTemplates(Map<String, String> alertTemplates) {
        this.alertTemplates = alertTemplates;
    }

    public Map<String, Map<Integer, String>> getAlertTypeDefs() {
        return alertTypeDefs;
    }

    public void setAlertTypeDefs(Map<String, Map<Integer, String>> alertTypeDefs) {
        this.alertTypeDefs = alertTypeDefs;
    }

    public MessageQueue getAlertQueue() {
        return alertQueue;
    }

    public void setAlertQueue(MessageQueue alertQueue) {
        this.alertQueue = alertQueue;
    }

    public String getBackupStorge() {
        return backupStorge;
    }

    public void setBackupStorge(String backupStorge) {
        this.backupStorge = backupStorge;
    }

    public String[] getImportFileStorges() {
        return importFileStorges;
    }

    public void setImportFileStorges(String[] importFileStorges) {
        this.importFileStorges = importFileStorges;
    }

    public synchronized void beat() {
        LOG.info("Start scan import task...");
        if (this.importFileStorges != null) {
            for (int i = 0; i < importFileStorges.length; i++) {
                File file = new File(importFileStorges[i]);
                if (!file.exists()) continue;
                doScanTask(file);
            }
        }
        LOG.info("End scan import task.");
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getSpeed() {
        return getPeriod();
    }

    private void doScanTask(File file) {
        if (file == null || !file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (int i = 0; i < files.length; i++) doScanTask(files[i]);
        } else {
            String key = file.getPath();
            Long size = (Long) importTaskMap.get(key);
            if (size == null || size.longValue() < file.length()) importTaskMap.put(key, new Long(file.length())); else {
                importParticipantFromFile(key);
                FileUtil.mkdirs(this.backupStorge + file.getParent());
                FileUtil.copyFile(file.getPath(), this.backupStorge + file.getPath());
                FileUtil.deleteFile(file.getPath());
                importTaskMap.remove(key);
            }
        }
    }

    public void importParticipantFromFile(String filePath) {
        FileInputStream instream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) return;
            String fileName = file.getName();
            String alertType = null;
            int pos = fileName.indexOf("_");
            if (pos > 0) alertType = fileName.substring(0, pos);
            if (alertType == null || alertType.length() <= 0) alertType = DEFAULT_ALERT_TYPE;
            Map<Integer, String> alertTypeDef = alertTypeDefs.get(alertType);
            if (alertTypeDef == null) {
                LOG.warn("Can not find alter type definition for " + alertType);
                alertTypeDef = alertTypeDefs.get(DEFAULT_ALERT_TYPE);
                alertType = DEFAULT_ALERT_TYPE;
            }
            instream = new FileInputStream(file);
            LOG.info("Ready to import task from:" + file.getName() + " ...");
            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "GBK"));
            while ((line = reader.readLine()) != null) {
                if (line.length() < 3) continue;
                String[] fields = null;
                if (DEFAULT_ALERT_TYPE.equalsIgnoreCase(alertType)) {
                    fields = line.split(separator.replace("|", "\\|"), 3);
                    if (fields.length == 2) {
                        String[] temparr = new String[3];
                        temparr[0] = fields[0];
                        temparr[1] = "0";
                        temparr[2] = fields[1];
                        fields = temparr;
                    }
                } else fields = StringUtil.split(line, separator);
                if (fields == null || fields.length <= 0) continue;
                AlertTask alertTask = new AlertTask();
                alertTask.setAlertType(alertType);
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i] == null) continue;
                    if (alertTypeDef != null && alertTypeDef.containsKey(i)) alertTask.setParameter(alertTypeDef.get(i), fields[i]); else alertTask.setParameter("field" + i, fields[i]);
                }
                String messageTemplate = alertTask.getParameter("message");
                if (messageTemplate == null || messageTemplate.length() <= 0) {
                    if (alertTemplates.containsKey(alertType)) messageTemplate = alertTemplates.get(alertType);
                }
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("alertTask", alertTask);
                paramMap.putAll(alertTask.getParamMap());
                alertTask.setMessage(templateResolver.resolveTemplate(messageTemplate, paramMap));
                alertQueue.post(alertTask);
            }
        } catch (Throwable err) {
            LOG.error("Import task from file meet error", err);
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (IOException err) {
                    LOG.error("Import task from file meet error", err);
                }
            }
        }
        LOG.info("Import task from file end.");
    }
}
