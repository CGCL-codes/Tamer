package theweb.resources;

import java.io.InputStream;

public interface ResourceLocation {

    public String getName();

    public String getPath();

    public InputStream getInputStream();

    public long lastModified();
}
