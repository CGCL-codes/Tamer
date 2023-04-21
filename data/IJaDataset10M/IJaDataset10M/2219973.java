package org.wings.plaf.xhtml.css1;

import java.io.IOException;
import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class ParagraphCG extends org.wings.plaf.xhtml.ParagraphCG {

    protected void writeAttributes(Device d, SParagraph paragraph) throws IOException {
        Utils.writeStyleAttribute(d, paragraph.getStyle());
        super.writeAttributes(d, paragraph);
    }
}
