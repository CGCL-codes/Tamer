package poa.game.event;

import org.newdawn.slick.state.StateBasedGame;
import poa.game.Camera;
import poa.game.player.Player;
import poa.game.world.World;
import poa.hud.Hud;

/**
 *  PagesOfAdventure: A top-down puzzle adventure.
 *  Copyright (C) 2010, 2011  PagesOfAdventure
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * @author Edward_Lii
 */
public abstract class Event {

    /**
	 * Let the event init itself.
	 * @param parameters The additional parameters.
	 */
    public abstract void init(String[] parameters);

    /**
	 * Called to apply the event.
	 * @param world The world.
	 * @param player The player.
	 * @param hud The HudState.
	 * @param camera The camera.
	 * @param sbg The StateBasedGame.
	 */
    public abstract void apply(World world, Player player, Hud hud, Camera camera, StateBasedGame sbg);
}
