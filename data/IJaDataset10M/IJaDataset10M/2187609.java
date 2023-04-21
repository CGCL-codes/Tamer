package plasmid.fileWorker;

import java.io.*;
import java.util.*;
import java.util.zip.Checksum;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class JCp {

    Walker walker;

    Visitor visitor = null;

    String sourcePath;

    String targetPath;

    byte[] buf = new byte[32 * 1024];

    byte[] buf2 = new byte[buf.length];

    long bytesRead = 0;

    Hashtable mtimeMap = new Hashtable(20000);

    Hashtable cksumMap = new Hashtable(20000);

    static long mtimeCollisions = 0;

    static long checkSumCollisions = 0;

    static long files = 0;

    public JCp() {
    }

    public static void main(String[] args) {
        int i = 0;
        JCp jcp = new JCp();
        if (args[0].equals("-r")) {
            i++;
            jcp.visitor = jcp.new ReadVisitor();
        } else if (args[0].equals("-s")) {
            i++;
            jcp.visitor = jcp.new StatVisitor();
        } else if (args[0].equals("-c")) {
            i++;
            jcp.visitor = jcp.new CksumVisitor();
        } else if (args[0].equals("-C")) {
            i++;
            jcp.visitor = jcp.new CksumVisitor(true);
        } else {
            jcp.visitor = jcp.new CpVisitor();
            jcp.targetPath = args[args.length - 1];
            args[args.length - 1] = null;
        }
        jcp.walker = new Walker(jcp.visitor);
        for (; i < args.length; i++) {
            if (args[i] == null) continue;
            jcp.sourcePath = args[i];
            jcp.walker.pathWalk(jcp.sourcePath);
        }
        System.out.println("JCp done. bytesRead " + jcp.bytesRead + " cksumColl " + checkSumCollisions);
    }

    protected class ReadVisitor implements Visitor {

        public int preVisit(File f) {
            System.out.println("" + ReadVisitor.this + ": File " + f);
            if (f.isFile()) {
                try {
                    FileInputStream fis = new FileInputStream(f);
                    int br;
                    while ((br = fis.read(buf)) > 0) {
                        bytesRead += br;
                    }
                    fis.close();
                } catch (Exception e) {
                    System.out.println("File " + f + e);
                }
            }
            return 0;
        }

        public int postVisit(File f) {
            return 0;
        }
    }

    protected class StatVisitor implements Visitor {

        public int preVisit(File f) {
            Long mtime = new Long(f.lastModified());
            files++;
            String name = (String) mtimeMap.get(mtime);
            if (name != null) {
                mtimeCollisions++;
                if ((mtimeCollisions % 1000) == 0) System.out.println(mtimeCollisions + "collisions " + files + " files MTIME=" + mtime + " Match: " + name + " " + f.getPath());
            } else {
                mtimeMap.put(mtime, f.getPath());
            }
            return 0;
        }

        public int postVisit(File f) {
            return 0;
        }
    }

    /**
     * Another experiment - compute and compare checksums for every file in
     * a subtree. The checksum algorithm is "Adler32" from java.util.zip,
     * and seems to be very fast and pretty good. Run against my 3.5 gig of junk
     * on a year-old, very active "c:" drive, it completed in about 28 minutes.
     * <PRE>
     * Visit c:/\gstools
     * Visit c:/\gstools\gsview
     * Visit c:/\gstools\gs5.50
     * Visit c:/\gstools\gs5.50\fonts
     * JCp done. bytesRead 3424092507 cksumColl 1
     * real    27m56.75s
     * user    0m 0.00s
     * sys     0m 0.00s
     * </PRE>
     * That time included the byte-by-byte comparison of all files with
     * identical <checksums, size> (a hash table on the checksum value
     * is maintained). In those 70,000 plus files, there was one checksum
     * collision (same checksum & size, different content) - that was on two
     * of three disney(4).html files in different session caches (?). The files
     * had mostly indentical fields but different hex values (but all other
     * locations matched).
     *
     * As for the actual CRC costs - on my 266 P2, I'm getting 100megabytes/sec
     * for adler and 33megabytes/sec for CRC32 - i.e., both are much faster than
     * the disk.
     *
     * So the speed is great. But if I can't trust the checksum algorithm
     * "completely" (pick your P(collision)), it can't help where it might.
     * I.e., 1) avoiding prop'ing a whole file for vacuous changes,
     * 2) resolving false conflicts w/o complete transmission of large files,
     * maybe even block based propagation (there are probably other areas).
     */
    protected class CksumVisitor implements Visitor {

        protected boolean doAdler;

        CksumVisitor(boolean doAdler) {
            this.doAdler = doAdler;
        }

        CksumVisitor() {
            this.doAdler = false;
        }

        public int preVisit(File f) {
            Long cksumVal = null;
            if (f.isFile()) {
                try {
                    cksumVal = computeCksum(f);
                } catch (IOException e) {
                    System.out.println("CksumVisitor: " + f + " - " + e);
                    return -1;
                }
                files++;
                String name = (String) cksumMap.get(cksumVal);
                if (name != null) {
                    if (cmpFiles(name, f.getPath()) != 0) {
                        checkSumCollisions++;
                        if ((checkSumCollisions % 1) == 0) System.out.println(checkSumCollisions + "collisions " + files + " files CKSUM=" + cksumVal + " Match: " + name + " " + f.getPath());
                    }
                } else {
                    cksumMap.put(cksumVal, f.getPath());
                }
            } else if (f.isDirectory()) {
                System.out.println("Visit " + f);
            }
            if (false) System.out.println("CksumVisitor " + f + " val " + cksumVal + " bytesRead " + bytesRead);
            return 0;
        }

        protected Long computeCksum(File f) throws IOException {
            Checksum cksum = doAdler ? (Checksum) new Adler32() : (Checksum) new CRC32();
            FileInputStream fis = new FileInputStream(f);
            CheckedInputStream cis = new CheckedInputStream(fis, cksum);
            int br;
            while ((br = cis.read(buf)) > 0) {
                bytesRead += br;
            }
            Long cksumValObj = new Long(cis.getChecksum().getValue());
            cis.close();
            cis = null;
            fis = null;
            cksum = null;
            return cksumValObj;
        }

        protected int cmpFiles(String p1, String p2) {
            File f1 = new File(p1), f2 = new File(p2);
            FileInputStream fis1 = null, fis2 = null;
            byte[] buf1 = buf;
            int ret = -1;
            try {
                if (f1.length() != f2.length()) {
                    ret = (int) (f1.length() - f2.length());
                    return ret;
                }
                fis1 = new FileInputStream(f1);
                fis2 = new FileInputStream(f2);
                int b1 = 0, b2 = 0;
                while (true) {
                    int nr1 = fis1.read(buf1);
                    int nr2 = fis2.read(buf2);
                    bytesRead += nr1 > 0 ? nr1 : 0;
                    bytesRead += nr2 > 0 ? nr2 : 0;
                    if (nr1 == -1 || nr1 != nr2) {
                        ret = nr1 - nr2;
                        break;
                    }
                    for (int i = 0; i < nr1; i++) {
                        b1 = buf1[i];
                        b2 = buf2[i];
                        if (b1 != b2) {
                            break;
                        }
                    }
                }
                ret = b1 - b2;
            } catch (IOException e) {
                ret = -1;
            }
            try {
                if (fis1 != null) fis1.close();
                if (fis2 != null) fis2.close();
            } catch (IOException e) {
            }
            return ret;
        }

        public int postVisit(File f) {
            return 0;
        }
    }

    protected class CpVisitor implements Visitor {

        byte[] buf = new byte[32 * 1024];

        long bytesRead = 0;

        /**
         * There's a bug here -- I'm copying all the files into the same
         * target directory (not making new subdirs).  Ok for now.
         */
        public int preVisit(File sourceFile) {
            System.out.println("" + CpVisitor.this + ": File " + sourceFile);
            if (sourceFile.isFile()) {
                try {
                    File targetFile = new File(targetPath, sourceFile.getName());
                    if (targetFile.exists()) {
                        System.out.println("Exists!: " + targetPath + sourceFile.getName());
                        return 0;
                    }
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    FileInputStream fis = new FileInputStream(sourceFile);
                    int br;
                    while ((br = fis.read(buf)) > 0) {
                        fos.write(buf, 0, br);
                        bytesRead += br;
                    }
                    fos.close();
                    fis.close();
                } catch (Exception e) {
                    System.out.println("File " + sourceFile + e);
                }
            }
            return 0;
        }

        public int postVisit(File sourceFile) {
            return 0;
        }
    }
}
