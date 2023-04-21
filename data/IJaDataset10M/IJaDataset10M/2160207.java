package org.bwapi.unit;

import static org.junit.Assert.assertNotNull;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.bwapi.bridge.BridgeException;
import org.bwapi.unit.model.BroodwarButton;
import org.bwapi.unit.server.BwapiBridgeBotServerCallback;
import org.bwapi.unit.server.BwapiBridgeBotServerImpl;
import org.xvolks.jnative.misc.basicStructures.HWND;

/**
 * Base test case for JUnit 4 testing
 * 
 * @author Chad Retz
 */
public class BwapiTestCase {

    /**
     * Start a Starcraft game with the given settings
     * 
     * @param testInformation
     * @param chaosProcess
     * @throws Exception
     */
    private static void startStarcraft(BwapiTestInformation testInformation, AtomicReference<Process> chaosProcess) throws Exception {
        assertNotNull("testInformation must be set in base class", testInformation);
        BwapiTestUtils.createMapFileDirectory(new File(BwapiTestUtils.getMapDirectory(testInformation.getStarcraftFolder())));
        HWND scWnd = BwapiTestUtils.loadStarcraft(testInformation.getChaosLauncherFolder(), chaosProcess);
        BwapiTestUtils.waitForAndClickButton(scWnd, BroodwarButton.SINGLE_PLAYER);
        BwapiTestUtils.waitForAndClickButton(scWnd, BroodwarButton.EXPANSION_PACK);
        BwapiTestUtils.waitForAndClickButton(scWnd, BroodwarButton.ID_OK);
        BwapiTestUtils.waitForAndClickButton(scWnd, BroodwarButton.PLAY_CUSTOM);
        BwapiTestUtils.delay(1000);
        BwapiTestUtils.selectMap(scWnd, testInformation.getStarcraftFolder(), testInformation.getMap());
        BwapiTestUtils.selectGameType(scWnd, testInformation.getGameType());
        BwapiTestUtils.setupPlayers(scWnd, testInformation.getPlayers());
        BwapiTestUtils.relativeClick(scWnd, BroodwarButton.ID_OK.getX(), BroodwarButton.ID_OK.getY(), InputEvent.BUTTON1_MASK);
        BwapiTestUtils.delay(1000);
        BwapiTestUtils.relativeClick(scWnd, BroodwarButton.ID_OK.getX(), BroodwarButton.ID_OK.getY(), InputEvent.BUTTON1_MASK);
    }

    /**
     * Stop Starcraft
     * 
     * @param chaosProcess
     * @throws Exception
     */
    private static void stopStarcraft(AtomicReference<Process> chaosProcess) throws Exception {
        BwapiTestUtils.killStarcraft();
        if (chaosProcess.get() != null) {
            chaosProcess.get().destroy();
        }
    }

    /**
     * Execute bot with the given information
     * 
     * @param testInformation
     * @throws Exception
     */
    protected void execute(BwapiTestInformation testInformation) throws Exception {
        final AtomicBoolean completed = new AtomicBoolean(false);
        final AtomicReference<Throwable> throwable = new AtomicReference<Throwable>(null);
        BwapiBridgeBotServerImpl.startServer(testInformation.getBridgeBotClass(), new BwapiBridgeBotServerCallback() {

            @Override
            public void onEnd() {
                completed.set(true);
            }

            @Override
            public void onFailure(Throwable error) {
                throwable.set(error);
            }
        }, testInformation.isConsideredOverWhenUnitsGone());
        AtomicReference<Process> chaosProcess = new AtomicReference<Process>(null);
        try {
            startStarcraft(testInformation, chaosProcess);
            while (!completed.get() && throwable.get() == null) {
                BwapiTestUtils.delay(1000);
            }
            if (throwable.get() != null) {
                throw new BridgeException("Failure during execution", throwable.get());
            }
        } finally {
            try {
                stopStarcraft(chaosProcess);
            } catch (Exception e) {
                System.out.println("Error closing starcraft: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
