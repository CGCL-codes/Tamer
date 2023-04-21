package org.csiro.darjeeling.infuser.outputphase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.csiro.darjeeling.infuser.bytecode.CodeBlock;
import org.csiro.darjeeling.infuser.bytecode.ExceptionHandler;
import org.csiro.darjeeling.infuser.structure.DescendingVisitor;
import org.csiro.darjeeling.infuser.structure.Element;
import org.csiro.darjeeling.infuser.structure.LocalId;
import org.csiro.darjeeling.infuser.structure.ParentElement;
import org.csiro.darjeeling.infuser.structure.elements.AbstractClassDefinition;
import org.csiro.darjeeling.infuser.structure.elements.AbstractHeader;
import org.csiro.darjeeling.infuser.structure.elements.AbstractInfusion;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethod;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodDefinition;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodDefinitionList;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodImplementation;
import org.csiro.darjeeling.infuser.structure.elements.AbstractReferencedInfusionList;
import org.csiro.darjeeling.infuser.structure.elements.AbstractStaticFieldList;
import org.csiro.darjeeling.infuser.structure.elements.internal.InternalClassDefinition;
import org.csiro.darjeeling.infuser.structure.elements.internal.InternalMethodImplementation;
import org.csiro.darjeeling.infuser.structure.elements.internal.InternalStringTable;

/**
 * The DI Writer Visitor walks over the Infusion element tree and writes out a .di file. Each element is first written
 * into a byte buffer, are then recursively stitched together.  
 * 
 * @author Niels Brouwers
 */
public class DIWriterVisitor extends DescendingVisitor {

    private BinaryOutputStream out;

    private AbstractInfusion rootInfusion;

    public DIWriterVisitor(OutputStream out, AbstractInfusion rootInfusion) {
        this.rootInfusion = rootInfusion;
        this.out = new BinaryOutputStream(out);
    }

    @Override
    public void visit(AbstractInfusion element) {
        try {
            writeChildren(element, 0);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void visit(AbstractReferencedInfusionList element) {
        try {
            out.writeUINT8(element.getId().getId());
            int nrElements = element.getChildren().size();
            out.writeUINT8(nrElements);
            int offset = nrElements * 2 + 1;
            for (int i = 0; i < nrElements; i++) {
                out.writeUINT16(offset);
                offset += element.get(i).getHeader().getInfusionName().length() + 1;
            }
            for (int i = 0; i < nrElements; i++) {
                out.write(element.get(i).getHeader().getInfusionName().getBytes());
                out.write(0);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(AbstractHeader element) {
        try {
            out.writeUINT8(element.getId().getId());
            out.writeUINT8(element.getMajorVersion());
            out.writeUINT8(element.getMinorVersion());
            AbstractMethodImplementation entryPoint = element.getEntryPoint();
            if (entryPoint != null) out.writeUINT8(entryPoint.getGlobalId().getEntityId()); else out.writeUINT8(255);
            out.write(element.getInfusionName().getBytes());
            out.write(0);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(InternalClassDefinition element) {
        try {
            out.writeUINT8(element.getReferenceFieldCount());
            out.writeUINT8(element.getNonReferenceFieldsSize());
            LocalId superClassId = new LocalId(0, 0);
            if (element.getSuperClass() != null) {
                superClassId = element.getSuperClass().getGlobalId().resolve(rootInfusion);
            } else {
                superClassId = new LocalId(255, 0);
            }
            writeLocalId(out, superClassId);
            if (element.getCInit() != null) out.writeUINT8(element.getCInit().getGlobalId().getEntityId()); else out.writeUINT8(255);
            writeLocalId(out, element.getNameId().resolve(rootInfusion));
            out.writeUINT8(element.getInterfaces().size());
            for (AbstractClassDefinition classDef : element.getInterfaces()) writeLocalId(out, classDef.getGlobalId().resolve(rootInfusion));
            out.writeUINT8(element.getChildren().size());
            for (AbstractMethod method : element.getChildren()) {
                writeLocalId(out, method.getMethodDef().getGlobalId().resolve(rootInfusion));
                writeLocalId(out, method.getMethodImpl().getGlobalId().resolve(rootInfusion));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(InternalMethodImplementation element) {
        try {
            out.writeUINT8(element.getReferenceArgumentCount());
            out.writeUINT8(element.getIntegerArgumentCount());
            out.writeUINT8(element.getReferenceLocalVariableCount() - element.getReferenceArgumentCount() - (element.isStatic() ? 0 : 1));
            out.writeUINT8(element.getIntegerLocalVariableCount() - element.getIntegerArgumentCount());
            out.writeUINT8(element.getMethodDefinition().getParameterCount() + (element.isStatic() ? 0 : 1));
            out.writeUINT8(element.getMaxStack());
            CodeBlock code = element.getCodeBlock();
            int flags = 0;
            if (element.isNative()) flags |= 1;
            if (element.isStatic()) flags |= 2;
            out.writeUINT8(flags);
            out.writeUINT8(element.getMethodDefinition().getReturnType().getTType());
            if (element.getCode() == null) {
                out.writeUINT16(0);
            } else {
                byte[] codeArray = code.toByteArray();
                out.writeUINT16(codeArray.length);
                out.write(codeArray);
            }
            if (code != null) {
                ExceptionHandler exceptions[] = code.getExceptionHandlers();
                out.writeUINT8(exceptions.length);
                for (ExceptionHandler exception : exceptions) {
                    out.writeUINT8(exception.getCatchType().getInfusionId());
                    out.writeUINT8(exception.getCatchType().getLocalId());
                    out.writeUINT16(exception.getStart().getPc());
                    out.writeUINT16(exception.getEnd().getPc());
                    out.writeUINT16(exception.getHandler().getPc());
                }
            } else {
                out.writeUINT8(0);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeLocalId(BinaryOutputStream out, LocalId id) throws IOException {
        out.writeUINT8(id.getInfusionId());
        out.writeUINT8(id.getLocalId());
    }

    @Override
    public void visit(AbstractStaticFieldList element) {
        try {
            out.writeUINT8(element.getId().getId());
            out.writeUINT8(element.getNrRefs());
            out.writeUINT8(element.getNrBytes());
            out.writeUINT8(element.getNrShorts());
            out.writeUINT8(element.getNrInts());
            out.writeUINT8(element.getNrLongs());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(InternalStringTable element) {
        try {
            out.writeUINT8(element.getId().getId());
            String[] strings = element.elements();
            out.writeUINT16(strings.length);
            int offset = strings.length * 2 + 3;
            for (int i = 0; i < strings.length; i++) {
                out.writeUINT16(offset);
                offset += strings[i].length() + 2;
            }
            for (int i = 0; i < strings.length; i++) {
                out.writeUINT16(strings[i].length());
                out.write(strings[i].getBytes());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T extends Element> void visit(ParentElement<T> element) {
        try {
            out.writeUINT8(element.getId().getId());
            writeChildren(element, 1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T extends Element> void writeChildren(ParentElement<T> element, int offset) throws IOException {
        ArrayList<T> exportChildren = new ArrayList<T>();
        for (T child : element.getChildren()) exportChildren.add(child);
        assert (exportChildren.size() < 256) : "Number of children in parent element must not exceed 255";
        out.writeUINT8(exportChildren.size());
        byte[][] serializedElements = new byte[exportChildren.size()][];
        int i = 0;
        for (T child : exportChildren) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DIWriterVisitor visitor = new DIWriterVisitor(outStream, rootInfusion);
            child.accept(visitor);
            serializedElements[i] = outStream.toByteArray();
            outStream.close();
            i++;
        }
        int pointer = offset + 1 + 2 * (exportChildren.size());
        for (i = 0; i < exportChildren.size(); i++) {
            out.writeUINT16(pointer);
            pointer += serializedElements[i].length;
        }
        for (i = 0; i < exportChildren.size(); i++) out.write(serializedElements[i]);
    }

    @Override
    public void visit(AbstractMethodDefinitionList element) {
        try {
            out.writeUINT8(element.getId().getId());
            out.writeUINT8(element.getChildren().size());
            for (AbstractMethodDefinition methodDef : element.getChildren()) {
                int nrArgs = methodDef.getParameterCount();
                out.writeUINT8(nrArgs);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(Element element) {
        try {
            out.writeUINT8(element.getId().getId());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
