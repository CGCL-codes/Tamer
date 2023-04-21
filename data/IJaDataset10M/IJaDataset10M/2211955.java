package erki.abcpeter.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.TreeMap;
import erki.abcpeter.BotInterface;
import erki.abcpeter.Parser;
import erki.abcpeter.msgs.observers.TextMessageObserver;
import erki.abcpeter.msgs.receive.TextMessage;
import erki.abcpeter.msgs.response.ResponseMessage;
import erki.abcpeter.util.Logger;

/**
 * Tells other people where you are.
 * 
 * @author Edgar Kalkowski
 */
public class WhereAmI implements Parser, TextMessageObserver {

    private static final String WHERE_FILE = "config" + System.getProperty("file.separator") + "where";

    private BotInterface bot;

    private TreeMap<String, String> map;

    @Override
    public void init(BotInterface bot) {
        loadMessages();
        this.bot = bot;
        bot.register(this);
    }

    @SuppressWarnings("unchecked")
    private void loadMessages() {
        try {
            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(WHERE_FILE));
            map = (TreeMap<String, String>) objectIn.readObject();
            objectIn.close();
            Logger.info(this, "Information where users are loaded from " + WHERE_FILE + ": " + map);
        } catch (FileNotFoundException e) {
            Logger.info(this, "Could not find any information about where " + "users are.");
        } catch (IOException e) {
            Logger.error(this, e);
            Logger.warning(this, "Could not load information about where " + "users are.");
        } catch (ClassNotFoundException e) {
            Logger.error(this, e);
            Logger.warning(this, "Could not load information about where " + "users are.");
        } finally {
            if (map == null) {
                map = new TreeMap<String, String>();
            }
        }
    }

    private void saveMessages() {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(WHERE_FILE));
            objectOut.writeObject(map);
            objectOut.close();
            Logger.info(this, "Information about where users are stored in " + WHERE_FILE + ".");
        } catch (FileNotFoundException e) {
            Logger.error(this, e);
            Logger.warning(this, "Could not save any information about " + "where users are.");
        } catch (IOException e) {
            Logger.error(this, e);
            Logger.warning(this, "Could not save any information about " + "where users are.");
        }
    }

    @Override
    public LinkedList<? extends ResponseMessage> inform(TextMessage message) {
        LinkedList<ResponseMessage> result = new LinkedList<ResponseMessage>();
        String whereIs = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,] ?[wW]o ist (.*)\\?";
        if (message.getText().matches(whereIs)) {
            String user = message.getText().replaceAll(whereIs, "$2");
            if (map.containsKey(user)) {
                String where = map.get(user);
                result.add(new ResponseMessage(user + " ist " + where, 100, 2500));
            } else {
                result.add(new ResponseMessage("Das weiß ich nicht.", 100, 2500));
            }
        }
        String whereAmI = "(" + bot.getName() + "|" + bot.getName().toLowerCase() + ")[:,] ?[iI]ch bin (.*)";
        if (message.getText().matches(whereAmI)) {
            String where = message.getText().replaceAll(whereAmI, "$2");
            String returned = "(wieder )?da.";
            if (where.matches(returned)) {
                map.remove(message.getName());
            } else {
                if (map.containsKey(message.getName())) {
                    map.remove(message.getName());
                }
                map.put(message.getName(), where);
            }
            saveMessages();
            result.add(new ResponseMessage("Alles klar.", 100, 2000));
        }
        return result;
    }
}
