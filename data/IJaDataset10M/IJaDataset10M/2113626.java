package net.sf.brightside.luxurycruise.tapestry.pages.errors;

import net.sf.brightside.luxurycruise.tapestry.pages.Index;
import org.apache.tapestry.Asset;
import org.apache.tapestry.annotations.OnEvent;
import org.apache.tapestry.annotations.Path;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.ioc.annotations.Inject;

public class CannotDeleteCruiseError {

    @Persist
    private String message;

    @Inject
    @Path("context:styles/styles.css")
    private Asset styles;

    public Asset getStyles() {
        return styles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @OnEvent(component = "HomeLink")
    protected Object onHome() {
        return Index.class;
    }
}
