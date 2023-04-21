package persistencia.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import org.apache.derby.iapi.util.ByteArray;
import persistencia.FilmeFactory;
import persistencia.entity.Filme;

public class Main {

    public static void main(String[] args) throws Exception {
        Filme filme = FilmeFactory.createFilme();
        byte[] serializado = serializa(filme);
        imprimeBytes(serializado);
        Filme filme2 = (Filme) deserializa(serializado);
        System.out.println(filme2.getDiretor().getNome());
    }

    static byte[] serializa(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput oout = new ObjectOutputStream(out);
        oout.writeObject(obj);
        out.close();
        return out.toByteArray();
    }

    static Object deserializa(byte[] bytes) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInput oin = new ObjectInputStream(in);
        Object obj = oin.readObject();
        oin.close();
        return obj;
    }

    private static void imprimeBytes(byte[] serializado) {
        BigInteger bi = new BigInteger(serializado);
        System.out.println(bi.toString(2));
    }
}
