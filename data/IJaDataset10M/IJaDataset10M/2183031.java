package net.lukemurphey.nsia;

public interface DatabaseSerialization {

    public Object load(int ID, Application application);

    public void save();
}
