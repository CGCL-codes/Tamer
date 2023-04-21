package com.peterhi.data;

import java.util.HashSet;
import java.util.Set;

public class Account {

    public static final String F_ACC_ID = "accId";

    public static final String F_ACC_NAME = "accName";

    public static final String F_ACC_EMAIL = "accEmail";

    public static final String F_ACC_PASS = "accPass";

    public static final String F_ACC_CUR_SES = "accCurSes";

    public static final String F_ACC_ONLINE = "accOnline";

    public static final String F_ACC_ENABLED = "accEnabled";

    public static final String F_ACC_ACTIVATED = "accActivated";

    public static final String F_ACC_ACTIVATE_SESSION = "accActivateSession";

    public static final String F_ACC_LOCALE = "accLocale";

    private Long accId;

    private String accName;

    private String accEmail;

    private char[] accPass;

    private String accCurSes;

    private boolean accOnline;

    private boolean accEnabled = true;

    private boolean accActivated;

    private String accActivateSession;

    private String accLocale;

    private Set<Channel> accChannels = new HashSet<Channel>();

    protected Account() {
    }

    public Account(String accEmail) {
        this.accEmail = accEmail;
    }

    public String getAccActivateSession() {
        return accActivateSession;
    }

    public void setAccActivateSession(String value) {
        accActivateSession = value;
    }

    public boolean isAccActivated() {
        return accActivated;
    }

    public void setAccActivated(boolean value) {
        accActivated = value;
    }

    public boolean isAccEnabled() {
        return accEnabled;
    }

    public void setAccEnabled(boolean value) {
        accEnabled = value;
    }

    protected Set<Channel> getAccChannels() {
        return accChannels;
    }

    protected void setAccChannels(Set<Channel> value) {
        accChannels = value;
    }

    public void addAccChannel(Channel channel) {
        accChannels.add(channel);
        channel.getChnlAccounts().add(this);
    }

    public void removeAccChannel(Channel channel) {
        accChannels.remove(channel);
        channel.getChnlAccounts().remove(this);
    }

    public Long getAccId() {
        return accId;
    }

    protected void setAccId(Long id) {
        this.accId = id;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accountName) {
        this.accName = accountName;
    }

    public String getAccEmail() {
        return accEmail;
    }

    public void setAccEmail(String displayName) {
        this.accEmail = displayName;
    }

    public char[] getAccPass() {
        return accPass;
    }

    public void setAccPass(char[] password) {
        this.accPass = password;
    }

    public String getAccCurSes() {
        return accCurSes;
    }

    public void setAccCurSes(String curSes) {
        accCurSes = curSes;
    }

    public boolean isAccOnline() {
        return accOnline;
    }

    public void setAccOnline(boolean online) {
        this.accOnline = online;
    }

    public String getAccLocale() {
        return accLocale;
    }

    public void setAccLocale(String locale) {
        this.accLocale = locale;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id:" + accId);
        sb.append(",name:" + accName);
        sb.append(",email:" + accEmail);
        sb.append(",pass:" + new String(accPass));
        sb.append(",ses:" + accCurSes);
        sb.append(",online:" + accOnline);
        sb.append(",enabled:" + accEnabled);
        sb.append(",locale:" + accLocale);
        return sb.toString();
    }
}
