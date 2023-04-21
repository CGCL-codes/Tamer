package org.broadleafcommerce.profile.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.broadleafcommerce.profile.dataprovider.PhoneDataProvider;
import org.broadleafcommerce.profile.domain.Phone;
import org.broadleafcommerce.test.BaseTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

public class PhoneTest extends BaseTest {

    @Resource
    private PhoneService phoneService;

    List<Long> phoneIds = new ArrayList<Long>();

    String userName = new String();

    private Long phoneId;

    @Test(groups = { "createPhone" }, dataProvider = "setupPhone", dataProviderClass = PhoneDataProvider.class, dependsOnGroups = { "readCustomer" })
    @Transactional
    @Rollback(false)
    public void createPhone(Phone phone) {
        userName = "customer1";
        assert phone.getId() == null;
        phone = phoneService.savePhone(phone);
        assert phone.getId() != null;
        phoneId = phone.getId();
    }

    @Test(groups = { "readPhoneById" }, dependsOnGroups = { "createPhone" })
    public void readPhoneById() {
        Phone phone = phoneService.readPhoneById(phoneId);
        assert phone != null;
        assert phone.getId() == phoneId;
    }
}
