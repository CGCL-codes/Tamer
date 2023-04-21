package polr.client.ui.base.skin.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.newdawn.slick.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.*;
import org.newdawn.slick.opengl.Texture;
import polr.client.ui.base.skin.ComponentAppearance;
import polr.client.ui.base.skin.FontUIResource;

/**
 * 
 * @author davedes
 */
public class XMLSkin {

    public static final String ALIGN_TOP = "top";

    public static final String ALIGN_CENTER = "center";

    public static final String ALIGN_BOTTOM = "bottom";

    public static final String ALIGN_LEFT = "left";

    public static final String ALIGN_RIGHT = "right";

    public static final String ALIGN_NONE = "none";

    private Sheet defaultSheet = null;

    private Font defaultFont = null;

    private HashMap sheets = null;

    private HashMap resources = null;

    private HashMap appearances = null;

    /** Creates a new instance of XMLSkin */
    public XMLSkin(InputStream in) throws IOException, SAXException, SlickException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(in);
            Element element = document.getDocumentElement();
            read(element);
        } catch (ParserConfigurationException e) {
            throw new IOException("error configuring parser");
        }
    }

    protected void read(Element e) throws IOException, SlickException {
        if (!"skin".equals(e.getNodeName())) throw new IOException("skin definition must have a root of <skin>");
        ArrayList appearanceElements = new ArrayList();
        NodeList nl;
        nl = e.getElementsByTagName("textures");
        if (nl.getLength() == 0) throw new IOException("no <textures> declared");
        loadTextures((Element) nl.item(0));
        nl = e.getElementsByTagName("resources");
        if (nl.getLength() != 0) {
            loadResources((Element) nl.item(0));
        }
    }

    private Font loadFont(Element e) throws IOException, SlickException {
        String id = e.getAttribute("id");
        if (resources.containsKey(id)) throw new IOException("two fonts cannot have the same id: " + id);
        String type = e.getAttribute("type");
        Font ret = null;
        if ("AngelCodeFont".equals(type)) {
            String fntRef = e.getAttribute("fnt");
            String imgRef = e.getAttribute("img");
            ret = new FontUIResource.AngelCodeFont(fntRef, imgRef);
        } else if ("TrueTypeFont".equals(type)) {
            throw new IOException("true type fonts not supported yet");
        } else if ("SpriteSheetFont".equals(type)) {
            throw new IOException("sprite sheet fonts not supported yet");
        }
        return ret;
    }

    public Object getResource(String id) {
        return resources.get(id);
    }

    public Font getFont(String id) {
        Object o = resources.get(id);
        return o instanceof Font ? (Font) o : null;
    }

    private void loadResources(Element e) throws IOException, SlickException {
        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                if (resources == null) resources = new HashMap();
                Element child = (Element) nl.item(i);
                String id = child.getAttribute("id");
                String name = child.getNodeName();
                if ("font".equals(name)) {
                    Font f = loadFont(child);
                    resources.put(id, f);
                    if (defaultFont == null) defaultFont = f;
                } else if ("object".equals(name)) {
                    resources.put(id, child.getAttribute("value"));
                }
            }
        }
        nl = e.getElementsByTagName("defaultFont");
        if (nl.getLength() > 0) {
            String id = ((Element) nl.item(0)).getAttribute("id");
            Font f = getFont(id);
            if (f == null) {
                throw new IOException("cannot use default font '" + id + "' because it does not exist");
            }
        }
    }

    private void loadTextures(Element e) throws IOException, SlickException {
        sheets = new HashMap();
        NodeList nl;
        nl = e.getElementsByTagName("sheet");
        if (nl.getLength() == 0) throw new IOException("no <sheet> elements declared");
        for (int i = 0; i < nl.getLength(); i++) {
            Element child = (Element) nl.item(i);
            String id = child.getAttribute("id");
            if (sheets.containsKey(id)) throw new IOException("two sheets cannot have the same id: " + id);
            String ref = e.getAttribute("ref");
            Sheet s = new Sheet(ref);
            sheets.put(id, s);
            if (defaultSheet == null) defaultSheet = s;
        }
        nl = e.getElementsByTagName("defaultSheet");
        if (nl.getLength() > 0) {
            String id = ((Element) nl.item(0)).getAttribute("id");
            Sheet sh = (Sheet) sheets.get(id);
            if (sh == null) {
                throw new IOException("cannot use default sheet '" + id + "' because it does not exist");
            }
            defaultSheet = sh;
        }
    }

    protected void loadAppearance(Element e) throws IOException {
    }

    private Image parseRect(Image target, String rect) {
        StringTokenizer tk = new StringTokenizer(rect);
        int x = Integer.parseInt(tk.nextToken());
        int y = Integer.parseInt(tk.nextToken());
        int width = Integer.parseInt(tk.nextToken());
        int height = Integer.parseInt(tk.nextToken());
        return target.getSubImage(x, y, width, height);
    }

    protected class XMLAppearance {

        protected ComponentAppearance create() {
            return null;
        }

        public Parts getParts() {
            return null;
        }
    }

    protected class Parts {

        Image topleft, topright, botleft, botright, top, bot, left, right, center, area;

        public void draw(Graphics g, int x, int y, int width, int height, int space) {
            int cw = topleft.getWidth() + space;
            int ch = topleft.getHeight() + space;
            int topheight = top.getHeight();
            int leftwidth = left.getWidth();
            topleft.draw(x, y);
            botleft.draw(x, y + height - ch + space);
            topright.draw(x + width - cw + space, y);
            botright.draw(x + width - cw + space, y + height - ch + space);
            top.draw(x + cw, y, width - (cw * 2), topheight);
            bot.draw(x + cw, y + height - ch + space, width - (cw * 2), topheight);
            left.draw(x, y + ch, leftwidth, height - (ch * 2));
            right.draw(x + width - cw + space, y + ch, leftwidth, height - (ch * 2));
            center.draw(x + cw, y + cw, width - (cw * 2), height - (ch * 2));
        }
    }

    protected class Sheet {

        Image image;

        public Sheet(String ref) throws SlickException {
            this.image = new Image(ref);
        }

        public Image getImage() {
            return image;
        }

        public Image getSubImage(int x, int y, int width, int height) {
            return image.getSubImage(x, y, width, height);
        }
    }

    protected class Mask {

        private int _r, _g, _b;

        private Image image;

        private byte[] data = null;

        private int width;

        private int offsetMult;

        public Mask(Image image, Color color) {
            this.image = image;
            this._r = color.getRedByte();
            this._g = color.getGreenByte();
            this._b = color.getGreenByte();
        }

        /**
		 * Translate an unsigned int into a signed integer
		 * 
		 * @param b
		 *            The byte to convert
		 * @return The integer value represented by the byte
		 */
        private int translate(byte b) {
            if (b < 0) return 256 + b;
            return b;
        }

        private boolean isColor(int x, int y) {
            Texture texture = image.getTexture();
            if (data == null) {
                data = texture.getTextureData();
                width = texture.getTextureWidth();
                offsetMult = texture.hasAlpha() ? 4 : 3;
            }
            int offset = x + (y * width);
            offset *= offsetMult;
            int r = translate(data[offset]);
            int g = translate(data[offset + 1]);
            int b = translate(data[offset + 2]);
            return r == _r && g == _g && b == _b;
        }
    }
}
