package net.sf.amemailchecker.gui.messageviewer.messagedetails.messagebodyview;

import net.sf.amemailchecker.mail.model.Attachment;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ImageView;
import java.awt.*;

class MessageBodyImageView extends ImageView {

    private MessageBodyPaneModel model;

    public MessageBodyImageView(Element element, MessageBodyPaneModel model) {
        super(element);
        this.model = model;
    }

    @Override
    public Image getImage() {
        String imageName = getImageName();
        Image image = model.getMessageDocuments().getImage(imageName);
        if (image != null) {
            return image;
        }
        for (Attachment attachment : model.getMessage().getInline()) {
            if (attachment.getName().equals(imageName)) {
                image = Toolkit.getDefaultToolkit().createImage(attachment.getData());
                model.getMessageDocuments().addImage(imageName, image);
                return image;
            }
        }
        if (isCompleteURL()) {
            image = super.getImage();
            if (image != null) {
                model.getMessageDocuments().addImage(imageName, image);
            }
            return image;
        }
        return null;
    }

    private boolean isCompleteURL() {
        Object src = getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
        if (src == null) return false;
        String srcStr = ((String) src).toLowerCase();
        return srcStr.startsWith("file") || srcStr.startsWith("http");
    }

    private String getImageName() {
        Object src = getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
        if (src == null) return "";
        String[] pathTokens = ((String) src).split("[/]");
        return pathTokens[pathTokens.length - 1];
    }
}
