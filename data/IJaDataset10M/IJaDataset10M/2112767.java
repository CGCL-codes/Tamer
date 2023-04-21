package dismapi;

import static dismapi.ViewableUtils.*;

/**
 *
 * @author pucgenie
 */
public class MountedInfo implements Viewable {

    public String dir;

    public String img;

    public int iOfImg;

    public String irw;

    public String sok;

    public MountedInfo(String dir, String img, int iOfImg, String irw, String sok) {
        this.dir = dir;
        this.img = img;
        this.iOfImg = iOfImg;
        this.irw = irw;
        this.sok = sok;
    }

    public String getString() {
        return String.format("Mount Dir : %s\nImage File : %s\nImage Index : %d\nMounted Read/Write : %s\nStatus : %s", dir, img, iOfImg, irw, sok);
    }

    @Override
    public String toString() {
        return img;
    }

    public static MountedInfo parse(String[] lines, int ix) {
        return new MountedInfo(split(lines[ix++], "Mount Dir : "), split(lines[ix++], "Image File : "), Integer.parseInt(split(lines[ix++], "Image Index : ")), split(lines[ix++], "Mounted Read/Write : "), split(lines[ix++], "Status : "));
    }
}
