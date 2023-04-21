package net.sourceforge.antme.tasks.config;

import net.sourceforge.antme.tasks.ToolkitConfigTask;

/**
 * This class represents a <code>&lt;cpentry /&gt;</code> item
 * nested with a <code>meToolkitConfig</code> task.
 * 
 * @author khunter
 *
 */
public class CPEntry {

    public static final String LANG_NEED_FILE_OR_DIR = "CPEntry.LANG_NEED_FILE_OR_DIR";

    public static final String LANG_ONLY_FILE_OR_DIR = "CPEntry.LANG_ONLY_FILE_OR_DIR";

    private String _dir;

    private String _file;

    /**
	 * Default constructor.
	 *
	 */
    public CPEntry() {
    }

    /**
	 * Directory setter.  This is called in response to the
	 * <code>dir</code> attribute's presence in the tag.
	 * @param dir	<code>String</code> specifying the directory path.
	 */
    public void setDir(String dir) {
        _dir = dir;
    }

    /**
	 * Returns the <code>String</code> specifying the directory path.
	 * @return	<code>String</code> specifying the directory path.
	 */
    public String getDir() {
        return _dir;
    }

    /**
	 * File setter.  This is called in response to the
	 * <code>file</code> attribute's presence in the tag.
	 * @param file	<code>String</code> specifying the file path.
	 */
    public void setFile(String file) {
        _file = file;
    }

    /**
	 * Returns the <code>String</code> specifying the file path.
	 * @return	<code>String</code> specifying the file path.
	 */
    public String getFile() {
        return _file;
    }

    /**
	 * Validate the contents of the object.
	 * 
	 * @param task	Enclosing <code>ToolkitConfigTask</code>
	 */
    public void validate(ToolkitConfigTask task) {
        if (_file == null && _dir == null) {
            task.throwBuildException(LANG_NEED_FILE_OR_DIR);
        }
        if (_file != null && _dir != null) {
            task.throwBuildException(LANG_ONLY_FILE_OR_DIR);
        }
    }
}
