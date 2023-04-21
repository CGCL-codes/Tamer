package blizzard.mpq;

import java.io.File;

public class MPQDebugger {

    public static void main(String[] args) {
        try {
            MPQArchive mpqInterface = new MPQArchive(new File("C:\\Spel\\World of Warcraft\\Data\\interface.MPQ"));
            String[] filelist = mpqInterface.getFileList();
            MPQFileInputStream fileStream = mpqInterface.openFile("Interface\\FrameXML\\FrameXML.toc");
            byte[] buffer = new byte[(int) fileStream.getFileSize()];
            fileStream.read(buffer);
            fileStream.close();
            String fileContents = new String(buffer);
            System.out.println(fileContents);
            mpqInterface.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
