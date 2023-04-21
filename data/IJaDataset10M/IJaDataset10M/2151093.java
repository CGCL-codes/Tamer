package visitpc.messages;

import java.io.Serializable;

public class DisconnectSrcClientFromDestClient implements Serializable {

    public String username;

    public String password;

    public String pcName;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("DisconnectSrcClientFromDestClient\n");
        sb.append("username:       " + username + "\n");
        sb.append("password:       " + password + "\n");
        sb.append("pcName:         " + pcName + "\n");
        return sb.toString();
    }
}
