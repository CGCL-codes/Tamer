package net.woodstock.rockapi.service.test;

import net.woodstock.rockapi.service.impl.AbstractService;

public class TestServiceImpl extends AbstractService implements TestService {

    private TestBusiness business;

    private TestDAO dao;

    public void test() {
        System.out.println("Service : " + this.getClass().getCanonicalName());
        System.out.println("Business: " + this.business.getClass().getCanonicalName());
        System.out.println("DAO     : " + this.dao.getClass().getCanonicalName());
    }

    public void setBusiness(TestBusiness business) {
        this.business = business;
    }

    public void setDao(TestDAO dao) {
        this.dao = dao;
    }
}
