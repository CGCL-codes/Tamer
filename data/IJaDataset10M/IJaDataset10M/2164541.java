package blue.mixer;

import java.io.Serializable;
import java.util.ArrayList;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author Steven Yi
 */
public class SendsList implements Serializable {

    ArrayList sends = new ArrayList();

    public Send getSend(int index) {
        return (Send) sends.get(index);
    }

    public void addSend(Send send) {
        sends.add(send);
    }

    public void removeSend(Send send) {
        sends.remove(send);
    }

    public int size() {
        return sends.size();
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
