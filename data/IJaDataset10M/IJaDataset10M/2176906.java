package com.tetrasix.majix.rtf;

import java.io.*;
import com.tetrasix.majix.xml.*;

public class RtfLineBreak extends RtfObject {

    RtfLineBreak() {
    }

    void Dump(PrintWriter out) {
        out.print("<line/>");
    }

    public void generate(XmlGenerator gen, XmlWriter out, XmlGeneratorContext context) {
        gen.rtfgenerate(this, out, context);
    }
}
