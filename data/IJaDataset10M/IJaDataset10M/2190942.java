package gui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Vector;
import match.Coordinate;
import match.Match;
import player.*;

/**
 * represent the controller for the game, in the MVC pattern.
 * 
 * @author WormHole Interactive
 */
public class Controller extends UnicastRemoteObject implements RemoteController {

    ArrayList<View> registeredViews;

    Match registeredModel;

    /** Creates a new instance of Controller */
    public Controller() throws RemoteException {
        registeredViews = new ArrayList<View>();
    }

    /**
	 * Binds a model to this controller. Once added, the controller will listen
	 * for all model property changes and propogate them on to registered views.
	 * In addition, it is also responsible for resetting the model properties
	 * when a view changes state.
	 * 
	 * @param model
	 *            The model to be added
	 */
    public void addModel(Match model) throws RemoteException {
        registeredModel = model;
    }

    /**
	 * Binds a view to this controller. The controller will propogate all model
	 * property changes to each view for consideration.
	 * 
	 * @param view
	 *            The view to be added
	 */
    public void addView(View view) throws RemoteException {
        registeredViews.add(view);
    }

    /**
	 * This method is used to implement the PropertyChangeListener interface.
	 * Any model changes will be sent to this controller through the use of this
	 * method.
	 * 
	 * @param evt
	 *            An object that describes the model's property change.
	 */
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        for (View view : registeredViews) {
            view.modelPropertyChange(evt);
        }
    }

    /**
	 * Request to set the current cubeItem in the GameCube to the hinted
	 * position (but don't mark it)
	 */
    public void hint() throws RemoteException {
    }

    public void addAI(Object source) throws RemoteException {
        try {
            switch(registeredModel.getSlots()) {
                case 2:
                    Player cp1 = new ArtificialPlayer(Color.WHITE, "Computer2", ((Gui) source).IP);
                    registeredModel.addPlayer(cp1);
                    break;
                case 1:
                    Player cp2 = new ArtificialPlayer(Color.WHITE, "Computer3", ((Gui) source).IP);
                    registeredModel.addPlayer(cp2);
                    break;
                case 0:
                    return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(Player p) throws RemoteException {
        registeredModel.addPlayer(p);
    }

    public void disconnectPlayer(Player p) throws RemoteException {
        try {
            registeredModel.removePlayer(p);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Launched only MasterPlayer to start the game
	 */
    public void startALL() throws RemoteException {
        registeredModel.startRemoteGuis();
    }

    /**
	 * Launched only MasterPlayer if you disconnect or turn off your game.
	 */
    public void disconnectALL() throws RemoteException {
        registeredModel.removeALL();
    }

    @Override
    public void terminateGame() throws RemoteException {
    }

    public Vector<Player> getModelPlayers() throws RemoteException {
        return registeredModel.getPlayers();
    }

    public void playCubeItem(Coordinate c) throws RemoteException {
        registeredModel.playCubeItem(c);
    }

    public void startMatch() throws RemoteException {
        registeredModel.startMatch();
    }

    public void stopMatch() throws RemoteException {
        registeredModel.stopMatch();
    }

    @Override
    public void suggest() throws RemoteException {
        Coordinate c = registeredModel.getSuggestion();
        propertyChange(new PropertyChangeEvent(this, "Blink", null, c));
    }
}
