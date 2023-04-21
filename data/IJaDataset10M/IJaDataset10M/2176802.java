package satmule.domain.infoSearcher.protocol;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author agscjmartind
 *
 */
public class Name implements Serial {

    private String name;

    private Length length;

    public void read(InputStream is) {
        length.read(is);
        byte temp[] = new byte[(int) (length.getLength())];
        try {
            is.read(temp);
            name = new String(temp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void write(OutputStream os) {
        length.write(os);
        try {
            os.write(name.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Name(InputStream is) {
        this("");
        read(is);
    }

    public Name(String name) {
        this.length = new Length(name.length());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.length = new Length(name.length());
        this.name = name;
    }

    public Length getLength() {
        return length;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object o) {
        boolean ret = true;
        try {
            Name oo = (Name) o;
            ret = oo.getName().equals(this.getName());
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }
}
