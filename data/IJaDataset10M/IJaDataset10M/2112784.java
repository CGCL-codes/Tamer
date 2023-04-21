package plasmid.topology;

import COM.odi.util.OSHashMap;
import COM.odi.util.OSHashSet;
import plasmid.Persistible;

/** A primitive sketch of an interface
  * for policy modules. Need something
  * more substantial (perhaps ala jdk1.2
  * JAAS api).
  **/
public interface Policy extends Persistible {

    final int OP_INVALID = 0;

    final int OP_READ = 1;

    final int OP_WRITE = 2;

    public boolean allowed(User u, int op);
}
