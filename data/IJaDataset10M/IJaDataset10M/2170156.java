package com.united_chat.sex_game.exceptions;

/**
 * This exception is thrown if an level passed to a Level manager for addition already exists.
 * <br />
 * Created on: 29th of May, 2003<br />
 * Last Modified: $Date: 2003/05/28 23:55:42 $
 * @author <a href="mailto:andrew@united-chat.com">Andrew Hughes</a>
 * @author <a href="mailto:brett@united-chat.com">Brett Royles</a>
 * @version $Revision: 1.1 $<br />
 * <br />
 * Version History:
 * <code>
 * <br />$Log: LevelExistsException.java,v $
 * <br />Revision 1.1  2003/05/28 23:55:42  andy2002
 * <br />Implemented LevelManager interface and add() test.
 * <br />
 * </code>
 * <br />
 * <strong>Copyright (c) 2003 Andrew Hughes, Brett Royles</strong>
 * <br />
 * <p>This file is part of Sex Game.</p>
 * <p>
 * Sex Game is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * </p>
 * <p>
 * Sex Game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Sex Game; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * </p>
 */
public class LevelExistsException extends Exception {

    public LevelExistsException() {
        super("The level already exists.");
    }
}
