package org.linkedgeodata.access;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class LGDDAOImpl implements ILGDDAO {

    private IURLBuilder urlBuilder;

    public LGDDAOImpl(IURLBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    /**
	 * 
	 * 
	 * @param rect
	 * @param model The model to store the data in. If null, a default model
	 *        will be created.
	 * @return
	 */
    @Override
    public Model getData(RectangularShape rect, Model model) {
        return readModel(urlBuilder.getData(rect), model);
    }

    @Override
    public Model getData(Point2D point, double radius, Model model) {
        return readModel(urlBuilder.getData(point, radius), model);
    }

    private Model readModel(String url, Model result) {
        if (result == null) {
            result = ModelFactory.createDefaultModel();
        }
        System.out.println("Reading model from: " + url);
        result.read(url);
        return result;
    }
}
