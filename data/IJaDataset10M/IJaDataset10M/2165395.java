package com.certesystems.swingforms.links;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.cayenne.exp.Expression;
import org.apache.commons.jxpath.JXPathContext;
import com.certesystems.swingforms.Action;
import com.certesystems.swingforms.components.image.ActionFileLoad;
import com.certesystems.swingforms.components.image.ActionURLImageLoad;
import com.certesystems.swingforms.components.image.ImagePane;
import com.certesystems.swingforms.fields.FieldImage;
import com.certesystems.swingforms.forms.Form;
import com.certesystems.swingforms.tools.Messages;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class LinkImage extends Link {

    private int xSize = 0;

    private int ySize = 0;

    private Component videoScreen;

    private Form form;

    public LinkImage() {
    }

    public LinkImage(Form form, ImagePane container, String mapping) {
        setContainer(container);
        setMapping(mapping);
        setKey(false);
        setVirtual(mapping.indexOf('/') > 0);
        this.form = form;
    }

    public FieldImage getField() {
        return (FieldImage) super.getField();
    }

    public void comboToField(JXPathContext context) {
        try {
            BufferedImage bim = getContainer().getImage();
            if (bim != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bim, "jpg", bos);
                byte array[] = bos.toByteArray();
                context.setValue(getMapping(), array);
            } else {
                context.setValue(getMapping(), null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fieldToCombo(JXPathContext context, boolean requery) {
        try {
            byte bytear[] = null;
            try {
                bytear = (byte[]) context.getValue(getMapping());
            } catch (Exception e) {
            }
            if (bytear != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytear, 0, bytear.length);
                JPEGImageDecoder dec = JPEGCodec.createJPEGDecoder(bis);
                BufferedImage bim = dec.decodeAsBufferedImage();
                getContainer().setImage(bim);
            } else {
                getContainer().setImage(null);
            }
        } catch (Exception ex) {
            getContainer().setImage(null);
            ex.printStackTrace();
        }
    }

    public void setEditable(boolean value) throws Exception {
        getContainer().setEditable(value);
    }

    public ImagePane getContainer() {
        return (ImagePane) super.getContainer();
    }

    public List<Action<?>> getModifyFieldActions() {
        List<Action<?>> list = new ArrayList<Action<?>>();
        list.add(new ActionFileLoad(getContainer(), Messages.getString("LinkImage.fileCapture")));
        list.add(new ActionURLImageLoad(this, Messages.getString("LinkImage.webCapture")));
        return list;
    }

    public void verifyCombo() throws Exception {
    }

    public Expression createExpression() {
        return null;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int size) {
        xSize = size;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int size) {
        ySize = size;
    }
}
