package net.rptools.lib.swing;

import java.awt.Image;
import java.awt.Paint;
import java.awt.datatransfer.Transferable;

public interface ImagePanelModel {

    public int getImageCount();

    public Transferable getTransferable(int index);

    public Object getID(int index);

    public Image getImage(Object ID);

    public Image getImage(int index);

    public String getCaption(int index);

    public Paint getBackground(int index);

    public Image[] getDecorations(int index);
}
