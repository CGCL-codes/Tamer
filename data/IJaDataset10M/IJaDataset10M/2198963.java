package core;

import java.io.File;
import core.Tools.Tool;
import exceptions.AlreadyReservedException;
import exceptions.NotReservedByToolException;

/**
 * An Input File is a file to be processed by a tool. The actual (@link File) is
 * contained within an InputFile, but access to it is only granted after
 * reserving the InputFile, for the {@link Tool) requesting access to it. This
 * is done in an attempt to prevent simultaneous access to the File
 * 
 * @author Daniel Grob
 * @version 0.1
 */
public class InputFile {

    private Tool currentTool = null;

    /**
	 * This is the original File, which is needed to be carried along, so we
	 * know the original Filename and stuff.
	 */
    private File file = null;

    /**
	 * This is the file we currently work at. This may or may not be the
	 * original file. Most likely its a file in JFM's temporary Folder.
	 */
    private File fileBuffer = null;

    /**
	 * This is the constructor used when first creating an InputFile. Please
	 * notice that there is another constructor, used to chain {@link Tool}s
	 * together, which takes the result of one Tool, and creates an input for
	 * the next tool.
	 * 
	 * @param f
	 *            the File from which a new InputFile shall be generated
	 */
    public InputFile(final File f) {
        this.file = f;
        this.fileBuffer = f;
    }

    /**
	 * This creates a new InputFile from an output of a Tool. This is needed
	 * when chaining Tools together.
	 * 
	 * @param of
	 *            the OutputFile from which a new InputFile shall be generated
	 */
    public InputFile(final OutputFile of) {
        this.file = of.getInputFile().file;
        this.fileBuffer = of.getResult();
    }

    public File getFile(final Tool t) throws NotReservedByToolException {
        if (this.currentTool != t) throw new NotReservedByToolException(t);
        return this.fileBuffer;
    }

    public Tool getReservingTool() {
        return this.currentTool;
    }

    public boolean isReserved() {
        return this.currentTool != null;
    }

    public void reserveFor(final Tool t) throws AlreadyReservedException {
        if (this.currentTool != null) throw new AlreadyReservedException(this.currentTool);
        this.currentTool = t;
    }

    public void returnFrom(final Tool t) throws NotReservedByToolException {
        if (this.currentTool != t) throw new NotReservedByToolException(t);
        this.currentTool = null;
    }
}
