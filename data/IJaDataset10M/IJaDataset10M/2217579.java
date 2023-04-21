package handlers.bypasshandlers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import l2.universe.L2DatabaseFactory;
import l2.universe.gameserver.handler.IBypassHandler;
import l2.universe.gameserver.model.L2ItemInstance;
import l2.universe.gameserver.model.actor.L2Character;
import l2.universe.gameserver.model.actor.L2Npc;
import l2.universe.gameserver.model.actor.instance.L2PcInstance;
import l2.universe.gameserver.network.serverpackets.ExBrPremiumState;

public class PA implements IBypassHandler {

    private static final String[] COMMANDS = { "BuyPa" };

    private static final Logger _log = Logger.getLogger(PA.class.getName());

    @Override
    public boolean useBypass(final String command, final L2PcInstance activeChar, final L2Character target) {
        if (!(target instanceof L2Npc)) return false;
        try {
            final StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            final int type = Integer.parseInt(st.nextToken());
            final String acc = activeChar.getAccountName();
            final L2ItemInstance spbs = activeChar.getInventory().getItemByItemId(4356);
            switch(type) {
                case 1:
                    if (spbs == null) {
                        activeChar.sendMessage("You do not have Gold Einhasad.");
                        return false;
                    }
                    activeChar.destroyItem("Premium", spbs, 1, activeChar, true);
                    setPremium(1, acc);
                    activeChar.sendMessage("Congratulate, you have received a premium account for 1 day.");
                    break;
                case 2:
                    if (spbs == null || spbs.getCount() < 8) {
                        activeChar.sendMessage("You don't have Gold Einhasad.");
                        activeChar.sendMessage("You need 8 Gold Einhasad.");
                        return false;
                    }
                    activeChar.destroyItem("Premium", spbs, 8, activeChar, true);
                    setPremium(10, acc);
                    activeChar.sendMessage("Congratulate, you have received a premium account for 10 days.");
                    break;
                case 3:
                    if (spbs == null || spbs.getCount() < 20) {
                        activeChar.sendMessage("You don't have Gold Einhasad.");
                        activeChar.sendMessage("You need 20 Gold Einhasad.");
                        return false;
                    }
                    activeChar.destroyItem("Premium", spbs, 20, activeChar, true);
                    setPremium(30, acc);
                    activeChar.sendMessage("Congratulate, you have received a premium account for 30 days.");
                    break;
            }
            activeChar.sendPacket(new ExBrPremiumState(activeChar, 1));
            return true;
        } catch (final Exception e) {
            _log.info("Exception in " + getClass().getSimpleName());
        }
        return false;
    }

    private void setPremium(final int day, final String account) {
        Connection con = null;
        try {
            final Calendar finishtime = Calendar.getInstance();
            finishtime.setTimeInMillis(System.currentTimeMillis());
            finishtime.set(Calendar.SECOND, 0);
            finishtime.add(Calendar.DAY_OF_MONTH, day);
            con = L2DatabaseFactory.getInstance().getConnection();
            final PreparedStatement statement = con.prepareStatement("UPDATE account_premium SET premium_service=?,enddate=? WHERE account_name=?");
            statement.setInt(1, 1);
            statement.setLong(2, finishtime.getTimeInMillis());
            statement.setString(3, account);
            statement.execute();
            statement.close();
        } catch (final SQLException e) {
            _log.info("PremiumService:  Could not increase data");
        } finally {
            L2DatabaseFactory.close(con);
        }
    }

    @Override
    public String[] getBypassList() {
        return COMMANDS;
    }
}
