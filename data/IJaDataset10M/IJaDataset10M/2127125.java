package spc.gaius.actalis.rest;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.persistence.EntityManagerFactory;
import org.apache.openjpa.sdo.SDOEntityManager;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import spc.gaius.actalis.registry.DasFactory;
import spc.gaius.actalis.util.Constants;
import spc.gaius.actalis.util.GaiusUtil;
import spc.gaius.actalis.util.InvalidDataException;
import commonj.sdo.DataObject;
import commonj.sdo.Type;
import commonj.sdo.helper.TypeHelper;
import commonj.sdo.helper.XMLHelper;

public abstract class AbstractValidator implements RestValidator {

    protected String requestor;

    protected String typename;

    protected Type type;

    protected SDOEntityManager em;

    private boolean validated = false;

    public AbstractValidator() {
        super();
        EntityManagerFactory emf = DasFactory.INSTANCE.getDASJPA();
        em = (SDOEntityManager) emf.createEntityManager();
    }

    public void validate() {
        checkData();
        validated = true;
    }

    /**
     * @param validated
     *            <code>true</code> se gia' validato
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    /**
     * @return the validated
     */
    public boolean isValidated() {
        return validated;
    }

    protected abstract void checkAction(Object n, Object o);

    protected void checkData() {
        if (typename == null) {
            throw new InvalidDataException("Invalid Typename");
        }
        GaiusUtil.parseSDOtypes();
        if ((type = TypeHelper.INSTANCE.getType(Constants.GAIUS_NAMESPACE, typename)) == null) {
            throw new InvalidDataException("Invalid Typename: " + typename);
        }
    }

    /**
     * @return the typename
     */
    public String getTypename() {
        return typename;
    }

    /**
     * @param typename
     *            the typename to set
     */
    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public SDOEntityManager getEm() {
        return em;
    }

    public void setEm(SDOEntityManager em) {
        this.em = em;
    }
}
