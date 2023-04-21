package com.ecs.etrade.uiinterfaces;

import java.util.Date;
import org.springframework.context.ApplicationContext;
import com.ecs.etrade.bo.broker.BrokerManagerImpl;
import com.ecs.etrade.bo.utils.TradeApplicationContext;
import com.ecs.etrade.da.CompanyInfo;
import com.ecs.etrade.daextn.CompanyInfoDAOExtn;
import com.empower.model.CompanyInfoModel;
import com.empower.model.ContactDetailsModel;

public class CompanyInfoUIImpl implements CompanyInfoUI {

    private BrokerManagerImpl brokerManager;

    public CompanyInfoUIImpl() {
        ApplicationContext factory = TradeApplicationContext.getApplicationContext();
        brokerManager = (BrokerManagerImpl) factory.getBean("brokerManagerService");
    }

    public CompanyInfoModel getCompanyInfo() throws Exception {
        return null;
    }

    public boolean saveCompanyInfo(CompanyInfoModel cmpnyInfoMdl) throws Exception {
        CompanyInfo cmpnyInfo = convertInfoModelToDAO(cmpnyInfoMdl);
        brokerManager.createCompany(cmpnyInfo);
        return true;
    }

    public CompanyInfoModel convertDAOToInfoModel(CompanyInfo cmpnyInfo) {
        CompanyInfoModel cmpnyInfoModel = new CompanyInfoModel();
        ContactDetailsModel cntctDetails = new ContactDetailsModel();
        cntctDetails.setAddress1(cmpnyInfo.getAddress1());
        cntctDetails.setAddress2(cmpnyInfo.getAddress2());
        cntctDetails.setCityDistrict(cmpnyInfo.getCity());
        cntctDetails.setContactPerson("");
        cntctDetails.setEMailId(cmpnyInfo.getEmailId());
        cntctDetails.setFaxNbr(cmpnyInfo.getFax());
        cntctDetails.setPhone1Nbr(cmpnyInfo.getPhone1());
        cntctDetails.setPhone2Nbr(cmpnyInfo.getPhone2());
        cntctDetails.setPinCode(new Integer(cmpnyInfo.getPinCode()));
        cntctDetails.setState(cmpnyInfo.getState());
        cmpnyInfoModel.setContactDtlsMdl(cntctDetails);
        cmpnyInfoModel.setCaption(cmpnyInfo.getCaption());
        cmpnyInfoModel.setCompanyName(cmpnyInfo.getCompanyName());
        cmpnyInfoModel.setCompanyURL(cmpnyInfo.getCompanyUrl());
        return cmpnyInfoModel;
    }

    public CompanyInfo convertInfoModelToDAO(CompanyInfoModel cmpnyInfoMdl) {
        CompanyInfo cmpnyInfo = new CompanyInfo();
        cmpnyInfo.setAddress1(cmpnyInfoMdl.getContactDtlsMdl().getAddress1());
        cmpnyInfo.setAddress2(cmpnyInfoMdl.getContactDtlsMdl().getAddress2());
        cmpnyInfo.setCaption(cmpnyInfoMdl.getCaption());
        cmpnyInfo.setCity(cmpnyInfoMdl.getContactDtlsMdl().getCityDistrict());
        cmpnyInfo.setCompanyName(cmpnyInfoMdl.getCompanyName());
        cmpnyInfo.setCompanyUrl(cmpnyInfoMdl.getCompanyURL());
        cmpnyInfo.setCountry("India");
        cmpnyInfo.setCreationDate(new Date());
        cmpnyInfo.setEmailId(cmpnyInfoMdl.getContactDtlsMdl().getEMailId());
        cmpnyInfo.setFax(cmpnyInfoMdl.getContactDtlsMdl().getFaxNbr());
        cmpnyInfo.setPhone1(cmpnyInfoMdl.getContactDtlsMdl().getPhone1Nbr());
        cmpnyInfo.setPhone2(cmpnyInfoMdl.getContactDtlsMdl().getPhone2Nbr());
        cmpnyInfo.setPinCode(cmpnyInfoMdl.getContactDtlsMdl().getPinCode().toString());
        cmpnyInfo.setState(cmpnyInfoMdl.getContactDtlsMdl().getState());
        return cmpnyInfo;
    }
}
