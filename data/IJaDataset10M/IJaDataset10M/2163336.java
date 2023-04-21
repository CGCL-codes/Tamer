package eu.more.cryptographicservicecore.caches;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CSContainer implements Serializable {

    private static final long serialVersionUID = -6396758106385124607L;

    public Map<String, PrivateKey> prKeys = Collections.synchronizedMap(new HashMap<String, PrivateKey>());

    public Map<String, PublicKey> puKeys = Collections.synchronizedMap(new HashMap<String, PublicKey>());

    public Set<String> trustedTickets = Collections.synchronizedSet(new HashSet<String>());
}
