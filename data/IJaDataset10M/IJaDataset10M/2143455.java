package j2se.typestate.file;

/**
 * 
 * @author yahave
 *
 */
public class Test1 {

    public static void main(String[] args) {
        FileHolder x = new FileHolder();
        FileHolder y = new FileHolder();
        FileHolder z = new FileHolder();
        FileComponent f = new FileComponent();
        x.file = f;
        f = new FileComponent();
        y.file = f;
        f = new FileComponent();
        z.file = f;
        FileComponent f1 = x.file;
        FileComponent f2 = y.file;
        f1.close();
        f2.read();
    }
}
