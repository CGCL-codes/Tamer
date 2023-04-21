package lu.albert.ovum.model;

import java.util.List;
import java.util.Vector;
import lu.albert.ovum.exceptions.InvalidClassException;
import lu.albert.ovum.model.iface.Town;
import lu.albert.ovum.model.vocabulary.RPG;
import org.apache.log4j.Logger;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * An implementation of the Town interface
 *
 * @author Michel Albert <michel@albert.lu>
 */
public class TownImpl extends LocalizableThingImpl implements Town {

    /** The logger of this class */
    private Logger logger = Logger.getLogger(TownImpl.class);

    /**
    * Instantiate a town from an URI
    * @param uri
    * @throws InvalidClassException if we tried to instantiate a non-town
    */
    public TownImpl(String uri) throws InvalidClassException {
        super(uri);
        if (!this.resource.hasProperty(RDF.type, RPG.Town)) throw new InvalidClassException(uri + " is not of class Town!");
    }

    /**
    * @see lu.albert.ovum.model.iface.Town#getHasShop()
    */
    public List getHasShop() {
        Vector out = new Vector();
        StmtIterator iter = this.resource.listProperties(RPG.hasShop);
        try {
            while (iter.hasNext()) {
                Statement tmp = iter.nextStatement();
                out.add(new ShopImpl(tmp.getObject().asNode().getURI()));
            }
        } catch (InvalidClassException e) {
            logger.fatal("Tried to instantiate a class " + "from an incompatible URI!", e);
        } finally {
            iter.close();
        }
        return out;
    }
}
