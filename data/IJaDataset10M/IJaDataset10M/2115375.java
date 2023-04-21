package net.sf.rottz.tv.server.communication.updates.infomessages;

import net.sf.rottz.tv.common.communication.ConnectNewsResponseType;
import net.sf.rottz.tv.common.communication.InfoMessageType;
import net.sf.rottz.tv.common.communication.UpdateType;
import net.sf.rottz.tv.common.media.NewsCategory;
import net.sf.rottz.tv.server.world.PlayerServer;

public class ServerUpdateConnectNewsResponse extends ServerUpdateInfoMessage {

    private final ConnectNewsResponseType response;

    private final NewsCategory newsType;

    private final boolean connectStatus;

    public ServerUpdateConnectNewsResponse(PlayerServer player, NewsCategory newsType, boolean connectStatus, ConnectNewsResponseType response) {
        super(player);
        this.newsType = newsType;
        this.connectStatus = connectStatus;
        this.response = response;
        buildOutputPacket();
    }

    @Override
    public InfoMessageType getMessageType() {
        return InfoMessageType.CONNECT_NEWS_RESPONSE;
    }

    @Override
    public int[] getMessageValues() {
        int connectValue = 0;
        if (connectStatus == true) connectValue = 1;
        return new int[] { newsType.ordinal(), connectValue, response.ordinal() };
    }

    @Override
    public UpdateType getUpdateType() {
        return UpdateType.CONNECT_NEWS_RESPONSE;
    }
}
