package net.moonbiter.ebs.descriptors;

import net.moonbiter.OperationFailureException;
import net.moonbiter.ebs.validation.ValidationException;

/**
 * Describes how to compose an object a specific type
 * from some inputs. 
 * 
 * @author ftomassetti
 */
public interface Descriptor<INPUTS, DESCRIBED_TYPE> {

    /**
	 * Produce an instance of the DESCRIBED_TYPE using
	 * the given inputs. The rules uses are the ones specified
	 * by the descriptors contained in the descriptorsSet.
	 * 
	 * @param inputs
	 * @param descriptorsSet
	 * @return
	 * @throws OperationFailureException 
	 */
    DESCRIBED_TYPE produce(INPUTS inputs, DescriptorsSet<INPUTS> descriptorsSet) throws OperationFailureException, ValidationException;
}
