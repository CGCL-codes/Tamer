package nl.softwaredesign.javast.java15;

import nl.softwaredesign.javast.node.AccessModifier;
import nl.softwaredesign.javast.node.ClassOrInterfaceOrEnumNode;
import nl.softwaredesign.javast.node.Classname;
import nl.softwaredesign.javast.node.PackageWrapper;
import nl.softwaredesign.javast.statement.RValue;
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
 *
 * This class represents a declaration of one of the elements of an enum.
 */
public class EnumDeclarationStatement extends ClassOrInterfaceOrEnumNode {

    private RValue[] parameters;

    public EnumDeclarationStatement() {
        super((PackageWrapper) null, AccessModifier.PRIVATE, "NoEnumName");
    }

    public EnumDeclarationStatement(String enumInstanceName, RValue... parameters) {
        super((PackageWrapper) null, AccessModifier.NONE, enumInstanceName);
        setInnerClass(true);
        this.parameters = parameters;
    }

    public EnumDeclarationStatement(String enumInstanceName) {
        super((PackageWrapper) null, AccessModifier.NONE, enumInstanceName);
        setInnerClass(true);
        this.parameters = new RValue[0];
    }

    public void addParameter(RValue parameter) {
        RValue tmp[] = new RValue[parameters.length + 1];
        System.arraycopy(parameters, 0, tmp, 0, parameters.length);
        tmp[tmp.length - 1] = parameter;
        parameters = tmp;
    }

    protected void writeSignature(WriteContext context, Writer writer) throws IOException {
        context.writeIndentation(writer);
        writer.write(getClassName());
        if (parameters != null && parameters.length > 0) {
            writer.write("(");
            boolean first = true;
            for (RValue parameter : parameters) {
                if (first) first = false; else writer.write(", ");
                parameter.write(writer, context.noCloseNoIndentLine());
            }
            writer.write(")");
        }
    }

    protected void writeClassContents(Writer writer, WriteContext context) throws IOException {
        if ((getInnerClasses() != null && getInnerClasses().size() > 0) || (getFields() != null && getFields().size() > 0) || (getMethods() != null && getMethods().size() > 0)) {
            super.writeClassContents(writer, context);
        }
    }

    public List<Classname> getRequiredImports() {
        List<Classname> imports = super.getRequiredImports();
        if (parameters != null) {
            for (RValue parameter : parameters) {
                imports.addAll(parameter.getRequiredImports());
            }
        }
        return imports;
    }
}
