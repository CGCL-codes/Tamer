package org.perfectjpattern.core.behavioral.visitor;

import org.slf4j.*;

/**
 * Concrete Visitor implementation that exemplifies the case where your Visitor 
 * already inherits from another class and thus can not extend PerfectJPattern's
 * base reusable {@link AbstractVisitor} implementation.
 *
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Jun 8, 2008 11:59:05 PM $
 */
public class DoVisitor implements IHybridVisitor {

    /** 
     * {@inheritDoc}
     */
    public void visit(ICarPart anElement) {
        AbstractVisitor.reusableVisit(this, anElement);
    }

    /**
     * Visit Wheel
     * 
     * @param aWheel
     */
    public void visitWheel(Wheel aWheel) {
        theLogger.debug("Steering my wheel");
    }

    /**
     * Visit Engine
     * 
     * @param anEngine
     */
    public void visitEngine(Engine anEngine) {
        theLogger.debug("Starting my engine");
    }

    /**
     * Visit Body
     * 
     * @param aBody
     */
    public void visitBody(Body aBody) {
        theLogger.debug("Moving my body");
    }

    /**
     * Visit Car
     * 
     * @param aCar
     */
    public void visitCar(Car aCar) {
        theLogger.debug("Vroom!");
        visitEngine(aCar.getEngine());
        visitBody(aCar.getBody());
        for (Wheel myWheel : aCar.getWheels()) {
            visitWheel(myWheel);
        }
    }

    protected static void setLogger(Logger aLogger) {
        theLogger = aLogger;
    }

    /**
     * Provides logging facilities for this class 
     */
    private static Logger theLogger = LoggerFactory.getLogger(DoVisitor.class);
}
