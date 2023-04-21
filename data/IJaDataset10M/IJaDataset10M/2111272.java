package name.huzhenbo.java.serialization;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.*;

public class FooTest {

    @Test
    public void should_be_able_to_serialize_the_subclass() throws IOException, ClassNotFoundException {
        String serializedFile = "out\\test\\JavaTry\\name\\huzhenbo\\java\\serialization\\foo.ser";
        Foo foo = new Foo(1, 2);
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(serializedFile));
        os.writeObject(foo);
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(serializedFile));
        Foo deseredFoo = (Foo) is.readObject();
        assertEquals(1, deseredFoo.getX());
        assertEquals(2, deseredFoo.getY());
    }
}
