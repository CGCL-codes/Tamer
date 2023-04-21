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

package agonism.ch.bvr.impl;

import agonism.ch.bvr.IScene;
import agonism.ch.bvr.IBeaver;
import agonism.ch.bvr.IFriendlyBeaver;

import agonism.ch.mazer.impl.ActorHeading;
import agonism.ch.mazer.impl.CellOwnerMap;
import agonism.ch.mazer.impl.MazerGameState;
import agonism.ch.mazer.impl.MazeWallMap;
import agonism.ch.mazer.impl.Reachability;
import agonism.ch.mazer.impl.Shoot;
import agonism.ch.mazer.impl.ShootingMap;
import agonism.ch.mazer.impl.Tank;

import agonism.ce.AbstractScene;
import agonism.ce.Action;
import agonism.ce.Actor;
import agonism.ce.ActorID;
import agonism.ce.Debug;
import agonism.ce.Engine;
import agonism.ce.FilteredEnumeration;
import agonism.ce.GameState;
import agonism.ce.Scene;
import agonism.ce.TeamID;

import agonism.ce.cells.Board;
import agonism.ce.cells.BoardFacet;
import agonism.ce.cells.Cell;
import agonism.ce.cells.ChangeHeading;
import agonism.ce.cells.CellGeometry;
import agonism.ce.cells.CurrentCell;
import agonism.ce.cells.Direction;

import agonism.ce.facets.ActorCurrentAction;

import com.objectspace.jgl.UnaryFunction;

import java.io.PrintWriter;
import java.util.Enumeration;

public final class BeaverScene
	extends AbstractScene
	implements IScene
{
	// Must be kept private so that players cannot access it by creating a class in this package
	private final Engine m_engine;
	private final MazerGameState m_state;
	
	public BeaverScene(Engine engine, MazerGameState state, TeamID teamID)
	{
		super(state, teamID);

		m_state = state;
		m_engine = engine;

		Debug.assert(getTeamID() != null, "TeamID is null");
	}
	
	public Board getBoard()
	{
		return BoardFacet.of(m_state).getBoard();
	}

	public double getTime()
	{
		return m_state.getTime();
	}
	
	public double getGameDuration()
	{
		return m_state.getDuration();
	}
	
	public boolean isOwner(Cell cell)
	{
		CellOwnerMap ownerMap = CellOwnerMap.of(m_state);
		return getTeamID().equals(ownerMap.getOwner(cell));
	}

	public boolean isReachable(Cell cell)
	{
		return Reachability.of(m_state).isReachable(cell);
	}
	
	public boolean isFiredOn(Cell cell)
	{
		return ShootingMap.of(m_state).isShooting(cell);
	}

	public int hasWall(Cell cell, Direction direction)
	{
		MazeWallMap maze = MazeWallMap.of(m_state);
		if ( maze.hasWall(cell, direction) )
			return WALL_TRUE;
		else
			return WALL_FALSE;
	}

	public Enumeration friendlyBeavers()
	{
		return friendlyActors();
	}
	
	public Enumeration enemyBeavers()
	{
		return enemyActors();
	}

	public Enumeration liveBeavers(Enumeration n)
	{
		return new FilteredEnumeration(n, 
									   new UnaryFunction()
									   {
										   public Object execute(Object obj)
										   {
											   IBeaver beaver = (IBeaver)obj;
											   return beaver.isAlive() ? beaver : null;
										   }
									   });
	}
	
	protected Actor wrap(Actor actor)
	{
		if ( actor == null )
			return null;

		if ( getTeamID().equals(actor.getTeamID()) )
			return new WrapFriendlyBeaver((Tank)actor);
		else
			return new WrapEnemyBeaver((Tank)actor);
	}

	private class WrapBeaver
	{
		protected final Tank m_beaver;
		
		public WrapBeaver(Tank beaver)
		{
			m_beaver = beaver;
		}
		
		public final ActorID getID()
		{
			return m_beaver.getID();
		}
		
		public final TeamID getTeamID()
		{
			return m_beaver.getTeamID();
		}

		public final Direction getHeading()
		{
			return m_beaver.getHeading(m_state);
		}
		
		public final Cell getCell()
		{
			return m_beaver.getCurrentCell(m_state);
		}

		public final boolean isAlive()
		{
			return m_beaver.isAlive(m_state);
		}
		
		public final String toString()
		{
			return m_beaver.toString();
		}

		public final void toString(PrintWriter writer)
		{
			writer.print(toString());
		}
	}

	private class WrapEnemyBeaver
		extends WrapBeaver
		implements IBeaver
	{
		public WrapEnemyBeaver(Tank beaver)
		{
			super(beaver);
		}
	}
		
	private class WrapFriendlyBeaver
		extends WrapBeaver
		implements IFriendlyBeaver
	{
		public WrapFriendlyBeaver(Tank beaver)
		{
			super(beaver);
		}
		
		public final boolean shoot()
		{
			return action(new Shoot(m_beaver));
		}
		
		public final boolean step()
		{
			return action(new BeaverStep(m_beaver));
		}

		public final boolean turn(boolean cw)
		{
			return turn(cw, ActorHeading.Heading.class);
		}

		private boolean action(Action action)
		{
			if ( !isAlive() )
				return false;
			
			ActorCurrentAction aca = ActorCurrentAction.of(m_state);
			if ( aca.getCurrentAction(m_beaver) != null )
				return false;
			
			m_engine.addAction(action);

			return action != null;
		}

		private boolean turn(boolean cw, Class clazz)
		{
			ChangeHeading ch = new ChangeHeading(m_beaver);
			ch.setRotationDirection( cw ? CellGeometry.CW : CellGeometry.CCW );
			ch.setStateFacetClass(clazz);
			return action(ch);
		}
	}
}





