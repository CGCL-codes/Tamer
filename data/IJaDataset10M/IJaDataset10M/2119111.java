package spc.gaius.actalis.log;

import org.apache.openjpa.sdo.ImplHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import commonj.sdo.DataObject;
import commonj.sdo.helper.TypeHelper;
import commonj.sdo.helper.XMLHelper;
import spc.gaius.actalis.registry.Environ;
import spc.gaius.actalis.rest.GaiusGetValidator;
import spc.gaius.actalis.rest.GenericAction;
import spc.gaius.actalis.rest.AbstractValidator;
import spc.gaius.actalis.util.Constants;
import spc.gaius.actalis.util.GaiusUtil;

public class LogObjFactory {

    public static final LogObjFactory INSTANCE = init();

    private static LogObjFactory init() {
        return new LogObjFactory();
    }

    public LogObj makeLogObj(Signature s, AbstractValidator av, Object retval, boolean trace) {
        LogObj ret = makeLogObj(s, av, trace);
        String t3 = retVal2Text(retval, trace);
        ret.setText3(t3);
        return ret;
    }

    public LogObj makeLogObj(Signature s, AbstractValidator av, boolean trace) {
        LogObj ret = new LogObj();
        ret.setEventID(Environ.INSTANCE.get(s.toShortString()));
        ret.setOriginator(Thread.currentThread().getName() + ":" + av.getRequestor());
        ret.setTarget(av.getTypename());
        if (trace && (av instanceof GaiusGetValidator)) {
            ret.setText1(((GaiusGetValidator) av).getGquery());
        }
        return ret;
    }

    public LogObj makeLogObj(Signature s, GenericAction ac, DataObject d, boolean trace) {
        return makeLogObj(s, ac, d, null, trace);
    }

    public LogObj makeLogObj(Signature s, GenericAction ac, DataObject d, Object retval, boolean trace) {
        LogObj ret = makeLogObj(s, (AbstractValidator) ac.getValidator(), trace);
        String t = d.getType().getName() + ":" + d.getString(ImplHelper.getIdentityProperty(d.getType()));
        if (TypeHelper.INSTANCE.getType(Constants.GAIUS_NAMESPACE, Constants.NAMEDOBJECT_TYPE).isInstance(d)) {
            t = t + ":";
            if (d.isSet(Constants.NAME_PROPERTY)) {
                t = t + d.getString(Constants.NAME_PROPERTY);
            }
            if (GaiusUtil.isProvisionable(d) && d.isSet(Constants.SURNAME_PROPERTY)) {
                t = t + " " + d.getString(Constants.SURNAME_PROPERTY);
            }
        } else if (d.getType().getName().equals(Constants.PROVISIONINGACTION_TYPE)) {
            t = t + ":" + d.getString(Constants.POTRANSFORMED_ID);
        }
        ret.setTarget(t);
        if (trace) {
            String ss = XMLHelper.INSTANCE.save(d, null, Constants.GAIUSROOT_TYPE);
            ret.setText1(ss);
        }
        String t3 = retVal2Text(retval, trace);
        ret.setText3(t3);
        return ret;
    }

    private String retVal2Text(Object retval, boolean trace) {
        String ret;
        if (retval instanceof DataObject) {
            DataObject d = (DataObject) retval;
            ret = d.getType().getName() + ":" + d.getString(ImplHelper.getIdentityProperty(d.getType()));
        } else {
            ret = retval == null ? "null" : retval.toString();
        }
        return ret;
    }

    public LogObj makeLogObj(Signature s, DataObject pa, Object retval, boolean trace) {
        LogObj ret = makeLogObj(s, pa, trace);
        String t3 = retVal2Text(retval, trace);
        ret.setText3(t3);
        return ret;
    }

    public LogObj makeLogObj(Signature s, DataObject pa, boolean trace) {
        LogObj ret = new LogObj();
        ret.setEventID(Environ.INSTANCE.get(s.toShortString()));
        String t = pa.getString(Constants.ID_PROPERTY) + ":" + pa.getString(Constants.PROVACTIONTYPE_TYPE) + ":" + pa.getString(Constants.PROVISIONABLEOBJECTID_PROPERTY) + ":" + pa.getString(Constants.ORGANIZATIONID_PROPERTY) + ":" + pa.getString(Constants.PROVISIONABLEOBJECTTARGET_TYPE) + ":" + pa.getDataObject(Constants.TARGETSYSTEM_TYPE).getString(Constants.ID_PROPERTY) + ":" + pa.getString(Constants.POTRANSFORMED_ID);
        ret.setTarget(t);
        ret.setOriginator(Thread.currentThread().getName() + ":" + "GAIUS");
        if (trace) {
            ret.setText1(pa.getString(Constants.SERIALIZEDOBJECT_TYPE));
        }
        return ret;
    }

    public LogObj makeLogObj(JoinPoint jp, boolean trace) {
        LogObj ret = new LogObj();
        ret.setEventID(Environ.INSTANCE.get(jp.getSignature().toShortString()));
        String t = parameters2String(jp);
        ret.setTarget(t);
        ret.setOriginator(Thread.currentThread().getName() + ":" + "GAIUS");
        return ret;
    }

    public LogObj makeLogObj(JoinPoint jp, Object retval, boolean trace) {
        LogObj ret = makeLogObj(jp, trace);
        String t3 = retVal2Text(retval, trace);
        ret.setText3(t3);
        return ret;
    }

    private String parameters2String(JoinPoint jp) {
        Object[] args = jp.getArgs();
        String s = "";
        for (int i = 0; i < args.length; i++) {
            if (i > 0) s += ":";
            s += args[i];
        }
        return s;
    }
}
