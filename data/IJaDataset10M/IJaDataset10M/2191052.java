package radeox.macro;

import java.io.IOException;
import java.io.Writer;
import cmspider.utilities.string.StringUtilities;
import radeox.macro.parameter.MacroParameter;

public class HTMLMacro extends BaseLocaleMacro {

    @Override
    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        writer.write(StringUtilities.htmlDecoding(params.getContent()));
    }

    public String getLocaleKey() {
        return "macro.html";
    }
}
