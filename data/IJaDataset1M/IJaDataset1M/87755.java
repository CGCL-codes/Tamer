package enzimaweb.ui;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import enzimaweb.WebSerializer;

public class ListItemTest {

    private ListItem listItem;

    private WebSerializer mockedSerializer;

    @Before
    public void setUp() throws Exception {
        mockedSerializer = createMock(WebSerializer.class);
        listItem = new ListItem();
        listItem.getInfo().setId("sample_id");
    }

    @Test
    public void testToString() {
        Assert.assertEquals("toString() should be equals to getInfo().getId().", listItem.getInfo().getId(), listItem.toString());
    }

    @Test
    public void testSerialize() {
        mockedSerializer.openListItem(listItem.getInfo());
        mockedSerializer.closeListItem();
        replay(mockedSerializer);
        listItem.serialize(mockedSerializer);
        verify(mockedSerializer);
    }
}
