package net.disy.ogc.wps.v_1_0_0.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.disy.ogc.wps.v_1_0_0.annotation.Metadata;
import net.opengis.ows.v_1_1_0.MetadataType;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;

public class WpsProcessUtilities {

    public static <E, T extends Throwable> E getSingleRequired(E[] array, Predicate<? super E> predicate, T throwable) throws T {
        return getSingleRequired(Arrays.asList(array), predicate, throwable);
    }

    public static <E, T extends Throwable> E getSingleRequired(List<E> list, Predicate<? super E> predicate, T throwable) throws T {
        Collection<E> found = CollectionUtils.select(list, predicate);
        if (found.size() != 1) {
            throw throwable;
        }
        return found.iterator().next();
    }

    public static List<MetadataType> getMetadata(Metadata[] mds) {
        if (mds == null || mds.length == 0) {
            return Collections.emptyList();
        } else {
            final List<MetadataType> metadata = new ArrayList<MetadataType>(mds.length);
            for (Metadata md : mds) {
                final MetadataType item = new MetadataType();
                final String about = md.about();
                final String href = md.href();
                final String title = md.title();
                if (!StringUtils.isEmpty(about)) {
                    item.setAbout(about);
                }
                if (!StringUtils.isEmpty(href)) {
                    item.setHref(href);
                }
                if (!StringUtils.isEmpty(title)) {
                    item.setTitle(title);
                }
                metadata.add(item);
            }
            return metadata;
        }
    }
}
