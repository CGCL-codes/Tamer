package hambo.pim;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.SQLException;
import hambo.svc.ServiceException;

public class Number2NicknameMapper {

    public static final int MOBILE = 0;

    public static final int FAX = 1;

    public static final int ICQ = 2;

    private Hashtable contactNumbers = null;

    private String userOId = null;

    private String countryCode = null;

    public Number2NicknameMapper(String userOId, String countryCode) throws SQLException, ServiceException {
        this.userOId = userOId;
        this.countryCode = countryCode;
        fetchInfo();
    }

    private void fetchInfo() throws SQLException, ServiceException {
        Vector exts = ExternalContactBO.getExternals(userOId);
        contactNumbers = new Hashtable();
        Hashtable mobHt = new Hashtable();
        Hashtable faxHt = new Hashtable();
        Hashtable icqHt = new Hashtable();
        contactNumbers.put(Integer.toString(MOBILE), mobHt);
        contactNumbers.put(Integer.toString(FAX), faxHt);
        contactNumbers.put(Integer.toString(ICQ), icqHt);
        for (int i = 0; i < exts.size(); i++) {
            ExternalContactDO eDO = (ExternalContactDO) exts.elementAt(i);
            String nickname = eDO.getNickname();
            String mob = MessengerUtil.makeValidNumber(eDO.getCellph(), countryCode);
            String fax = MessengerUtil.makeValidNumber(eDO.getFax(), countryCode);
            String icq = eDO.getImno();
            if (mob != null && !mob.equals("")) mobHt.put(mob, nickname);
            if (fax != null && !fax.equals("")) faxHt.put(fax, nickname);
            if (icq != null && !icq.equals("")) icqHt.put(icq, nickname);
        }
    }

    public String getNickname(String lookup, int type) {
        String nickname = null;
        nickname = (String) ((Hashtable) contactNumbers.get(Integer.toString(type))).get(MessengerUtil.makeValidNumber(lookup, countryCode));
        if (nickname == null) return lookup; else return nickname;
    }
}
