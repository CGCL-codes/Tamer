package oboes;

/** 
 * The <code>Relationship</code> class encapsulates the idea of a link between the <code>OBO_Object</code> containing
 * this <code>Relationship</code> and a target <code>OBO_Object</code> within the same ontology.
 * See <a href="http://www.geneontology.org/GO.format.shtml#oboflat">http://www.geneontology.org/GO.format.shtml#oboflat</a>
 * for a description of the OBO <code>relationship</code> tag and its uses within the OBO file format.
 *
 * @author  Michael Xiang (mxiang@mit.edu)
 * @version 3.0 (7/20/2006)
 */
public class Relationship {

    /**
     * Characterizes the relationship of the given <code>Relationship</code> object. This value is
     * determined by the first string to follow the <code>relationship</code> tag in an OBO stanza.
     * For example, a valid value for Gene Ontology is "part_of".
     */
    public String relationship;

    /**
     * Indicates the integer ontology ID of the target object. It is equal to the <code>integer_id</code>
     * field of the target <code>OBO_Object</code>. This field is only applicable if the target OBO_Object
     * is a Term (and not a Typedef).
     */
    public Integer integer_id;
}
