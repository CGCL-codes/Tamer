package com.erlang4j.internal.process;

import static com.erlang4j.api.Erlang4jMessageLanguage.binding;
import static com.erlang4j.api.Erlang4jMessageLanguage.state;
import static com.erlang4j.api.Erlang4jMessageLanguage.string;
import junit.framework.TestCase;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpNode;
import com.erlang4j.api.exceptionHandler.SysoutExceptionHandler;
import com.erlang4j.internal.adapters.MockAdapter;
import com.erlang4j.internal.adapters.MockSwingAdapter;
import com.erlang4j.internal.basicIo.MockBasicMailBox;
import com.erlang4j.internal.messages.MessageComposer;

public class StateMchineOneStateTests extends TestCase {

    private MockAdapter adapter1;

    private MockAdapter adapter2;

    private MockSwingAdapter adapter3;

    private StateMachine messageProcessor;

    private final MessageComposer composer = new MessageComposer(binding());

    private OtpErlangPid self;

    private MockBasicMailBox mockBasicMailBox;

    public void testAccessors() {
        assertEquals(self, messageProcessor.self());
        assertEquals(string("receive"), messageProcessor.receive());
        assertEquals(1, mockBasicMailBox.receiveCount);
        assertEquals(0, mockBasicMailBox.timeout);
        assertFalse(messageProcessor.isAlive());
    }

    public void testProcessor1() {
        messageProcessor.process(composer.compose("{a1,[P0]}", "val1"));
        assertEquals(1, adapter1.getCount());
        assertEquals(0, adapter2.getCount());
        assertEquals(0, adapter3.getCount());
        assertEquals(binding("V1", "val1", "Initial", 1), adapter1.context);
    }

    public void testProcessor2() {
        messageProcessor.process(composer.compose("{a2,P0}", "val1"));
        assertEquals(0, adapter1.getCount());
        assertEquals(1, adapter2.getCount());
        assertEquals(0, adapter3.getCount());
        assertEquals(binding("V2", "val1", "Initial", 1), adapter2.context);
    }

    public void testProcessor3() {
        messageProcessor.process(string("something"));
        assertEquals(0, adapter1.getCount());
        assertEquals(0, adapter2.getCount());
        assertEquals(1, adapter3.getCount());
        assertEquals(binding("Any", "something", "Initial", 1), adapter3.context);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        adapter1 = new MockAdapter("{a1,[V1]}");
        adapter2 = new MockAdapter("{a2,V2}");
        adapter3 = new MockSwingAdapter("Any");
        self = new OtpNode("somevalue").createPid();
        mockBasicMailBox = new MockBasicMailBox(self, string("receive"));
        messageProcessor = new StateMachine(mockBasicMailBox, new SysoutExceptionHandler(), binding("Initial", 1), state(null, adapter1, adapter2, adapter3));
    }
}
