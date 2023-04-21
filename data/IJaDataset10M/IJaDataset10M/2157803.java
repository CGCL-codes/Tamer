package com.entelience.probe.mim.wtmp;

import com.entelience.probe.mim.user.UserDb;
import com.entelience.probe.mim.user.AuthSystem;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WindowsEventlogEntry {

    private static final org.apache.log4j.Logger _logger = com.entelience.util.Logs.getProbeLogger();

    private static final boolean TRACE = false;

    protected java.util.Date date = null;

    protected int n1 = -1;

    protected int n2 = -1;

    protected int eventId = -1;

    protected String subSystem = null;

    protected String username = null;

    protected String host = null;

    protected String message = null;

    protected String data[] = null;

    protected int logonType = -1;

    protected byte raw_data[] = null;

    protected boolean raw = false;

    protected boolean used = false;

    protected boolean isLogout = false;

    protected boolean isRestart = false;

    protected int success_login = 0;

    protected int failed_login = 0;

    protected int su_success = 0;

    protected int su_failed = 0;

    protected String suType = null;

    protected String logInOutJoin = null;

    private static final Pattern p_antislash = Pattern.compile("\\\\");

    private static final Pattern p_username_valid = Pattern.compile("^[a-zA-Z0-9_\\\\ ]+$");

    public WindowsEventlogEntry(java.util.Date date, int n1, int n2, int eventId, String subSystem, String host, String data[]) {
        this.date = date;
        this.n1 = n1;
        this.n2 = n2;
        this.eventId = eventId;
        this.subSystem = subSystem;
        this.host = host;
        this.data = data;
    }

    /**
     * method to normalise and check validity of username
     *
     * @param domainIn like WORKGROUP
     * @param username like user1
     * @return WORKGROUP\\user1
     *
     * @todo this might be a problem if a user decides to try to connect
     * using a bogus username.
     */
    public String normalise(String domainIn, String username) throws Exception {
        if (username == null) throw new Exception("Cannot normalise null username");
        String domain = domainIn;
        if (domain == null) domain = host;
        Matcher m_antislash = p_antislash.matcher(username);
        if (m_antislash.find()) {
            Matcher m_username_valid = p_username_valid.matcher(username);
            if (!m_username_valid.matches()) throw new Exception("Username [" + username + "] is invalid.");
            return username;
        } else {
            Matcher m_username_valid = p_username_valid.matcher(username);
            if (!m_username_valid.matches()) throw new Exception("Username [" + username + "] is invalid.");
            Matcher m_domain_valid = p_username_valid.matcher(domain);
            if (!m_domain_valid.matches()) throw new Exception("Domain [" + domain + "] is invalid.");
            return domain + "\\" + username;
        }
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append(super.toString());
        out.append(" eventId=").append(eventId);
        out.append(" subSystem=[").append(subSystem).append(']');
        out.append(" host=[").append(host).append(']');
        out.append(" date=").append(date);
        out.append(" n1=").append(n1);
        out.append(" n2=").append(n2);
        out.append(" logonType=").append(logonType);
        out.append(" data.length=").append(data.length);
        for (int i = 0; i < data.length; ++i) out.append(" data[").append(i).append("]=[").append(data[i]).append(']');
        if (used) {
            out.append(" logInOutJoin ").append(logInOutJoin);
            out.append(" login ").append(success_login).append(',').append(failed_login);
            out.append(" su type ").append(suType).append(' ').append(su_success).append(',').append(su_failed);
            out.append(" isLogout ").append(isLogout);
            out.append(" isRestart ").append(isRestart);
        }
        if (TRACE) {
            if (raw_data != null) {
                out.append(" raw_data [").append(new String(raw_data, 0, raw_data.length)).append(']');
            }
        }
        return out.toString();
    }

    public boolean getUsed() {
        return used;
    }

    public void markSUSuccess(String suType) {
        used = true;
        su_success++;
        this.suType = suType;
    }

    public void markSUFailed(String suType, int nTries) {
        used = true;
        su_failed += nTries;
        this.suType = suType;
    }

    public void markLoginSuccess() {
        used = true;
        success_login++;
    }

    public void markLoginFailed(int nTries) {
        used = true;
        failed_login += nTries;
    }

    public void markLogout() {
        used = true;
        isLogout = true;
    }

    public void markRestart() {
        used = true;
        isRestart = true;
    }

    public static final Pattern p_join = Pattern.compile("(\\(0x[0-9A-F]+,0x[0-9A-F]+\\))");

    protected boolean store(AuthSystem as, WtmpDb wtmpDb, UserDb userDb) throws Exception {
        if ("Security".equals(subSystem)) {
            boolean ignore = false;
            if ((512 <= eventId && eventId <= 518) || (541 <= eventId && eventId <= 547) || (560 <= eventId && eventId <= 566) || (592 <= eventId && eventId <= 595) || (608 <= eventId && eventId <= 620) || (624 <= eventId && eventId <= 670) || (672 <= eventId && eventId <= 677) || (678 <= eventId && eventId <= 683)) ignore = true;
            switch(eventId) {
                case 528:
                    username = normalise(data[1], data[0]);
                    logInOutJoin = data[2];
                    logonType = Integer.parseInt(data[3]);
                    if (logonType == 2 || logonType == 10 || logonType == 11) markLoginSuccess(); else ignore = true;
                    break;
                case 540:
                    ignore = true;
                    break;
                case 538:
                    username = normalise(data[1], data[0]);
                    logInOutJoin = data[2];
                    logonType = Integer.parseInt(data[3]);
                    if (logonType == 2 || logonType == 10 || logonType == 11) markLogout(); else ignore = true;
                    break;
                case 529:
                case 530:
                case 531:
                case 532:
                case 533:
                case 534:
                case 535:
                case 536:
                case 537:
                case 539:
                    username = normalise(data[1], data[0]);
                    logonType = Integer.parseInt(data[2]);
                    if (logonType == 2 || logonType == 10 || logonType == 11 || logonType == 7) markLoginFailed(1); else ignore = true;
                    break;
                case 576:
                    ignore = true;
                    break;
                case 577:
                    username = normalise(data[6], data[5]);
                    markSUSuccess("577");
                    break;
                case 578:
                    username = normalise(data[7], data[6]);
                    markSUSuccess("578");
                    break;
                default:
            }
            if (getUsed()) {
                _logger.debug("Storing " + this);
                if (isLogout) {
                    wtmpDb.insertLogOut(as, userDb.getUserAccount(as, username), logInOutJoin, date);
                } else if (isRestart) {
                    wtmpDb.insertMachineEvent(as, date, 2);
                } else if (success_login + failed_login > 0) {
                    wtmpDb.insertLogIn(as, userDb.getUserAccount(as, username), logInOutJoin, date, success_login, failed_login);
                } else if (su_success + su_failed > 0) {
                    wtmpDb.insertPrivilegeEvent(as, userDb.getUserAccount(as, username), date, null, su_success, su_failed, suType);
                } else throw new Exception("Unable to store event " + this);
            } else {
                if (ignore) {
                    _logger.debug("Ignoring event " + this);
                } else {
                    _logger.warn("Storing unknown security sub-system event " + this);
                    wtmpDb.insertOtherEvent(as, date, this.toString());
                }
            }
        } else {
            _logger.debug("Not a security subsystem message " + this);
        }
        return true;
    }
}
