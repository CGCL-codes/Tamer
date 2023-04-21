package net.sf.jpkgmk.prototype;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import net.sf.jpkgmk.pkgmap.PkgMapEntry;
import net.sf.jpkgmk.pkgmap.PkgMapEntryLink;
import net.sf.jpkgmk.pkgmap.PkgMapEntryType;
import net.sf.jpkgmk.util.StringUtil;
import net.sf.jpkgmk.util.StringUtil.KeyValuePair;

/**
 * @author gommma (gommma AT users.sourceforge.net)
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 2 $ $Date: 2008-08-20 15:14:19 -0400 (Wed, 20 Aug 2008) $
 * @since 1.0
 */
public class PrototypeEntrySymbolicLink extends AbstractPrototypeEntry {

    /**
	 * Constructor taking the mandatory arguments to create a prototype file entry
	 * @param entryPath
	 */
    public PrototypeEntrySymbolicLink(String entryPath) {
        this(null, null, entryPath, null);
    }

    /**
	 * Full constructor taking all arguments supported for this entry type
	 * @param fileClass
	 * @param entryPath path to the file or directory
	 * @param entryPathSource the target path in that this file should have in the package
	 */
    public PrototypeEntrySymbolicLink(Integer part, String fileClass, String entryPath, String entryPathSource) {
        super(part, PrototypeEntryType.S, fileClass, entryPath, entryPathSource, null, null, null, null);
        if (entryPath == null) {
            throw new NullPointerException("The parameter 'entryPath' must not be null");
        }
        if (entryPathSource == null) {
            throw new NullPointerException("The parameter 'entryPathSource' must not be null");
        }
    }

    @Override
    protected void create(File targetDir, String entryPathExpanded, String entryPathSourceExpanded) throws IOException {
    }

    @Override
    protected PkgMapEntry createPkgMapEntry(Integer part, PkgMapEntryType resultType, String entryClass, String entryPath, String entryPathSource, Integer major, Integer minor, String mode, String owner, String group, File basedir) {
        return new PkgMapEntryLink(part, resultType, entryClass, entryPath, entryPathSource);
    }

    public static class PrototypeEntrySymbolicLinkParser extends AbstractPrototypeEntryParser implements PrototypeEntryParser {

        public PrototypeEntrySymbolicLinkParser() {
            super();
        }

        @Override
        protected PrototypeEntry parseItems(String[] items, Integer part, PrototypeEntryType type, int currentIndex, PrototypeEntryCommandDefault entryCommandDefault) {
            if (items.length < 3) {
                throw new IllegalArgumentException("The given array must contain at least 3 items to be a valid link: " + Arrays.asList(items));
            }
            String fileClass = items[currentIndex++];
            String path = items[currentIndex++];
            KeyValuePair keyValue = StringUtil.resolveKeyValue(path);
            String entryPath = keyValue.getKey();
            String entryPathSource = keyValue.getValue();
            PrototypeEntrySymbolicLink entry = new PrototypeEntrySymbolicLink(part, fileClass, entryPath, entryPathSource);
            return entry;
        }
    }
}
