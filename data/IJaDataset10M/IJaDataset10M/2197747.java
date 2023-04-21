package edu.zao.fire;

import java.io.File;
import java.io.IOException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import edu.zao.fire.editors.metadata.MetadataTag;
import edu.zao.fire.editors.metadata.MetadataTagCache;
import edu.zao.fire.editors.metadata.MetadataTagList;

public class MetadataRule implements RenamerRule {

    private final MetadataTagList tagList = new MetadataTagList();

    @Override
    public String getNewName(File file) throws IOException {
        String fileName = file.getName();
        String newName = "";
        String extension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = fileName.substring(dotIndex);
        }
        try {
            MetadataTagCache tagCache = MetadataTagCache.getInstance();
            Tag songTag = tagCache.getTagFromFile(file);
            for (MetadataTag tag : tagList.getTags()) {
                FieldKey key = tag.getFieldKey();
                String value;
                if (key == null) {
                    value = tag.getDefaultText();
                } else {
                    value = songTag.getFirst(key);
                    if (value.isEmpty()) {
                        value = tag.getDefaultText();
                    }
                }
                newName += value;
            }
        } catch (Exception e) {
            return file.getName();
        }
        newName = newName + extension;
        return newName;
    }

    @Override
    public void setup() {
    }

    @Override
    public void tearDown() {
    }

    public MetadataTagList getTagList() {
        return tagList;
    }
}
