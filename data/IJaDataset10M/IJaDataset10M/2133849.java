package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;
import org.hibernate.HibernateException;
import com.technoetic.xplanner.domain.repository.UserStoryRepository;

public class TestStoryOptionsTag extends AbstractOptionsTagTestCase {

    private UserStory STORY1;

    private UserStory STORY2;

    protected void setUp() throws Exception {
        tag = new DummyStoryOptionsTag();
        super.setUp();
    }

    public void testGetOptionsSimpleCase() throws Exception {
        STORY1 = newUserStory(ITERATION_0_1);
        STORY2 = newUserStory(ITERATION_0_1);
        ArrayList results = new ArrayList();
        results.add(STORY1);
        results.add(STORY2);
        getTag().editableStories = results;
        Collection actualStories = tag.getOptions();
        StoryModel[] expectedOptions = new StoryModel[] { new StoryModel(new IterationModel(ITERATION_0_1), STORY1), new StoryModel(new IterationModel(ITERATION_0_1), STORY2) };
        assertStoriesEquals(expectedOptions, actualStories.toArray());
    }

    private DummyStoryOptionsTag getTag() {
        return (DummyStoryOptionsTag) tag;
    }

    private void assertStoriesEquals(StoryModel[] expectedOptions, Object[] options) {
        assertNotNull(expectedOptions);
        assertNotNull(options);
        assertEquals(expectedOptions.length, options.length);
        for (int i = 0; i < expectedOptions.length; i++) {
            StoryModel expectedOption = expectedOptions[i];
            StoryModel actualOption = (StoryModel) options[i];
            assertEquals(expectedOption.getUserStory(), actualOption.getUserStory());
            assertEquals(expectedOption.getIterationModel(), actualOption.getIterationModel());
        }
    }

    private class DummyStoryOptionsTag extends StoryOptionsTag {

        private UserStoryRepository userStoryRepository = new UserStoryRepository(null, null, 0) {

            public List fetchStoriesWeCanMoveTasksTo(int actualStoryId) {
                return editableStories;
            }

            public Iteration getIteration(UserStory story) {
                return ITERATION_0_1;
            }

            public Project getProject(UserStory story) {
                return PROJECT1;
            }

            public UserStory getUserStory(int storyId) throws HibernateException {
                return STORY1;
            }
        };

        protected UserStoryRepository getUserStoryRepository() {
            return userStoryRepository;
        }

        public ArrayList editableStories;
    }
}
