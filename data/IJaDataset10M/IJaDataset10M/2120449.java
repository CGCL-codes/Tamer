package net.nohaven.proj.javeau.fs.model;

public interface FSDirectory extends FSItem {

    public FSDirectory getParent();

    public String getPath();

    public FSItem[] getContents();

    public FSItem get(String name);

    public boolean isDevice();
}
