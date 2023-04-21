package net.cepra.test.timecard;

import net.cepra.core.server.ApplicationContext;
import net.cepra.timecard.common.activity.ActivitySearchResult;
import net.cepra.timecard.server.service.IActivityService;
import org.junit.Test;

public class TestActivityOverview {

    @Test
    public void loadActivities() {
        IActivityService service = (IActivityService) ApplicationContext.getInstance().getBean(IActivityService.NAME);
        ActivitySearchResult result = service.search(4711, 2008, 3);
        System.out.println(result.toString());
    }
}
