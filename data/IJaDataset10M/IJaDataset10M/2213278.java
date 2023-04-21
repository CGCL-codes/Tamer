package org.radeox.macro;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.context.BaseFilterContext;
import org.radeox.filter.context.FilterContext;
import org.radeox.macro.code.SourceCodeFormatter;
import org.radeox.macro.parameter.MacroParameter;
import org.radeox.util.Service;
import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.api.engine.context.RenderContext;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class CodeMacro extends LocalePreserved {

    private static Log log = LogFactory.getLog(CodeMacro.class);

    private Map formatters;

    private FilterContext nullContext = new BaseFilterContext();

    private String start;

    private String end;

    public String getLocaleKey() {
        return "macro.code";
    }

    public void setInitialContext(InitialRenderContext context) {
        super.setInitialContext(context);
        Locale outputLocale = (Locale) context.get(RenderContext.OUTPUT_LOCALE);
        String outputName = (String) context.get(RenderContext.OUTPUT_BUNDLE_NAME);
        ResourceBundle outputMessages = ResourceBundle.getBundle(outputName, outputLocale);
        start = outputMessages.getString(getLocaleKey() + ".start");
        end = outputMessages.getString(getLocaleKey() + ".end");
    }

    public CodeMacro() {
        formatters = new HashMap();
        Iterator formatterIt = Service.providers(SourceCodeFormatter.class);
        while (formatterIt.hasNext()) {
            try {
                SourceCodeFormatter formatter = (SourceCodeFormatter) formatterIt.next();
                String name = formatter.getName();
                if (formatters.containsKey(name)) {
                    SourceCodeFormatter existing = (SourceCodeFormatter) formatters.get(name);
                    if (existing.getPriority() < formatter.getPriority()) {
                        formatters.put(name, formatter);
                        log.debug("Replacing formatter: " + formatter.getClass() + " (" + name + ")");
                    }
                } else {
                    formatters.put(name, formatter);
                    log.debug("Loaded formatter: " + formatter.getClass() + " (" + name + ")");
                }
            } catch (Exception e) {
                log.warn("CodeMacro: unable to load code formatter", e);
            }
        }
        addSpecial('[');
        addSpecial(']');
        addSpecial('{');
        addSpecial('}');
        addSpecial('*');
        addSpecial('-');
        addSpecial('#');
        addSpecial('\\');
    }

    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        SourceCodeFormatter formatter = null;
        if (params.getLength() == 0 || !formatters.containsKey(params.get("0"))) {
            formatter = (SourceCodeFormatter) formatters.get(initialContext.get(RenderContext.DEFAULT_FORMATTER));
            if (null == formatter) {
                log.warn("Formatter not found.");
                formatter = (SourceCodeFormatter) formatters.get("java");
            }
        } else {
            formatter = (SourceCodeFormatter) formatters.get(params.get("0"));
        }
        String result = formatter.filter(params.getContent(), nullContext);
        writer.write(start);
        writer.write(replace(result.trim()));
        writer.write(end);
        return;
    }
}
