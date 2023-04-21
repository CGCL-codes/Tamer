package com.usoog.creepattack.tower;

import com.usoog.commons.gamecore.exception.NoSuchTileException;
import com.usoog.commons.gamecore.gamegrid.ScaledCanvas;
import com.usoog.creepattack.CAGameGrid;
import com.usoog.creepattack.CAGameManager;
import com.usoog.creepattack.CAGameState;
import com.usoog.creepattack.CAPlayer;
import com.usoog.creepattack.creep.CACreep;
import com.usoog.creepattack.creep.CreepData;
import com.usoog.creepattack.tile.CATile;
import com.usoog.creepattack.tower.base.CATower;
import com.usoog.creepattack.tower.base.Cleric;
import com.usoog.tdcore.exception.InsufficientResourcesException;
import com.usoog.tdcore.exception.TileAlreadyHasTowerException;
import com.usoog.tdcore.exception.TileNotBuildableException;
import com.usoog.tdcore.exception.UnsellableTowerException;
import com.usoog.tdcore.message.MessageTowerBuy;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;

/**
 *
 * @author jimmy
 */
public class Treasurer extends Cleric {

    private Color color = new Color(225, 215, 0);

    @Override
    public void doUpgrade() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void paint(Graphics2D g, int gameTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void paintEffects(Graphics2D g, int gameTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initFromData(CAGameState gameState, MessageTowerBuy<CACreep, CreepData, CATower, CATile, CAPlayer, CAGameGrid, CAGameManager, CAGameState> data) throws NoSuchTileException, TileAlreadyHasTowerException, TileNotBuildableException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetScale(ScaledCanvas<CATile, CAPlayer, CAGameGrid, CAGameManager, CAGameState> sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isExpired() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isActive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancelBuild() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void upgrade(int activateTick, String mode) throws InsufficientResourcesException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Long> getUpgradeCost(String mode, boolean afterQueue, Map<String, Long> target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canUpgrade(String mode, boolean afterQueue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sell(int activateTick) throws UnsellableTowerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Long> getSellPrice(Map<String, Long> target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMode(int activateTick, String mode, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMode(String mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
