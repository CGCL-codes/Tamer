package yaw.uml.ruml.common;

import java.io.Serializable;
import yaw.core.util.IProgressIndicator;
import yaw.core.util.IProgressIndicator.Break;

public interface IProgressMessage extends Serializable {

    void update(IProgressIndicator pi) throws Break;
}
