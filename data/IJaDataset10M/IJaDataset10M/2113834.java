package net.sf.planofattack.util;

import java.awt.Image;
import javax.swing.Icon;

public interface ImageRegistry {

    public abstract Icon getIcon(String name);

    public abstract Image getImage(String name);
}
