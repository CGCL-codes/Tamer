package net.sf.jpkgmk.prototype;

import java.io.File;
import java.util.Arrays;
import net.sf.jpkgmk.ParseException;
import net.sf.jpkgmk.pkgmap.PkgMapEntry;
import net.sf.jpkgmk.pkgmap.PkgMapEntryInfo;
import net.sf.jpkgmk.pkgmap.PkgMapEntryType;
import net.sf.jpkgmk.util.StringUtil;
import net.sf.jpkgmk.util.StringUtil.KeyValuePair;

/**
 * @author gommma (gommma AT users.sourceforge.net)
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 2 $ $Date: 2008-08-20 15:14:19 -0400 (Wed, 20 Aug 2008) $
 * @since 1.0
 */
public class PrototypeEntryInfo extends PrototypeEntryFile {

    /**
	 * Constructor taking the mandatory arguments to create a prototype file entry
	 * @param entryPath
	 */
    public PrototypeEntryInfo(String entryPath) {
        this(null, entryPath, null);
    }

    /**
	 * Full constructor taking all arguments supported for this entry type
	 * @param part The part number of this entry
	 * @param entryPath the target path that this file/directory should have in the created package
	 * @param entryPathSource path to the local source file
	 */
    public PrototypeEntryInfo(Integer part, String entryPath, String entryPathSource) {
        super(part, PrototypeEntryType.I, null, entryPath, entryPathSource, null, null, null, null);
        if (entryPath == null) {
            throw new NullPointerException("The parameter 'value' must not be null");
        }
    }

    @Override
    protected PkgMapEntry createPkgMapEntry(Integer part, PkgMapEntryType resultType, String entryClass, String entryPath, String entryPathSource, Integer major, Integer minor, String mode, String owner, String group, File basedir) {
        return new PkgMapEntryInfo(part, entryPath);
    }

    public static class PrototypeEntryInfoParser extends AbstractPrototypeEntryParser implements PrototypeEntryParser {

        public PrototypeEntryInfoParser() {
            super();
        }

        @Override
        protected PrototypeEntry parseItems(String[] items, Integer part, PrototypeEntryType type, int currentIndex, PrototypeEntryCommandDefault entryCommandDefault) {
            if (items.length < 2) {
                throw new ParseException("No valid info entry line '" + Arrays.asList(items) + "'. Example: 'i /usr/wrap=/bla/my/path'");
            }
            KeyValuePair keyValuePair = StringUtil.resolveKeyValue(items[currentIndex++]);
            String entryPath = keyValuePair.getKey();
            String entryPathSource = keyValuePair.getValue();
            PrototypeEntry entry = new PrototypeEntryInfo(part, entryPath, entryPathSource);
            return entry;
        }
    }
}
