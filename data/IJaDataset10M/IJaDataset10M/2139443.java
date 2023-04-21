package com.pragprog.dhnako.carserv.dom.vehicle;

import java.util.List;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Exploration;

@Named("Models")
public interface ModelRepository {

    @Exploration
    public List<Model> allModels();

    public Model findModel(Make make, @Named("Name") String name);
}
