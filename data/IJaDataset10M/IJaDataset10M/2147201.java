package server.handler;

import io.Repository;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import message.ResultResponse;
import message.admin.AddPresence;
import message.admin.CloseChannel;
import message.admin.CountOpenChannel;
import message.admin.ListPresence;
import message.admin.ListOpenChannel;
import message.admin.OpenChannel;
import message.admin.RemovePresence;
import org.slf4j.Logger;
import server.managed.AdminData;
import server.managed.ChannelData;
import server.managed.Presence;
import util.Log;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.NameNotBoundException;
import common.Constants;
import common.Result;

public class AdminHandler implements ClientSessionListener, Serializable {

    private static final Logger logger = Log.getLogger(AdminHandler.class);

    private static final long serialVersionUID = 7443172645636588896L;

    private String bindingKey;

    private ClientSession ses;

    public AdminHandler(ClientSession ses, String bindingKey) {
        this.ses = ses;
        this.bindingKey = bindingKey;
    }

    public void disconnected(boolean graceful) {
        AdminData data = getData();
        String name = data.getSession().getName();
        String grace = graceful ? "normal" : "by force";
        logger.info(String.format("d/c '%s' (%s)", name, grace));
        DataManager dman = AppContext.getDataManager();
        dman.removeBinding(bindingKey);
        dman.removeObject(data);
        logger.info(String.format("unbound '%s'", bindingKey));
    }

    public void send(Object o) throws IOException {
        ses.send(Repository.getInstance().toByteArray(o));
    }

    public void receivedMessage(byte[] bytes) {
        try {
            Object o = Repository.getInstance().fromByteArray(bytes, 0);
            processMessage(o);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private AdminData getData() {
        DataManager dman = AppContext.getDataManager();
        return dman.getBinding(bindingKey, AdminData.class);
    }

    private void processMessage(Object o) {
        if (o instanceof AddPresence) {
            doAddPresence((AddPresence) o);
        } else if (o instanceof RemovePresence) {
            doRemovePresence((RemovePresence) o);
        } else if (o instanceof ListPresence) {
            doListPresence((ListPresence) o);
        } else if (o instanceof OpenChannel) {
            doOpenChannel((OpenChannel) o);
        } else if (o instanceof CloseChannel) {
            doCloseChannel((CloseChannel) o);
        } else if (o instanceof CountOpenChannel) {
            doCountOpenChannel((CountOpenChannel) o);
        } else if (o instanceof ListOpenChannel) {
            doListOpenChannel((ListOpenChannel) o);
        }
    }

    private void doAddPresence(AddPresence msg) {
        try {
            ChannelData channelData = ChannelData.getChannelData(msg.channelName);
            Result response = Result.UNKNOWN;
            if (channelData == null) {
                response = Result.NOT_FOUND;
            } else {
                Presence p = new Presence(msg.presenceName, false);
                boolean succeeded = channelData.addPresence(p);
                if (succeeded) {
                    response = Result.OK;
                } else {
                    response = Result.ALREADY_EXISTS;
                }
            }
            send(new ResultResponse(response));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void doRemovePresence(RemovePresence msg) {
        try {
            ChannelData channelData = ChannelData.getChannelData(msg.channelName);
            Result response = Result.UNKNOWN;
            if (channelData == null) {
                response = Result.NOT_FOUND;
            } else {
                Presence p = new Presence(msg.presenceName, false);
                channelData.removePresence(p);
                response = Result.OK;
            }
            send(new ResultResponse(response));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void doListPresence(ListPresence msg) {
        try {
            ChannelData channelData = ChannelData.getChannelData(msg.channelName);
            Result result = Result.UNKNOWN;
            StringBuffer list = new StringBuffer();
            if (channelData == null) {
                result = Result.NOT_FOUND;
            } else {
                String ls = System.getProperty("line.separator");
                list.append("result: ");
                for (Presence p : channelData.getPresences()) {
                    list.append(ls);
                    list.append(p.getAccount());
                }
                result = Result.OK;
            }
            send(new ResultResponse(new Result(result.getCode(), list.toString())));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void doOpenChannel(OpenChannel oc) {
        try {
            ChannelManager cman = AppContext.getChannelManager();
            DataManager dman = AppContext.getDataManager();
            String boundName = Constants.CHANNEL_DATA + oc.channelName;
            Result response = Result.UNKNOWN;
            try {
                dman.getBinding(boundName, ChannelData.class);
                response = Result.ALREADY_EXISTS;
            } catch (NameNotBoundException ex) {
                logger.info(String.format("creating channel '%s'", oc.channelName));
                Channel newChannel = cman.createChannel(oc.channelName, null, Delivery.RELIABLE);
                ChannelData data = new ChannelData(newChannel);
                dman.setBinding(boundName, data);
                if (oc.presences != null) {
                    List<String> presences = Arrays.asList(oc.presences);
                    for (String p : presences) {
                        data.addPresence(new Presence(p, false));
                    }
                }
                logger.info(String.format("instance of %s bound to '%s'", data.getClass().getName(), boundName));
                response = Result.OK;
            }
            send(new ResultResponse(response));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void doCloseChannel(CloseChannel cc) {
        try {
            ChannelManager cman = AppContext.getChannelManager();
            DataManager dman = AppContext.getDataManager();
            String boundName = Constants.CHANNEL_DATA + cc.channelName;
            try {
                ChannelData data = dman.getBinding(boundName, ChannelData.class);
                dman.removeBinding(boundName);
                dman.removeObject(data);
                logger.info(String.format("instance of %s released from '%s'", data.getClass().getName(), boundName));
                Channel c = cman.getChannel(cc.channelName);
                logger.info("closing channel: " + c.getName());
                c.close();
            } catch (NameNotBoundException ex) {
            }
            send(new ResultResponse(Result.OK));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void doCountOpenChannel(CountOpenChannel channel) {
        try {
            int count = 0;
            String temp = null;
            DataManager dman = AppContext.getDataManager();
            while ((temp = dman.nextBoundName(temp)) != null) {
                if (temp.startsWith(Constants.CHANNEL_DATA)) {
                    count++;
                }
            }
            send(new ResultResponse(new Result(Result.OK.getCode(), Integer.toString(count))));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }

    private void doListOpenChannel(ListOpenChannel channel) {
        try {
            StringBuffer response = new StringBuffer();
            String temp = null;
            DataManager dman = AppContext.getDataManager();
            while ((temp = dman.nextBoundName(temp)) != null) {
                if (temp.startsWith(Constants.CHANNEL_DATA)) {
                    response.append(temp.substring(Constants.CHANNEL_DATA.length()));
                }
            }
            send(new ResultResponse(new Result(Result.OK.getCode(), response.toString())));
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
        }
    }
}
