package com.dukesoftware.utils.filemanage.concrete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.dukesoftware.utils.filemanage.template.DirectoryDigger;
import com.dukesoftware.utils.filemanage.template.FileOperatorTaskAdaptor;
import com.dukesoftware.utils.io.SystemPrintOut;

public class FileSearcher extends FileOperatorTaskAdaptor {

    public static void main(String[] args) throws IOException {
        FileSearcher searcher = new FileSearcher("INSTALL.INI");
        DirectoryDigger.createDigger(searcher).execute(new File("c:/ATI"));
        List<File> list = searcher.list;
        SystemPrintOut.print(list);
    }

    private final String fname;

    private final ArrayList<File> list = new ArrayList<File>();

    public FileSearcher(String string) {
        this.fname = string;
    }

    @Override
    public boolean accept(File file) {
        return file.getName().contains(fname);
    }

    @Override
    public void process(File file) {
        list.add(file);
    }
}
