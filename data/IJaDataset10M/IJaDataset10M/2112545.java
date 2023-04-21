package org.bluprint.app.model;

import java.io.IOException;
import org.eclipse.uml2.Model;
import org.bluprint.app.util.InvalidURIException;

public interface ModelDataSource {

    public void init();

    public Model readModel(String[] modelUris, String[] modelSearchPaths) throws IOException, InvalidModelException, IllegalArgumentException, InvalidURIException;

    public void writeModel(String uri, Model model) throws IOException;
}
