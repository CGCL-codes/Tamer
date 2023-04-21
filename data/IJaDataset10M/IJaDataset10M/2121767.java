package com.entelience.probe.mim.wtmp;

import java.util.Date;

/**
 * User Authentication Record.
 *
 * (FROM ZSFGDDL.txt)
<pre>
*-------------------------------------------------------------------------*
* User Profile Sub Record                                                 *
*-------------------------------------------------------------------------*
?SECTION zSFG-ddl-user-subrec
?TALBOUND 0
DEF zSFG-ddl-user-subrec.
 02 zpassword-changed
     TYPE zspi-ddl-uint.
 02 zdefaultvolume
     TYPE zspi-ddl-byte OCCURS 47 TIMES.
 02 zdefaultsecurity
     TYPE zspi-ddl-uint.
 02 zlogonfailcount
     TYPE zspi-ddl-uint.
 02 zaudit-authen-pass
     TYPE zspi-ddl-enum.
 02 zaudit-authen-fail
     TYPE zspi-ddl-enum.
 02 zaudit-manage-pass
     TYPE zspi-ddl-enum.
 02 zaudit-manage-fail
     TYPE zspi-ddl-enum.
 02 zaudit-user-action-pass
     TYPE zspi-ddl-enum.
 02 zaudit-user-action-fail
     TYPE zspi-ddl-enum.
 02 zfreeze
     TYPE zspi-ddl-uint.
 02 zpasswordperiod
     TYPE zspi-ddl-uint.
 02 zlastmodtime
     TYPE zspi-ddl-timestamp.
 02 zlastlogontime
     TYPE zspi-ddl-timestamp.
 02 zpasswordexpires
     TYPE zspi-ddl-timestamp.
 02 zuserexpires
     TYPE zspi-ddl-timestamp.
 02 zpasswordmaychange
     TYPE zspi-ddl-timestamp.
 02 zpasswordlastchange
     TYPE zspi-ddl-timestamp.
 02 zstaticlogonfailcount
     TYPE zspi-ddl-int2.
 02 zpassword-expiry-grace
     TYPE zspi-ddl-int.
 02 zownertypeid
     TYPE zspi-ddl-enum.
 02 zownerusernumber
     TYPE zsfg-ddl-aud-usernumber.
 02 zownerusername
     TYPE zspi-ddl-byte OCCURS 18 TIMES.
 02 zci-prog
     TYPE zspi-ddl-byte OCCURS 47 TIMES.
 02 zci-lib
     TYPE zspi-ddl-byte OCCURS 47 TIMES.
 02 zci-swap
     TYPE zspi-ddl-byte OCCURS 47 TIMES.
 02 zci-name
     TYPE zspi-ddl-byte OCCURS 6 TIMES.
 02 zci-cpu
     TYPE zspi-ddl-int.
 02 zci-pri
     TYPE zspi-ddl-int.
 02 zci-param-text
     TYPE zspi-ddl-byte OCCURS 255 TIMES.
 02 zdefault-protection
     TYPE zsfg-ddl-aud-default-prot.
END.
</pre>
 */
public class TandemAuditUserProfile {

    public int ZPASSWORD_CHANGED;

    public String ZDEFAULTVOLUME;

    public int ZDEFAULTSECURITY;

    public int ZLOGONFAILCOUNT;

    public int ZAUDIT_AUTHEN_PASS;

    public int ZAUDIT_AUTHEN_FAIL;

    public int ZAUDIT_MANAGE_PASS;

    public int ZAUDIT_MANAGE_FAIL;

    public int ZAUDIT_USER_ACTION_PASS;

    public int ZAUDIT_USER_ACTION_FAIL;

    public int ZFREEZE;

    public int ZPASSWORDPERIOD;

    public Date ZLASTMODTIME;

    public Date ZLASTLOGONTIME;

    public Date ZPASSWORDEXPIRES;

    public Date ZUSEREXPIRES;

    public Date ZPASSWORDMAYCHANGE;

    public Date ZPASSWORDLASTCHANGE;

    public long ZSTATICLOGONFAILCOUNT;

    public int ZPASSWORD_EXPIRY_GRACE;

    public int ZOWNERTYPEID;

    public TandemAuditUserNumber ZOWNERUSERNUMBER;

    public String ZOWNERUSERNAME;

    public String ZCI_PROG;

    public String ZCI_LIB;

    public String ZCI_SWAP;

    public String ZCI_NAME;

    public int ZCI_CPU;

    public int ZCI_PRI;

    public String ZCI_PARAM_TEXT;

    public boolean apparentlyOptional = false;

    public int dp_ZAUDIT_ACCESS_PASS;

    public int dp_ZAUDIT_ACCESS_FAIL;

    public int dp_ZAUDIT_MANAGE_PASS;

    public int dp_ZAUDIT_MANAGE_FAIL;

    public int dp_ZFREEZE;

    public int dp_ZOWNERTYPEID;

    public TandemAuditUserNumber dp_ZOWNERUSERNUMBER;

    public String dp_ZOWNERUSERNAME;

    public int dp_ZNUMACLENTRIES;

    public TandemAuditAclEntry dp_ZACLENTRY[];

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append(super.toString());
        out.append(" ZPASSWORD_CHANGED=").append(ZPASSWORD_CHANGED);
        out.append(" ZDEFAULTVOLUME=[").append(ZDEFAULTVOLUME).append(']');
        out.append(" ZDEFAULTSECURITY=").append(ZDEFAULTSECURITY);
        out.append(" ZLOGONFAILCOUNT=").append(ZLOGONFAILCOUNT);
        out.append(" ZAUDIT_AUTHEN_PASS=").append(ZAUDIT_AUTHEN_PASS);
        out.append(" ZAUDIT_AUTHEN_FAIL=").append(ZAUDIT_AUTHEN_FAIL);
        out.append(" ZAUDIT_MANAGE_PASS=").append(ZAUDIT_MANAGE_PASS);
        out.append(" ZAUDIT_MANAGE_FAIL=").append(ZAUDIT_MANAGE_FAIL);
        out.append(" ZAUDIT_USER_ACTION_PASS=").append(ZAUDIT_USER_ACTION_PASS);
        out.append(" ZAUDIT_USER_ACTION_FAIL=").append(ZAUDIT_USER_ACTION_FAIL);
        out.append(" ZFREEZE=").append(ZFREEZE);
        out.append(" ZPASSWORDPERIOD=").append(ZPASSWORDPERIOD);
        out.append(" ZLASTMODTIME=").append(ZLASTMODTIME);
        out.append(" ZLASTLOGONTIME=").append(ZLASTLOGONTIME);
        out.append(" ZPASSWORDEXPIRES=").append(ZPASSWORDEXPIRES);
        out.append(" ZUSEREXPIRES=").append(ZUSEREXPIRES);
        out.append(" ZPASSWORDMAYCHANGE=").append(ZPASSWORDMAYCHANGE);
        out.append(" ZPASSWORDLASTCHANGE=").append(ZPASSWORDLASTCHANGE);
        out.append(" ZSTATICLOGONFAILCOUNT=").append(ZSTATICLOGONFAILCOUNT);
        out.append(" ZPASSWORD_EXPIRY_GRACE=").append(ZPASSWORD_EXPIRY_GRACE);
        out.append(" ZOWNERTYPEID=").append(ZOWNERTYPEID);
        out.append(" ZOWNERUSERNUMBER=").append(ZOWNERUSERNUMBER);
        out.append(" ZOWNERUSERNAME=[").append(ZOWNERUSERNAME).append(']');
        out.append(" ZCI_PROG=[").append(ZCI_PROG).append(']');
        out.append(" ZCI_LIB=[").append(ZCI_LIB).append(']');
        out.append(" ZCI_SWAP=[").append(ZCI_SWAP).append(']');
        out.append(" ZCI_NAME=[").append(ZCI_NAME).append(']');
        out.append(" ZCI_CPU=").append(ZCI_CPU);
        out.append(" ZCI_PRI=").append(ZCI_PRI);
        out.append(" ZCI_PARAM_TEXT=[").append(ZCI_PARAM_TEXT).append(']');
        if (apparentlyOptional) {
            out.append(" dp_ZAUDIT_ACCESS_PASS=").append(dp_ZAUDIT_ACCESS_PASS);
            out.append(" dp_ZAUDIT_ACCESS_FAIL=").append(dp_ZAUDIT_ACCESS_FAIL);
            out.append(" dp_ZAUDIT_MANAGE_PASS=").append(dp_ZAUDIT_MANAGE_PASS);
            out.append(" dp_ZAUDIT_MANAGE_FAIL=").append(dp_ZAUDIT_MANAGE_FAIL);
            out.append(" dp_ZFREEZE=").append(dp_ZFREEZE);
            out.append(" dp_ZOWNERTYPEID=").append(dp_ZOWNERTYPEID);
            out.append(" dp_ZOWNERUSERNUMBER=").append(dp_ZOWNERUSERNUMBER);
            out.append(" dp_ZOWNERUSERNAME=[").append(dp_ZOWNERUSERNAME).append(']');
            out.append(" dp_ZNUMACLENTRIES=").append(dp_ZNUMACLENTRIES);
            if (dp_ZACLENTRY == null || dp_ZACLENTRY.length == 0) {
                out.append(" dp_ZACLENTRY has no members");
            } else {
                for (int i = 0; i < dp_ZACLENTRY.length; ++i) {
                    out.append(" dp_ZACLENTRY[").append(i).append("]={").append(dp_ZACLENTRY[i]).append('}');
                }
            }
        }
        return out.toString();
    }
}
