package jpfm.volume.vector;

import jpfm.DirectoryStream;
import jpfm.FileDescriptor;
import jpfm.FileFlags;
import jpfm.volume.CommonFileAttributesProvider;
import jpfm.volume.utils.StringMaker;

/**
 *
 * @author Shashank Tulsyan
 */
public class VectorDirectory extends VectorNode {

    protected String name;

    protected VectorNode parent;

    public VectorDirectory(String name, VectorNode parent) {
        this.name = name;
        this.parent = parent;
    }

    public VectorDirectory(String name, VectorNode parent, int initialCapacity, int capacityIncrement, CommonFileAttributesProvider attributesProvider) {
        super(initialCapacity, capacityIncrement, attributesProvider);
        this.name = name;
        this.parent = parent;
    }

    public VectorDirectory(String name, VectorNode parent, CommonFileAttributesProvider attributesProvider) {
        super(10, 0, attributesProvider);
        this.parent = parent;
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    public final FileDescriptor getParentFileDescriptor() {
        return parent.getFileDescriptor();
    }

    @Override
    public String toString() {
        return StringMaker.createString(this, super.fileDescriptor.toString());
    }

    public VectorNode getParent() {
        return parent;
    }

    public FileFlags getFileFlags() {
        return new FileFlags.Builder().setExecutable().build();
    }
}
