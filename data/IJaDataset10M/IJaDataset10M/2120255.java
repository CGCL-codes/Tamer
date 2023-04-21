package it.infn.mib.awt;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class CreateJavaFromShape {

    public static void create(Shape s, String name, PrintStream pw) {
        pw.println("import java.awt.*;");
        pw.println("import java.awt.geom.*;");
        pw.println("public class " + name + " {");
        pw.println("    public static Shape create() {");
        pw.println("        GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);");
        double[] coords = new double[6];
        for (PathIterator pi = s.getPathIterator(null); !pi.isDone(); pi.next()) {
            switch(pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    pw.println("        gp.moveTo(" + coords[0] + "f, " + coords[1] + "f);");
                    break;
                case PathIterator.SEG_LINETO:
                    pw.println("        gp.lineTo(" + coords[0] + "f, " + coords[1] + "f);");
                    break;
                case PathIterator.SEG_QUADTO:
                    pw.println("	    gp.quadTo(" + coords[0] + "f, " + coords[1] + "f, " + coords[2] + "f, " + coords[3] + "f);");
                    break;
                case PathIterator.SEG_CUBICTO:
                    pw.println("	    gp.cubicTo " + coords[0] + "f, " + coords[1] + "f, " + coords[2] + "f, " + coords[3] + "f, " + coords[4] + "f, " + coords[5] + "f);");
                    break;
                case PathIterator.SEG_CLOSE:
                    pw.println("	    gp.closePath();");
                    break;
            }
        }
        pw.println("	    return gp;");
        pw.println("    }");
        pw.println("}");
    }

    public static void main(String[] args) throws IOException {
        Shape s = new RasterShape(args[0]);
        CreateJavaFromShape.create(s, args[1], System.out);
    }
}
