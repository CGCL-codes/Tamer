package de.hft_stuttgart.botwar.server.system;

import de.hft_stuttgart.botwar.common.commands.GameOverCmd;
import de.hft_stuttgart.botwar.common.interfaces.IRemoteLogin;
import de.hft_stuttgart.botwar.common.interfaces.IRemotePlayer;
import de.hft_stuttgart.botwar.common.models.ChassisInfo;
import de.hft_stuttgart.botwar.common.models.GameResultInfo;
import de.hft_stuttgart.botwar.common.models.NewRobotInfo;
import de.hft_stuttgart.botwar.common.models.Password;
import de.hft_stuttgart.botwar.common.models.RobotInfo;
import de.hft_stuttgart.botwar.server.DataBase;
import de.hft_stuttgart.botwar.server.model.Coinsystem;
import static de.hft_stuttgart.botwar.common.models.GameResultInfo.Result.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 * @author pmv-mail@gmx.de
 */
public class RobotSysTest {

    IRemoteLogin login;

    IRemotePlayer player;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Helpers.startServer(Helpers.standardArgs);
        IRemoteLogin login = (IRemoteLogin) Naming.lookup(Helpers.LOGIN_URL);
        IRemotePlayer player = login.login("admin", new Password("admin"));
        List<GameResultInfo> playerList = new ArrayList<GameResultInfo>(2);
        playerList.add(new GameResultInfo(player.getPlayerID(), WON, Coinsystem.getCoinsForLevel(Coinsystem.getTopLevel())));
        playerList.add(new GameResultInfo("admin2", LOST, 6));
        DataBase.get().updateStat(new GameOverCmd(playerList));
        assertEquals(Coinsystem.getCoinsForLevel(Coinsystem.getTopLevel()), player.getCoins());
        player.logout();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Helpers.stopServer();
    }

    @Before
    public void setUp() throws Exception {
        login = (IRemoteLogin) Naming.lookup(Helpers.LOGIN_URL);
        player = login.login("admin", new Password("admin"));
    }

    @After
    public void tearDown() throws Exception {
        player.logout();
    }

    @Test
    public void testGetAvailableAraments() throws RemoteException {
        assertTrue(player.getAvailableArmaments().size() > 0);
    }

    @Test
    public void testGetAvailableChassises() throws RemoteException {
        assertTrue(player.getAvailableChassises().size() > 0);
    }

    @Test
    public void testGetAvailableShields() throws RemoteException {
        assertTrue(player.getAvailableShields().size() > 0);
    }

    @Test
    public void testSaveRobot() throws RemoteException, IllegalArgumentException {
        String armId = player.getAvailableArmaments().get(0).getName();
        String shieldId = player.getAvailableShields().get(0).getName();
        String chassisId = player.getAvailableChassises().get(1).getName();
        NewRobotInfo robotInfo = new NewRobotInfo("TestRobot", chassisId, shieldId, armId);
        player.saveRobot(robotInfo);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSaveRobotWithZeroCost() throws RemoteException, IllegalArgumentException {
        String armId = player.getAvailableArmaments().get(0).getName();
        String shieldId = player.getAvailableShields().get(0).getName();
        String chassisId = player.getAvailableChassises().get(0).getName();
        NewRobotInfo robotInfo = new NewRobotInfo("New Standard Robot", chassisId, shieldId, armId);
        player.saveRobot(robotInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRobotWithNonExistentParts() throws RemoteException, IllegalArgumentException {
        NewRobotInfo robotInfo = new NewRobotInfo("Fail", "They", "don't", "exist");
        player.saveRobot(robotInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveRobotWithToExpensiveParts() throws RemoteException, IllegalArgumentException, LoginException, NoSuchAlgorithmException {
        player.logout();
        player = login.login("admin2", new Password("admin2"));
        String armId = player.getAvailableArmaments().get(1).getName();
        String shieldId = player.getAvailableShields().get(1).getName();
        String chassisId = player.getAvailableChassises().get(1).getName();
        NewRobotInfo robotInfo = new NewRobotInfo("TestRobot", chassisId, shieldId, armId);
        player.saveRobot(robotInfo);
    }

    @Test
    public void testGetAvailableRobotsLobby() throws RemoteException, IllegalArgumentException {
        List<RobotInfo> list = player.getAvailableRobots();
        assertTrue(list.size() > 0);
    }

    /**
     *
     * Test of getAvailableRobots method, of class RemotePlayer and DataBase.
     */
    @Test
    public void testGetAvailableRobotsGameLobby() throws RemoteException, IllegalArgumentException {
        String armId = player.getAvailableArmaments().get(0).getName();
        String shieldId = player.getAvailableShields().get(0).getName();
        String chassisId = player.getAvailableChassises().get(0).getName();
        for (ChassisInfo chassis : player.getAvailableChassises()) {
            if (chassis.getLevelRank() > 1) {
                chassisId = chassis.getName();
                break;
            }
        }
        NewRobotInfo robotInfo = new NewRobotInfo("HighLevelRobot", chassisId, shieldId, armId);
        player.saveRobot(robotInfo);
        RobotInfo robot = null;
        for (RobotInfo r : player.getAvailableRobots()) {
            if ("HighLevelRobot".equals(r.getRobotName())) {
                robot = r;
                break;
            }
        }
        assertNotSame(1, robot.getLevelRank());
        assertNotNull(robot);
        int gameID = player.createGame("GameName", player.getMapInfos().get(0), 2, 2, 1);
        player.enterGame(gameID);
        List<RobotInfo> list = player.getAvailableRobots();
        assertTrue(list.size() > 0);
        assertFalse(list.contains(robot));
    }

    @Test
    public void testGetRobotInfo() throws RemoteException, IllegalArgumentException {
        RobotInfo r = player.getRobotInfo(1);
        assertNotNull(r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRobotInfo2() throws RemoteException, IllegalArgumentException {
        player.getRobotInfo(100000);
    }

    @Test
    public void testDeleteRobot() throws RemoteException {
        List<RobotInfo> list = null;
        String armId = player.getAvailableArmaments().get(0).getName();
        String shieldId = player.getAvailableShields().get(0).getName();
        String chassisId = player.getAvailableChassises().get(1).getName();
        NewRobotInfo robotInfo = new NewRobotInfo("RobotToDelete", chassisId, shieldId, armId);
        player.saveRobot(robotInfo);
        list = player.getAvailableRobots();
        for (RobotInfo robot : list) {
            if ("RobotToDelete".equals(robot.getRobotName())) {
                player.removeRobot(robot.getRobotID());
                break;
            }
        }
        list = player.getAvailableRobots();
        for (RobotInfo robot : list) {
            if ("RobotToDelete".equals(robot.getRobotName())) {
                fail("Robot isn't deleted!");
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteStandardRobot() throws RemoteException {
        for (RobotInfo robot : player.getAvailableRobots()) {
            if (robot.getCompleteCost() == 0) {
                player.removeRobot(robot.getRobotID());
            }
        }
    }
}
