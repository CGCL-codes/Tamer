package nl.softwaredesign.javast.statement;

import junit.framework.TestCase;
import nl.softwaredesign.javast.MockStatement;
import nl.softwaredesign.javast.TestUtil;
import nl.softwaredesign.javast.constant.ConstantString;
import nl.softwaredesign.javast.node.ASTNode;

/**
 * Copyright (C) 2006-2008 M. Stellinga (javast@softwaredesign.nl)<br>
 * <br>
 * This file is part of Javast.
 * <br><br>
 * Javast is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <br><br>
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <br><br>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <br><br>
 */
public class TestCastStatement extends TestCase {

    public void testImports() {
        MockStatement ms = new MockStatement(String.class);
        MockStatement ms2 = new MockStatement(int.class);
        CastStatement cs = new CastStatement(String.class, ms2);
        ms.testImports(cs);
        ms2.testImports(cs);
    }

    public void testCodeGeneration() {
        ASTNode node = new CastStatement(int.class, new ConstantString("test"));
        TestUtil.compareOutputToFile(node, "nl/softwaredesign/javast/statement/testCastStatement.txt", 1);
    }
}
