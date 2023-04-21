package net.sourceforge.spacebutcher2.game.main.level;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import net.sourceforge.spacebutcher2.game.dataman.LevelData;
import net.sourceforge.spacebutcher2.game.main.SpaceButcher2Game;
import net.sourceforge.spacebutcher2.game.main.enemies.EnemiesGenerator;
import net.sourceforge.spacebutcher2.game.main.enemies.EnemiesGeneratorsFactory;
import net.sourceforge.spacebutcher2.game.main.enemies.EnemiesManager;
import net.sourceforge.spacebutcher2.game.main.objects.bonus.BonusesManager;

/**
 * Class representing real game state: generation of enemies and their actions.
 *
 * @author M.Olszewski
 */
class GameState extends BaseState {

    /**
   * Package constructor of {@link GameState} object. 
   */
    GameState() {
        super(LevelsConsts.GAME_STATE);
    }

    /** 
   * @see net.sourceforge.spacebutcher2.game.main.level.BaseState#enter()
   */
    @Override
    public void enter() {
        EnemiesManager.getInstance().startSpawning();
        BonusesManager.getInstance().enableBonusesCreation();
    }

    /** 
   * @see net.sourceforge.spacebutcher2.game.main.level.BaseState#exit()
   */
    @Override
    public void exit() {
        EnemiesManager.getInstance().stopSpawning();
        BonusesManager.getInstance().disableBonusesCreation();
    }

    /** 
   * @see net.sourceforge.spacebutcher2.game.main.level.BaseState#load(net.sourceforge.spacebutcher2.game.dataman.LevelData)
   */
    @Override
    public void load(LevelData data) {
        int generatorsCount = data.getSimultaneousWavesCount();
        EnemiesGenerator[] generators = new EnemiesGenerator[generatorsCount];
        for (int i = 0; i < generatorsCount; i++) {
            generators[i] = EnemiesGeneratorsFactory.getInstance().getEnemiesGenerator(data.getEnemiesWaveFlags(), data.getEnemiesTypes(), data.getWavesCount(), data.getMinEnemiesPerWave(), data.getMaxEnemiesPerWave(), data.getSpawnDelay(), new Rectangle(0, 0, SpaceButcher2Game.SCREEN_WIDTH, SpaceButcher2Game.SCREEN_HEIGHT));
        }
        EnemiesManager.getInstance().setEnemiesGenerators(generators);
        BonusesManager.getInstance().setupBonusesForLevel(data.getBonusesTypes(), data.getBonusesCreationChance());
    }

    /** 
   * @see net.sourceforge.spacebutcher2.game.main.level.BaseState#update(long)
   */
    @Override
    public void update(long elapsedTime) {
        EnemiesManager enemiesMan = EnemiesManager.getInstance();
        if (enemiesMan.isSpawningDone()) {
            if (enemiesMan.getEnemiesGroup().getSize() <= 0) {
                requestTransitionTo(LevelStates.LEVEL_END_STATE);
            }
        }
        if (parent.keyPressed(KeyEvent.VK_ESCAPE)) {
            requestTransitionTo(LevelStates.GAME_OVER_STATE);
            parent.bsInput.refresh();
        }
    }

    /** 
   * @see net.sourceforge.spacebutcher2.game.main.level.BaseState#render(java.awt.Graphics2D)
   */
    @Override
    public void render(Graphics2D g) {
    }
}
