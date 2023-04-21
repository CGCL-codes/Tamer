package crl.blackberry.pdfwriter;

import java.util.Vector;

public class Page {

    private PDFDocument mDocument;

    private IndirectObject mIndirectObject;

    private Vector mPageFonts;

    private Vector mXObjects;

    private IndirectObject mPageContents;

    public Page(PDFDocument document) {
        mDocument = document;
        mIndirectObject = mDocument.newIndirectObject();
        mPageFonts = new Vector();
        mXObjects = new Vector();
        setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN, StandardFonts.WIN_ANSI_ENCODING);
        mPageContents = mDocument.newIndirectObject();
        mDocument.includeIndirectObject(mPageContents);
    }

    public IndirectObject getIndirectObject() {
        return mIndirectObject;
    }

    private String getFontReferences() {
        String result = "";
        if (!mPageFonts.isEmpty()) {
            result = "    /Font <<\n";
            int x = 0;
            for (int loop = 0; loop < mPageFonts.size(); loop++) {
                result += "      /F" + Integer.toString(++x) + " " + ((IndirectObject) mPageFonts.elementAt(loop)).getIndirectReference() + "\n";
            }
            result += "    >>\n";
        }
        return result;
    }

    private String getXObjectReferences() {
        String result = "";
        if (!mXObjects.isEmpty()) {
            result = "    /XObject <<\n";
            for (int loop = 0; loop < mXObjects.size(); loop++) {
                result += "      " + ((XObjectImage) mXObjects.elementAt(loop)).asXObjectReference() + "\n";
            }
            result += "    >>\n";
        }
        return result;
    }

    public void render(String pagesIndirectReference) {
        mIndirectObject.setDictionaryContent("  /Type /Page\n  /Parent " + pagesIndirectReference + "\n" + "  /Resources <<\n" + getFontReferences() + getXObjectReferences() + "  >>\n" + "  /Contents " + mPageContents.getIndirectReference() + "\n");
    }

    public void setFont(String subType, String baseFont) {
        IndirectObject lFont = mDocument.newIndirectObject();
        mDocument.includeIndirectObject(lFont);
        lFont.setDictionaryContent("  /Type /Font\n  /Subtype /" + subType + "\n  /BaseFont /" + baseFont + "\n");
        mPageFonts.addElement(lFont);
    }

    public void setFont(String subType, String baseFont, String encoding) {
        IndirectObject lFont = mDocument.newIndirectObject();
        mDocument.includeIndirectObject(lFont);
        lFont.setDictionaryContent("  /Type /Font\n  /Subtype /" + subType + "\n  /BaseFont /" + baseFont + "\n  /Encoding /" + encoding + "\n");
        mPageFonts.addElement(lFont);
    }

    private void addContent(String content) {
        mPageContents.addStreamContent(content);
        String streamContent = mPageContents.getStreamContent();
        mPageContents.setDictionaryContent("  /Length " + Integer.toString(streamContent.length()) + "\n");
        mPageContents.setStreamContent(streamContent);
    }

    public void addRawContent(String rawContent) {
        addContent(rawContent);
    }

    public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text) {
        addText(leftPosition, topPositionFromBottom, fontSize, text, Transformation.DEGREES_0_ROTATION);
    }

    public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text, String transformation) {
        addContent("BT\n" + transformation + " " + Integer.toString(leftPosition) + " " + Integer.toString(topPositionFromBottom) + " Tm\n" + "/F" + Integer.toString(mPageFonts.size()) + " " + Integer.toString(fontSize) + " Tf\n" + "(" + text + ") Tj\n" + "ET\n");
    }

    public void addLine(int fromLeft, int fromBottom, int toLeft, int toBottom) {
        addContent(Integer.toString(fromLeft) + " " + Integer.toString(fromBottom) + " m\n" + Integer.toString(toLeft) + " " + Integer.toString(toBottom) + " l\nS\n");
    }

    public void addRectangle(int fromLeft, int fromBottom, int toLeft, int toBottom) {
        addContent(Integer.toString(fromLeft) + " " + Integer.toString(fromBottom) + " " + Integer.toString(toLeft) + " " + Integer.toString(toBottom) + " re\nS\n");
    }

    private String ensureXObjectImage(XObjectImage xObject) {
        for (int loop = 0; loop < mXObjects.size(); loop++) {
            XObjectImage x = (XObjectImage) mXObjects.elementAt(loop);
            if (x.getId().equals(xObject.getId())) {
                return x.getName();
            }
        }
        mXObjects.addElement(xObject);
        xObject.appendToDocument();
        return xObject.getName();
    }

    public void addImage(int fromLeft, int fromBottom, int width, int height, XObjectImage xImage, String transformation) {
        final String name = ensureXObjectImage(xImage);
        final String translate = "1 0 0 1 " + fromLeft + " " + fromBottom;
        final String scale = "" + width + " 0 0 " + height + " 0 0";
        final String rotate = transformation + " 0 0";
        addContent("q\n" + translate + " cm\n" + rotate + " cm\n" + scale + " cm\n" + name + " Do\n" + "Q\n");
    }
}
