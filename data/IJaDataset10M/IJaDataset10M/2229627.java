package com.whitebearsolutions.imagine.wbsagnitio;

import java.io.File;
import java.util.ArrayList;
import com.whitebearsolutions.util.Configuration;
import com.whitebearsolutions.directory.*;
import com.whitebearsolutions.directory.ldap.LDAPEntry;

public class SambaLDAP {

    public static final int ADD_WORKSTATION = 2;

    public static final int REMOVE_WORKSTATION = 3;

    private Configuration _c;

    private EntryBase _eb;

    public SambaLDAP(File config, String name, int type) throws Exception {
        this._c = new Configuration(config);
        this._eb = new EntryBase(this._c);
        switch(type) {
            default:
                break;
            case ADD_WORKSTATION:
                addWorkstation(name);
                Thread.sleep(1200);
                break;
            case REMOVE_WORKSTATION:
                removeWorkstation(name);
                Thread.sleep(1200);
                break;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int type = -1;
        File _config = null;
        String _name = null;
        for (int j = 0; j < args.length; j++) {
            if (args[j].equals("-c")) {
                _config = new File(args[j + 1]);
                j++;
                if (!_config.exists()) {
                    System.out.println("Error: el fichero de configuracion no existe.");
                    return;
                }
            } else if (args[j].equals("-w")) {
                if ((j + 1) < args.length) {
                    _name = args[j + 1];
                    if (type != REMOVE_WORKSTATION) {
                        type = ADD_WORKSTATION;
                    }
                    j++;
                } else {
                    System.out.println("Error: no se ha definido un nombre de equipo.");
                    return;
                }
            } else if (args[j].equals("-r")) {
                type = REMOVE_WORKSTATION;
            }
        }
        if (_config == null) {
            _config = new File("/etc/wbsagnitio-admin/config.xml");
            if (!_config.exists()) {
                System.out.println("Error: el fichero de configuracion no existe.");
                return;
            }
        }
        if (_name != null) {
            try {
                new SambaLDAP(_config, _name, type);
            } catch (Exception _ex) {
                System.out.println("Error: " + _ex.getMessage());
            }
        }
    }

    private void addWorkstation(String workstation) throws Exception {
        int id = 10001;
        try {
            com.whitebearsolutions.directory.Entry _e = this._eb.getEntry("sambaDomainName=" + _c.getProperty("samba.domain") + ",ou=dominios," + _c.getProperty("ldap.basedn"), false);
            id = Integer.parseInt((String) _e.getAttribute("sambaNextRid")[0]);
        } catch (Exception _ex) {
        }
        Entry _e = new LDAPEntry("uid=" + workstation + ",ou=equipos," + _c.getProperty("ldap.basedn"));
        _e.setAttribute("objectClass", new String[] { "account", "posixAccount" });
        _e.setAttribute("uid", workstation);
        _e.setAttribute("cn", workstation.replaceAll("\\$", ""));
        _e.setAttribute("gecos", workstation.replaceAll("\\$", ""));
        _e.setAttribute("homeDirectory", "/dev/null");
        _e.setAttribute("uidNumber", String.valueOf(id));
        _e.setAttribute("loginShell", "/bin/false");
        _e.setAttribute("gidNumber", "515");
        this._eb.addEntry(_e);
        _e = this._eb.getEntry("sambaDomainName=" + _c.getProperty("samba.domain") + ",ou=dominios," + _c.getProperty("ldap.basedn"), true);
        _e.setAttribute("sambaNextRid", String.valueOf(id + 1));
        this._eb.updateEntry(_e);
    }

    private void removeWorkstation(String workstation) throws Exception {
        Query _q = new Query();
        _q.addCondition(workstation, "uid", Query.EXACT);
        _q.addCondition("515", "gidNumber", Query.EXACT);
        ArrayList<Entry> _results = this._eb.search(_q, false);
        if (_results != null && _results.size() > 0) {
            this._eb.removeEntry(((com.whitebearsolutions.directory.Entry) _results.get(0)).getID());
        }
    }
}
