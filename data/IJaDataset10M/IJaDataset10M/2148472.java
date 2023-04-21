package com.jot.test.tryouts.p2json;

import com.jot.user.generators.AllFiles;
import com.jot.user.generators.AllFiles.ClassDescription;

@Deprecated
public class MyClassDescription extends AllFiles.ClassDescription {

    boolean generated = false;

    String alias;

    public MyClassDescription(ClassDescription cd) {
        clazz = cd.clazz;
        basePath = cd.basePath;
        relativePath = cd.relativePath;
        sourcePath = cd.sourcePath;
    }
}
