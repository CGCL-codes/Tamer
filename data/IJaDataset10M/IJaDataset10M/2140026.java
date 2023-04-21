package edu.whitman.halfway.jigs.cmdline;

import edu.whitman.halfway.jigs.*;
import edu.whitman.halfway.util.*;
import java.util.*;
import org.apache.log4j.Logger;

public class CategoryList extends AbstractCmdLine {

    protected static Logger log = Logger.getLogger(CategoryList.class);

    static int[] opts = { RECURSE_ALBUM, LOG4J_FILE, RECURSIVE, HELP };

    public CategoryList() {
        super(opts);
    }

    public String getAdditionalParseArgs() {
        return "A";
    }

    public static void main(String[] args) {
        (new CategoryList()).mainDriver(args);
    }

    public void doMain() {
        boolean doAlbumCats = hasOption('A');
        Album crnt = getAlbum();
        log.debug("Processing " + crnt);
        if (!doAlbumCats) {
            final CountMap map = new CountMap();
            AlbumUtil.mediaItemWalk(getAlbum(), new MediaItemFunction() {

                public void process(MediaItem item) {
                    if (item instanceof Picture) {
                        Picture pic = (Picture) item;
                        AlbumObjectDescriptionInfo aoi = pic.getDescriptionInfo();
                        CategorySet cs = (CategorySet) aoi.getData(AlbumObjectDescriptionInfo.JIGS_CATEGORY);
                        Iterator iter = cs.getCategorySet().iterator();
                        while (iter.hasNext()) {
                            map.increment(iter.next());
                        }
                    }
                }
            }, recurse);
            ArrayList keys = new ArrayList(map.keySet());
            Collections.sort(keys);
            int total = 0;
            for (int i = 0; i < keys.size(); i++) {
                int count = map.count(keys.get(i));
                System.out.println(StringUtil.pad(((Category) keys.get(i)).getName(), 20, " ") + "\t" + count);
                total += count;
            }
            System.out.println("Total " + total + " images in categories");
        } else {
            CategorySet cats = new CategorySet();
            cats.addCategorySet(CategorySet.findAlbumCategorySet(crnt, recurse));
            Iterator iter = cats.getCategorySet().iterator();
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
        }
    }

    protected void specificUsage() {
        System.out.println("Usage: CategoryList [-r] [-l logConf] [directory_list]" + StringUtil.newline + "\t -A list album categories instead of media item (picture) categories" + StringUtil.newline);
    }
}
