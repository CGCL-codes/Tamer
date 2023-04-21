package bordeauxjug.guava6functions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @since 1.0
 * @author lforet
 */
public class LoginServiceBefore implements LoginService {

    @Override
    public List<String> strongifyPasswords(List<String> passwords) {
        List<String> toReturn = new ArrayList<String>();
        for (String password : passwords) {
            toReturn.add(password.length() < 5 ? "12345" : password);
        }
        return toReturn;
    }
}
