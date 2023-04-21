package org.jcr_blog.frontend.wtc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.jcr_blog.frontend.wtc.types.Archive;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
public class CurrentBlogArchivesService {

    /**
     *
     * @return all archives in the current blog
     */
    @Produces
    @Named
    @RequestScoped
    public List<Archive> getCurrentBlogArchives() {
        Archive[] archives = new Archive[10];
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < archives.length; i++) {
            calendar.add(Calendar.MONTH, -1);
            archives[i] = new Archive(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        }
        return Arrays.asList(archives);
    }
}
