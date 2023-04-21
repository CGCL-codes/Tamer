package de.catsoft.rdbs2j.db.db2;

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
public class SQLDTBinary extends SQLOptionDataType {

    private static final String _NAME = "binary";

    private static final String _DEFAULT_JAVA_TYPE = "byte[]";

    /**
    * @roseuid 3CFCC40501F9
    */
    public SQLDTBinary() {
        super(_NAME, _DEFAULT_JAVA_TYPE);
    }
}
