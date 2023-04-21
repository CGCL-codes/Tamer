package org.wynnit.minows;

import javax.ejb.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.codec.binary.*;
import java.sql.Timestamp;
import java.math.*;
import org.jruby.util.ByteList;
import org.wynnit.minows.minowutils.*;

/**
 * This is the bean class for the CodeSupportBean enterprise bean.
 * Created May 4, 2007 2:54:26 PM
 * @author steve
 */
public class CodeSupportBean implements SessionBean, CodeSupportLocalBusiness {

    private SessionContext context;

    private CodeStoreLocalHome cslh;

    private ActionStoreLocalHome aslh;

    private EventStoreLocalHome eslh;

    private ActionListLocalHome allh;

    private TicketUtilsLocal tul;

    private static final Integer goodoutcome = new Integer(1);

    private static final Integer badoutcome = new Integer(2);

    private static final Integer erroroutcome = new Integer(-1);

    /**
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext aContext) {
        context = aContext;
    }

    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() {
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() {
    }

    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() {
    }

    /**
     * See section 7.10.3 of the EJB 2.0 specification
     * See section 7.11.3 of the EJB 2.1 specification
     */
    public void ejbCreate() {
        cslh = lookupCodeStoreBean();
        aslh = lookupActionStoreBean();
        eslh = lookupEventStoreBean();
        allh = lookupActionListBean();
        tul = lookupTicketUtilsBean();
    }

    public void Channel(Integer command, String actionid) throws FinderException {
        ActionStoreLocal actsl = aslh.findByPrimaryKey(new Long(actionid));
        switch(command.intValue()) {
            case 54:
                actsl.setReturnString(CodeVersion(actsl));
                break;
            case 29:
                actsl.setReturnString(CodeMethodLoad(actsl));
                break;
            case 55:
                actsl.setReturnString(MethodVersion(actsl));
                break;
            case 25:
                actsl.setReturnString(ClassList(actsl));
                break;
            case 46:
                actsl.setReturnString(InjectClass(actsl));
                break;
            case 47:
                actsl.setReturnString(ClassLoad(actsl));
                break;
            case 68:
                actsl.setReturnString(CommandList(actsl));
                break;
            case 93:
                actsl.setReturnString(MethodList(actsl));
                break;
        }
    }

    public String ClassList(ActionStoreLocal asl) {
        CodeStoreLocal csl;
        String classList = "";
        HashMap main = new HashMap();
        ArrayList unique = new ArrayList();
        Character c = new Character('"');
        long lastkey = 0;
        long tm = 0;
        try {
            Collection list = cslh.findByActive(new Integer(1));
            Iterator p = list.iterator();
            while (p.hasNext()) {
                csl = (CodeStoreLocal) p.next();
                if (!unique.contains(csl.getClassname())) {
                    HashMap hm = new HashMap();
                    tm = System.currentTimeMillis();
                    while (System.currentTimeMillis() == lastkey) ;
                    tm = System.currentTimeMillis();
                    String key = "K" + new Long(tm).toString();
                    lastkey = tm;
                    System.out.println("ClassList " + key);
                    hm.put("id", c.toString() + csl.getId().toString() + c.toString());
                    hm.put("classname", csl.getClassname());
                    hm.put("methodname", csl.getMethodname());
                    hm.put("category", csl.getCategory());
                    hm.put("parent", c.toString() + csl.getParent().toString() + c.toString());
                    hm.put("descr", csl.getDescription());
                    main.put(key, hm);
                    unique.add(csl.getClassname());
                }
            }
            System.out.println("ClassList size " + String.valueOf(main.size()));
            if (!main.isEmpty()) classList = MinowUtils.Pack64(main);
        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage());
            asl.setOutcome(badoutcome);
            return null;
        }
        System.out.println("ClassList is " + classList);
        asl.setOutcome(goodoutcome);
        return classList;
    }

    public String InjectClass(ActionStoreLocal asl) {
        String result = null;
        Base64 b64 = new Base64();
        Object obj = null;
        HashMap args = null;
        try {
            System.out.println("InjectClass = " + asl.getId().toString());
            if (asl.getTicketActionindex().longValue() > 0) args = tul.ReadActionParameters(asl); else args = tul.PrepareArgs(null, asl);
            String newtpbody = null;
            String descr = null;
            String cat = null;
            String name = null;
            String parent = null;
            obj = MinowUtils.RetrieveArg(args, "code");
            if (obj != null) newtpbody = obj.toString();
            obj = MinowUtils.RetrieveArg(args, "description");
            if (obj != null) descr = obj.toString(); else descr = "none";
            obj = MinowUtils.RetrieveArg(args, "category");
            if (obj != null) cat = obj.toString();
            obj = MinowUtils.RetrieveArg(args, "name");
            if (obj != null) name = obj.toString();
            obj = MinowUtils.RetrieveArg(args, "parent");
            if (obj != null) parent = obj.toString(); else parent = "0";
            if (newtpbody == null || cat == null || name == null) {
                System.out.println("Code,Name or category null");
                asl.setOutcome(badoutcome);
                return result;
            }
            Long uniqueid = new Long(System.currentTimeMillis());
            Timestamp ts = new Timestamp(new Date().getTime());
            System.out.println("InjectClass code for " + newtpbody);
            try {
                System.out.println("hello");
                Collection list = cslh.findByNameActive(name);
                Iterator it = list.iterator();
                if (it.hasNext()) {
                    CodeStoreLocal csl = (CodeStoreLocal) it.next();
                    System.out.println("InjectClass " + csl.getVersion().toString() + "," + csl.getActive().toString());
                    System.out.println("InjectClass updating existing class");
                    csl.setActive(new Integer(0));
                    Integer version = new Integer(csl.getVersion().intValue() + 1);
                    cslh.create(uniqueid, name, "", newtpbody, descr, cat, new Integer(1), version, ts, new Long(parent));
                } else {
                    System.out.println("InjectClass new record");
                    cslh.create(uniqueid, name, "", newtpbody, descr, cat, new Integer(1), new Integer(0), ts, new Long(parent));
                }
            } catch (Exception ex) {
                System.out.println("InjectClass update error: " + ex.getMessage());
                asl.setOutcome(badoutcome);
                return null;
            }
            asl.setReturnString(MinowUtils.Pack64(uniqueid));
            asl.setOutcome(goodoutcome);
            System.out.println("InjectClass end: " + asl.getStatus().toString());
        } catch (Exception ex) {
            System.out.println("InjectClass error: " + ex.getMessage());
            asl.setOutcome(badoutcome);
        }
        return result;
    }

    public String ClassLoad(ActionStoreLocal asl) {
        String result = null;
        Base64 b64 = new Base64();
        try {
            System.out.println("ClassLoad");
            HashMap hm = tul.PrepareArgs(null, asl);
            Collection list = cslh.findByNameActiveClass(hm.get("classname").toString(), "S");
            if (list.iterator().hasNext()) {
                System.out.println("ClassLoad found = " + hm.get("classname").toString());
                CodeStoreLocal csl = (CodeStoreLocal) list.iterator().next();
                result = new String(b64.encode(csl.getClasscode().getBytes()));
            }
            asl.setOutcome(goodoutcome);
        } catch (Exception ex) {
            System.out.println("ClassLoad error: " + ex.getMessage());
            asl.setOutcome(badoutcome);
        }
        return result;
    }

    private org.wynnit.minows.ActionStoreLocalHome lookupActionStoreBean() {
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            org.wynnit.minows.ActionStoreLocalHome rv = (org.wynnit.minows.ActionStoreLocalHome) c.lookup("java:comp/env/ejb/ActionStoreBean");
            return rv;
        } catch (javax.naming.NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public String CodeVersion(ActionStoreLocal asl) throws FinderException {
        String result = null;
        System.out.println("Name= " + asl.getArgs());
        try {
            HashMap hm = tul.PrepareArgs(null, asl);
            Collection ver = cslh.findByNameActiveClass(hm.get("class").toString(), hm.get("category").toString());
            if (ver.iterator().hasNext()) {
                CodeStoreLocal csl = (CodeStoreLocal) ver.iterator().next();
                System.out.println("Version " + csl.getVersion().toString());
                result = MinowUtils.Pack64(csl.getVersion().toString());
            }
            asl.setOutcome(goodoutcome);
        } catch (Exception ex) {
            System.out.println("CodeVersion error: " + ex.getMessage());
            asl.setOutcome(badoutcome);
        }
        return result;
    }

    private org.wynnit.minows.CodeStoreLocalHome lookupCodeStoreBean() {
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            org.wynnit.minows.CodeStoreLocalHome rv = (org.wynnit.minows.CodeStoreLocalHome) c.lookup("java:comp/env/ejb/CodeStoreBean");
            return rv;
        } catch (javax.naming.NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public String CodeMethodLoad(ActionStoreLocal asl) {
        String result = null;
        HashMap args = null;
        HashMap code = new HashMap();
        try {
            System.out.println("CodeMethodLoad");
            if (asl.getTicketActionindex().longValue() > 0) args = tul.ReadActionParameters(asl); else args = tul.PrepareArgs(null, asl);
            String methodname = null;
            String classname = null;
            if (args.containsKey(ByteList.create("methodname"))) methodname = args.get(ByteList.create("methodname")).toString();
            if (args.containsKey(ByteList.create("classname"))) classname = args.get(ByteList.create("classname")).toString();
            System.out.println("CodeMethodLoad name = " + classname + "," + methodname);
            Collection list = cslh.findByNameActiveMethod(classname, methodname);
            if (list.iterator().hasNext()) {
                System.out.println("CodeMethodLoad found = " + methodname);
                CodeStoreLocal csl = (CodeStoreLocal) list.iterator().next();
                code.put("code", MinowUtils.Pack64(csl.getClasscode()));
                code.put("version", csl.getVersion().toString());
                result = MinowUtils.Pack64(code);
            }
            asl.setOutcome(goodoutcome);
        } catch (Exception ex) {
            System.out.println("CodeMethodLoad error: " + ex.getMessage());
            asl.setOutcomeMessage(ex.getMessage());
            ex.printStackTrace();
            asl.setOutcome(badoutcome);
        }
        return result;
    }

    public String MethodVersion(ActionStoreLocal asl) throws FinderException {
        System.out.println("Name= " + asl.getArgs());
        String methodname = null;
        String classname = null;
        try {
            HashMap hm = tul.PrepareArgs(null, asl);
            if (hm.containsKey("methodname")) methodname = hm.get("methodname").toString();
            if (hm.containsKey("classname")) classname = hm.get("classname").toString();
            Collection ver = cslh.findByNameActiveMethod(classname, methodname);
            if (ver.iterator().hasNext()) {
                CodeStoreLocal csl = (CodeStoreLocal) ver.iterator().next();
                System.out.println("Version " + csl.getVersion().toString());
                asl.setOutcome(goodoutcome);
                return MinowUtils.Pack64(csl.getVersion().toString());
            } else {
                asl.setOutcome(badoutcome);
                return null;
            }
        } catch (Exception ex) {
            System.out.println("MethodLoad error: " + ex.getMessage());
            asl.setOutcome(badoutcome);
            return null;
        }
    }

    private org.wynnit.minows.EventStoreLocalHome lookupEventStoreBean() {
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            org.wynnit.minows.EventStoreLocalHome rv = (org.wynnit.minows.EventStoreLocalHome) c.lookup("java:comp/env/ejb/EventStoreBean");
            return rv;
        } catch (javax.naming.NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public String CommandList(ActionStoreLocal asl) {
        String result = null;
        HashMap main = new HashMap();
        int count = 0;
        try {
            Collection list = allh.findByActive(new Integer(1));
            Iterator p = list.iterator();
            while (p.hasNext()) {
                ActionListLocal all = (ActionListLocal) p.next();
                HashMap hm = new HashMap();
                String key = "K" + String.valueOf(count++);
                System.out.println("ActionList " + key);
                String name = all.getActionname();
                String comment = all.getComment();
                String arghelp = all.getArghelp();
                if (name != null) {
                    hm.put("name", name);
                    if (comment != null) hm.put("comment", comment); else hm.put("comment", "");
                    if (arghelp != null) hm.put("arghelp", arghelp); else hm.put("arghelp", "");
                }
                if (count > 50) break;
                main.put(key, hm);
            }
            System.out.println("ActionList size " + String.valueOf(main.size()));
            if (!main.isEmpty()) result = MinowUtils.Pack64(main);
            asl.setOutcome(goodoutcome);
        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage());
            asl.setOutcomeMessage(ex.getMessage());
            asl.setOutcome(badoutcome);
            return null;
        }
        return result;
    }

    private org.wynnit.minows.ActionListLocalHome lookupActionListBean() {
        try {
            javax.naming.Context c = new javax.naming.InitialContext();
            org.wynnit.minows.ActionListLocalHome rv = (org.wynnit.minows.ActionListLocalHome) c.lookup("java:comp/env/ejb/ActionListBean");
            return rv;
        } catch (javax.naming.NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private TicketUtilsLocal lookupTicketUtilsBean() {
        try {
            Context c = new InitialContext();
            TicketUtilsLocalHome rv = (TicketUtilsLocalHome) c.lookup("java:comp/env/TicketUtilsBean");
            return rv.create();
        } catch (NamingException ne) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        } catch (CreateException ce) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ce);
            throw new RuntimeException(ce);
        }
    }

    public String MethodList(ActionStoreLocal asl) {
        String result = null;
        HashMap args = null;
        HashMap main = new HashMap();
        Character c = new Character('"');
        long lastkey = 0;
        long tm = 0;
        try {
            if (asl.getTicketActionindex().longValue() > 0) args = tul.ReadActionParameters(asl); else args = tul.PrepareArgs(null, asl);
            if (args.containsKey(ByteList.create("classname"))) {
                String classname = args.get(ByteList.create("classname")).toString();
                Collection list = cslh.findByClassname(classname);
                for (Iterator p = list.iterator(); p.hasNext(); ) {
                    CodeStoreLocal csl = (CodeStoreLocal) p.next();
                    HashMap hm = new HashMap();
                    tm = System.currentTimeMillis();
                    while (System.currentTimeMillis() == lastkey) ;
                    tm = System.currentTimeMillis();
                    String key = "K" + new Long(tm).toString();
                    lastkey = tm;
                    System.out.println("MethodCodeList " + key);
                    hm.put("methodname", c.toString() + csl.getMethodname() + c.toString());
                    hm.put("version", c.toString() + csl.getVersion().toString() + c.toString());
                    main.put(key, hm);
                }
            }
            System.out.println("MethodCodeList size " + String.valueOf(main.size()));
            if (!main.isEmpty()) result = MinowUtils.Pack64(main);
            asl.setOutcome(goodoutcome);
        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage());
            asl.setOutcome(badoutcome);
        }
        System.out.println("MethodCodeList done");
        return result;
    }
}
