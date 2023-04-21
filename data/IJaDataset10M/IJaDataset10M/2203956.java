package restful;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.*;

public class RestfulTopicTest {

    @Test
    public void testRestfulTopicAreEquals() {
        RestfulTopic topic1 = new RestfulTopic("topic");
        RestfulTopic topic2 = new RestfulTopic("topic");
        assertEquals(topic1, topic2);
    }

    @Test
    public void testRestfulTopicAreNotEquals() {
        RestfulTopic topic1 = new RestfulTopic("topic");
        RestfulTopic topic2 = new RestfulTopic("anotherTopic");
        assertThat(topic2, is(not(equalTo(topic1))));
    }
}
