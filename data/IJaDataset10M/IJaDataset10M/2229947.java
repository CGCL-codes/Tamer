package test.pdf;

import java.io.*;
import java.util.*;
import uk.org.retep.pdf.*;
import test.*;

/**
 * This class tests the PDF package by building a simple document
 */
public class LowLevelTest implements Runnable {

    public LowLevelTest() {
    }

    public void run() {
        RunTests.section("Testing uk.org.retep.pdf package");
        PDF pdf = new PDF();
        RunTests.result(this, "Create document for low-level access", true);
        PDFPage page = new PDFPage();
        pdf.add(page);
        RunTests.result(this, "Create page", true);
        PDFStream ps = new PDFStream();
        pdf.add(ps);
        page.add(ps);
        RunTests.result(this, "Create stream for low level access", true);
        PrintWriter pw = ps.getWriter();
        pw.println("150 350 m 150 350 l S");
        pw.println("% Draw thicker, dashed line segment");
        pw.println("4 w % Set a linewidth of 4 points.");
        pw.println("[4 6] 0 d");
        pw.println("% Set a dash pattern with 4 units on, 6 units off.");
        pw.println("150 250 m");
        pw.println("400 250 l");
        pw.println("S");
        pw.println("[ ] 0 d % reset dash pattern to a solid line");
        pw.println("1 w % reset linewidth to 1 unit");
        pw.println("% Draw a rectangle, 1 unit light blue border,");
        pw.println("% filled with red");
        pw.println(".5 .75 1 rg % light blue for fill color");
        pw.println("1 0 0 RG % red for stroke color");
        pw.println("200 200 50 75 re");
        pw.println("B");
        pw.println("% Draw a curve using a Bezier curve,");
        pw.println("% filled with gray and with a colored border.");
        pw.println(".5 .1 .2 RG");
        pw.println("0.7 g");
        pw.println("300 300 m");
        pw.println("300 400 400 400 400 300 c");
        pw.println("b");
        RunTests.result(this, "write raw graphics to pdf stream", true);
        PDFOutline out = new PDFOutline("Test Card", page);
        pdf.add(out);
        pdf.getOutline().add(out);
        RunTests.result(this, "add an Outline to page", true);
        String fname = "test/pdf/test.pdf";
        try {
            FileOutputStream fos = new FileOutputStream(fname);
            pdf.write(fos);
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        RunTests.result(this, "write pdf document to file " + fname, true);
    }
}
