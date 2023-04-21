package comdemo.internal.feeds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.red5.server.api.IContext;
import org.red5.server.api.IScope;
import org.red5.server.messaging.IMessage;
import org.red5.server.messaging.IMessageInput;
import org.red5.server.stream.IProviderService;
import org.red5.server.stream.message.RTMPMessage;
import com.thebitstream.comserver.feeds.IResourceFeed;
import com.thebitstream.comserver.nodes.IComserverNode;
import com.thebitstream.comserver.stream.IResourceSink;

public class FLVFeed extends Thread implements IResourceFeed {

    public List<IResourceSink> streams = new ArrayList<IResourceSink>();

    public List<String> files = new ArrayList<String>();

    private boolean doRun;

    private int lastTime;

    private long startTime;

    private int streamTime;

    private IMessageInput msgIn;

    private IProviderService providerService;

    private IScope filesScope;

    public FLVFeed(IScope filesScope) {
        this.filesScope = filesScope;
        IContext context = filesScope.getContext();
        providerService = (IProviderService) context.getBean(IProviderService.BEAN_NAME);
    }

    @Override
    public void addResourceSink(IResourceSink arg0) {
        streams.add(arg0);
    }

    @Override
    public void execute(IResourceSink sink) {
    }

    @Override
    public void onClientAdded(IComserverNode arg0) {
        System.out.println("onClientAdded");
    }

    @Override
    public void onClientRemoved(IComserverNode arg0) {
        System.out.println("onClientRemoved");
    }

    @Override
    public void removeResourceSink(IResourceSink arg0) {
        streams.remove(arg0);
    }

    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (files.size() == 0) return;
        int currentFile = 0;
        String input = files.get(currentFile++);
        System.out.println("cue " + input);
        msgIn = providerService.getVODProviderInput(filesScope, input);
        if ((msgIn == null)) return;
        doRun = true;
        while (doRun) {
            IMessage msg;
            try {
                msg = msgIn.pullMessage();
                RTMPMessage lMsg = null;
                if (msg == null) {
                    System.out.println("msg == null");
                    lastTime = -1;
                    doSleep(1000);
                    if (currentFile >= files.size() && files.size() > 0) {
                        currentFile = 0;
                    }
                    input = files.get(currentFile++);
                    System.out.println("cue " + input);
                    msgIn = providerService.getVODProviderInput(filesScope, input);
                    continue;
                }
                long now = 0;
                if (msg instanceof RTMPMessage) {
                    lMsg = (RTMPMessage) msg;
                    if (lastTime == -1) {
                        now = startTime = System.currentTimeMillis();
                        streamTime = 0;
                    } else {
                        now = System.currentTimeMillis() - startTime;
                    }
                    int time = lMsg.getBody().getTimestamp();
                    streamTime = time;
                    if (streamTime > now) {
                        doSleep((int) (streamTime - now));
                    }
                    lastTime = time;
                    for (int i = 0; i < streams.size(); i++) {
                        long delta = startTime - streams.get(i).getStream().getCreationTime();
                        lMsg.getBody().setTimestamp((int) delta + time);
                        streams.get(i).getStream().dispatchStreamEvent(lMsg.getBody());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doSleep(int milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
