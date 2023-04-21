package de.tu.depth.fragments;

import java.util.UUID;

public class Image extends EmbeddableFragment {

    public static final String IMAGE_TYPE_STRING = "image";

    public enum SubType {

        JPEG, GIF, PNG, BMP, TIFF
    }

    public Image(HierarchyFragment parent, String name, SubType imageType) {
        super(parent, name);
        setSubType(imageType);
    }

    public Image(HierarchyFragment parent, String name, SubType imageType, UUID uuid) {
        super(parent, name, uuid);
        setSubType(imageType);
    }

    public String getTypeString() {
        return IMAGE_TYPE_STRING;
    }
}
