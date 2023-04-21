package com.dotmarketing.velocity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.Resource;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.VelocityUtil;

public class ResourceWrapper implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2833599158288791699L;

    private transient Resource resource;

    public ResourceWrapper() {
    }

    public ResourceWrapper(Resource resource) {
        this.resource = resource;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.writeObject(resource.getName());
        } catch (IOException e) {
            Logger.error(this, "Unable to get the resource loader.  Looks like the velocity cache/cluster is not working.", e);
        }
    }

    private void readObject(java.io.ObjectInputStream ois) throws IOException, ClassNotFoundException {
        VelocityEngine ve = VelocityUtil.getEngine();
        try {
            this.resource = ve.getTemplate((String) ois.readObject());
        } catch (Exception e) {
            Logger.error(this, "Unable to get the resource loader.  Looks like the velocity cache/cluster is not working.", e);
        }
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
