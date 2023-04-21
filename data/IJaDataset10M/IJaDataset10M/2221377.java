package com.googlecode.pondskum.stub;

import com.googlecode.pondskum.client.BigpondAccountInformation;
import com.googlecode.pondskum.client.BigpondAccountInformationImpl;
import com.googlecode.pondskum.client.BigpondMonthlyUsage;
import com.googlecode.pondskum.client.BigpondMonthlyUsageImpl;
import com.googlecode.pondskum.client.BigpondUsage;
import com.googlecode.pondskum.client.BigpondUsageInformation;
import com.googlecode.pondskum.client.BigpondUsageInformationImpl;
import java.util.ArrayList;
import java.util.List;

public final class StubbyNewMonthBigpondUsageInformationBuilder {

    private static final int MONTHLY_ALLOWANCE = 50;

    private final StubInformationCreator informationCreator;

    public StubbyNewMonthBigpondUsageInformationBuilder() {
        informationCreator = new StubInformationCreator();
    }

    public BigpondUsageInformation build() {
        BigpondAccountInformation accountInformation = new BigpondAccountInformationImpl();
        accountInformation.setAccountName("James Bond");
        accountInformation.setAccountNumber("0000007");
        accountInformation.setCurrentPlan("MI6");
        accountInformation.setMonthlyAllowanceShaping("Infinite");
        accountInformation.setMonthlyAllowance(MONTHLY_ALLOWANCE);
        accountInformation.setMonthlyPlanFee("$1,000,000");
        List<BigpondMonthlyUsage> bigpondUsageList = new ArrayList<BigpondMonthlyUsage>();
        bigpondUsageList.add(new BigpondMonthlyUsageImpl("Feb", informationCreator.createUsage("-", "-", "-", "-")));
        BigpondUsage totalUsage = informationCreator.createUsage("-", "-", "-", "-");
        return new BigpondUsageInformationImpl(accountInformation, bigpondUsageList, totalUsage);
    }
}
