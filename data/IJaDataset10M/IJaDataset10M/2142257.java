package freemarker.provisionnal.debug.impl;

import java.rmi.RemoteException;
import freemarker.core.Environment;
import freemarker.provisionnal.debug.DebugModel;
import freemarker.template.TemplateModel;

public interface IDebugModelFactory {

    DebugModel newModel(TemplateModel model, int extraTypes) throws RemoteException;

    DebugModel newModel(Environment key) throws RemoteException;
}
