package com.rapidminer.operator.io;

import java.util.List;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeRepositoryLocation;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryLocation;
import com.rapidminer.repository.RepositoryManager;

/** This operator stores IOObjects at a location in a repository.
 * 
 * @author Simon Fischer
 *
 */
public class RepositoryStorer extends AbstractWriter<IOObject> {

    public static final String PARAMETER_REPOSITORY_ENTRY = "repository_entry";

    public RepositoryStorer(OperatorDescription description) {
        super(description, IOObject.class);
    }

    @Override
    public IOObject write(IOObject ioobject) throws OperatorException {
        try {
            RepositoryLocation location = getParameterAsRepositoryLocation(PARAMETER_REPOSITORY_ENTRY);
            return RepositoryManager.getInstance(null).store(ioobject, location, this);
        } catch (RepositoryException e) {
            throw new UserError(this, e, 315, getParameterAsString(PARAMETER_REPOSITORY_ENTRY), e.getMessage());
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterTypeRepositoryLocation type = new ParameterTypeRepositoryLocation(PARAMETER_REPOSITORY_ENTRY, "Repository entry.", false);
        type.setExpert(false);
        types.add(type);
        return types;
    }
}
