package com.gomapgen;

import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;

public class JpegMetadata {

    public static void test(String[] args) {
        JpegMetadata meta = new JpegMetadata();
        int length = args.length;
        for (int i = 0; i < length; i++) meta.readAndDisplayMetadata(args[i]);
    }

    void readAndDisplayMetadata(String fileName) {
        try {
            File file = new File(fileName);
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(iis, true);
                IIOMetadata metadata = reader.getImageMetadata(0);
                String[] names = metadata.getMetadataFormatNames();
                int length = names.length;
                for (int i = 0; i < length; i++) {
                    System.out.println("Format name: " + names[i]);
                    displayMetadata(metadata.getAsTree(names[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void displayMetadata(Node root) {
        displayMetadata(root, 0);
    }

    void indent(int level) {
        for (int i = 0; i < level; i++) System.out.print("    ");
    }

    void displayMetadata(Node node, int level) {
        indent(level);
        System.out.print("<" + node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) {
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
            }
        }
        Node child = node.getFirstChild();
        if (child == null) {
            System.out.println("/>");
            return;
        }
        System.out.println(">");
        while (child != null) {
            displayMetadata(child, level + 1);
            child = child.getNextSibling();
        }
        indent(level);
        System.out.println("</" + node.getNodeName() + ">");
    }
}
