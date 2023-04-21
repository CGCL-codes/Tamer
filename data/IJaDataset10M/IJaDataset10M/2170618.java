package org.apache.openjpa.sdo.systems;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.openjpa.sdo.ImplHelper;
import org.apache.openjpa.sdo.SDOEntityManager;
import org.apache.openjpa.sdo.SDOEntityManagerFactory;
import org.apache.openjpa.sdo.SDOMetaDataFactory;
import org.apache.openjpa.sdo.SDOTypeParser;
import org.apache.openjpa.sdo.systems.store.LdapConfiguration;
import org.apache.openjpa.sdo.systems.store.LdapHandler;
import org.apache.tuscany.sdo.api.SDOUtil;
import test.common.GaiusXSDAbstractSDOTest;
import commonj.sdo.DataGraph;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/**
 * @author Administrator
 * 
 */
public abstract class AbstractSystemsTest extends GaiusXSDAbstractSDOTest {

    protected static final String LDAPNAMESPACE = "spc.gaius.design.schema/ldap.xsd";

    protected static final String ADNAMESPACE = "spc.gaius.design.schema/ad.xsd";

    protected static final String ROOTTYPE = "LdapRootType";

    protected static final String PERSON = "Person";

    protected static final String ORGANIZATIONALPERSON = "OrganizationalPerson";

    protected static final String INETORGPERSON = "InetOrgPerson";

    protected static final String GROUPOFNAMES = "OrganizationalRole";

    protected static final String ADGROUP = "Group";

    protected static final String ADUSER = "User";

    protected static final String MANY = "s";

    protected static final String DN = "dn";

    protected static final String CN = "cn";

    protected static final String O = "o";

    protected static final String OU = "ou";

    protected static final String DESCRIPTION = "description";

    protected static final String SN = "sn";

    protected static final String GIVENNAME = "givenName";

    protected static final String MEMBER = "roleOccupant";

    protected static final String ADMEMBER = "member";

    protected static final String ACCOUNTNAME = "sAMAccountName";

    protected static final String DISPLAYNAME = "displayName";

    protected static final String UNICODEPWD = "unicodePwd";

    protected static final String USERPASSWORD = "userPassword";

    protected static final String ACCOUNTCONTROL = "userAccountControl";

    protected static final int UF_ACCOUNTDISABLE = 0x0002;

    protected static final int UF_PASSWD_NOTREQD = 0x0020;

    protected static final int UF_PASSWD_CANT_CHANGE = 0x0040;

    protected static final int UF_NORMAL_ACCOUNT = 0x0200;

    protected static final int UF_DONT_EXPIRE_PASSWD = 0x10000;

    protected static final int UF_PASSWORD_EXPIRED = 0x800000;

    protected DataObject rootADMDG = null;

    protected DataObject rootNEAR = null;

    protected boolean run = true;

    protected void setUp() {
        super.setUp();
        try {
            DirContext ctx = doLogin();
            if (ctx == null) run = false;
        } catch (Throwable e) {
            log.error("Error on system login test", e);
            run = false;
        }
    }

    protected void parseSDOtypes() {
        SDOMetaDataFactory mdf = (SDOMetaDataFactory) emf.getConfiguration().getMetaDataRepositoryInstance().getMetaDataFactory();
        List<String> lr = new ArrayList<String>();
        lr.addAll(mdf.getResourceList());
        for (String s : lr) {
            List<Type> lt = SDOTypeParser.getParsedTypes(s);
            if (lt == null) {
                mdf.getPersistentTypeNames(false, null);
                lt = SDOTypeParser.getParsedTypes(s);
            }
        }
    }

    protected void deleteObjects() {
        SDOMetaDataFactory mdf = (SDOMetaDataFactory) emf.getConfiguration().getMetaDataRepositoryInstance().getMetaDataFactory();
        List<String> lr = new ArrayList<String>();
        lr.addAll(mdf.getResourceList());
        for (String s : lr) {
            List<Type> lt = SDOTypeParser.getParsedTypes(s);
            if (lt == null) {
                mdf.getPersistentTypeNames(false, null);
                lt = SDOTypeParser.getParsedTypes(s);
            }
            for (Type t : lt) {
                if (!t.isAbstract()) {
                    delete(t.getName());
                }
            }
        }
    }

    protected void deleteRootObjects(DataObject obj) {
        SDOEntityManager em = (SDOEntityManager) emf.createEntityManager();
        DataObject src = obj.getRootObject();
        List<Property> lp = src.getInstanceProperties();
        for (Property p : lp) {
            if (!p.getType().isDataType() && src.isSet(p)) {
                if (p.isMany()) {
                    List<DataObject> ld = src.getList(p);
                    ArrayList<DataObject> ldb = new ArrayList<DataObject>();
                    for (DataObject d : ld) {
                        DataObject o = em.find(p.getType(), d.get(ImplHelper.getIdentityProperty(d.getType())));
                        if (o != null) {
                            em.getTransaction().begin();
                            em.remove(o);
                            em.getTransaction().commit();
                        }
                    }
                } else {
                    DataObject d = src.getDataObject(p);
                    DataObject o = em.find(p.getType(), d.get(ImplHelper.getIdentityProperty(d.getType())));
                    if (o != null) {
                        em.getTransaction().begin();
                        em.remove(o);
                        em.getTransaction().commit();
                    }
                }
            }
        }
    }

    protected void setList(DataObject obj, String property, Object val) {
        setList(obj, property, val, true);
    }

    protected void setList(DataObject obj, String property, Object val, boolean clear) {
        if (obj.getType().getProperty(property) != null) {
            Object o = obj.get(property);
            if (o instanceof List) {
                List l = obj.getList(property);
                if (clear) {
                    l.clear();
                }
                l.add(val);
            } else {
                obj.set(property, val);
            }
        }
    }

    protected void setDN(DataObject obj, String o, String cn) {
        setList(obj, CN, cn, true);
        setList(obj, O, o, true);
        obj.set(DN, CN + "=" + cn + "," + O + "=" + o);
    }

    protected void setDN(DataObject obj, String o, String ou, String cn) {
        setList(obj, CN, cn, true);
        setList(obj, O, o, true);
        setList(obj, OU, ou, true);
        obj.set(DN, CN + "=" + cn + "," + OU + "=" + ou + "," + O + "=" + o);
    }

    protected void setDN(DataObject obj, String[] o, String[] ou, String[] cn) {
        String dn = "";
        if (cn != null && cn.length != 0) {
            for (int i = 0; i < cn.length; i++) {
                boolean clear = i == 0 ? true : false;
                setList(obj, CN, cn[i], clear);
                if (!dn.equals("")) {
                    dn = dn + ",";
                }
                dn = dn + CN + "=" + cn[i];
            }
        }
        if (ou != null && ou.length != 0) {
            for (int i = 0; i < ou.length; i++) {
                boolean clear = i == 0 ? true : false;
                setList(obj, OU, ou[i], clear);
                if (!dn.equals("")) {
                    dn = dn + ",";
                }
                dn = dn + OU + "=" + ou[i];
            }
        }
        if (o != null && o.length != 0) {
            for (int i = 0; i < o.length; i++) {
                boolean clear = i == 0 ? true : false;
                setList(obj, O, o[i], clear);
                if (!dn.equals("")) {
                    dn = dn + ",";
                }
                dn = dn + O + "=" + o[i];
            }
        }
        obj.set(DN, dn);
    }

    protected void setPersonNames(DataObject obj) {
        Object o = obj.get(CN);
        String cn;
        if (o instanceof List) {
            List l = (List) o;
            cn = (String) l.get(0);
        } else cn = o.toString();
        String sn = cn.substring(cn.indexOf(' ') + 1);
        String gn = cn.substring(0, cn.indexOf(' '));
        setList(obj, SN, sn, true);
        setList(obj, GIVENNAME, gn, true);
        if (obj.getType().getProperty(ACCOUNTNAME) != null) setList(obj, ACCOUNTNAME, gn + sn);
    }

    protected void setDN(DataObject obj, Map<String, List<String>> m) {
        String dn = "";
        for (String a : m.keySet()) {
            List<String> l = m.get(a);
            setList(obj, a, l.get(l.size() - 1));
            for (String v : m.get(a)) {
                if (!dn.equals("")) {
                    dn = dn + ",";
                }
                dn = dn + a + "=" + v;
            }
        }
        obj.set(DN, dn);
    }

    protected void createSystemsRootAD() {
        String password = "Password1!";
        DataGraph dg = SDOUtil.createDataGraph();
        rootADMDG = dg.createRootObject(ImplHelper.getRootType(ADNAMESPACE));
        DataObject rMDG = rootADMDG.createDataObject(ADGROUP + MANY);
        setDN(rMDG, null, null, new String[] { "Ministro" });
        setList(rMDG, DESCRIPTION, "Ministro della Giustizia governo Prodi");
        setList(rMDG, ACCOUNTNAME, "Ministro");
        DataObject rMPDM = rootADMDG.createDataObject(ADGROUP + MANY);
        setDN(rMPDM, null, null, new String[] { "Procuratore" });
        setList(rMPDM, DESCRIPTION, "Magistrato Procura della Repubblica");
        setList(rMPDM, ACCOUNTNAME, "Procuratore");
        DataObject uCM = rootADMDG.createDataObject(ADUSER + MANY);
        setDN(uCM, null, null, new String[] { "Clemente Mastella" });
        setList(uCM, DESCRIPTION, "In quota UDEUR");
        setPersonNames(uCM);
        uCM.setString(UNICODEPWD, password);
        uCM.setString(ACCOUNTCONTROL, Integer.toString(UF_NORMAL_ACCOUNT));
        rMDG.getList(ADMEMBER).add(uCM);
        DataObject uCF = rootADMDG.createDataObject(ADUSER + MANY);
        setDN(uCF, null, null, new String[] { "Clementina Forleo" });
        setList(uCF, DESCRIPTION, "Clementina Forleo");
        setPersonNames(uCF);
        uCF.setString(UNICODEPWD, password);
        uCF.setString(ACCOUNTCONTROL, Integer.toString(UF_NORMAL_ACCOUNT));
        rMPDM.getList(ADMEMBER).add(uCF);
        DataObject uGP = rootADMDG.createDataObject(ADUSER + MANY);
        setDN(uGP, null, null, new String[] { "Graziano Prelati" });
        setList(uGP, DESCRIPTION, "Il gr�");
        setPersonNames(uGP);
        uGP.setString(UNICODEPWD, password);
        uGP.setString(ACCOUNTCONTROL, Integer.toString(UF_NORMAL_ACCOUNT));
        rMPDM.getList(ADMEMBER).add(uGP);
    }

    protected void createSystemsRootLDAP() {
        DataGraph dg = SDOUtil.createDataGraph();
        dg = SDOUtil.createDataGraph();
        rootNEAR = dg.createRootObject(ImplHelper.getRootType(LDAPNAMESPACE));
        DataObject rRPESA = rootNEAR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rRPESA, null, null, new String[] { "Responsabile Progettazione e Sviluppo" });
        setList(rRPESA, DESCRIPTION, "Responsabile Progettazione e Sviluppo Actalis");
        DataObject rDAR = rootNEAR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rDAR, null, null, new String[] { "Dipendente Actalis Roma" });
        setList(rDAR, DESCRIPTION, "Dipendente Actalis Roma");
        DataObject rDA = rootNEAR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rDA, null, null, new String[] { "Dipendente Almaviva" });
        setList(rDA, DESCRIPTION, "Dipendente Almaviva");
        DataObject rRC = rootNEAR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rRC, null, null, new String[] { "Responsabile CCASI" });
        setList(rRC, DESCRIPTION, "Responsabile CCASI");
        DataObject uAM = rootNEAR.createDataObject(INETORGPERSON + MANY);
        setDN(uAM, null, null, new String[] { "Andrea Mercurio" });
        setList(uAM, DESCRIPTION, "Andrea Mercurio");
        setList(uAM, "userPassword", "Password1!");
        setPersonNames(uAM);
        rDA.getList(MEMBER).add(uAM);
        rRC.getList(MEMBER).add(uAM);
        DataObject uLM = rootNEAR.createDataObject(INETORGPERSON + MANY);
        setDN(uLM, null, null, new String[] { "Luciano Montebove" });
        setList(uLM, DESCRIPTION, "Luciano Montebove");
        setList(uLM, "userPassword", "Password1!");
        setPersonNames(uLM);
        rDA.getList(MEMBER).add(uLM);
        DataObject uST = rootNEAR.createDataObject(INETORGPERSON + MANY);
        setDN(uST, null, null, new String[] { "Sergio Tabanelli" });
        setList(uST, DESCRIPTION, "Sergio Tabanelli");
        setList(uST, "userPassword", "Password1!");
        setPersonNames(uST);
        rDAR.getList(MEMBER).add(uST);
        rRPESA.getList(MEMBER).add(uST);
    }

    protected DataObject createRootTR() {
        DataGraph dg = SDOUtil.createDataGraph();
        DataObject rootTR = dg.createRootObject(ImplHelper.getRootType(LDAPNAMESPACE));
        DataObject rRPESA = rootTR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rRPESA, null, null, new String[] { "Responsabile Progettazione e Sviluppo" });
        DataObject rDAR = rootTR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rDAR, null, null, new String[] { "Dipendente Actalis Roma" });
        DataObject rDA = rootTR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rDA, null, null, new String[] { "Dipendente Almaviva" });
        DataObject rRC = rootTR.createDataObject(GROUPOFNAMES + MANY);
        setDN(rRC, null, null, new String[] { "Responsabile CCASI" });
        DataObject uAM = rootTR.createDataObject(INETORGPERSON + MANY);
        setDN(uAM, null, null, new String[] { "Andrea Mercurio " });
        setList(uAM, O, "Gruppo Almaviva");
        setList(uAM, DESCRIPTION, "Andrea Mercurio");
        setList(uAM, "userPassword", "Password1!");
        setPersonNames(uAM);
        rDA.getList(MEMBER).add(uAM);
        rRC.getList(MEMBER).add(uAM);
        DataObject uLM = rootTR.createDataObject(INETORGPERSON + MANY);
        setDN(uLM, null, null, new String[] { "Luciano Montebove " });
        setList(uLM, O, "Gruppo Almaviva");
        setList(uLM, DESCRIPTION, "Luciano Montebove");
        setList(uLM, "userPassword", "Password1!");
        setPersonNames(uLM);
        rDA.getList(MEMBER).add(uLM);
        DataObject uST = rootTR.createDataObject(INETORGPERSON + MANY);
        setDN(uST, null, null, new String[] { "Sergio Tabanelli " });
        setList(uAM, O, "Actalis");
        setList(uST, DESCRIPTION, "Sergio Tabanelli");
        setList(uST, "userPassword", "Password1!");
        setPersonNames(uST);
        rDAR.getList(MEMBER).add(uST);
        rRPESA.getList(MEMBER).add(uST);
        return rootTR;
    }

    protected DirContext doLogin() {
        LdapConfiguration ldc = (LdapConfiguration) emf.getConfiguration();
        String bindDn = ldc.getConnectionUserName();
        String bindPass = ldc.getConnectionPassword();
        DirContext ctx = doLogin(emf, bindDn, bindPass, false);
        return ctx;
    }

    protected DirContext doLogin(SDOEntityManagerFactory emf, String bindDn, String bindPass) {
        return doLogin(emf, bindDn, bindPass, true);
    }

    protected DirContext doLogin(SDOEntityManagerFactory emf, String bindDn, String bindPass, boolean addbase) {
        String url;
        boolean ssl;
        LdapConfiguration ldc = (LdapConfiguration) emf.getConfiguration();
        LdapHandler ldh = (LdapHandler) ldc.getHandler();
        if (addbase) bindDn = ldh.addBaseDN(bindDn);
        url = ldc.getConnectionURL();
        ssl = ldc.getUseSSL();
        Hashtable env = new Hashtable(5);
        if (bindDn == null || bindPass == null || bindDn.length() == 0 || bindPass.length() == 0) {
            env.put("java.naming.security.authentication", "none");
        } else {
            env.put("java.naming.security.authentication", "simple");
            env.put("java.naming.security.principal", bindDn);
            env.put("java.naming.security.credentials", bindPass);
        }
        if (ssl) {
            env.put("java.naming.security.protocol", "ssl");
        }
        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("java.naming.provider.url", url);
        env.put("java.naming.referral", "follow");
        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            if (log.isTraceEnabled()) log.error("", e);
            ctx = null;
        }
        return ctx;
    }
}
