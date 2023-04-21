package jather;

import java.io.IOException;
import org.jgroups.blocks.RpcDispatcher;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChannelLoaderTest {

    GroupMember member;

    RpcDispatcher dispatcher;

    ChannelClassLoader classLoader;

    @Before
    public void init() throws Exception {
        member = new GroupMember() {
        };
        dispatcher = new RpcDispatcher(member.getConnectedChannel(), null, null, new JatherHandlerAdapter() {

            HiddenClassLoader cl = new HiddenClassLoader();

            @Override
            public byte[] getResource(String name) {
                try {
                    return Util.toBytes(cl.getResourceAsStream(name));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        classLoader = new ChannelClassLoader(dispatcher, member.getConnectedChannel().getLocalAddress());
    }

    @org.junit.After
    public void After() {
        member.closeChannel();
    }

    @Test
    public void testResource() throws Exception {
        assertNotNull("Make sure the bytes were loaded", classLoader.getResourceAsStream("log4j.properties"));
        assertEquals("Make sure the class was loaded", String.class, classLoader.loadClass("java.lang.String"));
        assertNotNull("Make sure the hidden class was loaded", classLoader.loadClass("hidden.HiddenString"));
    }
}
