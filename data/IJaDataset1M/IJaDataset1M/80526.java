package iec61970.generation.generationdynamics.validation;

import iec61970.wires.SynchronousMachine;
import org.eclipse.emf.common.util.EList;

/**
 * A sample validator interface for {@link iec61970.generation.generationdynamics.PrimeMover}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface PrimeMoverValidator {

    boolean validate();

    boolean validatePrimeMoverRating(String value);

    boolean validateDrives_SynchronousMachines(EList<SynchronousMachine> value);
}