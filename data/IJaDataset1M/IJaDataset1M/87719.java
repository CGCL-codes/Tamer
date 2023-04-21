package net.woodstock.rockapi.itext.beans.directcontent.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;

public class ItextImage extends ItextBasicImpl {

    private static final long serialVersionUID = 1L;

    private int angle = 0;

    private Image image = null;

    private String imageUrl = "";

    private float scale = 100;

    public ItextImage() {
        super();
    }

    public int getAngle() {
        return this.angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void write(Document document, PdfContentByte content) throws DocumentException {
        if (this.image == null) {
            try {
                this.image = Image.getInstance(this.imageUrl);
            } catch (BadElementException e) {
                throw new DocumentException(e);
            } catch (MalformedURLException e) {
                throw new DocumentException(e);
            } catch (IOException e) {
                throw new DocumentException(e);
            }
        }
        this.image.scalePercent(this.scale);
        this.image.setAbsolutePosition(this.left, document.getPageSize().top() - this.top);
        this.image.setTransparency(new int[] { 255, 255 });
        this.image.setRotation((float) Math.toRadians(this.angle));
        content.addImage(this.image);
    }
}
