package edu.whitman.halfway.jigs.cmdline;

import edu.whitman.halfway.jigs.*;
import org.apache.log4j.Logger;

public class CategoryAdd extends AbstractCmdLine {

    protected static Logger log = Logger.getLogger(CategoryAdd.class);

    static int[] opts = { RECURSE_ALBUM, LIST_ALBUM, LOG4J_FILE, HELP, RECURSIVE };

    public CategoryAdd() {
        super(opts);
    }

    public String getAdditionalParseArgs() {
        return "";
    }

    protected int getDesiredNumArgs() {
        return 1;
    }

    public void doMain() {
        if (argsLeft.length != 1) {
            System.err.println("Specify exactly one category on the command line.");
            usage();
        }
        final Category category = new Category(argsLeft[0]);
        AlbumUtil.mediaItemWalk(getAlbum(), new MediaItemFunction() {

            public void process(MediaItem item) {
                if (item instanceof Picture) {
                    Picture pic = (Picture) item;
                    AlbumObjectDescriptionInfo aoi = pic.getDescriptionInfo();
                    CategorySet cs = (CategorySet) aoi.getData(AlbumObjectDescriptionInfo.JIGS_CATEGORY);
                    cs.addCategory(category);
                    aoi.saveFile();
                }
            }
        }, recurse);
    }

    public static void main(String[] args) {
        (new CategoryAdd()).mainDriver(args);
    }

    protected void specificUsage() {
        System.out.println(" usage:  CategoryAdd [options] category");
        System.out.println("This will place all pictures in the album specified into the given category.");
    }
}
