package marten.aoe.fileio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class IoUtil {

    public static int getSize(Serializable object) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream;
        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(object);
            objectStream.flush();
            objectStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return byteStream.size();
    }
}
