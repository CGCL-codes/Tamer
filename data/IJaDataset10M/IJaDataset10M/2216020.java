package mobac.program.interfaces;

/**
 * Marker interface for file based map sources (map sources that use tiles present in the file system and therefore do
 * not stress online tile servers.
 */
public interface FileBasedMapSource extends MapSource {

    public void initialize();

    public void reinitialize();
}
