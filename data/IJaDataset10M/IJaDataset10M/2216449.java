/*

Copyright (C) 2001 Kevin E. Gilpin (kevin.gilpin@alum.mit.edu)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License (http://www.gnu.org/copyleft/gpl.html)
for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package agonism.ch.sheep.impl;

import agonism.ch.sheep.AbstractAvatar;

import agonism.ce.Actor;
import agonism.ce.ActorID;
import agonism.ce.Controller;
import agonism.ce.Debug;
import agonism.ce.Engine;
import agonism.ce.TeamID;
import agonism.ce.Traceable;
import agonism.ce.WrapperID;

import agonism.IChallengeFactory;
import agonism.IChallenge;

import java.util.Properties;

public class GameFactory
	implements IChallengeFactory,
			   Traceable
{
	public static final int TRACE_ID = Debug.getTraceID();

	public static final String NUMTURNS_KEY = "game.numTurns";
	public static final String PLAYERCLASS_KEY = "controller.class.name";

	private int m_actorID = 0;
	
	public IChallenge createChallenge(Properties gameProps, Properties[] controllerProps) throws Exception
	{
		/*
		 * Read the number of turns
		 * Create the board and the BoardState
		 * Read the number of bots per controller
		 *   Check that there aren't more bots than cells
		 * For each controller
		 *   Create the controller class
		 *   Create the bots for the controller
		 *   Initialize the controller with the bots and the properties
		 * Next controller
		 * Return a new engine with the board state and the controllers
		 */

		if ( controllerProps.length != 2 )
			throw new RuntimeException("Must be exactly 2 controllers");
		
		Debug.trace(this, "GameFactory starting");
		Debug.trace(this, "GameProperties : " + gameProps);
		
		int numTurns = Integer.valueOf(gameProps.getProperty(NUMTURNS_KEY, "100")).intValue();

		SheepGameState state = new SheepGameState();
		state.setDuration(numTurns);
		
		TeamID[] teams = new TeamID[controllerProps.length];
		Actor[] actors = createActors(state, teams);
		
		Controller[] controllers = new Controller[controllerProps.length];
		for ( int i=0;i<controllerProps.length;i++ )
		{
			Properties props = controllerProps[i];
			Debug.assert(props != null, "Properties at " + i + " are null for GameFactory");

			Actor playerActor = actors[i];
			ActorID[] ids = { playerActor.getID() };
			
			String controllerClassName;
			controllerClassName = props.getProperty(PLAYERCLASS_KEY);
			Debug.assert(controllerClassName != null, "No " + PLAYERCLASS_KEY + " specified for player " + i);
			Debug.trace(this, "Team[" + i + "] = " + controllerClassName);
			AbstractAvatar avatar = (AbstractAvatar)Class.forName(controllerClassName).newInstance();
			Controller controller = new ControllerAdapter(avatar);
			controller.initialize(ids, props, teams[i]);
			controllers[i] = controller;
			
			state.addActor(playerActor);
		}

		Engine engine = createEngine(state, controllers, gameProps);
		
		return engine;
	}

	protected Engine createEngine(SheepGameState state, Controller[] controllers, Properties gameProps)
	{
		return new SheepEngine(state, controllers, gameProps);
	}
	
	protected Actor[] createActors(SheepGameState state, TeamID[] teams)
	{
		m_actorID = 0;
		int numPlayers = teams.length;
		if ( numPlayers < 2 )
		{
			throw new RuntimeException("There must be at least 2 players");
		}

		Actor[] actors = new Actor[numPlayers];
		for ( int i=0;i<numPlayers;i++ )
		{
			TeamID team = new WrapperID(new Integer(i));
			actors[i] = new Sheep(new WrapperID(new Integer(m_actorID++)), team);
			teams[i] = team;
		}
		return actors;
	}
	
	public int getTraceID()
	{
		return TRACE_ID;
	}
}
