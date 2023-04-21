package com.germinus.xpression.groupware.communities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.germinus.xpression.groupware.CommunityManager;
import com.germinus.xpression.groupware.LiferayPortalException;
import com.germinus.xpression.groupware.LiferayRemoteException;
import com.germinus.xpression.groupware.LiferaySystemException;
import com.germinus.xpression.groupware.util.GroupwareManagerRegistry;
import com.germinus.xpression.groupware.util.LiferayHelperFactory;
import com.germinus.xpression.i18n.I18NString;

public class PublicCommunityType {

    public static final String PUBLIC = "public";

    private static Log log = LogFactory.getLog(PublicCommunityType.class);

    private String groupwareCommunityName;

    private String liferayGroupName;

    private CommunityManager communityManager;

    private I18NString communityTitle;

    public PublicCommunityType(String groupwareCommunityName, I18NString communityTitle, String liferayGroupName) {
        this.groupwareCommunityName = groupwareCommunityName;
        this.liferayGroupName = liferayGroupName;
        this.communityTitle = communityTitle;
    }

    CommunityManager getCommunityManager() {
        if (this.communityManager == null) {
            this.communityManager = GroupwareManagerRegistry.getCommunityManager();
        }
        return this.communityManager;
    }

    public void setCommunityManager(CommunityManager communityManager) {
        this.communityManager = communityManager;
    }

    public Community createMandatoryPublicCommunity() throws CommunityException {
        String publicGroupId = searchExistingPublicGroupId();
        Community community = new Community();
        if (publicGroupId != null) {
            community.setExternalId(publicGroupId);
        }
        community.setName(groupwareCommunityName);
        community.setType(PUBLIC);
        community.setTitle(communityTitle);
        return getCommunityManager().createCommunity(community, null);
    }

    private String searchExistingPublicGroupId() throws CommunityException {
        String publicGroupId = null;
        try {
            publicGroupId = LiferayHelperFactory.getLiferayHelper().getGroupIdByName(liferayGroupName);
        } catch (LiferaySystemException e) {
            throw new CommunityException("Error searching public community group by name[" + liferayGroupName + "]: " + e);
        } catch (LiferayPortalException e) {
            log.info("Public community not found");
        } catch (LiferayRemoteException e) {
            log.info("Public community not found");
        }
        return publicGroupId;
    }
}
