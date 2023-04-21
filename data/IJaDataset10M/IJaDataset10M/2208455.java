package net.community.chest.io.filter;

import java.util.Collection;
import net.community.chest.util.set.SetsUtils;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Feb 27, 2011 2:11:25 PM
 */
public class FolderOnlyFilesFilter extends AbstractRootFolderFilesFilter {

    public FolderOnlyFilesFilter(boolean caseSensitive, Collection<String> excludedNames) {
        super(caseSensitive, excludedNames);
    }

    public FolderOnlyFilesFilter(boolean caseSensitive, String... excludedNames) {
        this(caseSensitive, ((null == excludedNames) || (excludedNames.length <= 0)) ? null : SetsUtils.comparableSetOf(excludedNames));
    }

    public FolderOnlyFilesFilter(boolean caseSensitive) {
        this(caseSensitive, (Collection<String>) null);
    }

    public FolderOnlyFilesFilter() {
        this(false);
    }

    @Override
    public boolean isOnlyFolders() {
        if (!super.isOnlyFolders()) return true;
        return true;
    }

    @Override
    public void setOnlyFolders(boolean onlyFolders) {
        if (onlyFolders) super.setOnlyFolders(onlyFolders); else super.setOnlyFolders(true);
    }
}
