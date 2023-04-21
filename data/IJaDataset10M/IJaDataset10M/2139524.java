package configlib;

import java.io.IOException;

/**
 *
 * @author Anton Gravestam
 */
public class Person implements SerializableOld, Deserializable {

    String name = "";

    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void serialize(ArchiverOut archive) throws IOException {
        archive.put("name", name);
        archive.put("age", age);
    }

    public void desirialize(ArchiverIn archiverIn) {
        name = archiverIn.getString("name");
        age = archiverIn.getInt("age");
    }
}
