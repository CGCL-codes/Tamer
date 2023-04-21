package ru.beta2.testyard.engine;

import ru.beta2.testyard.ExpectMessageScript;
import ru.beta2.testyard.Script;
import ru.beta2.testyard.SkipControl;
import ru.beta2.testyard.config.TaggedHandler;
import ru.beta2.testyard.engine.points.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * User: Inc
 * Date: 19.06.2008
 * Time: 0:17:17
 */
public abstract class AbstractScript implements Script {

    protected Queue<ScriptPoint> points = new LinkedList<ScriptPoint>();

    protected Collection<SkipEntry> skippings = new ArrayList<SkipEntry>();

    protected abstract ScriptContext getContext();

    protected <T extends ScriptPoint> T addPoint(T point) {
        points.add(point);
        point.context = getContext();
        return point;
    }

    public Script login(int player) {
        return addPoint(new LoginPoint(player));
    }

    public Script logout(int player) {
        return addPoint(new LogoutPoint(player));
    }

    public Script expectJoinChannel(int player, String channel) {
        return addPoint(new ExpectPoint(new ChannelEvent(ChannelEvent.JOINED, player, channel)));
    }

    public Script expectLeaveChannel(int player, String channel) {
        return addPoint(new ExpectPoint(new ChannelEvent(ChannelEvent.LEFT, player, channel)));
    }

    public <T> ExpectMessageScript<T> expectSessionMessage(int player, T message) {
        tagMessage(message);
        return addPoint(new ExpectMessagePoint<T>(new MessageEvent(player, message)));
    }

    public <T> ExpectMessageScript<T> expectChannelMessage(int player, String channel, T message) {
        return addPoint(new ExpectMessagePoint<T>(new MessageEvent(player, channel, message)));
    }

    public <T> ExpectMessageScript<T> expectChannelMessage(String channel, T message) {
        return addPoint(new ExpectWholeChannelMessagePoint<T>(channel, message));
    }

    private <T> void tagMessage(T message) {
        if (!getContext().getSequenceGenerator().isAutoTagMessages()) {
            return;
        }
        TaggedHandler th = getContext().getCfg().getTaggedHandler();
        if (th.isTagged(message)) {
            if (th.getTag(message) == 0) {
                th.setTag(message, getContext().getSequenceGenerator().currentValue());
            }
        }
    }

    private void initMessageSeq(Object message) {
        TaggedHandler th = getContext().getCfg().getTaggedHandler();
        if (th.isTagged(message)) {
            if (th.getTag(message) == 0) {
                th.setTag(message, getContext().getSequenceGenerator().genNext());
            } else {
                getContext().getSequenceGenerator().assignValue(th.getTag(message));
            }
        }
    }

    public Script sendSessionMessage(int player, Object message) {
        initMessageSeq(message);
        return addPoint(new SendPoint(player, message));
    }

    public Script sendChannelMessage(int player, String channel, Object message) {
        initMessageSeq(message);
        return addPoint(new SendPoint(player, channel, message));
    }

    public SkipControl skipSessionMessages(Class messageClass) {
        SkipEntry se = new SkipEntry(messageClass);
        skippings.add(se);
        return se;
    }

    public SkipControl skipChannelMessages(String channel, Class messageClass) {
        SkipEntry se = new SkipEntry(channel, messageClass);
        skippings.add(se);
        return se;
    }

    private SkipControl skipChannels(String type) {
        SkipEntry se = new ChannelSkipEntry(type);
        skippings.add(se);
        return se;
    }

    public SkipControl skipJoinChannels() {
        return skipChannels(ChannelEvent.JOINED);
    }

    public SkipControl skipLeaveChannels() {
        return skipChannels(ChannelEvent.LEFT);
    }

    public Script pin() {
        return new PinnedScript(this);
    }

    public Script unpin() {
        return this;
    }
}
