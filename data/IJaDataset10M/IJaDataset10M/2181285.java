package name.pachler.nio.file;

import java.io.IOException;
import name.pachler.nio.file.WatchEvent.Kind;
import name.pachler.nio.file.WatchEvent.Modifier;

/**
 * This class represents an abstract Path object that a WatchService can
 * operate on.<br/>
 * Note that Path is a new way of representing file system paths in JDK7 and
 * is included here to provide source level compatibility. This implementation
 * only uses it as a wrapper for java.io.File.</br>
 * To create a new Path instance, either use the Bootstrapper.newPath()
 * or Paths.
 * @author count
 */
public abstract class Path implements Watchable {

    protected Path() {
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    public abstract WatchKey register(WatchService watcher, Kind<?>... events) throws IOException;

    public abstract WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException;

    public abstract Path resolve(Path other);

    @Override
    public abstract String toString();
}
