package org.ietr.preesm.codegen.model.semaphore;

import org.ietr.preesm.codegen.model.buffer.AbstractBufferContainer;
import org.ietr.preesm.codegen.model.buffer.Buffer;
import org.ietr.preesm.codegen.model.call.Constant;
import org.ietr.preesm.codegen.model.main.AbstractCodeElement;
import org.ietr.preesm.codegen.model.printer.CodeZoneId;
import org.ietr.preesm.codegen.model.printer.IAbstractPrinter;

/**
 * Special function call making the initialization of the semaphores. It
 * receives the number of semaphores to initialize and the buffer containing the
 * semaphores
 * 
 * @author mpelcat
 */
public class SemaphoreInit extends AbstractCodeElement {

    private Buffer semaphoreBuffer;

    private Constant semaphoreNumber;

    public SemaphoreInit(AbstractBufferContainer globalContainer, Buffer semaphoreBuffer) {
        super("semaphoreInit", globalContainer, null);
        this.semaphoreBuffer = semaphoreBuffer;
        this.semaphoreNumber = new Constant("semNumber", semaphoreBuffer.getSize());
    }

    public void accept(IAbstractPrinter printer, Object currentLocation) {
        currentLocation = printer.visit(this, CodeZoneId.body, currentLocation);
        semaphoreBuffer.accept(printer, currentLocation);
        semaphoreNumber.accept(printer, currentLocation);
    }

    /**
	 * Displays pseudo-code for test
	 */
    public String toString() {
        String code = super.getName();
        code += "(" + semaphoreBuffer.toString() + ");";
        return code;
    }
}
