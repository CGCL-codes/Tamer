package javasean;

import java.io.*;
import java.util.*;
import java.text.*;
import java.net.URL;
import java.net.URLConnection;

/**
* Collection of utilities for comparing the date of an Accession at NCBI and the date of an
* Accession held locally.  Brings the local copy up to date (via download) if it is older than
* what is available at NCBI.
*/
public class FileDownload {

    private String m_SrcURL;

    private int m_length = 0;

    private static int DATESTARTPOS = 67;

    public FileDownload(String the_SrcURL) {
        m_SrcURL = the_SrcURL;
    }

    public int getLength() {
        return m_length;
    }

    public String getURLDate(String the_fileName) {
        try {
            URL SrcURL = new URL(m_SrcURL + the_fileName);
            URLConnection SrcConn = SrcURL.openConnection();
            SrcConn.setUseCaches(false);
            InputStream in = SrcConn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            if ((line = br.readLine()) != null) {
                if (line.length() > DATESTARTPOS) {
                    String tmpLenStr = line.substring(30, 40).trim();
                    try {
                        m_length = Integer.decode(tmpLenStr).intValue();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return line.substring(DATESTARTPOS).trim();
                }
            }
            br.close();
        } catch (IOException ioex) {
        }
        return null;
    }

    public static Date convertDate(String the_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd'-'MMM'-'yyyy");
        ParsePosition pos = new ParsePosition(0);
        return sdf.parse(the_date, pos);
    }

    public String getDIRDate(String the_CompareDir, String the_fileName) {
        try {
            FileReader fr = new FileReader(the_CompareDir + the_fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            if ((line = br.readLine()) != null) {
                if (line.length() > DATESTARTPOS) {
                    return line.substring(DATESTARTPOS).trim();
                } else if ((line = br.readLine()) != null) {
                    if (line.length() > DATESTARTPOS) {
                        return line.substring(DATESTARTPOS).trim();
                    } else {
                        return null;
                    }
                }
            }
        } catch (IOException ioex) {
            System.out.println("FileDownload:getDIRDate() File <" + the_fileName + "> PROBABLY Not Found");
        }
        return null;
    }

    public int download(String the_DestDir, String the_fileName, String the_WriteSuffix) {
        try {
            String OutFileName = the_DestDir + the_fileName;
            if (the_WriteSuffix != null) {
                OutFileName += the_WriteSuffix;
            }
            System.out.println("DOWNLOADING TO<" + OutFileName + ">");
            FileOutputStream fos = new FileOutputStream(OutFileName);
            System.out.println("DOWNLOADING DONE");
            System.out.println(m_SrcURL + the_fileName);
            URL SrcURL = new URL(m_SrcURL + the_fileName);
            URLConnection SrcConn = SrcURL.openConnection();
            SrcConn.setUseCaches(false);
            InputStream in = SrcConn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sbuf = new StringBuffer();
            String line = null;
            if (the_WriteSuffix == null) {
                while ((line = br.readLine()) != null) {
                    sbuf.append(line + "\n");
                }
            } else {
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("<!DOCTYPE")) {
                        if ((line = br.readLine()) != null) {
                        }
                        if ((line = br.readLine()) != null) {
                        }
                    } else {
                        if (line.startsWith("</Seq-entry>")) {
                            sbuf.append(line + "\n");
                            String firstnextline = br.readLine();
                            if (firstnextline != null) {
                                String secondnextline = br.readLine();
                                if (secondnextline != null) {
                                    String thirdnextline = br.readLine();
                                    if ((thirdnextline == null) || (thirdnextline.length() < 2)) {
                                    } else {
                                        sbuf.append(secondnextline + "\n");
                                        sbuf.append(thirdnextline + "\n");
                                    }
                                }
                            }
                        } else {
                            sbuf.append(line + "\n");
                        }
                    }
                }
            }
            if (sbuf.toString().length() > 50) {
                fos.write(sbuf.toString().getBytes());
                System.out.println("FileDownload Successfully wrote <" + OutFileName + "> with <" + sbuf.toString().length() + "> Bytes");
                fos.close();
                return 0;
            } else {
                fos.close();
                return -1;
            }
        } catch (IOException ioex) {
            System.out.println("DOWNLOAD EXCEPTION");
            ioex.printStackTrace();
            return -1;
        }
    }

    public int updateFiles(String the_CompareDir, String the_DownloadDir, String the_fileName, String the_WriteSuffix) {
        String DIRDate = getDIRDate(the_CompareDir, the_fileName);
        int ret = 0;
        if (DIRDate == null) {
            ret = download(the_DownloadDir, the_fileName, the_WriteSuffix);
            return ret;
        }
        String URLDate = getURLDate(the_fileName);
        if (URLDate == null) {
            return -2;
        }
        Date urlDate = convertDate(URLDate);
        Date dirDate = convertDate(DIRDate);
        if (urlDate.compareTo(dirDate) > 0) {
            ret = download(the_DownloadDir, the_fileName, the_WriteSuffix);
        } else if (urlDate.compareTo(dirDate) == 0) {
        } else if (urlDate.compareTo(dirDate) < 0) {
        }
        return ret;
    }

    public static String getCurrentDate() {
        Date dt = new Date(System.currentTimeMillis());
        return dt.toString();
    }

    public static void main(String args[]) {
        String SeanToTry = "AE003408";
        if (args.length > 0) {
            SeanToTry = args[0];
        }
        String TESTSrcURL = "http://www.ncbi.nlm.nih.gov/entrez/viewer.cgi?view=gb&txt=on&val=";
        String TESTCompareDir = "/users/smutniak/SEAN_REPOS/";
        String TESTDownloadDir = "/users/smutniak/SEAN_REPOS/";
        FileDownload fd = new FileDownload(TESTSrcURL);
        fd.updateFiles(TESTCompareDir, TESTDownloadDir, SeanToTry, null);
    }

    public static void remove(String the_filePathName) {
        if (the_filePathName != null) {
            String command = "rm " + the_filePathName;
            try {
                System.out.println("WOULD BE ISSUING <" + command + ">");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean exists(String the_filePathName) {
        try {
            File f = new File(the_filePathName);
            if (f.canRead()) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
