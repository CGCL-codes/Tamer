package nl.softwaredesign.javast.java15;

import nl.softwaredesign.javast.GeneratorSettings;
import nl.softwaredesign.javast.node.Classname;
import nl.softwaredesign.javast.node.CodeBlockNode;
import nl.softwaredesign.javast.node.StringConstants;
import nl.softwaredesign.javast.statement.DeclarationStatement;
import nl.softwaredesign.javast.statement.RValue;
import nl.softwaredesign.javast.statement.Statement;
import nl.softwaredesign.javast.util.WriteContext;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

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
public class ForEachStatement extends CodeBlockNode implements Statement {

    private Statement loopVariableStatement;

    private RValue collectionToLoop;

    public ForEachStatement() {
    }

    public ForEachStatement(DeclarationStatement loopVariableStatement, RValue collectionToLoop) {
        this.loopVariableStatement = loopVariableStatement;
        this.collectionToLoop = collectionToLoop;
    }

    public Statement getLoopVariableStatement() {
        return loopVariableStatement;
    }

    public void setLoopVariableStatement(Statement loopVariableStatement) {
        this.loopVariableStatement = loopVariableStatement;
    }

    public RValue getCollectionToLoop() {
        return collectionToLoop;
    }

    public void setCollectionToLoop(RValue collectionToLoop) {
        this.collectionToLoop = collectionToLoop;
    }

    public void write(Writer writer, WriteContext context) throws IOException {
        context.writeIndentation(writer);
        writer.write(StringConstants._FOR);
        writer.write("(");
        if (loopVariableStatement != null) loopVariableStatement.write(writer, context.noCloseNoIndentLine());
        writer.write(": ");
        if (collectionToLoop != null) collectionToLoop.write(writer, context.noCloseNoIndentLine());
        writer.write("){");
        writer.write(GeneratorSettings.getEOL());
        super.write(writer, context.nextLevel());
        context.writeIndentation(writer);
        writer.write("}");
        context.writeLineEnd(writer);
    }

    public List<Classname> getRequiredImports() {
        List<Classname> imports = super.getRequiredImports();
        List<Classname> loopImports = loopVariableStatement.getRequiredImports();
        if (loopImports != null) imports.addAll(loopImports);
        List<Classname> collectionImports = collectionToLoop.getRequiredImports();
        if (collectionImports != null) imports.addAll(collectionImports);
        return imports;
    }

    public ForEachStatement clone() throws CloneNotSupportedException {
        ForEachStatement copy = (ForEachStatement) super.clone();
        copy.collectionToLoop = collectionToLoop.clone();
        copy.loopVariableStatement = loopVariableStatement.clone();
        return copy;
    }
}
