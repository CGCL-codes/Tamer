package net.sf.l2j.gameserver.network.clientpackets;

import java.nio.BufferUnderflowException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import net.sf.l2j.Config;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.ChatHandler;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

/**
 * This class is describes Say2 packet
 *
 * @version $Revision: 1.16.2.12.2.7 $ $Date: 2005/04/11 10:06:11 $
 */
public final class Say2 extends L2GameClientPacket {

    private static final String _C__38_SAY2 = "[C] 38 Say2";

    private static Logger _log = Logger.getLogger(Say2.class.getName());

    private static Logger _logChat = Logger.getLogger("chat");

    public static final int ALL = 0;

    public static final int SHOUT = 1;

    public static final int TELL = 2;

    public static final int PARTY = 3;

    public static final int CLAN = 4;

    public static final int GM = 5;

    public static final int PETITION_PLAYER = 6;

    public static final int PETITION_GM = 7;

    public static final int TRADE = 8;

    public static final int ALLIANCE = 9;

    public static final int ANNOUNCEMENT = 10;

    public static final int PARTYROOM_ALL = 16;

    public static final int PARTYROOM_COMMANDER = 15;

    public static final int HERO_VOICE = 17;

    private static final String[] CHAT_NAMES = { "ALL  ", "SHOUT", "TELL ", "PARTY", "CLAN ", "GM   ", "PETITION_PLAYER", "PETITION_GM", "TRADE", "ALLIANCE", "ANNOUNCEMENT", "WILLCRASHCLIENT:)", "FAKEALL?", "FAKEALL?", "FAKEALL?", "PARTYROOM_ALL", "PARTYROOM_COMMANDER", "HERO_VOICE" };

    private String _text;

    private int _type;

    private String _target;

    @Override
    protected void readImpl() {
        _text = readS();
        try {
            _type = readD();
        } catch (BufferUnderflowException e) {
            _type = CHAT_NAMES.length;
        }
        _target = _type == TELL ? readS() : null;
    }

    @Override
    protected void runImpl() {
        if (Config.DEBUG) _log.info("Say2: Msg Type = '" + _type + "' Text = '" + _text + "'.");
        if (_type < 0 || _type >= CHAT_NAMES.length) {
            _log.warning("Say2: Invalid type: " + _type);
            return;
        }
        L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar != null && activeChar instanceof L2PcInstance) if (!activeChar.getFloodProtectors().getChat().tryPerformAction("chat")) {
            activeChar.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }
        if (_text.length() >= 100) {
            _log.warning("Max input limit exceeded.");
            activeChar.sendMessage("You Cannot Input More Than 100 Characters");
            return;
        }
        if (activeChar == null) {
            _log.warning("[Say2.java] Active Character is null.");
            return;
        }
        if (_text.length() >= 100) {
            _log.warning("Say2: Max input exceeded.");
            return;
        }
        if (Config.USE_SAY_FILTER) checkText(activeChar);
        if (activeChar.isChatBanned()) {
            activeChar.checkBanChat(true);
            return;
        }
        if (activeChar.isInJail() && Config.JAIL_DISABLE_CHAT) if (_type == TELL || _type == SHOUT || _type == TRADE || _type == HERO_VOICE) {
            activeChar.sendMessage("You Have been Chat Banned");
            return;
        }
        if (_type == PETITION_PLAYER && activeChar.isGM()) _type = PETITION_GM;
        if (Config.LOG_CHAT) {
            LogRecord record = new LogRecord(Level.INFO, _text);
            record.setLoggerName("chat");
            if (_type == TELL) record.setParameters(new Object[] { CHAT_NAMES[_type], "[" + activeChar.getName() + " to " + _target + "]" }); else record.setParameters(new Object[] { CHAT_NAMES[_type], "[" + activeChar.getName() + "]" });
            _logChat.log(record);
        }
        IChatHandler handler = ChatHandler.getInstance().getChatHandler(_type);
        if (handler != null) handler.handleChat(_type, activeChar, _target, _text);
    }

    private void checkText(L2PcInstance activeChar) {
        if (Config.USE_SAY_FILTER) {
            String filteredText = _text;
            for (String pattern : Config.FILTER_LIST) filteredText = filteredText.replaceAll("(?i)" + pattern, Config.CHAT_FILTER_CHARS);
            if (Config.CHAT_FILTER_PUNISHMENT.equalsIgnoreCase("jail") && _text != filteredText) {
                int punishmentLength = 0;
                if (Config.CHAT_FILTER_PUNISHMENT_PARAM2 == 0) punishmentLength = Config.CHAT_FILTER_PUNISHMENT_PARAM1; else {
                    Connection con = null;
                    try {
                        con = L2DatabaseFactory.getInstance().getConnection();
                        PreparedStatement statement;
                        statement = con.prepareStatement("SELECT value FROM account_data WHERE (account_name=?) AND (var='jail_time')");
                        statement.setString(1, activeChar.getAccountName());
                        ResultSet rset = statement.executeQuery();
                        if (!rset.next()) {
                            punishmentLength = Config.CHAT_FILTER_PUNISHMENT_PARAM1;
                            PreparedStatement statement1;
                            statement1 = con.prepareStatement("INSERT INTO account_data (account_name, var, value) VALUES (?, 'jail_time', ?)");
                            statement1.setString(1, activeChar.getAccountName());
                            statement1.setInt(2, punishmentLength);
                            statement1.executeUpdate();
                            statement1.close();
                        } else {
                            punishmentLength = rset.getInt("value") + Config.CHAT_FILTER_PUNISHMENT_PARAM2;
                            PreparedStatement statement1;
                            statement1 = con.prepareStatement("UPDATE account_data SET value=? WHERE (account_name=?) AND (var='jail_time')");
                            statement1.setInt(1, punishmentLength);
                            statement1.setString(2, activeChar.getAccountName());
                            statement1.executeUpdate();
                            statement1.close();
                        }
                        rset.close();
                        statement.close();
                    } catch (SQLException e) {
                        _log.warning("Say2: Could not check character for chat filter punishment data: " + e);
                    } finally {
                        try {
                            con.close();
                        } catch (Exception e) {
                            _log.warning("Say2: Error, While Trying to Check Text (Say2.checkText()");
                        }
                    }
                }
                activeChar.setPunishLevel(L2PcInstance.PunishLevel.JAIL, punishmentLength);
            }
            _text = filteredText;
        }
    }

    @Override
    public String getType() {
        return _C__38_SAY2;
    }
}
