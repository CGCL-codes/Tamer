package suggestion.client.classes;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Member extends Account implements Serializable {

    public Member() {
    }

    public Member(int id, String l, String e, Date sd) {
        super(id, l, e, sd);
        type = 'M';
    }
}
