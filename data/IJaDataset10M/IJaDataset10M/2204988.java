package net.jabah.test;

import static org.easymock.classextension.EasyMock.*;
import junit.framework.TestCase;
import net.jabah.script.Script;
import net.jabah.script.actions.Link;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

public class TestScriptActionLink extends TestCase {

    public void testWithValidLink() throws AgiException {
        AgiChannel channel = createStrictMock(AgiChannel.class);
        AgiRequest request = createStrictMock(AgiRequest.class);
        Script linkedScript = createStrictMock(Script.class);
        linkedScript.execute(request, channel);
        expectLastCall().once();
        replay(channel);
        replay(request);
        replay(linkedScript);
        Link link = new Link();
        link.setScript(null);
        link.execute(request, channel);
    }

    public void testWithInvalidLink() throws AgiException {
        AgiChannel channel = createStrictMock(AgiChannel.class);
        AgiRequest request = createStrictMock(AgiRequest.class);
        replay(channel);
        replay(request);
        Link link = new Link();
        link.setScript(null);
        link.execute(request, channel);
    }
}
