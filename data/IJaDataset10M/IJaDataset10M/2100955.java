package jadaclient;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;

/**
 *
 * @author gabry
 */
public class Handler extends GameEngine {

    public static final int FIRST_PAGE = 0;

    public static final int NEW_ACCOUNT = 1;

    public static final int LIST_CHARACTERS = 2;

    public static final int NEW_CHARACTER = 3;

    private static Handler handler;

    private GameObject status;

    private Handler() {
        super();
        distribute = false;
    }

    /**
    This static method is used to get the current Handler in the application
     * @return an instance of Handler
     *
     */
    public static Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    @Override
    public void initResources() {
        nextGameID = FIRST_PAGE;
    }

    public GameObject getGame(int gameID) {
        switch(gameID) {
            case FIRST_PAGE:
                status = new JadaGTGEGUI(this);
                break;
            case NEW_ACCOUNT:
                status = new JadaNewAccount(this);
                break;
            case LIST_CHARACTERS:
                status = new JadaCharacters(this);
                break;
            case NEW_CHARACTER:
                status = new JadaNewCharacter(this);
                break;
        }
        return status;
    }
}
