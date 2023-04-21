package org.cid.distribution.plugins.list;

import java.util.Enumeration;
import java.util.LinkedList;
import org.apache.commons.collections.IteratorEnumeration;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.plugins.PluginCreateRule;
import org.apache.log4j.Logger;
import org.cid.distribution.base.DistributionComponentBase;

/**
 * <p>The StaticMemberManagerComponent is a read-only provider of {@link DistributionMember}
 * objects. Members are defined in the list's configuration file,
 * and cannot be modified.</p>  
 * @version $Revision:111 $
 */
public class StaticMemberManagerComponent extends DistributionComponentBase implements MemberManager {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(StaticMemberManagerComponent.class);

    /**
	 * The list of members.
	 */
    protected LinkedList<DistributionMember> members;

    /**
	 * Class constructor
	 */
    public StaticMemberManagerComponent() {
        super();
        members = new LinkedList<DistributionMember>();
    }

    /**
	 * Returns the list members.
	 * @return The lists members.
	 */
    public Enumeration<DistributionMember> getMembers() {
        return new IteratorEnumeration(members.iterator());
    }

    /**
	 * Adds a member to this member list.
	 * @param member The member to add.
	 */
    public void addMember(DistributionMember member) {
        this.members.add(member);
    }

    /**
	 * Configures additional rules for the commons-digester library.
	 */
    public static void addRules(Digester d, String patternPrefix) {
        DistributionComponentBase.addRules(d, patternPrefix);
        PluginCreateRule mcr = new PluginCreateRule(DistributionMember.class, SimpleDistributionMember.class);
        d.addRule(patternPrefix + "/members/member", mcr);
        d.addSetProperties(patternPrefix + "/members/member");
        d.addSetNext(patternPrefix + "/members/member", "addMember");
    }
}
