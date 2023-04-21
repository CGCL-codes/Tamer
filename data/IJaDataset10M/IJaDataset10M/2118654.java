package org.wings.plaf.css;

import java.io.IOException;
import org.wings.SLayoutManager;
import org.wings.STemplateLayout;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;
import org.wings.template.LabelTagHandler;
import org.wings.template.RangeTagHandler;
import org.wings.template.SimpleTagHandler;
import org.wings.template.TemplateParseContext;
import org.wings.template.TemplateSource;
import org.wings.template.parser.PageParser;

/**
 * @author Achim Derigs
 */
public class TemplateLayoutCG implements LayoutCG {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The parser looks for the '<OBJECT></OBJECT>' - tags.
     */
    static {
        PageParser parser = PageParser.getInstance();
        parser.addTagHandler("OBJECT", RangeTagHandler.class);
        parser.addTagHandler("WINGSOBJECT", RangeTagHandler.class);
        parser.addTagHandler("TEXTAREA", RangeTagHandler.class);
        parser.addTagHandler("SELECT", RangeTagHandler.class);
        parser.addTagHandler("INPUT", SimpleTagHandler.class);
        parser.addTagHandler("LABEL", LabelTagHandler.class);
    }

    private void write(Device device, STemplateLayout layout) throws IOException {
        final TemplateSource source = layout.getTemplateSource();
        if (source == null) {
            device.print("Unable to open template-file <em>null</em> in '" + layout);
        } else {
            layout.getPageParser().process(source, new TemplateParseContext(device, layout));
        }
    }

    /**
     * @param device  the device to write the code to
     * @param manager the layout manager
     * @throws IOException
     */
    public void write(Device device, SLayoutManager manager) throws IOException {
        write(device, (STemplateLayout) manager);
    }
}
