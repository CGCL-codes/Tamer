package com.ashridgetech.agiliplan.manager.hibernate;

import java.util.List;
import com.ashridgetech.agiliplan.dto.GroupCreateInfo;
import com.ashridgetech.agiliplan.dto.StoryCreateInfo;
import com.ashridgetech.agiliplan.entity.Group;
import com.ashridgetech.agiliplan.entity.Story;
import com.ashridgetech.agiliplan.service.Application;
import com.ashridgetech.riabase.entity.ValidationException;
import com.ashridgetech.riabase.manager.hibernate.ManagerImplTestBase;

public abstract class AgiliplanManagerImplTestBase extends ManagerImplTestBase {

    protected Application application;

    @Override
    protected String[] getConfigLocations() {
        if ("true".equals(System.getProperty("useIntTestConfig"))) {
            System.out.println("Using int test config");
            return new String[] { "applicationContext.xml", "applicationContext-int-test.xml" };
        } else {
            return new String[] { "applicationContext.xml", "applicationContext-unit-test.xml" };
        }
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    protected String description(String title) {
        return "Description of " + title;
    }

    protected Story createStory(Group group, String storyTitle) throws ValidationException {
        StoryCreateInfo info = new StoryCreateInfo();
        info.setTitle(storyTitle);
        info.setDescription(description(storyTitle));
        info.setGroup(group.getId());
        return application.getStoryManager().create(info);
    }

    protected Group createGroup(String name) throws ValidationException {
        GroupCreateInfo info = new GroupCreateInfo();
        info.setName(name);
        return application.getGroupManager().create(info);
    }

    @SuppressWarnings("unchecked")
    protected List query(String query) {
        return sessionFactory.getCurrentSession().createQuery(query).list();
    }
}
