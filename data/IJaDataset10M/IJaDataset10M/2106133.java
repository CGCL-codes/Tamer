package eu.pisolutions.ocelot.content.context.io;

import eu.pisolutions.ocelot.content.ContentInstruction;
import eu.pisolutions.ocelot.content.context.AbstractPathContext;
import eu.pisolutions.ocelot.content.context.GraphicsContext;
import eu.pisolutions.ocelot.content.context.SubpathContext;
import eu.pisolutions.ocelot.object.PdfRealNumberObject;

final class WritingPathContext extends AbstractPathContext {

    private final WritingGraphicsContext parent;

    public WritingPathContext(WritingGraphicsContext parent) {
        super();
        assert parent != null;
        this.parent = parent;
    }

    public GraphicsContext getParent() {
        return this.parent;
    }

    @Override
    protected SubpathContext createSubpath(float x, float y) {
        return new WritingSubpathContext(this, x, y);
    }

    @Override
    protected void doRectangle(float x, float y, float width, float height) {
        this.writeInstruction(new ContentInstruction("re", new PdfRealNumberObject(x), new PdfRealNumberObject(y), new PdfRealNumberObject(width), new PdfRealNumberObject(height)));
    }

    @Override
    protected void doClose() {
        super.doClose();
        if (this.isClippingPath()) {
            this.writeInstruction(new ContentInstruction(this.adjustedOperator("W")));
        }
        this.writeInstruction(new ContentInstruction(this.getPaintingOperator()));
    }

    void writeInstruction(ContentInstruction instruction) {
        this.parent.writeInstruction(instruction);
    }

    private String getPaintingOperator() {
        if (this.isFilledPath()) {
            if (this.isStrokedPath()) {
                if (this.isClosedPath()) {
                    return this.adjustedOperator("b");
                }
                return this.adjustedOperator("B");
            }
            return this.adjustedOperator("f");
        }
        if (this.isStrokedPath()) {
            if (this.isClosedPath()) {
                return "s";
            }
            return "S";
        }
        if (this.isClosedPath()) {
            return "h";
        }
        return "n";
    }

    private String adjustedOperator(String operator) {
        switch(this.getWindingRule()) {
            case NON_ZERO:
                return operator;
            case EVEN_ODD:
                return operator + '*';
            default:
                throw new AssertionError();
        }
    }
}
