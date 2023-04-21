package org.riverock.portlet.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;
import org.riverock.generic.main.CacheFile;
import org.riverock.portlet.schema.member.MemberArea;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.tools.SiteUtils;

/**
 * $Id: MemberFile.java,v 1.10 2006/05/02 17:44:09 serg_main Exp $
 */
public final class MemberFile extends CacheFile {

    private static final Logger log = Logger.getLogger(MemberFile.class);

    private Map<String, ModuleType> memberHash = null;

    protected void finalize() throws Throwable {
        if (memberHash != null) {
            memberHash.clear();
            memberHash = null;
        }
        super.finalize();
    }

    /**
     * @deprecated use Iterator
     * @return Enumeration
     */
    public Enumeration getMemberModules() {
        if (memberHash == null) return null;
        return Collections.enumeration(memberHash.values());
    }

    public int getMemberModuleCount() {
        if (memberHash == null) return 0;
        return memberHash.size();
    }

    public ModuleType getModule(String nameModule_) {
        if (memberHash == null || nameModule_ == null) return null;
        boolean isUseCache = isUseCache();
        if (log.isDebugEnabled()) log.debug("#7.1.0  " + nameModule_ + ", isUseCache:" + isUseCache + ", " + getFile().getName());
        if (isUseCache) {
            ModuleType moduleType = memberHash.get(nameModule_);
            if (log.isDebugEnabled()) log.debug("moduleType: " + moduleType);
            return moduleType;
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug("#7.00.01 " + getFile());
                log.debug("#7.01.00 check was file modified");
            }
            if (isNeedReload()) {
                if (log.isInfoEnabled()) log.info("Redeploy member file " + getFile().getName());
                setLastModified();
                processFile();
                if (log.isDebugEnabled()) log.debug("#7.01.02 Unmarshal done");
                return memberHash.get(nameModule_);
            }
        } catch (Exception e) {
            log.error("Exception while get module " + nameModule_, e);
            return null;
        }
        ModuleType moduleType = memberHash.get(nameModule_);
        if (log.isDebugEnabled()) {
            log.debug("#7.02 file not changed. Get module from cache");
            log.debug("moduleType: " + moduleType);
            log.debug("Values in HashMap: " + memberHash.size());
            for (Map.Entry<String, ModuleType> entry : memberHash.entrySet()) {
                log.debug("key: " + entry.getKey() + ", value: " + entry.getValue().getName());
            }
        }
        return moduleType;
    }

    public MemberFile(File tempFile) {
        super(tempFile, 1000 * 10);
        if (log.isDebugEnabled()) {
            log.debug("Start unmarshalling file " + tempFile);
        }
        processFile();
    }

    private static final Object syncObj = new Object();

    private void processFile() {
        try {
            log.debug("start processFile()");
            InputSource inSrc = new InputSource(new FileInputStream(getFile()));
            MemberArea ma = (MemberArea) Unmarshaller.unmarshal(MemberArea.class, inSrc);
            ma.validate();
            memberHash = new HashMap<String, ModuleType>();
            for (int i = 0; i < ma.getModuleCount(); i++) {
                ModuleType desc = ma.getModule(i);
                if (log.isDebugEnabled()) {
                    log.debug("put new module in filenaame hash, key: " + desc.getName() + ", " + desc.getName());
                }
                memberHash.put(desc.getName(), desc);
            }
            if (log.isDebugEnabled()) {
                synchronized (syncObj) {
                    try {
                        FileWriter w = new FileWriter(SiteUtils.getTempDir() + File.separatorChar + System.currentTimeMillis() + '-' + getFile().getName());
                        Marshaller.marshal(ma, w);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            String errorString = "Error processing member file " + getFile();
            log.error(errorString, e);
            throw new IllegalStateException(errorString, e);
        }
    }
}
