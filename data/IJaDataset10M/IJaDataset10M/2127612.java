package demo.hw.server;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class UserAdapter extends XmlAdapter<UserImpl, User> {

    public UserImpl marshal(User v) throws Exception {
        if (v instanceof UserImpl) {
            return (UserImpl) v;
        }
        return new UserImpl(v.getName());
    }

    public User unmarshal(UserImpl v) throws Exception {
        return v;
    }
}
