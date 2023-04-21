package ie.blackoutscout.common.beans.interfaces;

/**
 * This interface is for managing the Member bean.
 */
public interface IMember extends IBean {

    Integer getActive();

    void setActive(Integer active);

    Double getCash();

    void setCash(Double cash);

    Long getDateRegistered();

    void setDateRegistered(Long dateRegistered);

    String getEmail();

    void setEmail(String email);

    Long getLastClick();

    void setLastClick(Long lastClick);

    Long getMemberId();

    void setMemberId(Long memberId);

    Integer getMemberLevel();

    void setMemberLevel(Integer memberLevel);

    String getRealName();

    void setRealName(String realName);

    Long getTeamId();

    void setTeamId(Long teamId);

    String getUserName();

    void setUserName(String userName);

    Integer getSurveyed();

    void setSurveyed(Integer intValue);

    Integer getMoves();

    void setMoves(Integer intValue);

    Integer getRenames();

    void setRenames(Integer intValue);
}
