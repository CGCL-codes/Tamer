package com.pragprog.dhnako.carserv.dom.vehicle;

import java.util.List;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Exploration;

@Named("Makes")
public interface MakeRepository {

    @Exploration
    public List<Make> allMakes();

    public Make findMake(@Named("Name") String name);
}
