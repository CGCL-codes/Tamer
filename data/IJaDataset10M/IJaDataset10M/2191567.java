package com.lowagie.tools;

import java.io.FileOutputStream;
import java.util.HashMap;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Encrypts a PDF document. It needs iText (http://www.lowagie.com/iText).
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class encrypt_pdf {

    private static final int INPUT_FILE = 0;

    private static final int OUTPUT_FILE = 1;

    private static final int USER_PASSWORD = 2;

    private static final int OWNER_PASSWORD = 3;

    private static final int PERMISSIONS = 4;

    private static final int STRENGTH = 5;

    private static final int MOREINFO = 6;

    private static final int permit[] = { PdfWriter.AllowPrinting, PdfWriter.AllowModifyContents, PdfWriter.AllowCopy, PdfWriter.AllowModifyAnnotations, PdfWriter.AllowFillIn, PdfWriter.AllowScreenReaders, PdfWriter.AllowAssembly, PdfWriter.AllowDegradedPrinting };

    private static void usage() {
        System.out.println("usage: input_file output_file user_password owner_password permissions 128|40 [new info string pairs]");
        System.out.println("permissions is 8 digit long 0 or 1. Each digit has a particular security function:");
        System.out.println();
        System.out.println("AllowPrinting");
        System.out.println("AllowModifyContents");
        System.out.println("AllowCopy");
        System.out.println("AllowModifyAnnotations");
        System.out.println("AllowFillIn (128 bit only)");
        System.out.println("AllowScreenReaders (128 bit only)");
        System.out.println("AllowAssembly (128 bit only)");
        System.out.println("AllowDegradedPrinting (128 bit only)");
        System.out.println("Example permissions to copy and print would be: 10100000");
    }

    /**
     * Encrypts a PDF document.
     * 
     * @param args input_file output_file user_password owner_password permissions 128|40 [new info string pairs]
     */
    public static void main(String args[]) {
        System.out.println("PDF document encryptor");
        if (args.length <= STRENGTH || args[PERMISSIONS].length() != 8) {
            usage();
            return;
        }
        try {
            int permissions = 0;
            String p = args[PERMISSIONS];
            for (int k = 0; k < p.length(); ++k) {
                permissions |= (p.charAt(k) == '0' ? 0 : permit[k]);
            }
            System.out.println("Reading " + args[INPUT_FILE]);
            PdfReader reader = new PdfReader(args[INPUT_FILE]);
            System.out.println("Writing " + args[OUTPUT_FILE]);
            HashMap moreInfo = new HashMap();
            for (int k = MOREINFO; k < args.length - 1; k += 2) moreInfo.put(args[k], args[k + 1]);
            PdfEncryptor.encrypt(reader, new FileOutputStream(args[OUTPUT_FILE]), args[USER_PASSWORD].getBytes(), args[OWNER_PASSWORD].getBytes(), permissions, args[STRENGTH].equals("128"), moreInfo);
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
