package net.sourceforge.freejava.vfs;

import net.sourceforge.freejava.type.traits.Attributes;
import net.sourceforge.freejava.type.traits.IAttributes;
import net.sourceforge.freejava.vfs.path.BadPathException;
import net.sourceforge.freejava.vfs.path.IPath;

public abstract class AbstractFsEntry extends Attributes implements IFsEntry {

    private final IVolume volume;

    private final IPath path;

    private final String baseName;

    private boolean createParents;

    public AbstractFsEntry(IVolume volume, IPath path) {
        if (volume == null) throw new NullPointerException("volume");
        if (path == null) throw new NullPointerException("path");
        this.volume = volume;
        this.path = path;
        this.baseName = path.getBaseName();
        assert baseName != null;
    }

    /**
     * Construct an {@link IFsEntry} with abstract volume.
     * 
     * This is only used by {@link TransientVolume}.
     */
    AbstractFsEntry(IPath path) {
        if (path == null) throw new NullPointerException("path");
        this.volume = null;
        this.path = path;
        this.baseName = path.getBaseName();
    }

    /**
     * Construct an {@link IFsEntry} with abstract volume and abstract path.
     * 
     * This is only used by {@link TransientPath}.
     */
    AbstractFsEntry(String baseName) {
        if (baseName == null) throw new NullPointerException("baseName");
        this.volume = null;
        this.path = null;
        this.baseName = baseName;
    }

    @Override
    public abstract IFsEntry clone();

    /**
     * Populate this object with states recognizable from given object.
     * 
     * @return this
     */
    protected AbstractFsEntry populate(Object obj) {
        if (obj instanceof AbstractFsEntry) {
            AbstractFsEntry o = (AbstractFsEntry) obj;
            this.createParents = o.createParents;
        }
        return this;
    }

    @Override
    public IAttributes getAttributes() {
        return null;
    }

    @Override
    public IVolume getVolume() {
        return volume;
    }

    @Override
    public IPath getPath() {
        return path;
    }

    @Override
    public String getName() {
        return baseName;
    }

    @Override
    public String getExtension(boolean withDot, int maxWords) {
        if (baseName == null) return null;
        int pos = baseName.length();
        while (maxWords-- > 0) {
            int dot = baseName.lastIndexOf('.', pos);
            if (dot == -1) break;
            pos = dot;
        }
        if (!withDot) pos++;
        if (pos < baseName.length()) baseName.substring(pos);
        return "";
    }

    @Override
    public Long getCreationTime() {
        return null;
    }

    @Override
    public Long getLastModifiedTime() {
        return null;
    }

    @Override
    public boolean setLastModifiedTime(long lastModifiedTime) {
        return false;
    }

    @Override
    public int getModifiers() {
        return getModifiers(FileModifier.MASK_ALL);
    }

    @Override
    public Boolean exists() {
        return null;
    }

    @Override
    public boolean isExisted() {
        return exists() == Boolean.TRUE;
    }

    @Override
    public boolean isNotExisted() {
        return exists() == Boolean.FALSE;
    }

    @Override
    public boolean isBlob() {
        return false;
    }

    @Override
    public boolean isTree() {
        return false;
    }

    @Override
    public boolean isReadable() {
        return false;
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean deleteOnExit() {
        return false;
    }

    @Override
    public boolean getCreateParentsMode() {
        return createParents;
    }

    @Override
    public void setCreateParentsMode(boolean createParents) {
        this.createParents = createParents;
    }

    @Override
    public String toString() {
        return getPath().toString();
    }

    /**
     * Abstract implementation of {@link IFsEntry}, with transient volume support.
     * <p>
     * This is only useful if you don't want to allocate an {@link IVolume} instance before it's
     * used. And construct one on-demand, for optimization purpose.
     */
    public abstract static class TransientVolume extends AbstractFsEntry {

        public TransientVolume(IPath path) {
            super(path);
        }

        /**
         * This constructor is only used by {@link TransientPath}.
         */
        TransientVolume(String baseName) {
            super(baseName);
        }

        @Override
        public abstract IVolume getVolume();
    }

    /**
     * Abstract implementation of {@link IFsEntry}, with transient path support.
     * <p>
     * This is only useful if you don't want to allocate a {@link IPath} instance before it's used.
     * And construct one on-demand, for optimization purpose.
     */
    public abstract static class TransientPath extends TransientVolume {

        private final String pathString;

        /**
         * Creates a new fs entry with transient path.
         * 
         * The <code>pathString</code> should be in correct format to keep this fs entry in a good
         * state. Otherwise, {@link IllegalStateException} may be thrown when {@link #getPath()} is
         * called.
         * 
         * @param pathString
         *            non-<code>null</code> path string.
         */
        public TransientPath(String pathString) {
            super(getBaseName(pathString));
            this.pathString = pathString;
        }

        static String getBaseName(String path) {
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash == -1) return path; else return path.substring(lastSlash + 1);
        }

        @Override
        public abstract IVolume getVolume();

        /**
         * Constructs an {@link IPath} object on the fly.
         * 
         * @throws IllegalStateException
         *             If the <code>pathString</code> specified in the constructor is invalid.
         */
        @Override
        public final IPath getPath() {
            try {
                return constructPath(pathString);
            } catch (BadPathException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        /**
         * @throws BadPathException
         *             If <code>pathString</code> is invalid.
         */
        protected abstract IPath constructPath(String pathString) throws BadPathException;

        @Override
        public String toString() {
            return pathString;
        }
    }
}
