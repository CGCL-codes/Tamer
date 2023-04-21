package swarm.space;

public interface Discrete2d {

    int getValueAtX$Y(int x, int y);

    void putValue$atX$Y(int value, int x, int y);

    void fillWithValue(int value);

    int getSizeX();

    int getSizeY();
}
