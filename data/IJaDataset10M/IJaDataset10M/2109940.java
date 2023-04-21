package edu.richmond.is.webservices.util;

import edu.richmond.is.webservices.files2exist.Folder2ExistSettings;
import java.io.*;
import java.util.*;

public class FilesInFolderList {

    private File fileFolder;

    private ArrayList<File> fileFolderList = new ArrayList<File>();

    private ArrayList<File> folderList = new ArrayList<File>();

    private ArrayList<File> fileArrayList = new ArrayList<File>();

    private boolean recurseFolderFlag = true;

    public FilesInFolderList(String name, boolean recurse) throws IOException {
        this(new File(name), recurse);
    }

    public FilesInFolderList(File directory, boolean recurse) throws IOException {
        this.recurseFolderFlag = recurse;
        if (directory.isDirectory()) {
            this.fileFolder = new File(directory.getCanonicalPath());
        } else {
            throw new IOException(directory.toString() + " is not a directory");
        }
        getFiles(this.fileFolder);
    }

    private void getFiles(File folder) {
        File[] files = folder.listFiles();
        for (int j = 0; j < files.length; j++) {
            this.fileFolderList.add(files[j]);
            if (files[j].isDirectory()) {
                this.folderList.add(files[j]);
                if (recurseFolderFlag) {
                    getFiles(files[j]);
                }
            } else {
                this.fileArrayList.add(files[j]);
            }
        }
    }

    public long getFileListSize() {
        return this.fileArrayList.size();
    }

    public ArrayList<File> getfileArrayList() {
        return this.fileArrayList;
    }

    public static void main(String[] args) throws IOException {
        String testFolder = "/Volumes/HD2/appTests/f2e/data/pickup";
    }

    public static String getExtraPath(String rootPath, File file) throws IOException {
        String fullPath = file.getCanonicalPath();
        String tempString = fullPath.replaceFirst("^" + rootPath, "");
        String fileNameRegEx = "/" + file.getName() + "$";
        String temp2String = tempString.replaceAll(fileNameRegEx, "");
        return temp2String;
    }
}
