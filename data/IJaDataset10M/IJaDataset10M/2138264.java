package org.redwood.business.etl.logfileextractor.unclogfileextractor;

import org.redwood.business.etl.logfile.LogFileHome;
import org.redwood.business.etl.logfile.LogFileObject;
import org.redwood.business.etl.logfileextractor.LogFileExtractionException;
import org.redwood.business.etl.logfileextractor.LogFileExtractor;
import org.redwood.business.etl.logfileextractor.WildcardFilenameFilter;
import org.redwood.business.etl.logfileextractor.LogFileStreamWrapper;
import org.redwood.business.usermanagement.datasource.DataSourceObject;
import org.redwood.tools.MonitorMessenger;
import java.security.MessageDigest;
import javax.ejb.SessionBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.nio.*;

public class UncLogFileExtractorBean extends LogFileExtractor implements SessionBean {

    boolean DEBUG = true;

    private MonitorMessenger messenger = new MonitorMessenger();

    /**
   *
   * Transfers files specified in the DataSource object from
   * the source into the database.
   * @param datasource The DataSourceObject to be transferred. Multiple files can be
   *                   separated in the attribute rw_filename of the DataSourceObject
   *                   which contains the full path to the file
   * @return A Collection of the logfiles that have been transferred
   * @exception LogFileExtractionException Thrown if a parsing error occurs
   * @exception RemoteException Thrown if any other error occurs
   */
    public Collection transfer(DataSourceObject datasource) throws RemoteException, LogFileExtractionException {
        Vector logfiles = new Vector();
        LogFileObject curlogfile = null;
        initValues();
        websiteid = datasource.getRw_webSiteID();
        datasourceid = datasource.getRw_id();
        server = datasource.getRw_serverAddress();
        sfilename = datasource.getRw_filename();
        Vector filenames = new Vector();
        long curTime = 0;
        int transferredfilescount = 0;
        if (!server.startsWith("\\\\")) server = "\\\\" + server;
        sfilename = sfilename.replace('/', '\\');
        if (sfilename.indexOf('|') != -1) {
            int j = sfilename.indexOf('|');
            StringTokenizer tok = new StringTokenizer(sfilename, "|");
            while (tok.hasMoreTokens()) filenames.add(tok.nextToken().trim());
        } else {
            filenames.add(sfilename);
        }
        spath = "";
        sfilename = "";
        try {
            Enumeration filenamesEnum = filenames.elements();
            while (filenamesEnum.hasMoreElements()) {
                File lastmodifiedfile = null;
                String curfilename = (String) filenamesEnum.nextElement();
                if (!curfilename.startsWith("\\")) curfilename = "\\" + curfilename;
                int i = curfilename.lastIndexOf('\\');
                if (i != -1) {
                    spath = curfilename.substring(0, i);
                    sfilename = curfilename.substring(i + 1);
                }
                File f = new File(server + spath);
                WildcardFilenameFilter filter = new WildcardFilenameFilter("*", sfilename);
                File[] files = f.listFiles(filter);
                long lastmodifieddate = 0;
                long filesize = 0;
                if (files != null) {
                    int directorylen = files.length;
                    for (int j = 0; j < directorylen; j++) {
                        if ((j == 0) || (j > 0 && (lastmodifieddate < files[j].lastModified()))) {
                            lastmodifieddate = files[j].lastModified();
                            filesize = files[j].length();
                            lastmodifiedfile = (File) files[j];
                        }
                    }
                    curTime = System.currentTimeMillis();
                    LogFileStreamWrapper wrapper = new LogFileStreamWrapper(lastmodifiedfile);
                    boolean morefilesavailable = true;
                    ByteArrayOutputStream bout = null;
                    while (morefilesavailable) {
                        bout = new ByteArrayOutputStream();
                        Deflater compressor = new Deflater();
                        compressor.setLevel(Deflater.BEST_SPEED);
                        DeflaterOutputStream zip = new DeflaterOutputStream(bout, compressor);
                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        long bytecounter = 0;
                        byte read;
                        int limitcounter = 1;
                        ByteBuffer bf = ByteBuffer.allocate(1024 * 1024);
                        while ((read = (byte) wrapper.read()) != -1) {
                            md5.update(read);
                            bytecounter++;
                            if (!bf.hasRemaining()) {
                                zip.write(bf.array());
                                bf = ByteBuffer.allocate(1024 * 1024);
                                System.out.println("Transferred " + limitcounter + " MB of file(s)");
                                limitcounter++;
                            }
                            bf.put(read);
                        }
                        zip.write(bf.array());
                        zip.close();
                        bf.clear();
                        bf = null;
                        zip = null;
                        String checksum = getMD5Hash(md5);
                        Context ctx = new InitialContext();
                        Object lfRef = ctx.lookup(LogFileHome.COMP_NAME);
                        LogFileHome logfilehome = (LogFileHome) PortableRemoteObject.narrow(lfRef, LogFileHome.class);
                        Collection transferredfiles = logfilehome.findByWebsiteChecksum(websiteid, checksum);
                        String currentfilename = "";
                        if (transferredfiles.size() < 1) {
                            currentfilename = wrapper.getFilename();
                            curlogfile = logfilehome.create(primkeygen.getUniqueId_long(), currentfilename);
                            datetransferred = new java.util.Date(curTime);
                            filedate = new java.util.Date(wrapper.getFiledate());
                            curlogfile.setRw_checksum(checksum);
                            curlogfile.setRw_filesize(bytecounter);
                            curlogfile.setRw_filepath(spath);
                            curlogfile.setRw_websiteID(websiteid);
                            curlogfile.setRw_datasourceID(datasourceid);
                            curlogfile.setRw_serverAddress(server);
                            curlogfile.setRw_processState("TRANSFERRED");
                            curlogfile.setRw_dateTransferred(datetransferred);
                            curlogfile.setRw_fileDate(filedate);
                            curlogfile.setRw_file(bout.toByteArray());
                            bout.close();
                            bout = null;
                            String logfileformat = datasource.getLogfileFormat().getRw_name();
                            String apacheformat = datasource.getRw_apacheFormat();
                            curlogfile.setRw_logFileFormat(logfileformat);
                            if (apacheformat != null && logfileformat.toUpperCase().equals("APACHE")) {
                                if (!apacheformat.equals("")) {
                                    curlogfile.setRw_apacheFormatString(apacheformat);
                                }
                            }
                            logfiles.add(curlogfile);
                            transferredfilescount++;
                        } else {
                            Iterator it = transferredfiles.iterator();
                            messenger.sendMonitorError("File " + currentfilename + " already in Database (website #" + websiteid + ")");
                        }
                        morefilesavailable = wrapper.nextEntry();
                    }
                    wrapper.close();
                    wrapper = null;
                } else {
                    messenger.sendMonitorError("UNC path for datasource is wrong: " + server + spath + sfilename + " (Websiteid: #" + websiteid + ")");
                }
            }
            if (transferredfilescount == 0) throw new LogFileExtractionException("No logfiles transferred for website #" + websiteid);
            return logfiles;
        } catch (OutOfMemoryError e) {
            throw new LogFileExtractionException("Not enough memory (RAM) to store the logfile -> extend JVM memory via start up switches");
        } catch (NullPointerException e) {
            throw new LogFileExtractionException("Could transfer file (cause unknown)");
        } catch (Exception e) {
            e.printStackTrace();
            throw new LogFileExtractionException(e.getMessage());
        }
    }
}
