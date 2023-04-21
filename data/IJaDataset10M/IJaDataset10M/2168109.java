package net.sourceforge.unicodeconvert;

import java.io.*;
import net.sourceforge.unicodeconvert.converter.UnicodeConversion;

/**
 *@author     Quan Nguyen (all parts)
 *@author     Tuan Pham (directory traversing)
 *@author     Gero Herrmann (1.0.7: added argument count, handling of relative output file paths)
 *@version    1.2, 2005-05-27
 *@see        http://unicodeconvert.sourceforge.net/
 */
public class Cmdline {

    private static String sourceEncoding;

    /**
     *  Handle command line java -jar Uni.jar
     *
     *@param  args  [ISC|NCR|TCVN3|UNI-COMP|VIQR|VISCII|VNI|VPS] [inputfile] [outputfile]
     */
    public static void main(String[] args) {
        if (args.length == 3) {
            sourceEncoding = args[0];
            if (sourceEncoding.equalsIgnoreCase("UNI-COMP")) {
                sourceEncoding = "Unicode Composite";
            }
            File inputFile = new File(args[1]);
            File outputFile = new File(args[2]);
            (new Cmdline()).traverseDir(inputFile, outputFile);
        } else {
            System.err.println("Usage: java  -jar UnicodeConverter.jar\n" + "       javaw -jar UnicodeConverter.jar\n" + "   or  UnicodeConverter.jar\n" + "       (to launch the program in GUI mode)\n\n" + "   or  java -jar UnicodeConverter.jar <sourceEncoding> <inputfile/dir> <outputfile/dir>\n" + "       (to execute the program in command-line mode)\n\n" + "where possible Source Encoding options are\n" + "ISC, NCR, TCVN3, UNI-COMP, VIQR, VISCII, VNI, VPS.");
        }
    }

    /**
     *  Convert a file to Unicode.
     *
     *@param  iFile  input file handle
     *@param  oFile  outfile handle
     *@author        Tuan Pham (modified from Quan Nguyen's code)
     */
    private void convertAFile(File iFile, File oFile) {
        try {
            oFile.getAbsoluteFile().getParentFile().mkdirs();
            System.out.println(iFile.getPath() + " -> " + oFile.getPath());
            UnicodeConversion legacyText = new UnicodeConversion(sourceEncoding);
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(oFile), "UTF-8");
            out.write(legacyText.convert(iFile));
            out.close();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("I/O Error. Can't write to file.");
        }
    }

    /**
     *  Traverse the dir/file.
     *  if ifile.isDirectory(), convert all files and dirs -> ofile(dir)
     *  if ifile.isFile(), convert ifile -> ofile(file)
     *
     *@param  ifile  the input file
     *@param  ofile  the output file
     *@author        Tuan Pham
     */
    private void traverseDir(File ifile, File ofile) {
        try {
            if (!ifile.exists()) {
                System.out.println("File or directory does not exist!");
                return;
            }
            if (ifile.isFile()) {
                convertAFile(ifile, ofile);
            } else {
                File[] f = ifile.listFiles();
                if (!ofile.exists()) {
                    ofile.mkdir();
                }
                for (int i = 0; i < f.length; i++) {
                    File of = new File(ofile.getPath(), f[i].getName());
                    if (f[i].isDirectory()) {
                        of.mkdir();
                        traverseDir(f[i], of);
                    } else {
                        convertAFile(f[i], of);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
