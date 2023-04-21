package org.dcma.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DepartamentoServiceAsync {

    void addDepartamento(Departamento departamento, AsyncCallback<Void> callback);

    void updateDepartamento(Departamento departamento, AsyncCallback<Void> callback);

    void removeDepartamento(Departamento departamento, AsyncCallback<Void> callback);

    void listDepartamentos(AsyncCallback<List<Departamento>> callback);
}
