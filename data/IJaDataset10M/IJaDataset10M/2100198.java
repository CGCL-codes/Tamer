package org.wings.plaf.xhtml.css1;

import java.io.IOException;
import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;
import org.wings.plaf.xhtml.*;
import org.wings.style.Style;

public final class SeparatorCG extends org.wings.plaf.xhtml.SeparatorCG {

    protected void writeAttributes(Device d, SSeparator separator) throws IOException {
        Utils.writeStyleAttribute(d, separator.getStyle());
        super.writeAttributes(d, separator);
    }
}
