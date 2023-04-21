package astcentric.structure.query;

public class IntersectionOfNodeCollectionsTest extends CollectionOfCollectionsTestCase {

    public void testEmptyIntersection() {
        check(new IntersectionOfNodeCollections());
    }

    public void testIntersectionOfSingleCollection() {
        IntersectionOfNodeCollections collection = new IntersectionOfNodeCollections();
        collection.addCollection(_ab);
        check(collection, "a", "b");
    }

    public void testIntersectionOfTwoOverlappingCollections() {
        IntersectionOfNodeCollections collection = new IntersectionOfNodeCollections();
        collection.addCollection(_ab);
        collection.addCollection(_bd);
        check(collection, "b");
    }

    public void testIntersectionOfTwoDisjunctCollections() {
        IntersectionOfNodeCollections collection = new IntersectionOfNodeCollections();
        collection.addCollection(_ab);
        collection.addCollection(_c);
        check(collection);
    }
}
