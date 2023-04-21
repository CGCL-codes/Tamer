package net.sourceforge.myvd.inserts.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import net.sourceforge.myvd.chain.InterceptorChain;
import net.sourceforge.myvd.types.DNComparer;
import com.novell.ldap.util.DN;
import com.novell.ldap.util.RDN;

public class AccessMgr {

    TreeMap<DN, AciLevel> acl;

    ArrayList<AccessControlItem> acis;

    public AccessMgr() {
        this.acl = new TreeMap<DN, AciLevel>(new DNComparer());
        this.acis = new ArrayList<AccessControlItem>();
    }

    public void addACI(AccessControlItem aci) {
        this.acis.add(aci);
        this.add(aci);
    }

    private void add(AccessControlItem aci) {
        DN dn = aci.getDn();
        if (aci.getNum() == 3) {
        }
        Vector<RDN> rdns = dn.getRDNs();
        for (int i = rdns.size() - 1; i >= 0; i--) {
            DN levelDN = new DN();
            for (int l = rdns.size() - 1; l >= i; l--) {
                levelDN.addRDNToFront(rdns.get(l));
                AciLevel level = this.acl.get(levelDN);
                if (level == null) {
                    level = new AciLevel();
                    level.acis = new ArrayList<AccessControlItem>();
                    for (int m = 1; m < levelDN.countRDNs(); m++) {
                        DN parentDNs = new DN();
                        Vector<RDN> parentRDNs = levelDN.getRDNs();
                        for (int k = m; k < levelDN.countRDNs(); k++) {
                            parentDNs.addRDNToBack(parentRDNs.get(k));
                        }
                        AciLevel parentLevel = this.acl.get(parentDNs);
                        if (parentLevel != null) {
                            Iterator<AccessControlItem> aciIt = parentLevel.acis.iterator();
                            while (aciIt.hasNext()) {
                                AccessControlItem aciItem = aciIt.next();
                                if (!level.acis.contains(aciItem)) {
                                    level.acis.add(aciItem);
                                }
                            }
                        }
                    }
                    this.acl.put(levelDN, level);
                }
                if (!level.acis.contains(aci)) {
                    level.acis.add(aci);
                }
            }
        }
        Iterator<DN> it = this.acl.keySet().iterator();
        while (it.hasNext()) {
            dn = it.next();
            if (this.isDescendantOf(aci.getDn(), dn) && !acl.get(dn).acis.contains(aci)) {
                acl.get(dn).acis.add(aci);
            }
        }
    }

    public ArrayList<AccessControlItem> getACLs(DN dn) {
        DN toFindDn = new DN(dn.toString());
        Vector<RDN> rdns = dn.getRDNs();
        while (rdns.size() != 0) {
            AciLevel level = this.acl.get(toFindDn);
            if (level != null) {
                return level.acis;
            }
            rdns.remove(0);
            toFindDn = new DN();
            for (int i = 0; i < rdns.size(); i++) {
                toFindDn.addRDNToBack(rdns.get(i));
            }
        }
        return null;
    }

    public AccessControlItem getApplicableACI(DN dn, String attributeName, char perm, InterceptorChain chain) {
        if (dn.toString().length() == 0) {
            dn = new DN("CN=ROOTDSE");
        }
        ArrayList<AccessControlItem> acl = this.getACLs(dn);
        AccessControlItem aciToReturn = null;
        if (acl == null) {
            return null;
        }
        Iterator<AccessControlItem> it = acl.iterator();
        while (it.hasNext()) {
            AccessControlItem aci = it.next();
            if ((!aci.isSubtree) && (!aci.getDn().equals(dn))) {
                continue;
            }
            if (attributeName != null) {
                if (aci.isEntryPerms()) {
                    continue;
                }
                if (aci.isAllAttributes || aci.getAttributes().contains(attributeName.toLowerCase())) {
                    if (aci.checkSubject(chain, dn) && isPermission(perm, aci)) {
                        aciToReturn = aci;
                    }
                }
            } else {
                if (!aci.isEntryPerms()) {
                    continue;
                }
                if (this.isDescendantOf(aci.getDn(), dn) && aci.checkSubject(chain, dn) && isPermission(perm, aci)) {
                    aciToReturn = aci;
                }
            }
        }
        return aciToReturn;
    }

    private boolean isPermission(char perm, AccessControlItem aci) {
        switch(perm) {
            case 'r':
                return aci.isRead();
            case 'w':
                return aci.isWrite();
            case 's':
                return aci.isSearch();
            case 'o':
                return aci.isObliterate();
            case 'p':
                return aci.isPresenceSearch();
            case 'c':
                return aci.isCompare();
            case 'a':
                return aci.isCreate();
            case 'd':
                return aci.isDelete();
            case 'v':
                return aci.isView();
            case 'n':
                return aci.isRename();
        }
        return false;
    }

    private boolean isDescendantOf(DN parent, DN child) {
        Vector<RDN> parentRDNs = parent.getRDNs();
        Vector<RDN> childRDNs = child.getRDNs();
        if (childRDNs.size() < parentRDNs.size()) {
            return false;
        }
        int i = childRDNs.size() - 1;
        int l = parentRDNs.size() - 1;
        for (; l >= 0; ) {
            if (!parentRDNs.get(l).equals(childRDNs.get(i))) {
                return false;
            }
            i--;
            l--;
        }
        return true;
    }
}

class AciLevel {

    ArrayList<AccessControlItem> acis;
}
