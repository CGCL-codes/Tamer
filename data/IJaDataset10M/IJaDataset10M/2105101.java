package sk.tuke.ess.editor.simulation.schemymod.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sk.tuke.ess.editor.base.components.logger.Logger;
import sk.tuke.ess.editor.base.helpers.XMLHelper;
import sk.tuke.ess.editor.simulation.simeditbase.io.WrongDataFormatException;
import sk.tuke.ess.editor.simulation.simeditbase.io.kniznica.KniznicaLoader;
import sk.tuke.ess.editor.simulation.simeditbase.io.znacka.ZnackaXMLParser;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.IOPrimitiva;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.PolyLine;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.Primitiva;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.primitives.Text;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.IOSchema;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.Prepojenie;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.Schema;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema.Uzol;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka.Hierarchia;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka.Znacka;
import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka.ZnackaSchema;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 11.2.2012
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
class SchemaXMLParser implements SchemaXMLTagNames {

    private Schema schema;

    public Schema readFromXML(InputStream inputStream) throws WrongDataFormatException {
        try {
            Document doc = XMLHelper.createDocument(inputStream);
            Node schemaNode = doc.getElementsByTagName(SCHEMA).item(0);
            schema = new Schema();
            schema.nazov = schemaNode.getAttributes().getNamedItem(NAZOV).getNodeValue();
            loadModelForActiveSchema(getModelNode(schemaNode));
            return schema;
        } catch (Exception e) {
            throw new WrongDataFormatException(e.getMessage());
        }
    }

    private Node getModelNode(Node parent) {
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node modelNode = parent.getChildNodes().item(i);
            if (MODEL.equals(modelNode.getNodeName())) {
                return modelNode;
            }
        }
        throw new IllegalArgumentException("Zadaný node neobsahuje model node.");
    }

    private void loadModelForActiveSchema(Node modelNode) {
        for (int i = 0; i < modelNode.getChildNodes().getLength(); i++) {
            Node node = modelNode.getChildNodes().item(i);
            if (ZNACKY.equals(node.getNodeName())) {
                loadZnacky(node);
            } else if (PREPOJENIA.equals(node.getNodeName())) {
                loadPrepojenia(node);
            }
        }
        schema.recountSpojenia();
    }

    private void loadZnacky(Node znackyNode) {
        for (int i = 0; i < znackyNode.getChildNodes().getLength(); i++) {
            Node node = znackyNode.getChildNodes().item(i);
            if (ZNACKA.equals(node.getNodeName())) {
                loadZnacka(node);
            }
        }
    }

    private void loadZnacka(Node znackaNode) {
        ZnackaSchema znacka = createZnacka(znackaNode);
        znacka.meno = znackaNode.getAttributes().getNamedItem(NAZOV).getNodeValue();
        znacka.setId(Integer.parseInt(znackaNode.getAttributes().getNamedItem(ID_ZNACKA).getNodeValue()));
        for (int i = 0; i < znackaNode.getChildNodes().getLength(); i++) {
            Node node = znackaNode.getChildNodes().item(i);
            if (ZNACKA_POZICIA.equalsIgnoreCase(node.getNodeName())) {
                loadPozicia(node, znacka);
            } else if (ZNACKA_MIERKA.equalsIgnoreCase(node.getNodeName())) {
                loadMierka(node, znacka);
            } else if (ZNACKA_OTOCENIE.equalsIgnoreCase(node.getNodeName())) {
                loadOtocenie(node, znacka);
            } else if (POPIS_TEXT.equalsIgnoreCase(node.getNodeName())) {
                znacka.popisText = loadPopisText(node, znacka.meno);
            } else if (IOS.equalsIgnoreCase(node.getNodeName())) {
                loadIOs(node, znacka);
            } else if (MODEL.equalsIgnoreCase(node.getNodeName()) && znacka instanceof Hierarchia) {
                Schema parentSchema = schema;
                schema = ((Hierarchia) znacka).subSchema;
                schema.nazov = parentSchema.nazov + ":" + znacka.meno;
                loadModelForActiveSchema(node);
                ((Hierarchia) znacka).findAndSetGlobals();
                schema = parentSchema;
            }
        }
        schema.addZnacka(znacka);
    }

    private Text loadPopisText(Node node, String defaultText) {
        try {
            return (Text) new ZnackaXMLParser().createPrimitiva(node);
        } catch (WrongDataFormatException e) {
            Logger.getLogger().addWarning("Nesprávny formát popisného textu. Bude použitá generovaná hodnota <b>%s</b>", defaultText);
            return new Text(defaultText);
        }
    }

    private ZnackaSchema createZnacka(Node znackaNode) {
        String typ = znackaNode.getAttributes().getNamedItem(TYP).getNodeValue();
        String kniznica = znackaNode.getAttributes().getNamedItem(KNIZNICA).getNodeValue();
        Znacka znacka = KniznicaLoader.getInstance().getZnacka(kniznica, typ);
        if (znacka == null) {
            znacka = createUnkownZnacka(kniznica, typ);
            Logger.getLogger().addWarning("Neznáma značka <b>%s:%s</b>", kniznica, typ);
        }
        if (!(znacka instanceof ZnackaSchema)) {
            znacka = new ZnackaSchema(znacka);
        } else if (znacka instanceof Hierarchia) {
            ((Hierarchia) znacka).removeGlobals();
        }
        updatePozicia((ZnackaSchema) znacka);
        removeIOs(znacka);
        return (ZnackaSchema) znacka;
    }

    private Znacka createUnkownZnacka(String kniznica, String typ) {
        Znacka znacka = new Znacka();
        znacka.setNazovKategorie(kniznica);
        znacka.nazov = typ;
        return znacka;
    }

    private Znacka removeIOs(Znacka znacka) {
        List<Primitiva> primitivyToRemoveList = new ArrayList<Primitiva>();
        for (Primitiva primitiva : znacka.getPrimitivy()) {
            if (primitiva instanceof IOPrimitiva) {
                primitivyToRemoveList.add(primitiva);
            }
        }
        znacka.getPrimitivy().removeAll(primitivyToRemoveList);
        if (znacka instanceof ZnackaSchema) {
            ((ZnackaSchema) znacka).clearIOMap();
        }
        return znacka;
    }

    private void loadPozicia(Node node, ZnackaSchema znackaSchema) {
        znackaSchema.pozicia = new Point2D.Double(Double.parseDouble(node.getAttributes().getNamedItem(ZNACKA_X).getNodeValue()), Double.parseDouble(node.getAttributes().getNamedItem(ZNACKA_Y).getNodeValue()));
    }

    private void loadMierka(Node node, ZnackaSchema znackaSchema) {
    }

    private void loadOtocenie(Node node, ZnackaSchema znackaSchema) {
        znackaSchema.uhol = Double.parseDouble(node.getAttributes().getNamedItem(ZNACKA_OTOCENIE_UHOL).getNodeValue());
    }

    private void loadIOs(Node node, ZnackaSchema znackaSchema) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node ioNode = node.getChildNodes().item(i);
            if (IO.equalsIgnoreCase(ioNode.getNodeName())) {
                loadIO(ioNode, znackaSchema);
            }
        }
        znackaSchema.updatePocetIO(true);
    }

    private void loadIO(Node node, ZnackaSchema znackaSchema) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node ioPrimitiveNode = node.getChildNodes().item(i);
            Node prepojenieRefNode = node.getAttributes().getNamedItem(PREPOJENIE);
            int id = Integer.parseInt(node.getAttributes().getNamedItem(ID_IO).getNodeValue());
            int idPrimitiva = Integer.parseInt(node.getAttributes().getNamedItem(ID_PRIMITIVA_IO).getNodeValue());
            if (IO_PRIMITIVA.equalsIgnoreCase(ioPrimitiveNode.getNodeName())) {
                try {
                    IOSchema io = new IOSchema((IOPrimitiva) new ZnackaXMLParser().createPrimitiva(ioPrimitiveNode), id, prepojenieRefNode != null ? prepojenieRefNode.getNodeValue() : null);
                    io.getIOPrimitiva().ioID = idPrimitiva;
                    znackaSchema.addIO(io);
                } catch (WrongDataFormatException e) {
                    Logger.getLogger().addError("Nepodarilo sa načítať vstup/výstup pre značku <b>%s</b>: %s", znackaSchema.meno, e.getMessage());
                }
            }
        }
    }

    private void updatePozicia(ZnackaSchema znackaSchema) {
        Rectangle2D bounds = znackaSchema.getBounds2D();
        for (Primitiva p : znackaSchema.getPrimitivy()) {
            p.move(znackaSchema.pozicia.x - bounds.getMinX(), znackaSchema.pozicia.y - bounds.getMinY());
        }
        znackaSchema.pozicia.x -= bounds.getWidth() / 2;
        znackaSchema.pozicia.y -= bounds.getHeight() / 2;
    }

    private void loadPrepojenia(Node prepojeniaNode) {
        for (int i = 0; i < prepojeniaNode.getChildNodes().getLength(); i++) {
            Node node = prepojeniaNode.getChildNodes().item(i);
            if (PREPOJENIE.equalsIgnoreCase(node.getNodeName())) {
                loadPrepojenie(node);
            }
        }
    }

    private void loadPrepojenie(Node prepojenieNode) {
        Prepojenie prepojenie = new Prepojenie();
        prepojenie.setId(Integer.parseInt(prepojenieNode.getAttributes().getNamedItem(ID_PREPOJENIE).getNodeValue()));
        prepojenie.nazov = prepojenieNode.getAttributes().getNamedItem(NAZOV).getNodeValue();
        for (int i = 0; i < prepojenieNode.getChildNodes().getLength(); i++) {
            Node node = prepojenieNode.getChildNodes().item(i);
            if (POPIS_TEXT.equalsIgnoreCase(node.getNodeName())) {
                prepojenie.popisText = loadPopisText(node, prepojenie.nazov);
            } else if (SPOJ.equalsIgnoreCase(node.getNodeName())) {
                try {
                    loadSpoj(node, prepojenie);
                } catch (WrongDataFormatException e) {
                    Logger.getLogger().addError("Nepodarilo sa načítať čiaru prepojenia <b>%s</b>: %s", prepojenie.nazov, e.getMessage());
                }
            } else if (UZLY.equals(node.getNodeName())) {
                loadUzly(prepojenie, node);
            }
        }
        schema.addPrepojenie(prepojenie);
    }

    private void loadSpoj(Node spojNode, Prepojenie prepojenie) throws WrongDataFormatException {
        if (spojNode.getAttributes() == null) throw new WrongDataFormatException(String.format("Node spoj neobsahuje potrebné atribúty (%s, %s, %s, %s, %s)", ID_SPOJ, SPOJ_ID_ZNACKA_VSTUP, SPOJ_ID_VSTUP, SPOJ_ID_ZNACKA_VYSTUP, SPOJ_ID_VYSTUP));
        for (int i = 0; i < spojNode.getChildNodes().getLength(); i++) {
            Node node = spojNode.getChildNodes().item(i);
            if (PRIMITIVA.equalsIgnoreCase(node.getNodeName())) {
                PolyLine polyLine = (PolyLine) new ZnackaXMLParser().createPrimitiva(node);
                polyLine.setID(Integer.parseInt(spojNode.getAttributes().getNamedItem(ID_SPOJ).getNodeValue()));
                polyLine.setPrepojenie(prepojenie);
                prepojenie.getPolyLines().add(polyLine);
            }
        }
        prepojenie.getSpojenia().add(new Point(Integer.parseInt(spojNode.getAttributes().getNamedItem(SPOJ_ID_ZNACKA_VSTUP).getNodeValue()), Integer.parseInt(spojNode.getAttributes().getNamedItem(SPOJ_ID_VSTUP).getNodeValue())));
        prepojenie.getSpojenia().add(new Point(Integer.parseInt(spojNode.getAttributes().getNamedItem(SPOJ_ID_ZNACKA_VYSTUP).getNodeValue()), Integer.parseInt(spojNode.getAttributes().getNamedItem(SPOJ_ID_VYSTUP).getNodeValue())));
    }

    private void loadUzly(Prepojenie prepojenie, Node uzlyNode) {
        for (int i = 0; i < uzlyNode.getChildNodes().getLength(); i++) {
            Node node = uzlyNode.getChildNodes().item(i);
            if (UZOL.equals(node.getNodeName())) {
                prepojenie.getUzly().add(new Uzol(Integer.parseInt(node.getAttributes().getNamedItem(ID_BOD_START).getNodeValue()), Integer.parseInt(node.getAttributes().getNamedItem(ID_BOD_END).getNodeValue()), Integer.parseInt(node.getAttributes().getNamedItem(ID_PARENT).getNodeValue()), Integer.parseInt(node.getAttributes().getNamedItem(ID_LINE).getNodeValue())));
            }
        }
    }
}
