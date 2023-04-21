package construction;

/**
* @author theo
*
*/
public class Constructor02 {

    Constructor02(int i, int j) {
        int k = i + j;
        System.out.println(k);
    }

    public static void main() {
        Constructor02 p = new Constructor02(3, 2);
    }
}
