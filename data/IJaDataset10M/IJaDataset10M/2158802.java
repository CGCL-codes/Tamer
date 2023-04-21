package com.rolfje.speedforge.core;

import com.gforgegroup.DocmanFile;

public class GFFile {

    private DocmanFile sourceFile;

    public GFFile(DocmanFile docmanFile) {
        sourceFile = docmanFile;
    }

    public String getName() {
        return "file id " + sourceFile.getDocman_file_id();
    }
}
