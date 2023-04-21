package rez.utils;

/**
 *
 * @author  chris thorne
 */
public class FilePath {

    private String srcdirprename;

    private String dirprename;

    private String fileName;

    private String extension;

    private String containerDir;

    static final String fileSep = System.getProperty("file.separator");

    /** Creates a new instance of FilePath */
    public FilePath(String path) {
        int dotindex = path.lastIndexOf(".");
        int slashindex = 0;
        boolean simpleFileName = false;
        if (path.indexOf(fileSep) != -1) {
            slashindex = path.lastIndexOf(fileSep);
        } else if (path.lastIndexOf("/") != -1) {
            slashindex = path.lastIndexOf("/");
        } else {
            simpleFileName = true;
            slashindex = 0;
        }
        dotindex = dotindex < 0 ? 0 : dotindex;
        slashindex = slashindex < 0 ? 0 : slashindex;
        extension = path.substring(dotindex, path.length());
        fileName = slashindex < 1 ? path.substring(slashindex, dotindex) : path.substring(slashindex + 1, dotindex);
        dirprename = path.substring(0, slashindex);
        String sourceDir = "";
        int srcslashindex = 0;
        srcdirprename = "";
        containerDir = "";
        if (!simpleFileName) {
            if (dirprename.indexOf(fileSep) != -1) {
                srcslashindex = dirprename.lastIndexOf(fileSep);
            } else srcslashindex = dirprename.lastIndexOf("/");
            srcslashindex = srcslashindex < 0 ? 0 : srcslashindex;
            srcdirprename = sourceDir + dirprename.substring(0, srcslashindex);
            if (dirprename.indexOf(fileSep) != -1) {
                slashindex = dirprename.lastIndexOf(fileSep);
            } else slashindex = dirprename.lastIndexOf("/");
            slashindex = slashindex < 0 ? 0 : slashindex;
        }
    }

    public String getSrcDirPrename() {
        return dirprename;
    }

    public String getDirPrename() {
        return dirprename;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public String getContainerDir() {
        return containerDir;
    }
}
