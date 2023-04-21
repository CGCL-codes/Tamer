package corina;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import corina.core.App;
import corina.formats.WrongFiletypeException;

/**
   An Element, basically a reference to a Sample (stored on disk).

   <p>Normally used as a member of a SampleSet (though at this time
   SampleSet is only an ArrayList that provides text load/save of its
   elements).</p>

   <p>An Element holds:</p>

   <ul>
     <li>An active flag
     <li>A filename
     <li>A summary of details (the metadata from a Sample)
     <li>The range (updated with summary details)
   </ul>

   <p>The active flag, true by default, indicates whether this Element
   is to be used in a particular operation.  For example, Sum will
   ignore inactive Elements, so users can quickly and easily try
   removing Elements from a Sum to see how the result changes.</p>

   <p>The filename is the unique name of this Sample.  It is expected
   that at this place in the filesystem, there will be a Sample that
   can be loaded by the Sample constructor Sample(String filename).
   There will be trouble (i.e., IOExceptions) if this is not the case.
   An Element's filename is immutable: once an Element has been
   created, it cannot be pointed at any other sample.</p>

   <p>(Note: I have considered adding a new field, filetype, so
   non-auto-detectable filetypes can be loaded, but so far there has
   been no demand for this.)</p>

   <p>An Element also holds space for a summary of its details.  By
   default, this is empty, until the loadMeta() method has been
   called.  After a loadMeta(), there will be a Map of details,
   namely, the metadata fields from the Sample's metadata Map.  The
   entire metadata map is usually 500 bytes or less, so all fields are
   loaded.  (At 500 bytes each, a huge chronology with 200 elements
   would take up 100K for all of the preview information -- usually
   masters are much smaller, so this is only a couple kilobytes.
   There's no reason to try to load only some of the fields.)

   <p>OLD RAMBLINGS I DON'T WANT TO DELETE YET: there should be a way
   to load the Range - incorporate it into meta?.  If it could do
   this, then visual.Bargraph.Bar wouldn't be needed, and visual.*
   could be a lot cleaner, and then I could use a real MVC model for
   everything, and the whole GUI would be cleaner.  How about
   that.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: Element.java,v 1.9 2006/07/11 20:17:23 lucasmo Exp $
 */
public class Element implements Comparable {

    public boolean active;

    public Map details = null;

    public long lastModified = -1;

    private Range range = null;

    public final String filename;

    public String folder = null;

    public final String basename;

    public Range getRange() {
        if (details == null) try {
            loadMeta();
        } catch (WrongFiletypeException wfte) {
            return null;
        } catch (IOException ioe) {
            System.out.println("on " + filename + ", " + ioe);
            ioe.printStackTrace();
            return null;
        }
        return range;
    }

    public void setRange(Range r) {
        this.range = r;
    }

    public Exception error = null;

    /**
	 Construct an Element from a filename.  This Element will, by
	 default, be active.

	 @param filename the filename of the Sample to reference
	 */
    public Element(String filename) {
        this(filename, true);
    }

    /**
	 Construct an Element from a filename, and a preset Active flag.

	 @param filename the filename of the Sample to reference
	 @param active true if this Element is to be active
	 */
    public Element(String filename, boolean active) {
        this.active = active;
        if (filename.startsWith("?")) {
            String fn = filename.substring(1);
            int pos = fn.lastIndexOf(':');
            if (pos >= 0) {
                this.folder = fn.substring(0, pos);
                this.basename = fn.substring(pos + 1, fn.length());
                this.filename = App.prefs.getPref("corina.dir.data") + File.separator + this.folder.replace(":", File.separator) + File.separator + this.basename;
            } else {
                this.filename = filename;
                this.basename = new File(filename).getName();
            }
        } else {
            String fn = filename;
            if (Boolean.valueOf(App.prefs.getPref("corina.dir.adaptiveread")).booleanValue() == false) {
                this.filename = filename;
                this.basename = new File(filename).getName();
                return;
            }
            if (fn.startsWith("G:\\DATA\\")) {
                fn = fn.substring(8);
            } else if (filename.startsWith(App.prefs.getPref("corina.dir.data") + File.separator)) {
                fn = fn.substring(App.prefs.getPref("corina.dir.data").length() + File.separator.length());
            } else {
                this.filename = filename;
                this.basename = new File(filename).getName();
                return;
            }
            fn = fn.replace("\\", ":");
            fn = fn.replace(File.separator, ":");
            int pos = fn.lastIndexOf(':');
            if (pos >= 0) {
                this.folder = fn.substring(0, pos);
                this.basename = fn.substring(pos + 1, fn.length());
                this.filename = App.prefs.getPref("corina.dir.data") + File.separator + this.folder.replace(":", File.separator) + File.separator + this.basename;
            } else {
                this.filename = filename;
                this.basename = new File(filename).getName();
            }
        }
    }

    /**
	 Return the state of the Element's active flag.

	 @return true if this Element is active
	 */
    public boolean isActive() {
        return active;
    }

    /**
	 Return the Element's filename.

	 @return the filename this Element refers to
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 Return the Element's basename.

	 @return the basename of the file this Element refers to
	 */
    public String getBasename() {
        return basename;
    }

    /**
	 Return the Element's folder path.

	 @return the folder path of the file this Element refers to
	 */
    public String getFolder() {
        return folder;
    }

    /**
	 Return this Element's filename, with
	 <code>corina.dir.data</code> replaced by an "?", if it's in a
	 subfolder of that.  (Otherwise, returns the absolute filename.)

	 @return the filename, with ?'s
	 */
    public String getRelativeFilename() {
        if (basename == null || folder == null) return filename;
        return "?" + folder + ":" + basename;
    }

    /**
	 Load this Element.  Returns this Element in a Sample object.

	 @return the Sample referenced by this Element
	 @exception IOException if an IOException occurred while trying
	 to load it; this can also be the subclasses
	 FileNotFoundException
	 */
    public Sample load() throws IOException {
        return new Sample(filename);
    }

    private boolean dead = false;

    public void reloadMeta() throws IOException {
        details = null;
        loadMeta();
    }

    /**
	 Load the metadata fields for this Element.

	 @exception IOException if the Element could not be loaded
	 */
    public void loadMeta() throws IOException {
        if (dead) throw (IOException) error;
        if (details != null) {
            return;
        }
        try {
            Sample s = load();
            details = s.meta;
            range = s.range;
            lastModified = new File(filename).lastModified();
        } catch (IOException ioe) {
            dead = true;
            error = ioe;
            throw ioe;
        }
    }

    /**
	 Return the filename, so Element can be used in making Strings
	 without worrying about getFilename() calls.

	 @return the filename
	 */
    public String toString() {
        return filename;
    }

    public int compareTo(Object o) {
        return filename.compareTo(((Element) o).filename);
    }

    public boolean hasBark() {
        String term = (String) details.get("terminal");
        return (term != null && term.equalsIgnoreCase("B"));
    }

    public boolean hasPith() {
        String pith = (String) details.get("pith");
        return (pith != null && pith.equalsIgnoreCase("Y"));
    }

    public int numSapwood() {
        Integer sapwood = (Integer) details.get("sapwood");
        return (sapwood == null ? 0 : sapwood.intValue());
    }

    public Object getMeta(String field) {
        if (details == null) try {
            loadMeta();
        } catch (WrongFiletypeException wfte) {
            return null;
        } catch (IOException ioe) {
            System.out.println("on " + filename + ", " + ioe);
            ioe.printStackTrace();
            return null;
        }
        return details.get(field);
    }

    public boolean isSample() {
        if (details != null) return true;
        try {
            loadMeta();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }
}
