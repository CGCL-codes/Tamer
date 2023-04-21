package net.ontopia.topicmaps.nav.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.index.ScopeIndexIF;
import net.ontopia.topicmaps.core.index.ClassInstanceIndexIF;
import net.ontopia.topicmaps.utils.TypeHierarchyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Abstract class which provides access to filtering themes
 * out which are not relevant to the user context.
 */
public class BasenameUserThemeFilter extends AbstractUserThemeFilter {

    static Logger log = LoggerFactory.getLogger(BasenameUserThemeFilter.class.getName());

    protected ScopeIndexIF scopeIndex;

    protected ClassInstanceIndexIF instanceIndex;

    public BasenameUserThemeFilter(TopicMapIF topicMap) {
        setTopicMap(topicMap);
    }

    public void setTopicMap(TopicMapIF topicMap) {
        this.topicMap = topicMap;
        scopeIndex = (ScopeIndexIF) topicMap.getIndex("net.ontopia.topicmaps.core.index.ScopeIndexIF");
        instanceIndex = (ClassInstanceIndexIF) topicMap.getIndex("net.ontopia.topicmaps.core.index.ClassInstanceIndexIF");
    }

    /**
   * Process theme and find out if it belongs to the user context and
   * should therefore be displayed when selecting them. If any of the
   * following rules apply the theme should be not displayed.
   *
   * [Assumption:
   *      Topic "A" is a theme that scopes a base name of topic "B"]
   * 
   *
   * Rule 1: if there exists an association between topic belonging to 
   *         this theme and one or more topics which are related to the
   *         scoped base names
   *         OR
   *         if one or more topics belonging to scoped basename(s)
   *         is/are of type of the theme
   *
   *         ( "B" is associated with "A" OR
   *           "B" is an instance of "A" )
   *
   * Role 2: topic belonging to this theme is used to type association roles
   *         AND
   *         one or more topics belonging to with this theme scoped basenames
   *         are used to type associations in which the
   *         topic belonging to the theme is playing an association role
   *
   *         ( "A" is the role type in at least one association of type "B" AND
   *           "B" is an association type )
   *
   * Role 3: one or more of the topics belonging to the scoped basenames
   *         is/are used as a scope theme itself/themselves
   *
   *         ( "B" is a theme )
   *
   * @return boolean: true if this theme should not be displayed
   *                  for user context configuration
   */
    public boolean shouldNotBeUsed(TopicIF actTheme) {
        boolean usedAsTopicType = false;
        boolean usedAsAssociationRoleType = false;
        TypeHierarchyUtils hierarchyHelper = new TypeHierarchyUtils();
        Collection baseNames = scopeIndex.getTopicNames(actTheme);
        if (baseNames.size() == 0) return true;
        if (instanceIndex.usedAsTopicType(actTheme)) {
            usedAsTopicType = true;
        }
        if (instanceIndex.usedAsAssociationRoleType(actTheme)) {
            usedAsAssociationRoleType = true;
        }
        int nTotalTopics = baseNames.size();
        int nRemainingTopics = nTotalTopics;
        Iterator itNames = baseNames.iterator();
        while (itNames.hasNext()) {
            TopicNameIF baseName = (TopicNameIF) itNames.next();
            TopicIF topic = baseName.getTopic();
            boolean typedByTheme = false;
            boolean associatedWithTheme = false;
            boolean usedToTypeAssocs = false;
            boolean usedAsTopicNameTheme = false;
            if (usedAsTopicType && hierarchyHelper.isInstanceOf(topic, actTheme)) {
                typedByTheme = true;
            } else {
                if (hierarchyHelper.isAssociatedWith(topic, actTheme)) {
                    associatedWithTheme = true;
                } else {
                    if (usedAsAssociationRoleType && instanceIndex.usedAsAssociationType(topic)) {
                        usedToTypeAssocs = true;
                    } else {
                        boolean checkRule3 = false;
                        if (scopeIndex.usedAsTopicNameTheme(topic) && checkRule3) {
                            Iterator itNamesR3 = baseNames.iterator();
                            int nRemainingTopicsR3 = baseNames.size();
                            while (itNamesR3.hasNext()) {
                                TopicNameIF baseNameR3 = (TopicNameIF) itNamesR3.next();
                                TopicIF topicR3 = baseNameR3.getTopic();
                                if (scopeIndex.usedAsTopicNameTheme(topicR3)) {
                                    nRemainingTopicsR3--;
                                } else {
                                    break;
                                }
                            }
                            if (nRemainingTopicsR3 == 0) {
                                nRemainingTopics = 0;
                                break;
                            }
                        }
                    }
                }
            }
            if (associatedWithTheme || typedByTheme || usedToTypeAssocs || usedAsTopicNameTheme) nRemainingTopics--;
        }
        return (nRemainingTopics == 0);
    }
}
