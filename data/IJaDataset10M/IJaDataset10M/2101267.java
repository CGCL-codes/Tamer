package com.amd.javalabs.tools.doc;

import org.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

public class Section {

    private PDOutlineItem start;

    private PDOutlineItem end;

    public Section(PDOutlineItem _start, PDOutlineItem _end) {
        start = _start;
        end = _end;
    }

    public PDOutlineItem getEnd() {
        return end;
    }

    public PDOutlineItem getStart() {
        return start;
    }
}
