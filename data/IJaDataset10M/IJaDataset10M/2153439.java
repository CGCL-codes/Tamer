package zaphod.toy.hitincreaser;

import zaphod.toy.hitincreaser.internal.Reader;

public class MainProgram {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int i;
        String location = "http://gall.dcinside.com/list.php?id=wow_new1&no=1391994&page=1";
        Reader reader = null;
        for (i = 0; i < 100; i++) {
            reader = new Reader(location, 10000, 30);
            reader.start();
        }
        try {
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
