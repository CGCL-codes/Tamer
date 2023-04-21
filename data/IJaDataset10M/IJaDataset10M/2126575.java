package org.juddi.datatype.request;

import java.util.Vector;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.service.BusinessService;

/**
 * "Used to register or update complete information about a businessService
 *  exposed by a specified businessEntity."
 *
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
public class SaveService implements RegistryObject, Publish {

    String generic;

    AuthInfo authInfo;

    Vector serviceVector;

    /**
   *
   */
    public SaveService() {
    }

    /**
   *
   */
    public SaveService(AuthInfo info, BusinessService service) {
        this.authInfo = info;
        addBusinessService(service);
    }

    /**
   *
   */
    public SaveService(AuthInfo info, Vector services) {
        this.authInfo = info;
        this.serviceVector = services;
    }

    /**
   *
   * @param genericValue
   */
    public void setGeneric(String genericValue) {
        this.generic = genericValue;
    }

    /**
   *
   * @return String UDDI request's generic value.
   */
    public String getGeneric() {
        return this.generic;
    }

    /**
   *
   */
    public void setAuthInfo(AuthInfo info) {
        this.authInfo = info;
    }

    /**
   *
   */
    public AuthInfo getAuthInfo() {
        return this.authInfo;
    }

    /**
   *
   */
    public void addBusinessService(BusinessService businessService) {
        if (this.serviceVector == null) this.serviceVector = new Vector();
        this.serviceVector.add(businessService);
    }

    /**
   *
   */
    public void setServiceVector(Vector services) {
        this.serviceVector = services;
    }

    /**
   *
   */
    public Vector getBusinessServiceVector() {
        return this.serviceVector;
    }
}
