package general;

public class Song {

    String src = "";

    String name;

    String singer;

    boolean isOnserver = false;

    public Song() {
    }

    public Song(String src, String name, boolean isOnserver) {
        this.src = src;
        this.name = name;
        this.isOnserver = isOnserver;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger() {
        return this.singer;
    }

    @Override
    public String toString() {
        return name;
    }
}
