package domain;

import java.util.ArrayList;
import persistentLayer.DBQueries;
import ruleEngine.PersonalConstraint;

/**
 * @author BTS 
 * a TeamMember is a class that represents a team member of a group. 
 * It is the simple form of a worker in the company 
 * and has its specific methods that relate to him.
 */
public class TeamMember extends User {

    /**
		 * @author Badash
		 * default constructor
		 */
    public TeamMember() {
        _personalConstraints = new ArrayList<PersonalConstraint>();
        this._status = Const.VACANT;
        m_Queries = new DBQueries();
    }

    /**
		 * @author Badash
		 * constructor
		 * @param firstName
		 * @param lastName
		 * @param id
		 * @param pwd
		 * @param groupId
		 * @param position
		 * @param levelOfExperience
		 * @param email
		 * @param address
		 * @param permissionLevel
		 * @param familyStatus
		 * @param phoneNum
		 */
    public TeamMember(String firstName, String lastName, int id, String pwd, int groupId, String position, String levelOfExperience, String email, String address, int permissionLevel, String familyStatus, String phoneNum) {
        m_Queries = new DBQueries();
        super.set_address(address);
        super.set_email(email);
        super.set_familyStatus(familyStatus);
        super.set_groupID(groupId);
        this._id = id;
        super.set_lastName(lastName);
        super.set_levelOfExperience(levelOfExperience);
        super.set_password(pwd);
        super.set_position(position);
        this._premissionLevel = permissionLevel;
        super.set_firstName(firstName);
        super.set_phoneNum(phoneNum);
        _personalConstraints = new ArrayList<PersonalConstraint>();
        this._status = Const.VACANT;
    }

    public String getPageToDirect() {
        return "TeamMember.jsp";
    }
}
