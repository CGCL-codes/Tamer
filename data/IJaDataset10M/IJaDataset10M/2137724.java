package tony;

import java.io.IOException;
import java.io.ObjectInputStream;
import resources.ResourceAnchor;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            ObjectInputStream oin = new ObjectInputStream(ResourceAnchor.class.getResourceAsStream("getblob-1"));
            Object obj = oin.readObject();
            while (obj != null) {
                System.out.println(obj);
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
