package issrg.test;

public class TestHash {

    public static void main(String[] args) throws Exception {
        System.setProperty("line.separator", "\r\n");
        System.out.println(issrg.ac.Util.hashToString(new byte[] { (byte) 0xff }));
    }
}
