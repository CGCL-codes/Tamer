package playground.scnadine.MapMatching.mapMatching;

/**
Stores a linked list of Links and some additional data.
*/
public class Path {

    public PathNode first;

    /**
	Creates a Path consisting of one link.
	@param link A Link.
	@param firstPoint The first point matched to link.
	*/
    public Path(Link link, int firstPoint) {
        first = new PathNode(link, firstPoint, null);
        first.incRC();
    }

    /**
	Creates a Path consisting of another Path plus one link. The new link is
	appended at the end. The original path remains unchanged.
	@param link A Link.
	@param firstPoint The first point matched to link.
	@param path A Path.
	*/
    public Path(Link link, int firstPoint, Path path) {
        first = new PathNode(link, firstPoint, path.first);
        first.incRC();
    }

    /**
	Destroys the instance. Also destroys the PathNodes that are no longer
	referenced.
	*/
    @Override
    public void finalize() {
        first.decRC();
    }
}
