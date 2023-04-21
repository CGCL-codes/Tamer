package visitpc.filetransfer.messages;

import java.io.Serializable;

public class SetDirObject implements Serializable {

    public String dir;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName() + "\n");
        sb.append("dir:    " + dir + "\n");
        return sb.toString();
    }
}
