package egovframework.com.uss.ion.uas.service;

import java.util.List;

public class UserAbsnceVO extends UserAbsnce {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 사용자부재 목록
	 */
    List<UserAbsnceVO> userAbsnceList;

    /**
	 * 삭제대상 목록
	 */
    String[] delYn;

    /**
	 * 부재여부 조회조건
	 */
    String selAbsnceAt;

    /**
	 * @return the userAbsnceList
	 */
    public List<UserAbsnceVO> getUserAbsnceList() {
        return userAbsnceList;
    }

    /**
	 * @param userAbsnceList the userAbsnceList to set
	 */
    public void setUserAbsnceList(List<UserAbsnceVO> userAbsnceList) {
        this.userAbsnceList = userAbsnceList;
    }

    /**
	 * @return the delYn
	 */
    public String[] getDelYn() {
        return delYn;
    }

    /**
	 * @param delYn the delYn to set
	 */
    public void setDelYn(String[] delYn) {
        this.delYn = delYn;
    }

    /**
	 * @return the selAbsnceAt
	 */
    public String getSelAbsnceAt() {
        return selAbsnceAt;
    }

    /**
	 * @param selAbsnceAt the selAbsnceAt to set
	 */
    public void setSelAbsnceAt(String selAbsnceAt) {
        this.selAbsnceAt = selAbsnceAt;
    }
}
