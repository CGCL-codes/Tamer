package jhomenet.comm.client;

import java.io.*;

/***
 * Since all the semantics are captured in the name, we might
 * as well make this externalizable (saves a little bit on
 * reflection, saves a little bit on bandwidth). 
 */
public class ServerUnavailable extends Exception implements Externalizable {

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }

    public void writeExternal(ObjectOutput out) throws IOException {
    }
}
