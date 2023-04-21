package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;
import org.pdfbox.util.TextPosition;

public class PDFSections extends PDFSection {

    private TextPosition lastRight;

    private LinkedList<PDFSection> sections = new LinkedList<PDFSection>();

    PDFSection section;

    public PDFSections() {
    }

    @Override
    public void addAcceptDate(TextPosition position) {
        section.addAcceptDate(position);
    }

    @Override
    public void addBecause(TextPosition position) {
        section.addBecause(position);
    }

    @Override
    public void addPriotyNo(TextPosition position) {
        section.addPriotyNo(position);
    }

    @Override
    public void addPurpose(TextPosition position) {
        section.addPurpose(position);
    }

    @Override
    public void addRightAndEtc(TextPosition position) {
        section.addRightAndEtc(position);
    }

    public PDFSection get(int i) {
        return sections.get(i);
    }

    public List<PDFSection> getSections() {
        return java.util.Collections.unmodifiableList(sections);
    }

    public void newSection() {
        section = new PDFSection();
        sections.add(section);
        section.newPage();
    }

    public void nextPage() {
        if (section == null) {
            return;
        }
        section.newPage();
    }

    public int size() {
        return sections.size();
    }
}
