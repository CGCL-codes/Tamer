package takatuka.drivers.interfaces;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Zhongjie Cai
 * @version 1.0
 */
public interface IBlockStorage {

    public int read(int addr, int[] buf, int len);

    public int computeCrc(int addr, int len, int crc);

    public int getSize();

    public int write(int addr, int[] buf, int len);

    public int erase();

    public int sync();
}
