package de.catsoft.rdbs2j.gui;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/**
 * @author <a href="mailto:GBeutler@cat-gmbh.de">Guido Beutler</a>
 * @version 1.0
 * @copyright (c)2002,2003 by CAT Computer Anwendung Technologie GmbH
 * Oststr. 34
 * 50374 Erftstadt
 * Germany
 * 
 * @license This file is part of RDBS2J.
 * 
 * RDBS2J is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * RDBS2J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RDBS2J; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public class CATJComboBoxBoolean extends JComboBox {

    public static final int _TRUE_ID = 0;

    public static final int _FALSE_ID = 1;

    private Boolean _true = new Boolean(true);

    private Boolean _false = new Boolean(false);

    /**
    * @roseuid 3D74942603C6
    */
    public CATJComboBoxBoolean() {
        super();
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        dcbm.addElement(_true);
        dcbm.addElement(_false);
        setModel(dcbm);
    }
}
