package ch.laoe.ui;

import ch.oli4.persistence.PersistenceDefaultImpl;

/***********************************************************

This file is part of LAoE.

LAoE is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

LAoE is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LAoE; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


Class:   GPersistance
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	singleton persistance. 

History:
Date:			Description:									Autor:
17.10.00		erster Entwurf									oli4

***********************************************************/
public class GPersistence extends PersistenceDefaultImpl {

    /**
	*	constructor
	*/
    protected GPersistence(String fileName) {
        try {
            restore(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GPersistence persistance;

    /**
	*	create singleton
	*/
    public static GPersistence createPersistance(String fileName) {
        if (persistance == null) {
            persistance = new GPersistence(fileName);
        }
        return persistance;
    }

    /**
	*	create singleton
	*/
    public static GPersistence createPersistance() {
        return persistance;
    }
}
