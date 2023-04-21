package net.sf.japi.progs.jeduca.swing.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/** Swing FileFilter implementation that filters files with specified endings.
 * @author <a href="mailto:chris@riedquat.de">Christian Hujer</a>
 * @todo find a convenient way for i18n/l10n of this class
 */
public class EndingFileFilter extends FileFilter {

    /** Wether to accept directories. */
    private final boolean acceptDirectories;

    /** Wether to negate the endings. */
    private final boolean negate;

    /** The description text. */
    private String description;

    /** The fileendings to accept. */
    private final String[] endings;

    /** Create a DFileFilter.
     * <code>negate</code> is set to false on this file filter.
     * @param acceptDirectories pass <code>true</code> if this FileFilter should accept directories as well, <code>false</code> to deny directories
     * @param description The description to use for swing
     * @param endings The endings to accept
     */
    public EndingFileFilter(final boolean acceptDirectories, final String description, final String[] endings) {
        this(acceptDirectories, false, description, endings);
    }

    /** Create a DFileFilter.
     * @param acceptDirectories pass <code>true</code> if this FileFilter should accept directories as well, <code>false</code> to deny directories
     * @param negate pass <code>true</code> if the endings are to be negated, which means the filter will only accept files <em>not</em> ending on one
     * of the <var>endings</var>; usually you want to pass <code>false</code>
     * @param description The description to use for swing
     * @param endings The endings to accept
     */
    public EndingFileFilter(final boolean acceptDirectories, final boolean negate, final String description, final String[] endings) {
        this.acceptDirectories = acceptDirectories;
        this.negate = negate;
        this.description = description;
        this.endings = endings;
    }

    /** {@inheritDoc} */
    public boolean accept(final File f) {
        String fileName = f.getName();
        boolean ret;
        if (negate) {
            ret = true;
            for (int i = 0, l = endings.length; ret && i < l; i++) {
                ret &= !fileName.endsWith(endings[i]);
            }
            ret |= acceptDirectories && f.isDirectory();
        } else {
            ret = acceptDirectories && f.isDirectory();
            for (int i = 0, l = endings.length; !ret && i < l; i++) {
                ret |= fileName.endsWith(endings[i]);
            }
        }
        return ret;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return description;
    }
}
