package org.radeox.macro;

import org.radeox.macro.parameter.MacroParameter;
import java.io.IOException;
import java.io.Writer;

public class HelloWorldMacro extends BaseMacro {

    private String[] paramDescription = { "1: name to print" };

    public String getName() {
        return "hello";
    }

    public String getDescription() {
        return "Say hello example macro.";
    }

    public String[] getParamDescription() {
        return paramDescription;
    }

    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        if (params.getLength() == 1) {
            writer.write("Hello <b>");
            writer.write(params.get("0"));
            writer.write("</b>");
        } else {
            throw new IllegalArgumentException("Number of arguments does not match");
        }
    }
}
