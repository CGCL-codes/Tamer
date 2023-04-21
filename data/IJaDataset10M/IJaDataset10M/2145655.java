package self.amigo.elem.uml;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import self.amigo.elem.ARectangularHandledElement;
import self.amigo.elem.ARectangularLinkable;
import self.awt.PainterManager;
import self.gee.IDiagramLayer;
import self.gee.IGraphicElement;
import self.lang.StringUtils;
import self.xml.DomUtils;

public class NameAndCompositionCompartmentsView extends ARectangularLinkable {

    /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
    private static final long serialVersionUID = 1;

    public static final String NAMECOMPARTMENT_PROP = "NameCompartment";

    public static final String COMPOSITIONCOMPARTMENT_PROP = "Composition";

    public NameAndCompositionCompartmentsView() {
        delegate = createDelegate();
        setDelegate(delegate);
    }

    protected IGraphicElement createDelegate() {
        throw new UnsupportedOperationException("Templates are expected to override the factory method");
    }

    protected abstract class AEssential extends ARectangularHandledElement {

        protected transient ICompartmentsRenderInfo context = new PaintContext();

        protected NameCompartmentFeature nameCompartment = new NameCompartmentFeature();

        private AuxilaryCompartmentFeature composition = new AuxilaryCompartmentFeature();

        protected Rectangle figCompartment = new Rectangle();

        protected MultiCompartmentPainter compartmentPainter;

        private String[] compDetails = new String[1];

        private boolean[] compDetailsVis = new boolean[1];

        public AEssential() {
            fig.setBounds(10, 10, 110, 80);
            resetFrame();
        }

        protected boolean isNamedUnderlined() {
            return false;
        }

        public void setLayer(IDiagramLayer layerToDrawOn, boolean add) {
            super.setLayer(layerToDrawOn, add);
            compartmentPainter = (MultiCompartmentPainter) PainterManager.getPainterForOwner(layer, MultiCompartmentPainter.class);
        }

        public void getProperties(Map store) {
            store.put(NAMECOMPARTMENT_PROP, nameCompartment.clone());
            store.put(COMPOSITIONCOMPARTMENT_PROP, composition.clone());
        }

        public void setProperties(Map data) {
            nameCompartment.set((NameCompartmentFeature) data.get(NAMECOMPARTMENT_PROP));
            composition.set((AuxilaryCompartmentFeature) data.get(COMPOSITIONCOMPARTMENT_PROP));
            layer.setDirty();
        }

        public void readFrom(Node self, HashMap idObjLookUp) throws DOMException {
            super.readFrom(self, idObjLookUp);
            nameCompartment.readFrom(self, idObjLookUp);
            Node comp = DomUtils.getChildElementNode(self, COMPOSITIONCOMPARTMENT_PROP);
            composition.readFrom(comp, idObjLookUp);
        }

        public void writeTo(Document doc, Element self, HashMap objIdLookup) throws DOMException {
            super.writeTo(doc, self, objIdLookup);
            nameCompartment.writeTo(doc, self, objIdLookup);
            Element comp = doc.createElement(COMPOSITIONCOMPARTMENT_PROP);
            self.appendChild(comp);
            composition.writeTo(doc, comp, objIdLookup);
        }

        class PaintContext implements ICompartmentsRenderInfo {

            public Rectangle getBounds() {
                return figCompartment;
            }

            public String getName() {
                return nameCompartment.name;
            }

            public String getSymbol() {
                return StringUtils.toNullOrNonEmptyValue(nameCompartment.aidLink);
            }

            public boolean isNameUnderlined() {
                return AEssential.this.isNamedUnderlined();
            }

            public boolean isNameItalic() {
                return false;
            }

            public String getStereoType() {
                return StringUtils.toNullOrNonEmptyValue(nameCompartment.stereotype);
            }

            public String getNamespace() {
                return StringUtils.toNullOrNonEmptyValue(nameCompartment.nameSpace);
            }

            public boolean[] getVisibleAuxilaryCompartments() {
                compDetailsVis[0] = composition.visible;
                return compDetailsVis;
            }

            public String[] getAuxilaryCompartments() {
                compDetails[0] = composition.details;
                return compDetails;
            }
        }
    }
}
