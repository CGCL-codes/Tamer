package com.ekeymanlib.business;

import java.util.List;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import com.ekeymanlib.domain.User;
import com.ekeymanlib.dto.AppDeviceApiKeys;
import com.ekeymanlib.dto.AppDeviceGrid;
import com.ekeymanlib.dto.Application;
import com.ekeymanlib.dto.EncryptionKeys;
import com.ekeymanlib.dto.ResourceGrid;
import com.ekeymanlib.dto.SearchFilter;
import com.ekeymanlib.dto.VendorProfile;

public abstract class AbstractKeyManager implements InitializingBean, KeyManager {

    private ResourceBO resourceBO;

    private VendorBO vendorBO;

    private AppDeviceBO appDeviceBO;

    private UserBO userBO;

    protected User doGetUser(String openidurl) {
        return getUserBO().getUser(openidurl);
    }

    protected void doRegisterUser(String apiKey, String openidurl) {
        getUserBO().registerUser(apiKey, openidurl);
    }

    protected EncryptionKeys doCreateEncryptionKeys(String resourceUri, String publicKey, int keyBytesLength, int ivBytesLength, int saltBytesLength, int iterationCountMax, String location, String digitalSignature) {
        return getResourceBO().createResource(resourceUri, publicKey, keyBytesLength, ivBytesLength, saltBytesLength, iterationCountMax, location, digitalSignature);
    }

    protected EncryptionKeys doGetEncryptionKeys(String resourceUri, String publicKey, String location, String digitalSignature) {
        return getResourceBO().getResource(resourceUri, publicKey, location, digitalSignature);
    }

    protected void doDeleteEncryptionKeys(String resourceUri, String publicKey, String location, String digitalSignature) {
        getResourceBO().deleteResource(resourceUri, publicKey, location, digitalSignature);
    }

    protected void doDeleteResourceById(long id, String apiKey) {
        getResourceBO().deleteResourceById(id, apiKey);
    }

    protected void doDeleteAppDeviceById(long id, String apiKey) {
        getAppDeviceBO().deleteAppDeviceById(id, apiKey);
    }

    protected String doRegisterVendor(String name, String address, String city, String state, String zip, String phone, String emailAddress) {
        return getVendorBO().registerVendor(name, address, city, state, zip, phone, emailAddress);
    }

    protected AppDeviceApiKeys doCreateAppDevice(String username, String application, String device) {
        return getAppDeviceBO().createAppDevice(username, application, device);
    }

    protected long doGetAppDeviceCount(List<SearchFilter> searchFilters) {
        return getAppDeviceBO().getAppDeviceCount(searchFilters);
    }

    protected List<ResourceGrid> doFindResources(List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        return getResourceBO().findResources(searchFilters, sortIndex, sortOrder, firstResult, maxResults);
    }

    protected long doGetResourceCount(List<SearchFilter> searchFilters) {
        return getResourceBO().getResourceCount(searchFilters);
    }

    protected List<AppDeviceGrid> doFindAppDevices(List<SearchFilter> searchFilters, String sortIndex, String sortOrder, int firstResult, int maxResults) {
        return getAppDeviceBO().findAppDevices(searchFilters, sortIndex, sortOrder, firstResult, maxResults);
    }

    protected AppDeviceApiKeys doRegisterAppDevice(String apiKey, String application, String device) {
        return getAppDeviceBO().registerAppDevice(apiKey, application, device);
    }

    protected AppDeviceApiKeys doGenerateNewPrivateKey(String publicKey, String apiKey) {
        return getAppDeviceBO().generateNewPrivateKey(publicKey, apiKey);
    }

    protected void doUpdateVendor(String apiKey, String name, String address, String city, String state, String zip, String phone, String emailAddress) {
        getVendorBO().updateVendor(apiKey, name, address, city, state, zip, phone, emailAddress);
    }

    protected void doDeleteVendor(String apiKey) {
        getVendorBO().deleteVendor(apiKey);
    }

    protected void doDeleteUser(String openidurl) {
        getUserBO().deleteUser(openidurl);
    }

    protected VendorProfile doGetVendorProfile(String apiKey) {
        return getVendorBO().getVendorProfile(apiKey);
    }

    protected List<Application> doListApplications(String apiKey) {
        return getAppDeviceBO().listApplications(apiKey);
    }

    protected void initManager() {
    }

    public final void afterPropertiesSet() throws Exception {
        if (resourceBO == null) {
            throw new BeanCreationException("Must create ResourceBO");
        }
        if (vendorBO == null) {
            throw new BeanCreationException("Must create VendorBO");
        }
        if (appDeviceBO == null) {
            throw new BeanCreationException("Must create AppDeviceBO");
        }
        initManager();
    }

    public void setResourceBO(ResourceBO resourceBO) {
        this.resourceBO = resourceBO;
    }

    protected ResourceBO getResourceBO() {
        return resourceBO;
    }

    public void setVendorBO(VendorBO vendorBO) {
        this.vendorBO = vendorBO;
    }

    protected VendorBO getVendorBO() {
        return vendorBO;
    }

    public void setAppDeviceBO(AppDeviceBO appDeviceBO) {
        this.appDeviceBO = appDeviceBO;
    }

    protected AppDeviceBO getAppDeviceBO() {
        return appDeviceBO;
    }

    public void setUserBO(UserBO userBO) {
        this.userBO = userBO;
    }

    public UserBO getUserBO() {
        return userBO;
    }
}
