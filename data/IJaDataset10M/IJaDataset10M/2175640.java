package datasoul.serviceitems.imagelist;

import datasoul.config.DisplayControlConfig;
import datasoul.serviceitems.ServiceItem;
import datasoul.serviceitems.ServiceItemRenderer;
import datasoul.serviceitems.ContentlessServiceItem;
import datasoul.util.ObjectManager;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author samuel
 */
public class ImageListServiceItem extends ServiceItem {

    public ImageListServiceItem() {
        super();
        this.template = DisplayControlConfig.getInstance().getDefaultTemplateImage();
    }

    public void addImage(File f) throws IOException {
        BufferedImage img = ImageIO.read(f);
        addImage(img);
    }

    public void addImage(BufferedImage img) {
        ImageListServiceRenderer r1 = new ImageListServiceRenderer();
        r1.setImage(img);
        slides.add(r1);
        this.fireTableChanged();
    }

    public void delImage(int index) {
        slides.remove(index);
        this.fireTableChanged();
    }

    public ImageListServiceItem getEditableCopy() {
        ImageListServiceItem ret = new ImageListServiceItem();
        for (ServiceItemRenderer r : slides) {
            ret.slides.add(r);
        }
        return ret;
    }

    public void assignImages(ImageListServiceItem item) {
        slides.clear();
        slides.addAll(item.slides);
        this.fireTableChanged();
    }

    public BufferedImage getImage(int index) {
        return ((ImageListServiceRenderer) slides.get(index)).getImage();
    }

    @Override
    public Node writeObject(ZipWriter zip) throws Exception {
        Node n = super.writeObject(zip);
        if (zip.getVersion() < 2) {
            return ContentlessServiceItem.writeNotSupportedObject(this, zip);
        }
        Document doc = n.getOwnerDocument();
        Node nodeList = doc.createElement("ImageList");
        for (ServiceItemRenderer r : slides) {
            ImageListServiceRenderer imgrend = (ImageListServiceRenderer) r;
            BufferedImage img = imgrend.getImage();
            Node nodeItem = doc.createElement("ImageItem");
            nodeItem.setTextContent("img-" + img.hashCode() + ".png");
            zip.appendImage(img, "img-" + img.hashCode() + ".png");
            nodeList.appendChild(doc.importNode(nodeItem, true));
        }
        n.appendChild(doc.importNode(nodeList, true));
        return n;
    }

    @Override
    public void readObject(Node nodeIn, ZipReader zip) {
        super.readObject(nodeIn, zip);
        NodeList nodeList = nodeIn.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).getNodeName().equals("ImageList")) {
                NodeList nodeImgList = nodeList.item(i).getChildNodes();
                for (int j = 0; j < nodeImgList.getLength(); j++) {
                    if (nodeImgList.item(j).getNodeType() == Node.ELEMENT_NODE && nodeImgList.item(j).getNodeName().equals("ImageItem")) {
                        try {
                            ImageListServiceRenderer r = new ImageListServiceRenderer();
                            String imgname = nodeImgList.item(j).getTextContent();
                            InputStream is = zip.getInputStream(imgname);
                            BufferedImage img = ImageIO.read(is);
                            r.setImage(img);
                            slides.add(r);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void setImageList(String s) {
    }

    @Override
    public String getDefaultMonitorTemplate() {
        return DisplayControlConfig.getInstance().getMonitorTemplateImage();
    }

    @Override
    public Color getBackgroundColor() {
        return Color.decode("0xffdddd");
    }

    @Override
    public void edit() {
        ImageListEditorForm ilsi = new ImageListEditorForm(this);
        ilsi.setLocationRelativeTo(ObjectManager.getInstance().getDatasoulMainForm());
        ilsi.setVisible(true);
    }

    private static final Icon icon = new ImageIcon(ImageListServiceItem.class.getResource("/datasoul/icons/v2/image-x-generic.png"));

    @Override
    public Icon getIcon() {
        return icon;
    }
}
