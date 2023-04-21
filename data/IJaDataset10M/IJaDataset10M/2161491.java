package com.cci.bmc.action.cablemodem;

import java.util.List;
import java.util.Set;
import com.cci.bmc.domain.Account;
import com.cci.bmc.domain.CableModem;
import com.cci.bmc.domain.Cmts;
import com.cci.bmc.domain.Location;
import com.cci.bmc.domain.Service;
import com.cci.bmc.service.AccountService;
import com.cci.bmc.service.LocationService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class EditCableModem extends ActionSupport implements ModelDriven<CableModem>, Preparable {

    private static final long serialVersionUID = 8802085456037104664L;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public void prepare() {
        setModel(accountService.getCableModem(getId()));
        setAccountId(getModel().getAccount().getId());
        setAccount(accountService.getAccount(getAccountId()));
        setLocations(locationService.listLocations());
        setServices(getModel().getCmts().getLocation().getServices("Modem"));
        setCmtses(getModel().getCmts().getLocation().getCmtses());
    }

    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    private LocationService locationService;

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    private Long id;

    private Long accountId;

    private Account account;

    private CableModem modem;

    private Set<Service> services;

    private Set<Cmts> cmtses;

    private List<Location> locations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CableModem getModel() {
        return modem;
    }

    public void setModel(CableModem modem) {
        this.modem = modem;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Set<Cmts> getCmtses() {
        return cmtses;
    }

    public void setCmtses(Set<Cmts> cmtses) {
        this.cmtses = cmtses;
    }
}
