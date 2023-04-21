package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This is a monitor (see MVC2) for keyboard handling
 *
 * @author Julien Gouesse
 */
final class GameKeyboardMonitor implements KeyListener {

    private GameGLView gameView;

    GameKeyboardMonitor(GameGLView gameView) {
        this.gameView = gameView;
    }

    public final void keyPressed(KeyEvent event) {
        switch(gameView.getCycle()) {
            case START_SCREEN:
                {
                    break;
                }
            case MAIN_MENU:
                {
                    if (gameView.getGLMenu() != null) gameView.getGLMenu().keyPressed(event);
                    break;
                }
            case GAME:
                {
                    GameController gameController = gameView.getGameController();
                    gameController.setRunningFast(event.isShiftDown());
                    switch(event.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            {
                                if (!gameController.getPlayerHit()) gameController.setRunningForward(true);
                                break;
                            }
                        case KeyEvent.VK_DOWN:
                            {
                                if (!gameController.getPlayerHit()) gameController.setRunningBackward(true);
                                break;
                            }
                        case KeyEvent.VK_LEFT:
                            {
                                if (!gameController.getPlayerHit()) gameController.setTurningLeft(true);
                                break;
                            }
                        case KeyEvent.VK_RIGHT:
                            {
                                if (!gameController.getPlayerHit()) gameController.setTurningRight(true);
                                break;
                            }
                        case KeyEvent.VK_W:
                            {
                                if (!gameController.getPlayerHit()) gameController.setRunningForward(true);
                                break;
                            }
                        case KeyEvent.VK_Z:
                            {
                                if (!gameController.getPlayerHit()) gameController.setRunningForward(true);
                                break;
                            }
                        case KeyEvent.VK_S:
                            {
                                if (!gameController.getPlayerHit()) gameController.setRunningBackward(true);
                                break;
                            }
                        case KeyEvent.VK_Q:
                            {
                                if (!gameController.getPlayerHit()) gameController.setLeftStepping(true);
                                break;
                            }
                        case KeyEvent.VK_A:
                            {
                                if (!gameController.getPlayerHit()) gameController.setLeftStepping(true);
                                break;
                            }
                        case KeyEvent.VK_D:
                            {
                                if (!gameController.getPlayerHit()) gameController.setRightStepping(true);
                                break;
                            }
                        case KeyEvent.VK_ESCAPE:
                            {
                                gameController.performAtExit();
                                break;
                            }
                        case KeyEvent.VK_SPACE:
                            {
                                if (!gameController.getPlayerHit()) gameController.tryLaunchPlayerRocket();
                                break;
                            }
                    }
                    break;
                }
            case ABOUT_SCREEN:
                {
                    gameView.setCycle(GameCycle.MAIN_MENU);
                    break;
                }
            case OPTIONS_MENU:
                {
                    gameView.setCycle(GameCycle.MAIN_MENU);
                    break;
                }
        }
    }

    public final void keyTyped(KeyEvent event) {
        switch(gameView.getCycle()) {
            case START_SCREEN:
                {
                    break;
                }
            case MAIN_MENU:
                {
                    if (gameView.getGLMenu() != null) gameView.getGLMenu().keyTyped(event);
                    break;
                }
            case GAME:
                {
                    break;
                }
            case ABOUT_SCREEN:
                {
                    break;
                }
            case OPTIONS_MENU:
                {
                    break;
                }
        }
    }

    public final void keyReleased(KeyEvent event) {
        switch(gameView.getCycle()) {
            case START_SCREEN:
                {
                    switch(event.getKeyCode()) {
                        case KeyEvent.VK_F1:
                            {
                                gameView.setRecSnapShot(true);
                                break;
                            }
                    }
                    break;
                }
            case MAIN_MENU:
                {
                    if (gameView.getGLMenu() != null) gameView.getGLMenu().keyReleased(event);
                    break;
                }
            case GAME:
                {
                    GameController gameController = gameView.getGameController();
                    switch(event.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            {
                                gameController.setRunningForward(false);
                                break;
                            }
                        case KeyEvent.VK_DOWN:
                            {
                                gameController.setRunningBackward(false);
                                break;
                            }
                        case KeyEvent.VK_LEFT:
                            {
                                gameController.setTurningLeft(false);
                                break;
                            }
                        case KeyEvent.VK_RIGHT:
                            {
                                gameController.setTurningRight(false);
                                break;
                            }
                        case KeyEvent.VK_W:
                            {
                                gameController.setRunningForward(false);
                                break;
                            }
                        case KeyEvent.VK_Z:
                            {
                                gameController.setRunningForward(false);
                                break;
                            }
                        case KeyEvent.VK_S:
                            {
                                gameController.setRunningBackward(false);
                                break;
                            }
                        case KeyEvent.VK_Q:
                            {
                                gameController.setLeftStepping(false);
                                break;
                            }
                        case KeyEvent.VK_A:
                            {
                                gameController.setLeftStepping(false);
                                break;
                            }
                        case KeyEvent.VK_D:
                            {
                                gameController.setRightStepping(false);
                                break;
                            }
                        case KeyEvent.VK_F1:
                            {
                                gameView.setRecSnapShot(true);
                                break;
                            }
                        case KeyEvent.VK_F2:
                            {
                                gameView.setRecSnapFilm(!gameView.getRecSnapFilm());
                                break;
                            }
                        case KeyEvent.VK_F8:
                            {
                                if (!gameController.getPlayerHit()) gameController.setBcheat(!gameController.getBcheat());
                                break;
                            }
                        case KeyEvent.VK_P:
                            {
                                if (!gameController.getBpause()) {
                                    gameController.setBpause(true);
                                    gameView.setCycle(GameCycle.MAIN_MENU);
                                }
                                break;
                            }
                    }
                    break;
                }
            case ABOUT_SCREEN:
                {
                    break;
                }
            case OPTIONS_MENU:
                {
                    break;
                }
        }
    }
}
