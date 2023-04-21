package LONI.tree;

public class Library extends LoniNode {

    public Library(String name, String version, String myPackage, String uri, String compilationTime) {
        super();
        this.name = name;
        this.version = version;
        this.myPackage = myPackage;
        this.uri = uri;
        this.compilationTime = compilationTime;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMyPackage() {
        return myPackage;
    }

    public String getUri() {
        return uri;
    }

    public String getCompilationTime() {
        return compilationTime;
    }

    private final String name;

    private final String version;

    private final String myPackage;

    private final String uri;

    private final String compilationTime;
}
