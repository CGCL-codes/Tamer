package tripleo.framework.fs;

public class FileSystem {

    public static IDirHandler openUnix(String aBaseUrl) {
        return new UnixDirHandler(aBaseUrl);
    }
}
