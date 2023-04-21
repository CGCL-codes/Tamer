package erki.abcpeter.parsers.mailbox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;
import erki.abcpeter.BotInterface;
import erki.abcpeter.Parser;
import erki.abcpeter.bots.ErkiTalkBot;
import erki.abcpeter.msgs.observers.QueryMessageObserver;
import erki.abcpeter.msgs.observers.RawMessageObserver;
import erki.abcpeter.msgs.observers.TextMessageObserver;
import erki.abcpeter.msgs.receive.QueryMessage;
import erki.abcpeter.msgs.receive.RawMessage;
import erki.abcpeter.msgs.receive.TextMessage;
import erki.abcpeter.msgs.response.QueryResponseMessage;
import erki.abcpeter.msgs.response.RawResponseMessage;
import erki.abcpeter.msgs.response.ResponseMessage;
import erki.abcpeter.util.Logger;

/**
 * This class implements a mailbox for ErkiTalk. If one tries to use it with any
 * concrete subclass different from {@link ErkiTalkBot} it will deactivate
 * itself and log a warning. It checks if a user is authed as himself at the
 * server before allowing him to send or receive mail. This is also the reason
 * why this {@link Parser} cannot be used with irc as it is not possible to
 * determine whether or not a user is the one he pretends to be in a general
 * way.
 * 
 * @author Edgar Kalkowski
 */
public class Mailbox implements Parser, QueryMessageObserver {

    private static final String MSGS_FILE = "config" + System.getProperty("file.separator") + "msgs";

    private TreeMap<String, LinkedList<Message>> msgs;

    private TreeMap<String, Session> sessions = new TreeMap<String, Session>();

    private BotInterface bot;

    @Override
    public void init(final BotInterface bot) {
        this.bot = bot;
        if (!(bot instanceof ErkiTalkBot)) {
            Logger.warning(this, "Mailbox deactivated as it can only be used " + "with ErkiTalk!");
        } else {
            loadMessages();
            bot.register(this);
            bot.register(new TextMessageObserver() {

                @Override
                public LinkedList<? extends ResponseMessage> inform(TextMessage message) {
                    LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
                    String newMail = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,] ?(([nN]eue )?[nN]achricht an|" + "[mM]ail ?to) (.*)";
                    if (message.getText().matches(newMail)) {
                        result.add(new ResponseMessage("Bitte mittels " + "persönlicher Nachrichten mit der Mailbox " + "kommunizieren!", 100, 2000));
                    }
                    String status = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,] ?([Mm]ailbox ?[sS]tatus\\??!?\\.?|" + "[sS]tatus der [mM]ailbox\\??!?\\.?)";
                    if (message.getText().matches(status)) {
                        synchronized (msgs) {
                            if (msgs.keySet().isEmpty()) {
                                result.add(new ResponseMessage("Momentan sind " + "keine Nachrichten zum Versenden " + "gespeichert.", 100, 3000));
                            } else {
                                result.add(new ResponseMessage("Momentan " + "sind folgenden Nachrichten zum " + "Versenden gespeichert:", 100, 2000));
                                int i = 100;
                                for (String user : msgs.keySet()) {
                                    LinkedList<Message> m = msgs.get(user);
                                    if (m.size() == 1) {
                                        result.add(new ResponseMessage("Eine Nachricht für " + user + ".", 100, 2000 + i));
                                    } else {
                                        result.add(new ResponseMessage(m.size() + " Nachrichten für " + user + ".", 100, 2000 + i));
                                    }
                                    i += 100;
                                }
                            }
                        }
                    }
                    return result;
                }
            });
            new Thread() {

                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            Thread.sleep(300000);
                        } catch (InterruptedException e) {
                        }
                        Logger.debug(this, "Checking mail for online users " + "...");
                        for (final String user : bot.getUserList()) {
                            synchronized (msgs) {
                                if (msgs.keySet().contains(user)) {
                                    bot.register(new RawMessageObserver() {

                                        @Override
                                        public LinkedList<? extends ResponseMessage> inform(RawMessage message) {
                                            Logger.debug(this, "Mailbox has " + "been informed about " + "the raw message »" + message.getText() + "«.");
                                            if (message.getText().toUpperCase().startsWith("ISAUTHED ")) {
                                                String name = message.getText().substring("ISAUTHED ".length(), message.getText().indexOf(':'));
                                                String bool = message.getText().substring(message.getText().indexOf(':') + 2);
                                                if (name.equals(user) && bool.toUpperCase().equals("TRUE")) {
                                                    Message[] mes = msgs.get(user).toArray(new Message[0]);
                                                    Logger.debug(this, msgs.toString());
                                                    Logger.debug(this, "Current user: " + user);
                                                    msgs.remove(user);
                                                    saveMessages();
                                                    Logger.info(this, "Sending user " + user + " his msgs.");
                                                    for (int k = 0; k < mes.length; k++) {
                                                        Message m = mes[k];
                                                        bot.send(new QueryResponseMessage(m.getFrom() + " schrieb am " + m.getDateFormatted() + ":", 100, (k + 1) * 2000, m.getTo()));
                                                        int i = 100;
                                                        for (String line : m.getLines()) {
                                                            bot.send(new QueryResponseMessage(line, 100, (k + 1) * 2000 + i, m.getTo()));
                                                            i += 100;
                                                        }
                                                    }
                                                    bot.deregister(this);
                                                } else if (name.equals(user)) {
                                                    Logger.debug(this, "Current user: " + user);
                                                    bot.send(new QueryResponseMessage("Du hast Post! " + "Authentisiere " + "Dich, damit " + "sie Dir " + "zugestellt wird!", 100, 2000, user));
                                                    bot.deregister(this);
                                                }
                                            }
                                            return null;
                                        }
                                    });
                                    bot.send(new RawResponseMessage("ISAUTHED " + user, 100, 0));
                                }
                            }
                        }
                    }
                }
            }.start();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMessages() {
        try {
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(MSGS_FILE));
            msgs = (TreeMap<String, LinkedList<Message>>) objectIn.readObject();
            objectIn.close();
            Logger.info(this, "Messages loaded: " + msgs + ".");
        } catch (FileNotFoundException e) {
            Logger.info(this, "No saved messages found.");
        } catch (IOException e) {
            Logger.error(this, e);
            Logger.warning(this, "Could not load saved messages.");
        } catch (ClassNotFoundException e) {
            Logger.error(this, e);
            Logger.warning(this, "Could not load saved messages.");
        } finally {
            if (msgs == null) {
                msgs = new TreeMap<String, LinkedList<Message>>();
            }
        }
    }

    private void saveMessages() {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(MSGS_FILE));
            objectOut.writeObject(msgs);
            objectOut.close();
            Logger.info(this, "Messages saved to " + MSGS_FILE + ".");
        } catch (FileNotFoundException e) {
            Logger.error(this, e);
            Logger.warning(this, "Messages could NOT be saved!");
        } catch (IOException e) {
            Logger.error(this, e);
            Logger.warning(this, "Messages could NOT be saved!");
        }
    }

    public LinkedList<? extends ResponseMessage> inform(final QueryMessage message) {
        final LinkedList<QueryResponseMessage> result = new LinkedList<QueryResponseMessage>();
        String newMail = "([nN]eue )?[nN]achricht an (.*?)";
        Logger.debug(this, "Mailbox was informed about the line »" + message.getText() + "« from " + message.getName() + ".");
        Logger.debug(this, "users.containsName() == " + sessions.containsKey(message.getName()));
        Logger.debug(this, "\"" + message.getText() + "\".matches(newMail) == " + message.getText().matches(newMail));
        if (!sessions.containsKey(message.getName()) && message.getText().matches(newMail)) {
            Logger.debug(this, "Line matched newMail but user was not found.");
            String to = message.getText().replaceAll(newMail, "$2");
            sessions.put(message.getName(), new Session(message.getName(), to));
            result.add(new QueryResponseMessage("Ok. Deine Nachricht wird an " + to + " versandt.", 100, 2000, message.getName()));
        } else if (sessions.containsKey(message.getName())) {
            boolean parsed = false;
            final Session user = sessions.get(message.getName());
            Logger.debug(this, "Line matched newMail and user was found.");
            if (message.getText().toUpperCase().startsWith("ADD ")) {
                user.addLine(message.getText().substring("ADD ".length()));
                result.add(new QueryResponseMessage("Zeile hinzugefügt: " + message.getText().substring("ADD ".length()), 100, 1000, message.getName()));
                parsed = true;
            } else if (message.getText().toUpperCase().startsWith("DEL ")) {
                try {
                    int index = Integer.parseInt(message.getText().substring("DEL ".length()));
                    user.delLine(index);
                    result.add(new QueryResponseMessage("Zeile " + index + " gelöscht.", 100, 1000, message.getName()));
                } catch (NumberFormatException e) {
                    result.add(new QueryResponseMessage("Du musst die Nummer " + "der zu löschenden Zeile angeben!", 100, 1000, message.getName()));
                } catch (IndexOutOfBoundsException e) {
                    result.add(new QueryResponseMessage("Eine Zeile mit dieser Nummer existiert nicht.", 100, 1000, message.getName()));
                }
                parsed = true;
            } else if (message.getText().toUpperCase().startsWith("INS ")) {
                String line = message.getText().substring("INS ".length());
                String number = line.substring(0, line.indexOf(' '));
                line = line.substring(line.indexOf(' '));
                try {
                    int index = Integer.parseInt(number);
                    user.insLine(line, index);
                } catch (NumberFormatException e) {
                    result.add(new QueryResponseMessage("Du musst einen " + "gültigen Zeilenindex angeben!", 100, 1000, message.getName()));
                } catch (IndexOutOfBoundsException e) {
                    result.add(new QueryResponseMessage("Dein Zeilenindex ist " + "ungültig!", 100, 1000, message.getName()));
                }
                parsed = true;
            } else if (message.getText().toUpperCase().matches("(LIST|LS|LST)")) {
                if (user.getLines().length == 0) {
                    result.add(new QueryResponseMessage("Du hast bisher " + "nichts geschrieben.", 100, 2000, message.getName()));
                } else {
                    result.add(new QueryResponseMessage("Du hast bisher " + "geschrieben:", 100, 2000, message.getName()));
                    String[] lines = user.getLines();
                    for (int i = 0; i < lines.length; i++) {
                        result.add(new QueryResponseMessage((i < 10 ? "0" + i : i) + " | " + lines[i], 100, 2100 + 100 * i, message.getName()));
                    }
                }
                parsed = true;
            } else if (message.getText().toUpperCase().equals("QUIT")) {
                sessions.remove(user.getFrom());
                result.add(new QueryResponseMessage("Ok, alle bisherigen " + "Eingaben wurden gelöscht.", 100, 2000, message.getName()));
                parsed = true;
            } else if (message.getText().toUpperCase().equals("SEND")) {
                bot.register(new RawMessageObserver() {

                    @Override
                    public LinkedList<? extends ResponseMessage> inform(RawMessage message) {
                        Logger.debug(this, "Mailbox was informed about raw " + "message »" + message.getText() + "«.");
                        if (message.getText().toUpperCase().startsWith("ISAUTHED ")) {
                            String name = message.getText().substring("ISAUTHED ".length(), message.getText().indexOf(':'));
                            String bool = message.getText().substring(message.getText().indexOf(':') + 2);
                            if (name.equals(user.getFrom()) && bool.toUpperCase().equals("TRUE")) {
                                synchronized (msgs) {
                                    if (!msgs.containsKey(user.getTo())) {
                                        msgs.put(user.getTo(), new LinkedList<Message>());
                                    }
                                    msgs.get(user.getTo()).add(new Message(user.getFrom(), user.getTo(), Arrays.asList(user.getLines())));
                                    saveMessages();
                                }
                                sessions.remove(user.getFrom());
                                bot.send(new QueryResponseMessage("Deine " + "Nachricht ist zum Versenden " + "gespeichert.", 100, 2000, user.getFrom()));
                                bot.deregister(this);
                            } else if (name.equals(user.getFrom())) {
                                bot.send(new QueryResponseMessage("Du " + "bist leider nicht authentisiert. " + "Um eine Nachricht zu versenden " + "musst Du beim Server authentisiert " + "sein! Bitte authentisiere Dich und " + "versuche es erneut.", 100, 1000, user.getFrom()));
                                bot.deregister(this);
                            }
                        }
                        return null;
                    }
                });
                bot.send(new RawResponseMessage("ISAUTHED " + message.getName(), 100, 0));
                Logger.debug(this, "Requested if user »" + message.getName() + "« is authed.");
                parsed = true;
            }
            if (!parsed) {
                user.addLine(message.getText());
                result.add(new QueryResponseMessage("Zeile hinzugefügt: " + message.getText(), 100, 1000, message.getName()));
            }
        }
        String help = "(HELP|HILFE|MAN)";
        if (message.getText().toUpperCase().matches(help)) {
            Logger.debug(this, "Line matched help.");
            result.add(new QueryResponseMessage("Hilfe zur Mailbox:", 100, 2000, message.getName()));
            result.add(new QueryResponseMessage("»Neue Nachricht an <nick>« " + "oder »mailto <nick>« Eine neue Nachricht an <nick> " + "beginnen.", 100, 2100, message.getName()));
            result.add(new QueryResponseMessage("»ADD <line>« Fügt die Zeile " + "<line> ans Ende der Nachricht an. Dabei kann das " + "»ADD « auch weggelassen werden.", 100, 2200, message.getName()));
            result.add(new QueryResponseMessage("»DEL <index>« Löscht die " + "Zeile mit der Nummer <index>.", 100, 2300, message.getName()));
            result.add(new QueryResponseMessage("»INS <index> <line>« Fügt " + "die Zeile <line> an Position <index> ein.", 100, 2400, message.getName()));
            result.add(new QueryResponseMessage("»LIST« Listet die bisher " + "geschriebenen Zeilen auf.", 100, 2500, message.getName()));
            result.add(new QueryResponseMessage("»QUIT« Verwirft die " + "aktuelle Nachricht.", 100, 2500, message.getName()));
            result.add(new QueryResponseMessage("»SEND« Sendet die aktuelle " + "Nachricht, sofern Du authentisiert bist.", 100, 2600, message.getName()));
        }
        return result;
    }
}
