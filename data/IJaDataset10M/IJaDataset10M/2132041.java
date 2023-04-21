package org.jcompany.commons.facade;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Remote;
import org.jcompany.commons.IPlcFileEntity;
import org.jcompany.commons.PlcArgEntity;
import org.jcompany.commons.PlcFileEntity;
import org.jcompany.commons.PlcBaseContextVO;
import org.jcompany.commons.PlcBaseUserProfileEntity;
import org.jcompany.commons.PlcBaseEntity;
import org.jcompany.commons.PlcException;

@Remote
public interface IPlcFacadeRemote extends IPlcFacade {
}
