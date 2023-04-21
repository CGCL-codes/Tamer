package org.vd.servlet.method;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.vd.servlet.VDException;
import org.vd.store.VirtualStorage;

public interface MethodExecutor {

    public void execute(VirtualStorage storage, HttpServletRequest request, HttpServletResponse response) throws VDException, IOException;
}
