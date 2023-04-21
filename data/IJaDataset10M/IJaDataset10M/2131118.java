package fr.fg.client.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import fr.fg.client.ajax.Action;
import fr.fg.client.ajax.ActionCallbackAdapter;
import fr.fg.client.core.settings.Settings;
import fr.fg.client.core.tactics.TacticsTools;
import fr.fg.client.data.AllyData;
import fr.fg.client.data.AnswerData;
import fr.fg.client.data.ChatChannelData;
import fr.fg.client.data.ChatChannelsData;
import fr.fg.client.data.ChatMessageData;
import fr.fg.client.openjwt.animation.TimerHandler;
import fr.fg.client.openjwt.animation.TimerManager;
import fr.fg.client.openjwt.core.Config;
import fr.fg.client.openjwt.core.EventManager;
import fr.fg.client.openjwt.core.TextManager;
import fr.fg.client.openjwt.core.ToolTipManager;
import fr.fg.client.openjwt.core.TextManager.OutlineText;
import fr.fg.client.openjwt.ui.JSScrollPane;
import fr.fg.client.openjwt.ui.JSTextField;

public class Chat extends AbsolutePanel implements EventPreview, EventListener {

    private JSScrollPane scrollPane;

    private FlowPanel messagesLayout, chatBar;

    private Widget[] channelsTab;

    private JSTextField writeMessageField;

    private String lastWhisperAuthor;

    private int currentChannelIndex;

    private ArrayList<String> completion;

    private Action currentAction;

    private List<Channel> channels;

    private String[] channelsStyles;

    public Chat() {
        super(DOM.createDiv());
        this.currentChannelIndex = 0;
        this.completion = new ArrayList<String>();
        this.channels = new ArrayList<Channel>();
        this.channelsTab = new Widget[0];
        Element element = getElement();
        element.setId("chat");
        element.setAttribute("unselectable", "on");
        messagesLayout = new FlowPanel();
        messagesLayout.addStyleName("chat");
        chatBar = new FlowPanel();
        chatBar.getElement().setId("chatBar");
        add(chatBar);
        writeMessageField = new JSTextField();
        writeMessageField.setPixelHeight(24);
        writeMessageField.setMaxLength(180);
        chatBar.add(writeMessageField);
        writeMessageField.setFocus(true);
        chatBar.setVisible(false);
        scrollPane = new JSScrollPane();
        scrollPane.getElement().setId("chatMessages");
        scrollPane.setView(messagesLayout);
        scrollPane.setPixelSize(400, 104);
        add(scrollPane);
        updateSize();
        EventManager.addEventHook(this);
        sinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
    }

    public void setChannels(ChatChannelsData channelsData) {
        ArrayList<Channel> newChannels = new ArrayList<Channel>();
        for (int i = 0; i < channelsData.getChannelsCount(); i++) {
            ChatChannelData channelData = channelsData.getChannelAt(i);
            newChannels.add(new Channel(channelData.getName(), channelData.isActive()));
        }
        Collections.sort(newChannels, new Comparator<Channel>() {

            public int compare(Channel c1, Channel c2) {
                int priority1, priority2;
                AllyData ally = Client.getInstance().getAllyDialog().getAlly();
                String allyName = ally.getId() != 0 ? ally.getName() : "";
                if (c1.getName().startsWith("%")) {
                    priority1 = 5;
                } else if (c1.getName().startsWith("@")) {
                    priority1 = 4;
                } else if (c1.getName().startsWith("$")) {
                    if (c1.getName().substring(1).equals(allyName)) priority1 = 3; else priority1 = 2;
                } else {
                    priority1 = 1;
                }
                if (c2.getName().startsWith("%")) {
                    priority2 = 5;
                } else if (c2.getName().startsWith("@")) {
                    priority2 = 4;
                } else if (c2.getName().startsWith("$")) {
                    if (c2.getName().substring(1).equals(allyName)) priority2 = 3; else priority2 = 2;
                } else {
                    priority2 = 1;
                }
                if (priority1 < priority2) return 1; else if (priority1 > priority2) return -1; else return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        this.channels = newChannels;
        updateTabLabels();
    }

    public void updateSize() {
        int clientWidth = Window.getClientWidth();
        int width, height;
        if (clientWidth <= 1024) {
            width = Math.min(clientWidth - 470, 500);
            height = 91;
        } else {
            width = Math.min(clientWidth - 508, 560);
            height = 117;
        }
        scrollPane.setPixelSize(width, height);
        chatBar.setWidth(width + "px");
        writeMessageField.setPixelWidth(width - 31);
        scrollDown();
    }

    public void scrollDown() {
        scrollPane.update();
        scrollPane.scrollDown(9999);
    }

    public void addMessage(ChatMessageData messageData) {
        addMessage(messageData.getType(), messageData.getChannel(), messageData.getAuthor(), messageData.getContent(), messageData.hasAllyTag() ? messageData.getAllyTag() : null, messageData.hasAllyTag() ? messageData.getAllyName() : null);
    }

    public void addMessage(String type, String channelName, String author, String content, String allyTag, String allyName) {
        boolean hasAllyTag = allyTag != null;
        String richContent = Utilities.parseUrlAndSmilies(content);
        richContent = TacticsTools.parseTacticsLinks(richContent);
        boolean scroll = messagesLayout.getWidgetCount() < 10 || Math.floor(scrollPane.getScroll()) == 1;
        if (type.equals("default")) {
            if (content.startsWith("/me")) {
                content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\">" + (hasAllyTag ? "<span class=\"allyTag\">[" + allyTag + "]</span> " : "") + "</td><td>" + "<span class=\"sender\"><span class=\"author\">" + author + "</span></span>" + richContent.substring(3) + "</td></tr></table>";
                type = "action";
            } else {
                content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\">" + (hasAllyTag ? "<span class=\"allyTag\">[" + allyTag + "]</span> " : "") + "</td><td>" + "<span class=\"sender\"><span class=\"author\">" + author + "</span> : </span>" + richContent + "</td></tr></table>";
                type = "default";
            }
        } else if (type.equals("connection")) {
            content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"" + (hasAllyTag ? " rel=\"" + allyName + "\"" : "") + ">" + (hasAllyTag ? "<span class=\"allyTag\">[" + allyTag + "]</span> " : "") + "</td><td>" + "<span class=\"author\">" + author + "</span> s'est connecté(e)." + "</td></tr></table>";
            type = "notification";
        } else if (type.equals("disconnection")) {
            content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"" + (hasAllyTag ? " rel=\"" + allyName + "\"" : "") + ">" + (hasAllyTag ? "<span class=\"allyTag\">[" + allyTag + "]</span> " : "") + "</td><td>" + "<span class=\"author\">" + author + "</span> s'est déconnecté(e)." + "</td></tr></table>";
            type = "notification";
        } else if (type.equals("who") || type.equals("online") || type.equals("allyMembers")) {
            String[] players = content.split(",");
            String[] logins = new String[players.length], allyTags = new String[players.length], allyNames = new String[players.length];
            for (int i = 0; i < players.length; i++) {
                String player = players[i];
                if (player.contains("|")) {
                    logins[i] = player.substring(0, player.indexOf("|"));
                    allyTags[i] = player.substring(player.indexOf("|") + 1, player.lastIndexOf("|"));
                    allyNames[i] = player.substring(player.lastIndexOf("|") + 1);
                } else {
                    logins[i] = player;
                    allyTags[i] = null;
                    allyNames[i] = null;
                }
            }
            if (type.equals("allyMembers")) content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"></td><td>" + "Il y a " + players.length + " membre" + (players.length > 1 ? "s" : "") + " de votre alliance connecté" + (players.length > 1 ? "s" : "") + " :"; else content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"></td><td>" + "Il y a " + players.length + " joueur" + (players.length > 1 ? "s" : "") + " connecté" + (players.length > 1 ? "s" : "") + (type.equals("who") ? " sur le canal :" : " sur Fallen Galaxy :");
            type = "info";
            for (int i = 0; i < logins.length; i++) {
                content += (i == 0 ? "" : ",") + " " + (allyTags[i] != null ? "<span class=\"inlineAllyTag\" rel=\"" + allyNames[i] + "\">[" + allyTags[i] + "]</span>&nbsp;" : "") + "<span class=\"author\">" + logins[i] + "</span>";
            }
            content += "</td></tr></table>";
        } else if (type.equals("ping")) {
            double ping = Double.parseDouble(content);
            Date date = new Date();
            content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"></td><td>" + "Ping : " + (int) Math.ceil(date.getTime() - ping) + "ms" + "</td></tr></table>";
            type = "info";
        } else if (type.equals("whisperSent")) {
            content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"></td><td>" + "Vous murmurez à " + (hasAllyTag ? "<span class=\"inlineAllyTag\">[" + allyTag + "]</span> " : "") + "<span class=\"author\">" + author + "</span> : " + richContent + "</td></tr></table>";
            type = "whisper";
            if (!author.equals(Settings.getPlayerLogin())) lastWhisperAuthor = author;
        } else if (type.equals("whisperReceived")) {
            content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"" + (hasAllyTag ? " rel=\"" + allyName + "\"" : "") + ">" + (hasAllyTag ? "<span class=\"allyTag\">[" + allyTag + "]</span> " : "") + "</td><td>" + "<span class=\"author\">" + author + "</span> vous murmure : " + richContent + "</td></tr></table>";
            type = "whisper";
            if (!author.equals(Settings.getPlayerLogin())) lastWhisperAuthor = author;
        } else {
            content = "<table cellspacing=\"0\">" + "<tr><td class=\"allyTag\"></td><td>" + richContent + "</td></tr></table>";
        }
        OutlineText message = TextManager.getText(content, false, true);
        String channelStyle = "";
        for (int i = 0; i < channels.size(); i++) if (channels.get(i).getName().equals(channelName)) channelStyle = channelsStyles[i];
        message.addStyleName("msg " + type + (channelStyle.length() > 0 ? " channel-" + channelStyle : ""));
        NodeList<com.google.gwt.dom.client.Element> elements = message.getElement().getElementsByTagName("span");
        for (int i = 0; i < elements.getLength(); i++) {
            com.google.gwt.dom.client.Element element = elements.getItem(i);
            if (element.getClassName().toLowerCase().contains("allytag")) {
                String rel = element.getAttribute("rel");
                ToolTipManager.getInstance().register(element, rel.length() == 0 ? allyName : rel);
            }
        }
        messagesLayout.add(message);
        if (messagesLayout.getWidgetCount() > 50) messagesLayout.remove(0);
        if (!completion.contains(author)) completion.add(author);
        scrollPane.update();
        if (scroll) scrollDown();
    }

    public void clearMessages() {
        messagesLayout.clear();
        scrollPane.update();
    }

    public boolean isChatBarVisible() {
        return chatBar.isVisible();
    }

    public void setChatBarVisible(boolean visible) {
        if (visible) {
            chatBar.setVisible(true);
            writeMessageField.setFocus(true);
        } else {
            chatBar.setVisible(false);
            writeMessageField.setFocus(false);
        }
        Element div = DOM.createDiv();
        Client.getInstance().getAreaContainer().getElement().appendChild(div);
        Client.getInstance().getAreaContainer().getElement().removeChild(div);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) setChatBarVisible(false);
    }

    public void setWhisper(String login) {
        if (!isChatBarVisible()) {
            setChatBarVisible(true);
            writeMessageField.setText(login.length() > 0 ? "/w \"" + login + "\" " : "/w ");
            writeMessageField.setFocus(true);
        } else if (writeMessageField.getText().length() == 0) {
            writeMessageField.setText(login.length() > 0 ? "/w \"" + login + "\" " : "/w ");
            writeMessageField.setFocus(true);
        }
    }

    public void setChannelIndex(int channelIndex) {
        if (channelIndex == currentChannelIndex) return;
        this.currentChannelIndex = channelIndex;
        updateTabLabels();
    }

    public void onBrowserEvent(Event event) {
        switch(event.getTypeInt()) {
            case Event.ONCLICK:
                for (int i = 0; i < channelsTab.length; i++) if (channelsTab[i].getElement().isOrHasChild(event.getTarget())) {
                    setChannelIndex(i);
                    writeMessageField.setFocus(true);
                    if (event.getTarget().getClassName().contains("channel-state")) {
                        if (currentAction != null && currentAction.isPending()) return;
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("channel", channels.get(i).getName());
                        params.put("enable", String.valueOf(!channels.get(i).isActive()));
                        currentAction = new Action("chat/setchannelenable", params, UpdateManager.UPDATE_CALLBACK);
                    }
                    break;
                }
                if (event.getTarget().getClassName().indexOf("author") != -1) {
                    setWhisper(event.getTarget().getInnerHTML());
                    break;
                }
                break;
            case Event.ONKEYDOWN:
                if (writeMessageField.getElement().isOrHasChild(event.getTarget())) {
                    int keyCode = event.getKeyCode();
                    switch(keyCode) {
                        case 37:
                            if (currentChannelIndex > 0 && writeMessageField.getText().length() == 0) {
                                setChannelIndex(currentChannelIndex - 1);
                                event.preventDefault();
                                event.cancelBubble(true);
                            }
                            break;
                        case 39:
                            if (currentChannelIndex < channels.size() - 1 && writeMessageField.getText().length() == 0) {
                                setChannelIndex(currentChannelIndex + 1);
                                event.preventDefault();
                                event.cancelBubble(true);
                            }
                            break;
                        case 3:
                        case 13:
                            sendMessage();
                            break;
                        case 9:
                            event.preventDefault();
                            event.cancelBubble(true);
                            String text = writeMessageField.getText();
                            if (text.length() > 0) {
                                String beginPlayerName;
                                if (text.indexOf(" ") == -1) beginPlayerName = text; else beginPlayerName = text.substring(text.lastIndexOf(" ") + 1);
                                if (beginPlayerName.length() > 0) {
                                    beginPlayerName = beginPlayerName.toLowerCase();
                                    for (String playerName : completion) {
                                        if (playerName.toLowerCase().indexOf(beginPlayerName) == 0) {
                                            writeMessageField.setText(text.substring(0, text.length() - beginPlayerName.length()) + playerName + " ");
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
                break;
        }
    }

    public boolean onEventPreview(Event event) {
        switch(event.getTypeInt()) {
            case Event.ONKEYDOWN:
                if (event.getTarget().getNodeName().toLowerCase().equals("input")) return true;
                int keyCode = event.getKeyCode();
                switch(keyCode) {
                    case 82:
                        event.preventDefault();
                        event.cancelBubble(true);
                        setWhisper(lastWhisperAuthor != null ? lastWhisperAuthor : "");
                        break;
                    case 3:
                    case 13:
                        if (isVisible()) {
                            writeMessageField.setText("");
                            setChatBarVisible(!isChatBarVisible());
                        }
                        break;
                }
                break;
        }
        return true;
    }

    private void updateTabLabels() {
        if (currentChannelIndex >= channels.size()) currentChannelIndex = 0;
        for (int i = 0; i < channelsTab.length; i++) chatBar.remove(channelsTab[i]);
        channelsTab = new Widget[channels.size()];
        channelsStyles = new String[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            String style, label;
            if (channel.getName().startsWith("%")) {
                label = "Public " + channel.getName().substring(1);
                style = "public";
            } else if (channel.getName().startsWith("@")) {
                label = "Alliance";
                style = "ally";
            } else if (channel.getName().startsWith("$")) {
                AllyData ally = Client.getInstance().getAllyDialog().getAlly();
                if (ally.getId() != 0 && ally.getName().equals(channel.getName().substring(1))) {
                    label = "Ambassade";
                    style = "selfEmbassy";
                } else {
                    label = channel.getName().substring(1);
                    style = "embassy";
                }
            } else {
                label = channel.getName();
                style = "custom";
            }
            channelsStyles[i] = style;
            if (i == currentChannelIndex && !style.equals("custom")) label += " <img class=\"channel-state" + (!channel.isActive() ? " channel-state-disabled" : "") + "\" src=\"" + Config.getMediaUrl() + "images/misc/blank.gif\" unselectable=\"on\"/>";
            SimplePanel channelTab = new SimplePanel();
            channelTab.setStylePrimaryName("channel channel-" + style);
            OutlineText text = TextManager.getText(label);
            channelTab.getElement().setAttribute("unselectable", "on");
            channelTab.add(text);
            chatBar.insert(channelTab, i);
            channelsTab[i] = channelTab;
            if (i == currentChannelIndex) channelTab.addStyleDependentName("active"); else channelTab.removeStyleDependentName("active");
        }
    }

    private void sendMessage() {
        if (currentAction != null && currentAction.isPending()) return;
        String text = writeMessageField.getText();
        if (text.length() > 0) {
            if (text.equals("/debug")) {
                writeMessageField.setText("");
                setChatBarVisible(false);
                if (DOM.getElementById("console").getStyle().getProperty("display").equals("block")) {
                    addMessage("info", "", "", "Mode debug désactivé", null, null);
                    DOM.getElementById("console").getStyle().setProperty("display", "none");
                } else {
                    addMessage("info", "", "", "Mode debug activé", null, null);
                    DOM.getElementById("console").getStyle().setProperty("display", "block");
                }
                return;
            } else if (text.equals("/dumptimers")) {
                writeMessageField.setText("");
                setChatBarVisible(false);
                StringBuffer timers = new StringBuffer();
                int baseCount = TimerManager.getBaseUnitHandlers().size();
                int secondCount = TimerManager.getSecondUnitHandlers().size();
                int minuteCount = TimerManager.getMinuteUnitHandlers().size();
                timers.append("Total=" + (baseCount + secondCount + minuteCount) + ", base=" + baseCount + ", second=" + secondCount + ", minute=" + minuteCount + "\n");
                for (TimerHandler handler : TimerManager.getBaseUnitHandlers()) timers.append("[B]" + handler + ", ");
                for (TimerHandler handler : TimerManager.getSecondUnitHandlers()) timers.append("[S]" + handler + ", ");
                for (TimerHandler handler : TimerManager.getMinuteUnitHandlers()) timers.append("[M]" + handler + ", ");
                Utilities.log(timers.toString());
                return;
            }
            if (text.indexOf("/ping") == 0) {
                Date date = new Date();
                text = "/ping " + date.getTime();
            }
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("message", text);
            params.put("channel", channels.get(currentChannelIndex).getName());
            currentAction = new Action("chat/addmessage", params, new ActionCallbackAdapter() {

                public void onSuccess(AnswerData data) {
                    UpdateManager.UPDATE_CALLBACK.onSuccess(data);
                    writeMessageField.setText("");
                    setChatBarVisible(false);
                }
            });
        } else {
            setChatBarVisible(false);
        }
    }

    private class Channel {

        private String name;

        private boolean active;

        public Channel(String name, boolean active) {
            this.name = name;
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        public String getName() {
            return name;
        }
    }
}
