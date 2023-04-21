package mobac.program.interfaces;

import mobac.exceptions.MapSourceCreateException;

public interface WrappedMapSource {

    public MapSource getMapSource() throws MapSourceCreateException;
}
