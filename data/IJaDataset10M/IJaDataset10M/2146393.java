package spc.gaius.actalis.transformers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.tuscany.sdo.api.SDOUtil;
import spc.gaius.actalis.registry.ModelFactory;
import spc.gaius.actalis.systems.AbstractSystemsTest;
import spc.gaius.actalis.systems.TestApplyChanges;
import spc.gaius.actalis.test.AbstractSDOTest;
import commonj.sdo.DataGraph;
import commonj.sdo.DataObject;
import commonj.sdo.Type;
import commonj.sdo.helper.DataFactory;
import commonj.sdo.helper.EqualityHelper;
import commonj.sdo.helper.HelperContext;
import commonj.sdo.helper.XMLDocument;
import commonj.sdo.helper.XMLHelper;
import commonj.sdo.helper.XSDHelper;
import commonj.sdo.impl.HelperProvider;
import spc.gaius.actalis.util.Constants;
import spc.gaius.actalis.util.GaiusUtil;
import spc.gaius.actalis.util.TransfException;

public class TestXSLTTransformer extends AbstractSystemsTest {

    private static final Logger log = Logger.getLogger("spc.gaius.actalis.test");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    protected void setUp() {
        setPersistenceUnitName("allxsd");
        super.setUp();
        parseSDOtypes();
    }

    public void testDoTransformAD() {
        create();
        DataObject tsd = dataGraph.getRootObject().getDataObject(Constants.TARGETSYSTEM_TYPE + MANY + "[" + Constants.ID_PROPERTY + "=" + "tADMDG" + "]");
        createSystemsRootAD();
        DataObject u = (DataObject) dataGraph.getRootObject().get("GaiusUsers[Oid=uCM]");
        GaiusUtil.populateNewRootObject(u);
        Transformer tr = ModelFactory.INSTANCE.createTransformer(tsd, Constants.USER_TYPE);
        DataObject target = tr.transform(u);
        assertNotNull(target);
        GaiusUtil.unsetProperty(target.getRootObject(), Constants.ID_PROPERTY);
        DataObject ur = (DataObject) rootADMDG.get("Groups[dn=\"cn=Ministro\"]");
        assertNotNull(ur);
        GaiusUtil.populateNewRootObject(ur);
        if (log.isTraceEnabled()) {
            try {
                baos.reset();
                SDOUtil.saveDataGraph(u.getDataGraph(), baos, null);
                log.trace(baos.toString());
                baos.reset();
                SDOUtil.saveDataGraph(target.getRootObject().getDataGraph(), baos, null);
                log.trace(baos.toString());
                baos.reset();
                SDOUtil.saveDataGraph(ur.getDataGraph(), baos, null);
                log.trace(baos.toString());
            } catch (Exception e) {
                e.printStackTrace();
                throw new TransfException("Exception serializing transformed Data Object");
            }
        }
        boolean res = EqualityHelper.INSTANCE.equal(ur.getRootObject(), target.getRootObject());
        assertTrue(res);
    }

    public void testDoTransform() {
        create();
        DataObject tsd = dataGraph.getRootObject().getDataObject(Constants.TARGETSYSTEM_TYPE + MANY + "[" + Constants.ID_PROPERTY + "=" + "tNEAR" + "]");
        DataObject pr = dataGraph.getRootObject().getDataObject(Constants.PROFILE_TYPE + MANY + "[" + Constants.ID_PROPERTY + "=" + "pDA" + "]");
        DataObject ro = dataGraph.getRootObject().getDataObject(Constants.ROLE_TYPE + MANY + "[" + Constants.ID_PROPERTY + "=" + "rDA" + "]");
        ro.getList(Constants.PROFILE_TYPE + MANY).add(pr);
        tsd.getList(Constants.PROFILE_TYPE + MANY).add(pr);
        DataObject rootTR = createRootTR();
        GaiusUtil.populateNewRootObject(user);
        Transformer tr = ModelFactory.INSTANCE.createTransformer(tsd, Constants.USER_TYPE);
        DataObject target = tr.transform(user);
        assertNotNull(target);
        GaiusUtil.unsetProperty(target.getRootObject(), Constants.ID_PROPERTY);
        DataObject ur = (DataObject) rootTR.get(GROUPOFNAMES + Constants.MANY + "[dn=\"cn=Dipendente Almaviva\"]");
        assertNotNull(ur);
        ur.getList(MEMBER).remove(0);
        GaiusUtil.populateNewRootObject(ur);
        if (log.isTraceEnabled()) {
            try {
                baos.reset();
                SDOUtil.saveDataGraph(user.getDataGraph(), baos, null);
                log.trace(baos.toString());
                baos.reset();
                SDOUtil.saveDataGraph(target.getRootObject().getDataGraph(), baos, null);
                log.trace(baos.toString());
                baos.reset();
                SDOUtil.saveDataGraph(ur.getDataGraph(), baos, null);
                log.trace(baos.toString());
            } catch (Exception e) {
                e.printStackTrace();
                throw new TransfException("Exception serializing transformed Data Object");
            }
        }
        boolean res = EqualityHelper.INSTANCE.equal(ur.getRootObject(), target.getRootObject());
        assertTrue(res);
    }
}
