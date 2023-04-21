package octlight.image;

public interface Image {

    int getBitsPerChannel();

    ChannelLayout getChannelLayout();

    byte[] getData();

    int getWidth();

    int getHeight();
}
