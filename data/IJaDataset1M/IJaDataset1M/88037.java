package start;

import transfer.TransferPchome;
import album.PchomeAlbumDownload;

public class Start {

    public static void main(String[] args) {
        new TransferPchome("rancechang", "E:/");
    }

    public Start(String id, String path) {
        System.out.println("--------------------------");
        System.out.println("--start to get albumLink--");
        System.out.println("--------------------------");
        new PchomeAlbumDownload(id, path);
        System.out.println("----------------------------------");
        System.out.println("--start transferPchome albumLink--");
        System.out.println("----------------------------------");
        new TransferPchome(id, path);
        System.out.println("----------------------------------");
        System.out.println("-------------Finished-------------");
        System.out.println("----------------------------------");
    }
}
