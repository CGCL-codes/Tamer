package swingextras.icons;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * A resizable icon
 * @author Joao Leal
 */
public class ResizableIcon {

    private static final Logger logger = Logger.getLogger(ResizableIcon.class.getName());

    private String base;

    private String head;

    private List<Integer> sizes;

    private List<ImageIcon> images;

    /**
     * Creates a new resizable icon
     * @param base the base path name
     * @param head the head path name
     */
    public ResizableIcon(String base, String head) {
        if (base == null) {
            this.base = "";
        } else {
            this.base = base;
        }
        if (head == null || head.isEmpty()) {
            throw new IllegalArgumentException("the icon path head cannot be null nor empty");
        }
        this.head = head;
        ImageScaleFolder[] scaleFolders = IconManager.getScaleFolders();
        sizes = new ArrayList<Integer>(scaleFolders.length);
        images = new ArrayList<ImageIcon>(scaleFolders.length);
        for (int i = 0; i < scaleFolders.length; i++) {
            URL url = IconManager.getIconURL(base + scaleFolders[i].getFolder() + head);
            if (url != null) {
                sizes.add(scaleFolders[i].getSize());
                images.add(null);
            }
        }
        if (sizes.size() == 0) {
            logger.info("No resources found for the resizable icon: " + IconManager.getIconPath() + base + "?" + head);
        }
    }

    /**
     * Returns an icon with the requested size. If no icon is found with the
     * requested size then one will be created by resizing one of the existing
     * @param size the desired size for the icon
     * @return the icon
     */
    public ImageIcon getIcon(int size) {
        if (sizes.size() == 0) {
            return null;
        }
        int i = 0;
        for (i = 0; i < sizes.size() && sizes.get(i) <= size; i++) {
            if (sizes.get(i) == size) {
                return getImageAt(i);
            }
        }
        if (i >= sizes.size()) {
            i = sizes.size() - 1;
        }
        ImageIcon scaled = new ImageIcon();
        scaled.setImage(getImageAt(i).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
        return scaled;
    }

    private ImageIcon getImageAt(int i) {
        if (images.get(i) == null) {
            ImageScaleFolder[] scaleFolders = IconManager.getScaleFolders();
            URL url = IconManager.getIconURL(base + scaleFolders[i].getFolder() + head);
            if (url != null) {
                images.set(i, new ImageIcon(url));
            }
        }
        return images.get(i);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.base != null ? this.base.hashCode() : 0);
        hash = 59 * hash + (this.head != null ? this.head.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResizableIcon other = (ResizableIcon) obj;
        if (this.base != other.base && (this.base == null || !this.base.equals(other.base))) {
            return false;
        }
        if (this.head != other.head && (this.head == null || !this.head.equals(other.head))) {
            return false;
        }
        return true;
    }
}
