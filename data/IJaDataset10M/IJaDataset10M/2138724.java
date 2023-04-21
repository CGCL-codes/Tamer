package net.llando;

import java.util.ArrayList;

@LlandoClass(tag = "net.llando.ReleaseNotification", primitive = true)
public class ReleaseNotification extends Notification {

    @LlandoProperty(name = "released_objects")
    public ArrayList<String> releasedObjects;

    @LlandoProperty(name = "retainer")
    public String retainer;

    public ReleaseNotification() {
        setType("release");
    }
}
