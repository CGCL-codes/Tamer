package org.radeox.macro;

import org.radeox.macro.parameter.MacroParameter;
import org.radeox.macro.table.Table;
import org.radeox.macro.table.TableBuilder;
import java.io.IOException;
import java.io.Writer;

public class TableMacro extends BaseLocaleMacro {

    public String getLocaleKey() {
        return "macro.table";
    }

    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        String content = params.getContent();
        if (null == content) throw new IllegalArgumentException("TableMacro: missing table content");
        content = content.trim() + "\n";
        Table table = TableBuilder.build(content);
        table.calc();
        table.appendTo(writer);
        return;
    }
}
