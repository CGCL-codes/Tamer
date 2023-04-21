package fitService.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import org.eclipse.jface.resource.ImageDescriptor;

public class Operation {

    public static ImageDescriptor loadImages(String imgName) {
        Vector<ImageDescriptor> images = new Vector<ImageDescriptor>();
        URL url = null;
        EditorConstants ec = new EditorConstants();
        try {
            url = new URL(EditorConstants.PATH_IMAGE + ec.getImages().get(imgName));
        } catch (MalformedURLException e) {
        }
        ImageDescriptor image = ImageDescriptor.createFromFile(null, EditorConstants.PATH_IMAGE + ec.getImages().get(imgName));
        return image;
    }

    public void writeToFile(String FileName, StringBuffer content) {
        try {
            FileWriter writer = new FileWriter(FileName, false);
            writer.write(content.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
