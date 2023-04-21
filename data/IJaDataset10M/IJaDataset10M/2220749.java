package ds.sp;

public interface IServer {

    public void process(byte[] data);

    public byte[] getResult();
}
