package net.paoding.rose.mock.controllers.modulepath1;

import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

@Path("")
public class IndexController {

    @Get
    public Object index() {
        return getClass();
    }
}
