package com.tomczarniecki.s3.gui;

import com.tomczarniecki.s3.Lists;
import com.tomczarniecki.s3.Pair;
import org.apache.commons.io.FileUtils;
import java.text.DecimalFormat;
import java.util.List;

class FileSize {

    private final List<Pair<String, Long>> sizes;

    private final DecimalFormat format;

    public FileSize() {
        sizes = Lists.create();
        sizes.add(Pair.create("GB", FileUtils.ONE_GB));
        sizes.add(Pair.create("MB", FileUtils.ONE_MB));
        sizes.add(Pair.create("KB", FileUtils.ONE_KB));
        format = new DecimalFormat(",##0.0");
    }

    public String format(long size) {
        for (Pair<String, Long> entry : sizes) {
            if (size / entry.getValue() > 0) {
                double value = ((double) size) / ((double) entry.getValue());
                return format.format(value) + " " + entry.getKey();
            }
        }
        return size + " bytes";
    }
}
