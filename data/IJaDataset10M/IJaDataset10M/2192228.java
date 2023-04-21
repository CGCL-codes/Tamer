package net.videgro.oma.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.videgro.oma.domain.Member;
import net.videgro.oma.domain.MyStudy;
import net.videgro.oma.domain.Study;
import net.videgro.oma.managers.MemberManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class MemberService implements IMemberService {

    protected final Log logger = LogFactory.getLog(getClass());

    private MemberManager memberManager = null;

    public void setMemberManager(MemberManager mm) {
        this.memberManager = mm;
    }

    public MemberManager getMemberManager() {
        return memberManager;
    }

    public List getMemberList(String[] filterNames, String[] filterValues, int who) {
        logger.debug("getMemberList");
        List list = null;
        try {
            list = memberManager.getMemberList(filterNames, filterValues, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    public List getMemberListWithDetails(String[] filterNames, String[] filterValues, int who) {
        logger.debug("getMemberListWithDetails");
        List list = null;
        try {
            list = memberManager.getMemberListWithDetails(filterNames, filterValues, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    public Member getMember(int id, int who) {
        logger.debug("getMember " + id);
        Member result = null;
        try {
            result = memberManager.getMember(id, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public int setMember(Member m, int who) {
        logger.debug("setMember");
        int result = -1;
        try {
            Set<MyStudy> myStudies = new HashSet<MyStudy>();
            Iterator<MyStudy> iter = m.getMyStudies().iterator();
            while (iter.hasNext()) {
                Object object = (Object) iter.next();
                if (object instanceof String) {
                    String[] tmp = ((String) object).split("\\|");
                    Date dat = new Date();
                    Long time = Long.parseLong(tmp[2]);
                    if (time == 0) time = MyStudy.DATE_NOT_SET.getTimeInMillis();
                    dat.setTime(time);
                    MyStudy myStudy = new MyStudy(dat, new Study(tmp[0], tmp[1]));
                    myStudies.add(myStudy);
                } else if (object instanceof MyStudy) {
                    myStudies.add((MyStudy) object);
                }
            }
            m.setMyStudies(myStudies);
            result = memberManager.setMember(m, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public boolean setPassword(int id, String password, int who) {
        logger.debug("setPassword");
        boolean result = false;
        try {
            result = memberManager.setPassword(id, password, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public void deleteMember(int id, int who) {
        logger.debug("deleteMember " + id);
        try {
            memberManager.deleteMember(id, who);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }
}
