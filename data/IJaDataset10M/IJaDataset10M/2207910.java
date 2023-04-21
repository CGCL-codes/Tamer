package com.bank.service;

import java.util.Date;
import java.util.List;
import com.bank.entity.CustLogInfo;
import com.bank.entity.Customer;
import com.bank.entity.RegularAcc;
import com.bank.operator.OptRecord;

public class RegularAccSer extends AAccountSer implements IIndividualAccSer {

    OptRecord record;

    @Override
    public OptRecord checkbalance(Long idNumber, Long accountId, String password) {
        RegularAcc regacc = regaccDao.findById(accountId);
        CustLogInfo custloginfo = regacc.getRootuser();
        Customer customer = custloginfo.getCustomer();
        record = new OptRecord();
        record.setAccount(regacc);
        record.setTime(new Date());
        record.setEmployee(employee);
        record.setOpt("Check");
        if (regacc == null) {
            record.setOptdesc(new String("Acc: ") + accountId + " does not exist");
            optrecordDao.save(record);
            return record;
        }
        if (!regacc.getIsActive() || custloginfo == null || customer == null || !(custloginfo.getPassword().equals(password)) || !(customer.getIdNumber().equals(idNumber))) {
            record.setOptdesc(new String("Acc & pwd are not matched."));
            optrecordDao.save(record);
            return record;
        }
        record.setOptdesc(new String("The balance of acc: ") + regacc.getId() + " is " + regacc.getBalance().toString());
        record.setInAmt(0.0);
        record.setOutAmt(0.0);
        record.setBal(regacc.getBalance().doubleValue());
        optrecordDao.save(record);
        return record;
    }

    @Override
    public OptRecord createAccount(Long idNumber, char type, double initialAmount, String password, String name) {
        CustLogInfo custloginfo = saveCustomer(idNumber, name);
        custloginfo.setPassword(password);
        custloginfo.setLastLoggingTime(new Date());
        custloginfoDao.save(custloginfo);
        RegularAcc regacc = new RegularAcc();
        regacc.setBalance(new Double(initialAmount));
        regacc.setIsActive(true);
        regacc.setRegisteredTime(new Date());
        regacc.setType(type);
        regacc.setRootuser(custloginfo);
        regaccDao.save(regacc);
        record = new OptRecord();
        record.setAccount(regacc);
        record.setTime(new Date());
        record.setEmployee(employee);
        record.setOpt("OpenAcc");
        record.setInAmt(initialAmount);
        record.setOutAmt(0.0);
        record.setBal(regacc.getBalance().doubleValue());
        record.setOptdesc(new String("The new Acc is registered for ID: ") + regacc.getId());
        optrecordDao.save(record);
        return record;
    }

    @Override
    public OptRecord deposit(Long accountId, String password, double amount) {
        RegularAcc regacc = regaccDao.findById(accountId);
        CustLogInfo custloginfo = regacc.getRootuser();
        record = new OptRecord();
        record.setAccount(regacc);
        record.setTime(new Date());
        record.setEmployee(employee);
        record.setOpt("Deposit");
        if (regacc == null) {
            record.setOptdesc(new String("Acc: ") + accountId + " does not exist");
            optrecordDao.save(record);
            return record;
        }
        if (!regacc.getIsActive() || custloginfo == null || !(custloginfo.getPassword().equals(password))) {
            record.setOptdesc(new String("Acc & pwd are not matched."));
            optrecordDao.save(record);
            return record;
        }
        regacc.setBalance(regacc.getBalance() + Double.valueOf(amount));
        regaccDao.attachDirty(regacc);
        record.setOptdesc(new String("Deposit amount ") + amount + ", and total is: " + regacc.getBalance().toString());
        record.setInAmt(amount);
        record.setOutAmt(0.0);
        record.setBal(regacc.getBalance().doubleValue());
        optrecordDao.save(record);
        return record;
    }

    @Override
    public OptRecord destory(Long idNumber, Long accountId, String password) {
        RegularAcc regacc = regaccDao.findById(accountId);
        CustLogInfo custloginfo = regacc.getRootuser();
        Customer customer = custloginfo.getCustomer();
        record = new OptRecord();
        record.setTime(new Date());
        record.setAccount(regacc);
        record.setEmployee(employee);
        record.setOpt("DestoryAcc");
        if (regacc == null) {
            record.setOptdesc(new String("Acc: ") + accountId + " does not exist");
            optrecordDao.save(record);
            return record;
        }
        if (!regacc.getIsActive() || custloginfo == null || customer == null || !(custloginfo.getPassword().equals(password)) || !(customer.getIdNumber().equals(idNumber))) {
            record.setOptdesc(new String("Acc, id number & pwd are not matched."));
            optrecordDao.save(record);
            return record;
        }
        regacc.setIsActive(false);
        double t = regacc.getBalance();
        regacc.setBalance(new Double(0.0));
        regaccDao.attachDirty(regacc);
        record.setOptdesc(new String("Acc: ") + regacc.getId() + " is canceled." + " and Balance is token: " + String.valueOf(t));
        record.setInAmt(0.0);
        record.setOutAmt(t);
        record.setBal(regacc.getBalance().doubleValue());
        optrecordDao.save(record);
        return record;
    }

    @Override
    public OptRecord reset(Long idNumber, Long accountId, String oldPassword, String newPassword) {
        RegularAcc regacc = regaccDao.findById(accountId);
        CustLogInfo custloginfo = regacc.getRootuser();
        Customer customer = custloginfo.getCustomer();
        record = new OptRecord();
        record.setTime(new Date());
        record.setAccount(regacc);
        record.setEmployee(employee);
        record.setOpt("ResetPWD");
        if (regacc == null) {
            record.setOptdesc(new String("Acc: ") + accountId + " does not exist");
            optrecordDao.save(record);
            return record;
        }
        if (!regacc.getIsActive() || custloginfo == null || customer == null || !(custloginfo.getPassword().equals(oldPassword)) || !(customer.getIdNumber().equals(idNumber))) {
            record.setOptdesc(new String("Acc, id number & pwd are not matched."));
            optrecordDao.save(record);
            return record;
        }
        custloginfo.setPassword(newPassword);
        custloginfoDao.attachDirty(custloginfo);
        record.setOptdesc(new String("Password is set to be: ") + custloginfo.getPassword());
        optrecordDao.save(record);
        return record;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CustLogInfo saveCustomer(Long idNumber, String name) {
        Customer customer;
        List<Customer> customers = (List<Customer>) custDao.findByProperty("idNumber", idNumber);
        if (customers.size() < 1) {
            customer = new Customer(idNumber, name, new Date());
            this.registerCustomer(customer);
        } else customer = customers.iterator().next();
        CustLogInfo custloginfo = new CustLogInfo(customer, null);
        custloginfoDao.attachDirty(custloginfo);
        record = new OptRecord();
        record.setTime(new Date());
        record.setEmployee(employee);
        record.setOpt("CreateCust");
        record.setOptdesc(new String("Register Customer: ") + custloginfo.getId());
        optrecordDao.save(record);
        return custloginfo;
    }

    @Override
    public OptRecord transfer(Long accountId, Long idNumber, String password, String name, Long inAccountId, String inCustomerName, double amount) {
        RegularAcc regacc = regaccDao.findById(accountId);
        CustLogInfo custloginfo = regacc.getRootuser();
        Customer customer = custloginfo.getCustomer();
        RegularAcc iregacc = regaccDao.findById(inAccountId);
        CustLogInfo icustloginfo = iregacc.getRootuser();
        Customer icustomer = icustloginfo.getCustomer();
        if (regacc == null || iregacc == null) {
            record.setOptdesc(new String("Acc: ") + accountId + " and " + inAccountId + " do not exist");
            optrecordDao.save(record);
            return record;
        }
        record = new OptRecord();
        record.setAccount(regacc);
        record.setTime(new Date());
        record.setEmployee(employee);
        record.setOpt("Transfer");
        if (!regacc.getIsActive() || custloginfo == null || customer == null || !(custloginfo.getPassword().equals(password)) || !(customer.getIdNumber().equals(idNumber)) || !(customer.getName().equals(name)) || !iregacc.getIsActive() || icustloginfo == null || icustomer == null || !(icustomer.getName().equals(inCustomerName))) {
            record.setOptdesc(new String("Acc, id number & pwd are not matched."));
            optrecordDao.save(record);
            return record;
        }
        Double remain = regacc.getBalance() - Double.valueOf(amount);
        if (remain < 0.0) {
            record.setOptdesc(new String("Balance is not enough.") + regacc.getBalance() + " - " + String.valueOf(amount));
            optrecordDao.save(record);
            return record;
        }
        regacc.setBalance(remain);
        regaccDao.attachDirty(regacc);
        iregacc.setBalance(iregacc.getBalance() + Double.valueOf(amount));
        regaccDao.attachDirty(iregacc);
        record.setOptdesc(new String("transfer amount from id-") + regacc.getId().toString() + " to " + iregacc.getId().toString() + " " + String.valueOf(amount));
        record.setInAmt(0.0);
        record.setOutAmt(amount);
        record.setBal(regacc.getBalance().doubleValue());
        optrecordDao.save(record);
        record.setAccount(iregacc);
        record.setInAmt(amount);
        record.setOutAmt(0.0);
        record.setBal(iregacc.getBalance().doubleValue());
        optrecordDao.save(record);
        return record;
    }

    @Override
    public OptRecord withdraw(Long accountId, String password, double amount) {
        RegularAcc regacc = regaccDao.findById(accountId);
        CustLogInfo custloginfo = regacc.getRootuser();
        record = new OptRecord();
        record.setAccount(regacc);
        record.setTime(new Date());
        record.setEmployee(employee);
        record.setOpt("WithDraw");
        if (regacc == null) {
            record.setOptdesc(new String("Acc: ") + accountId + " does not exist");
            optrecordDao.save(record);
            return record;
        }
        if (!regacc.getIsActive() || custloginfo == null || !(custloginfo.getPassword().equals(password))) {
            record.setOptdesc(new String("Password is wrong."));
            optrecordDao.save(record);
            return record;
        }
        Double remain = regacc.getBalance() - Double.valueOf(amount);
        if (remain < 0.0) {
            record.setOptdesc(new String("Balance is not enough.") + regacc.getBalance() + " - " + String.valueOf(amount));
            optrecordDao.save(record);
            return record;
        }
        regacc.setBalance(remain);
        regaccDao.attachDirty(regacc);
        record.setOptdesc(new String("Draw balance: ") + String.valueOf(amount) + " and remain: " + regacc.getBalance());
        record.setInAmt(0.0);
        record.setOutAmt(amount);
        record.setBal(regacc.getBalance().doubleValue());
        optrecordDao.save(record);
        return record;
    }
}
