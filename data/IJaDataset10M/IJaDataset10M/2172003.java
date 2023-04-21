package info.bliki.wiki.model;

import info.bliki.wiki.filter.WikipediaScanner;
import java.util.List;

/**
 * Represents an [[Image:....]] wiki link with all the possible attributes.
 * 
 * Copied from Patch #1488331 sf.net user: o_rossmueller; modified by axelclk
 * http://sourceforge.net/tracker/index.php?func=detail&aid=1488331&group_id=128886&atid=713150
 * 
 */
public class ImageFormat {

    public static ImageFormat getImageFormat(String rawImageLink, String imageNamespace) {
        ImageFormat img = new ImageFormat();
        List<String> list = WikipediaScanner.splitByPipe(rawImageLink, null);
        if (list.size() > 0) {
            String token = list.get(0);
            img.setFilename("");
            if (token.length() > imageNamespace.length() && token.charAt(imageNamespace.length()) == ':') {
                if (imageNamespace.equalsIgnoreCase(token.substring(0, imageNamespace.length()))) {
                    img.setFilename(token.substring(imageNamespace.length() + 1));
                    img.setNamespace(imageNamespace);
                }
            }
            String caption;
            for (int j = 1; j < list.size(); j++) {
                caption = list.get(j).trim();
                if (caption.length() > 0) {
                    token = caption.toLowerCase();
                    if (token.equals("frame") || token.equals("thumb") || token.equals("thumbnail")) {
                        img.setType(token);
                        continue;
                    }
                    if (token.equals("right") || token.equals("left") || token.equals("center") || token.equals("none")) {
                        img.setLocation(token);
                        continue;
                    }
                    if (token.endsWith("px")) {
                        img.setSize(token);
                        continue;
                    }
                    img.setCaption(caption);
                }
            }
        }
        return img;
    }

    private String fFilename;

    private String fType;

    private String fLocation = "none";

    private String fSizeStr = null;

    private int fSize = -1;

    private String fCaption;

    private String fAlt;

    private String fNamespace;

    public String getAlt() {
        return fAlt;
    }

    public String getCaption() {
        return fCaption;
    }

    public String getFilename() {
        return fFilename;
    }

    public String getLocation() {
        return fLocation;
    }

    public String getNamespace() {
        return fNamespace;
    }

    /**
	 * Get the size of the image in pixel (example: "600px")
	 * 
	 * @param size
	 */
    public int getSize() {
        return fSize;
    }

    /**
	 * Get the size of the image as a string
	 * 
	 * @param size
	 */
    public String getSizeStr() {
        return fSizeStr;
    }

    public String getType() {
        return fType;
    }

    public void setAlt(String alt) {
        fAlt = alt;
    }

    public void setCaption(String caption) {
        this.fCaption = caption;
    }

    public void setFilename(String filename) {
        this.fFilename = filename;
    }

    public void setLocation(String location) {
        this.fLocation = location.toLowerCase();
    }

    public void setNamespace(String namespace) {
        this.fNamespace = namespace;
    }

    /**
	 * Set the size of the image in pixel. If the given string ends with "px"
	 * additionally calculate the integer value of the size (example: "600px") If
	 * the size is negative ignore it.
	 * 
	 * @param size
	 */
    public void setSize(String size) {
        fSizeStr = size.toLowerCase();
        if (fSizeStr.endsWith("px")) {
            try {
                this.fSize = Integer.parseInt(fSizeStr.substring(0, fSizeStr.length() - 2));
                if (fSize < 0) {
                    this.fSize = -1;
                    this.fSizeStr = null;
                }
            } catch (NumberFormatException e) {
                this.fSize = -1;
                this.fSizeStr = null;
            }
        }
    }

    public void setType(String type) {
        this.fType = type.toLowerCase();
    }
}
