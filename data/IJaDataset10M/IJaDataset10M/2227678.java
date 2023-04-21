package kanakata.testcase;

import kanakata.domain.Hiragana;
import kanakata.domain.Kana;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Utilities {

    protected final Logger log = Logger.getLogger(getClass().getName());

    public void renameImages(ImageNaming imageNaming) throws IOException {
        File root = new File("source/product/resource/media/images/hiragana");
        for (Hiragana hiragana : Hiragana.values()) {
            Kana kana = hiragana.getKana();
            String key = kana.getSyllable().key();
            File large = new File(root, key + ".png");
            if (large.exists()) {
                log.info("Renaming: " + large.getCanonicalPath());
                boolean renamed = large.renameTo(imageNaming.newNameFor(large, kana));
                if (renamed) {
                    log.info("Renamed: " + large.getCanonicalPath());
                } else {
                    log.warning("Failed:" + large.getCanonicalPath());
                }
            }
            File stroke = new File(root, key + "_stroke.png");
            if (stroke.exists()) {
                log.info("Renaming: " + stroke.getCanonicalPath());
                boolean renamed = stroke.renameTo(imageNaming.newNameFor(stroke, kana));
                if (renamed) {
                    log.info("Renamed: " + stroke.getCanonicalPath());
                } else {
                    log.warning("Failed:" + stroke.getCanonicalPath());
                }
            }
            File medium = new File(root, key + "_tile.png");
            if (medium.exists()) {
                log.info("Renaming: " + medium.getCanonicalPath());
                boolean renamed = medium.renameTo(imageNaming.newNameFor(medium, kana));
                if (renamed) {
                    log.info("Renamed: " + medium.getCanonicalPath());
                } else {
                    log.warning("Failed:" + medium.getCanonicalPath());
                }
            }
        }
    }

    public static interface ImageNaming {

        File newNameFor(File existing, Kana kana);
    }
}
