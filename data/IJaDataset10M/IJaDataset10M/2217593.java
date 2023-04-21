package fedora.server.storage.service;

import java.util.Hashtable;

/**
 *
 * <p><b>Title:</b> Mmap.java</p>
 * <p><b>Description:</b> </p>
 *
 * @author payette@cs.cornell.edu
 * @version $Id: Mmap.java 3966 2005-04-21 13:33:01Z rlw $
 */
public class Mmap {

    public String mmapName = null;

    public MmapMethodDef[] mmapMethods = new MmapMethodDef[0];

    public Hashtable wsdlOperationToMethodDef;
}
