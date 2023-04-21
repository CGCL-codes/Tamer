package be.lassi.pdf.tags;

import be.lassi.pdf.State;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/**
 * Support html to pdf translation of the anchor tag.
 */
public class A extends Tag {

    public A(final State state) {
        super(state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (getState().getCurrentParagraph() == null) {
            Paragraph paragraph = new Paragraph();
            getState().setCurrentParagraph(paragraph);
        }
        AnchorInfo anchor = new AnchorInfo(getState().getCurrentParagraph());
        String href = getState().getAttribute("href");
        if (href != null) {
            anchor.setHref(href);
        }
        String name = getState().getAttribute("name");
        if (name != null) {
            anchor.setName(name);
        }
        getState().getStack().push(anchor);
        Paragraph paragraph = new Paragraph();
        getState().setCurrentParagraph(paragraph);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() throws Exception {
        if (getState().getCurrentParagraph() == null) {
            Paragraph paragraph = new Paragraph();
            getState().setCurrentParagraph(paragraph);
        }
        AnchorInfo anchor = (AnchorInfo) getState().getStack().pop();
        String href = anchor.getHref();
        if (href != null && href.endsWith("ScreenShot.html")) {
            addScreenShot(href);
        } else {
            Paragraph tmp = anchor.getParagraph();
            Phrase tmp2 = new Phrase();
            tmp2.add(getState().getCurrentParagraph());
            tmp.add(tmp2);
            getState().setCurrentParagraph(tmp);
        }
    }

    private void addScreenShot(final String href) throws Exception {
        addScreenShotLabel();
        addScreenShotImage(href);
        getState().setCurrentParagraph(new Paragraph());
    }

    private void addScreenShotImage(final String href) throws Exception {
        int index = href.indexOf("ScreenShot.html");
        String imageName = href.substring(0, index) + ".png";
        Image img = Image.getInstance(State.DIR + imageName);
        float scalePercent = calculateScalePercent(img);
        img.scalePercent(scalePercent);
        img.setAlignment(Image.MIDDLE);
        getState().addElement(img);
    }

    private float calculateScalePercent(final Image img) {
        float maxWidth = State.PAGE_SIZE.getWidth() - State.PAGE_MARGIN_LEFT - State.PAGE_MARGIN_RIGHT;
        float scalePercent = IMG.IMAGE_SCALE_FACTOR;
        if ((img.getWidth() * scalePercent) > maxWidth) {
            scalePercent = Math.min(scalePercent, maxWidth * 100f / img.getWidth());
        }
        return scalePercent;
    }

    private void addScreenShotLabel() throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(State.FONT_PARAGRAPH);
        paragraph.add(new Phrase("Screen shot:"));
        paragraph.setSpacingBefore(5f);
        paragraph.setSpacingAfter(5f);
        getState().addElement(paragraph);
    }
}
