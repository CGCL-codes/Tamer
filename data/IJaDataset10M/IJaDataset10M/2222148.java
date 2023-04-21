package gdromImage;

public interface gdromImage {

    public boolean open();

    public boolean close();

    public int readSector(int sector_start, int sec_num);

    public String toString();
}
