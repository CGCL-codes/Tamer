package com.xfltr.hapax.parser;

import com.xfltr.hapax.TemplateDictionary;
import com.xfltr.hapax.TemplateLoaderContext;
import java.io.PrintWriter;

/**
 * Implementation of a {{#SECTION_NODE}} and the paired {{/SECTION_NODE}}.
 *
 * @author dcoker
 */
public class SectionNode extends TemplateNode {

    @Override
    public void evaluate(TemplateDictionary dict, TemplateLoaderContext context, PrintWriter collector) {
    }

    public boolean isOpenSectionTag() {
        return type_ == TYPE.OPEN;
    }

    public boolean isCloseSectionTag() {
        return type_ == TYPE.CLOSE;
    }

    enum TYPE {

        OPEN, CLOSE
    }

    private final String sectionName_;

    private final TYPE type_;

    private SectionNode(String nodeName, TYPE node_type) {
        this.sectionName_ = nodeName;
        this.type_ = node_type;
    }

    public String getSectionName() {
        return sectionName_;
    }

    public static SectionNode open(String nodeName) {
        return new SectionNode(nodeName, TYPE.OPEN);
    }

    public static SectionNode close(String nodeName) {
        return new SectionNode(nodeName, TYPE.CLOSE);
    }
}
