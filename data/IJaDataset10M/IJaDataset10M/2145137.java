package examples.file.tree;

public class ContentNode extends FileNode {

    int fSize;

    public ContentNode(String name, String perms) {
        super(name, perms);
    }

    public void setSize(int size) {
        fSize = size;
    }

    public int getSize() {
        return fSize;
    }

    protected boolean isEndNode() {
        return true;
    }

    protected String getType() {
        int dotIdx = fName.lastIndexOf('.');
        if (dotIdx == -1) {
            return "Unkown type";
        }
        String ends = fName.substring(dotIdx + 1);
        if (ends.equals("html")) {
            return "Web page";
        } else if (ends.equals("htm")) {
            return "Web page";
        } else if (ends.equals("gif")) {
            return "GIF image";
        } else if (ends.equals("jpeg")) {
            return "JPEG image";
        } else if (ends.equals("jpg")) {
            return "JPEG image";
        } else if (ends.equals("txt")) {
            return "Text file";
        }
        return ends + " file";
    }
}
