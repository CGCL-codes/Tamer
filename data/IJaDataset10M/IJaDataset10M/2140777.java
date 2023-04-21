package mp3;

import config.Tables;
import output.Answer;
import MYSQL.MysqlDb;
import config.HelpFiles;
import java.util.Hashtable;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 *
 * @author stylesuxx@gmail.com
 */
public class MP3 {

    private String option = "";

    private MultiUserChat muc;

    private String[] opt, table;

    private Tables tbl;

    private boolean isValid = false;

    private MysqlDb sqldb;

    private int TrackNr = 0;

    private XMPPConnection conn;

    private Playlist pl;

    private Answer asw;

    private HelpFiles help;

    private Hashtable config;

    private Shouter shout;

    public MP3(MultiUserChat muc, Tables tbl, MysqlDb sqldb, XMPPConnection conn, Answer asw, HelpFiles help, Hashtable config) {
        this.muc = muc;
        this.tbl = tbl;
        this.sqldb = sqldb;
        this.conn = conn;
        this.table = tbl.returnValidMusikOpts();
        this.asw = asw;
        this.help = help;
        this.config = config;
        pl = new Playlist(sqldb);
        shout = new Shouter(pl, "/media/truecrypt1/Warez/MP3Z/index");
    }

    public void processMessage(Message msg) {
        Message message = msg;
        String body = message.getBody().toLowerCase();
        String from = message.getFrom();
        String JID = muc.getOccupant(from).getJid().split("/")[0];
        opt = body.split(" ");
        if (opt.length > 1) {
            option = opt[1];
            for (int i = 0; i < table.length; i++) {
                if (table[i].equalsIgnoreCase(option)) isValid = true;
            }
        } else asw.print(help.mp3Help(JID), false, from);
        if (isValid) {
            if (option.equals("list")) {
                if (opt.length > 2) if (opt[2].length() == 1) asw.print(sqldb.getMp3Char(opt[2]), false, from); else asw.print(sqldb.getMp3Search(opt[2]), false, from);
            }
            if (option.equals("add") && opt.length > 2) {
                try {
                    TrackNr = Integer.parseInt(opt[2]);
                    pl.add(TrackNr);
                } catch (Exception ex) {
                    asw.print(help.mp3Help(JID), false, from);
                }
            }
            if (option.equals("playlist")) {
                asw.print(pl.toString(), false, from);
            }
            if (option.equals("del") && opt.length > 2) {
                try {
                    TrackNr = Integer.parseInt(opt[2]);
                    pl.remove(TrackNr);
                } catch (Exception ex) {
                    asw.print(help.mp3Help(JID), false, from);
                }
            }
            if (option.equals("random")) {
                int n = 0;
                if (opt.length > 2) {
                    try {
                        n = Integer.parseInt(opt[2]);
                    } catch (Exception ex) {
                        asw.print(help.mp3Help(JID), false, from);
                    }
                    if (n != 1) asw.print(n + " zufällige Tracks hinzugefügt.", false, JID); else asw.print("Ein zufälliger Track hinzugefügt.", false, JID);
                } else {
                    n = 1;
                }
            }
            if (option.equals("index") && sqldb.isStatus("Admin", JID)) {
            }
            if (option.equals("play") && sqldb.isStatus("Admin", JID)) {
                if (shout.connect()) shout.start();
            }
            if (option.equals("stop") && sqldb.isStatus("Admin", JID)) {
                shout.close();
            }
        }
    }
}
